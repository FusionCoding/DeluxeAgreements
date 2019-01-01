package de.fusion.deluxeagreements.config.common;

/**
 * This interface is used to later tell the plugin on how to write to the config
 */

public interface ConfigWriter {

  /**
   * Method will write something to the config on the main thread
   *
   * @param o as Object to set
   */
  void setObject(Object o);

  /**
   * Method will write something to the config on a seperate thread
   *
   * @param o as Object to set
   */
  void setAsyncObject(Object o);

}
