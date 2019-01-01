package de.fusion.deluxeagreements.sql;

import com.zaxxer.hikari.HikariConfig;

public class SQLConnection extends HikariConnection {


  public SQLConnection(String name, SQLCredentials configuration) {
    super(name, configuration);
  }

  @Override
  protected void appendConfigurationInfo(HikariConfig config) {
    String address = this.configuration.getAddress();
    String[] addressSplit = address.split(":");
    address = addressSplit[0];
    String port = addressSplit.length > 1 ? addressSplit[1] : "3306";
    String database = this.configuration.getDatabase();

    config.setJdbcUrl("jdbc:mysql://" + address + ":" + port + "/" + database);
    config.setUsername(this.configuration.getUsername());
    config.setPassword(this.configuration.getPassword());
  }

  @Override
  protected void appendProperties(HikariConfig config, SQLCredentials credentials) {
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("alwaysSendSetIsolation", "false");
    config.addDataSourceProperty("cacheServerConfiguration", "true");
    config.addDataSourceProperty("elideSetAutoCommits", "true");
    config.addDataSourceProperty("useLocalSessionState", "true");
    config.addDataSourceProperty("useServerPrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    config.addDataSourceProperty("cacheCallableStmts", "true");
    config.addDataSourceProperty("useUnicode", true);
    config.addDataSourceProperty("characterEncoding", "utf8");
  }
}
