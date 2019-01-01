package de.fusion.deluxeagreements.log;

import de.fusion.deluxeagreements.DeluxeAgreements;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

public class BaseLogger {

  private DeluxeAgreements adapter;
  private File file;
  public static final Pattern STRIP_COLOR_PATTERN = Pattern
      .compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");


  public BaseLogger(DeluxeAgreements adapter, File file) {
    this.adapter = adapter;
    this.file = file;
    File logDir = new File(this.adapter.getDataFolder(), "logs");
    if (!logDir.exists()) {
      logDir.mkdirs();
    }
    for (File fileInFolder : Objects.requireNonNull(logDir.listFiles())) {
      if (fileInFolder.getName().endsWith(".log")) {
        File f = new File(logDir, fileInFolder.getName().replace(".log", ".zip"));
        try {
          ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        List<File> files = new ArrayList<>();
        files.add(fileInFolder);
        try {
          Packager.packZip(f, files);
        } catch (IOException e) {
          e.printStackTrace();
        }
        if (!fileInFolder.delete()) {
          fileInFolder.deleteOnExit();
        }
      }
    }
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException ignored) {
      }
    }

  }


  public void write(LogType type, LogPriority priority, String message) {
    Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat f = new SimpleDateFormat("[HH:mm:ss]");
    String dateAsString = f.format(date);

    switch (priority) {
      case CONSOLE:
        adapter.logRaw(message);
        break;
      case DEBUG:
        if (DeluxeAgreements.getConfiguration().getPath("General.Debug").getBoolean()) {
          runBW(
              dateAsString + " - [" + type.toString() + "] > " + stripColor(message)
                  + "\n");
        }
        break;
      case FILE:
        runBW(dateAsString + " - [" + type.toString() + "] > " + stripColor(message) + "\n");
        break;
      case DEFAULT:
        runBW(dateAsString + " - [" + type.toString() + "] > " + stripColor(message) + "\n");
        adapter.logRaw(message);
        break;
    }
  }

  private void runBW(String message) {
    if (!adapter.isLog()) {
      return;
    }
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
      bw.write(message);
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeAsync(LogType type, LogPriority priority, String message) {
    adapter.getMainExecutorService().execute(() -> write(type, priority, message));
  }

  public void writeAsync(String message) {
    writeAsync(LogType.INFO, LogPriority.DEFAULT, message);

  }

  public void writeAsync(LogPriority priority, String message) {
    writeAsync(LogType.INFO, priority, message);
  }


  public void write(String message) {
    write(LogType.INFO, LogPriority.DEFAULT, message);
  }

  public void write(LogPriority priority, String message) {
    write(LogType.INFO, priority, message);
  }


  public void zipFile() {
    final Date date = new Date(System.currentTimeMillis());
    final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
    File logDir = new File(adapter.getDataFolder(), "logs");
    File f = new File(logDir, "DeluxeAgreements" + "-" + format.format(date) + ".zip");
    try {
      ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    List<File> files = new ArrayList<File>();
    files.add(this.file);
    try {
      Packager.packZip(f, files);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (!file.delete()) {
      file.deleteOnExit();
    }
  }

  private static String stripColor(String input) {
    return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
  }

  public File getFile() {
    return file;
  }
}
