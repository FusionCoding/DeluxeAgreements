package de.fusion.deluxeagreements;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import de.fusion.bstats.spigot.Metrics;
import de.fusion.deluxeagreements.commands.AgreeCommand;
import de.fusion.deluxeagreements.config.ConfigManager;
import de.fusion.deluxeagreements.listener.JoinListener;
import de.fusion.deluxeagreements.listener.MoveListener;
import de.fusion.deluxeagreements.log.BaseLogger;
import de.fusion.deluxeagreements.log.LogPriority;
import de.fusion.deluxeagreements.log.LogType;
import de.fusion.deluxeagreements.sql.HikariConnection;
import de.fusion.deluxeagreements.sql.SQLConnection;
import de.fusion.deluxeagreements.sql.SQLCredentials;
import de.fusion.deluxeagreements.sql.ScriptRunner;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the main class initializing everything
 */

public class DeluxeAgreements extends JavaPlugin {


  private ExecutorService executorService = Executors.newCachedThreadPool();
  private static DeluxeAgreements instance;
  private static ConfigManager configuration;
  private boolean running;
  private static TaskChainFactory taskChainFactory;
  private boolean log = true;
  private BaseLogger logger;
  private HikariConnection database;


  /**
   * Used to initialize the instance of the class
   */
  @Override
  public void onLoad() {
    super.onLoad();
    instance = this;
    running = true;

  }


  /**
   * Used to manage the configuration and everything else necessary.
   */
  public void onEnable() {
    super.onEnable();
    taskChainFactory = BukkitTaskChainFactory.create(this);
    File config = new File(getDataFolder(), "config.yml");
    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();
    }
    if (!config.exists()) {
      saveResource("config.yml", true);
    }

    configuration = new ConfigManager(config).build();
    initLogger();
    logRaw("Loaded DeluxeAgreements by FusionCoding");
    log = configuration.getPath("General.Log").getBoolean();
    if (log) {
      logRaw("Enabling File logging.");
    } else {
      logRaw("Disabled File logging.");
    }

    SQLCredentials credentials = new SQLCredentials(
        configuration.getPath("General.Data.Address").getString(),
        configuration.getPath("General.Data.Database").getString(),
        configuration.getPath("General.Data.Username").getString(),
        configuration.getPath("General.Data.Password").getString(),
        configuration.getPath("General.Data.Hikari-settings.maximum-pool-size").getInt(),
        configuration.getPath("General.Data.Hikari-settings.minimum-idle").getInt(),
        configuration.getPath("General.Data.Hikari-settings.maximum-lifetime").getInt(),
        configuration.getPath("General.Data.Hikari-settings.connection-timeout").getInt());
    database = new SQLConnection("deluxeagreements", credentials);

    try {
      database.init();
      if (database.getConnection() != null) {
        log("§aSuccessfully connected to MySQL.");

        ScriptRunner scriptRunner = null;
        try {
          scriptRunner = new ScriptRunner(database.getConnection(), false, false);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        try {
          scriptRunner
              .runScript(new InputStreamReader(getClass().getResourceAsStream("/mysql.sql")));
        } catch (SQLException | IOException e) {
          e.printStackTrace();
        }

      } else {
        log("§cCould not connect with the MySQL database.");
        Bukkit.getServer().getPluginManager().disablePlugin(this);
      }
    } catch (
        SQLException e)

    {
      log("§cCould not connect with the MySQL database. (View Log)");
      Bukkit.getServer().getPluginManager().disablePlugin(this);
      getBaseLogger().write(LogType.ERROR, LogPriority.FILE, getStackTrace(e));
      Bukkit.getServer().getPluginManager().disablePlugin(this);
    }

    PluginManager pm = Bukkit.getPluginManager();
    pm.registerEvents(new JoinListener(), this);
    pm.registerEvents(new MoveListener(), this);

    getCommand("agree").setExecutor(new AgreeCommand());

    Metrics metrics = new Metrics(this);

//    getCommand("DeluxeCleaner").setExecutor(new DeluxeCleanerCommand());
  }

  /**
   * Used to disable all running tasks listening for running
   */
  @Override
  public void onDisable() {
    super.onDisable();
    logRaw("Zipping log files.");
    logger.zipFile();
    logRaw("Goodbye!");
    running = false;
  }

  /**
   * This is used to log something to the console Automatically adds the prefix
   *
   * @param message to print
   */
  public void logRaw(String message) {
    Bukkit.getConsoleSender().sendMessage(getPrefix().replace("»", ">") + message);
  }

  /**
   * Method is used to easily run asynchronous tasks
   *
   * @return ExecutorService
   */
  public ExecutorService getMainExecutorService() {
    return executorService;
  }

  /**
   * This Method returns the instance of the plugin
   *
   * @return DeluxeCleaner
   */
  public static DeluxeAgreements getInstance() {
    return instance;
  }

  /**
   * Method will return a instance of 'ConfigManager' This allows easy access to the configuration
   * file
   *
   * @return ConfigManager
   */
  public static ConfigManager getConfiguration() {
    return configuration;
  }

  /**
   * Method will return the String of the plugin given in the configuration file
   *
   * @return String of DeluxeCleaner
   */
  public static String getPrefix() {
    return getConfiguration().getPath("General.Prefix").getString();
  }

  /**
   * Method returns the current state of the active plugin
   *
   * @return boolean
   */
  public boolean isRunning() {
    return running;
  }

  public static <T> TaskChain<T> newChain() {
    return taskChainFactory.newChain();
  }

  public static <T> TaskChain<T> newSharedChain(String name) {
    return taskChainFactory.newSharedChain(name);
  }

  public boolean isLog() {
    return log;
  }

  public void log(String message) {
    logger.write(message);
  }

  public BaseLogger getBaseLogger() {
    return logger;
  }

  public void initLogger() {
    File logDir = new File(getDataFolder(), "logs");
    final Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
    File log = new File(logDir, "DeluxeAgreements" + "-" + format.format(date) + ".log");
    logger = new BaseLogger(this, log);
  }

  public static String getStackTrace(final Throwable throwable) {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw, true);
    throwable.printStackTrace(pw);
    return sw.getBuffer().toString();
  }

  public HikariConnection getDatabase() {
    return database;
  }
}
