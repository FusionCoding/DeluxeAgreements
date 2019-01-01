package de.fusion.deluxeagreements.config.common;

import java.util.List;

/**
 * Class is offering common methods to access the config
 */

public interface ConfigReader {

  /**
   * Checks if the config contains the given Path
   *
   * @return boolean
   */
  boolean contains();

  /**
   * Returns the requested value as int
   *
   * @return int
   */
  int getInt();

  /**
   * Returns the requested value as boolean
   *
   * @return boolean
   */
  boolean getBoolean();

  /**
   * Returns the requested value as String
   *
   * @return String
   */
  String getString();

  /**
   * Returns a list as string
   *
   * @return String
   */
  String getStringList();

  /**
   * Returns the requested value as List
   *
   * @return List
   */
  List<?> getList();

  /**
   * Returns the requested value as Object
   *
   * @return Object
   */
  Object getObject();

}
