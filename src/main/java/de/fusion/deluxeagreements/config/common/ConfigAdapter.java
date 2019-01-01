package de.fusion.deluxeagreements.config.common;

import java.io.File;

/**
 * Class providing File & Configuration
 */
public abstract class ConfigAdapter {

  private final File f;
  private Object configuration;

  /**
   * Sets the file of the configuration
   */
  public ConfigAdapter(File f) {
    this.f = f;
  }

  /**
   * Returns the file of the configuration
   *
   * @return File
   */
  public File getFile() {
    return f;
  }

  /**
   * Returns the configuration as Object
   *
   * @return Configuration
   */
  public Object getConfiguration() {
    return configuration;
  }

  /**
   * Sets the actual configuration
   */
  public void setConfiguration(Object configuration) {
    this.configuration = configuration;
  }
}
