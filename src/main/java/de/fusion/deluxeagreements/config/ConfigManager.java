package de.fusion.deluxeagreements.config;


import de.fusion.deluxeagreements.DeluxeAgreements;
import de.fusion.deluxeagreements.config.common.ConfigAdapter;
import de.fusion.deluxeagreements.config.common.ConfigAdapterInterface;
import de.fusion.deluxeagreements.config.common.ConfigReader;
import de.fusion.deluxeagreements.config.common.ConfigWriter;
import java.io.File;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;


/**
 * This class is the main ConfigManager for Spigot This class handles everything and sets the actual
 * methods
 */

public class ConfigManager extends ConfigAdapter implements ConfigAdapterInterface {


  /**
   * Initialize the ConfigManager
   *
   * @param f as configuration File
   */
  public ConfigManager(File f) {
    super(f);
  }

  /**
   * Method builds the ConfigManager
   */
  public ConfigManager build() {
    reload();
    return this;
  }

  /**
   * Method reloads the configuration
   *
   * @return ConfigManager
   */
  public ConfigManager reload() {
    setConfiguration(YamlConfiguration.loadConfiguration(getFile()));
    return this;
  }

  /**
   * Method to set the path which needs to be written to
   *
   * @return ConfigWriter
   */
  @Override
  public ConfigWriter setPath(String path) {
    return new RealConfigWriter((YamlConfiguration) getConfiguration(), path);
  }

  /**
   * Method to set the path which needs to be read.
   *
   * @return ConfigReader
   */
  @Override
  public ConfigReader getPath(String path) {
    return new RealConfigReader((YamlConfiguration) getConfiguration(), path);
  }

  /**
   * Class defining methods to read the configuration
   */
  private class RealConfigReader implements ConfigReader {

    private final YamlConfiguration configuration;
    private final String path;

    /**
     * Initializes the config reader
     */
    private RealConfigReader(YamlConfiguration configuration, String path) {
      this.configuration = configuration;
      this.path = path;
    }

    @Override
    public boolean contains() {
      return configuration.contains(path);
    }

    @Override
    public int getInt() {
      return configuration.getInt(path);
    }

    @Override
    public boolean getBoolean() {
      return configuration.getBoolean(path);
    }

    @Override
    public String getString() {
      return ChatColor.translateAlternateColorCodes('&', configuration.getString(path));
    }

    @Override
    public String getStringList() {
      StringBuilder stringList = new StringBuilder();
      getList().forEach(string -> stringList.append(string).append("\n"));
      return ChatColor.translateAlternateColorCodes('&', stringList.toString());
    }

    @Override
    public List<?> getList() {
      return configuration.getList(path);
    }

    @Override
    public Object getObject() {
      return configuration.get(path);
    }
  }

  /**
   * Class defining methods to read the configuration
   */
  private class RealConfigWriter implements ConfigWriter {

    private final YamlConfiguration configuration;
    private final String path;

    /**
     * Initializes the config reader
     */
    private RealConfigWriter(YamlConfiguration configuration, String path) {
      this.configuration = configuration;
      this.path = path;
    }

    @Override
    public void setObject(Object o) {
      configuration.set(path, o);
    }

    @Override
    public void setAsyncObject(Object o) {
      DeluxeAgreements.getInstance().getMainExecutorService().submit(() -> this.setObject(o));
    }
  }
}
