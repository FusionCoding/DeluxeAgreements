package de.fusion.deluxeagreements.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class HikariConnection extends AbstractConnection {

  protected final SQLCredentials configuration;
  private HikariDataSource hikari;

  public HikariConnection(String name, SQLCredentials configuration) {
    super(name);
    this.configuration = configuration;
  }

  protected String getDriverClass() {
    return null;
  }

  protected void appendConfigurationInfo(HikariConfig config) {
    String address = this.configuration.getAddress();
    String[] addressSplit = address.split(":");
    address = addressSplit[0];
    String port = addressSplit.length > 1 ? addressSplit[1] : "3306";

    config.setDataSourceClassName(getDriverClass());
    config.addDataSourceProperty("serverName", address);
    config.addDataSourceProperty("port", port);
    config.addDataSourceProperty("databaseName", this.configuration.getDatabase());
    config.setUsername(this.configuration.getUsername());
    config.setPassword(this.configuration.getPassword());
  }

  protected abstract void appendProperties(HikariConfig config, SQLCredentials credentials);

  @Override
  public void init() {
    HikariConfig config = new HikariConfig();
    config.setPoolName("deluxeagreements");

    appendConfigurationInfo(config);
    appendProperties(config, this.configuration);

    config.setMaximumPoolSize(this.configuration.getMaxPoolSize());
    config.setMinimumIdle(this.configuration.getMinIdleConnections());
    config.setMaxLifetime(this.configuration.getMaxLifetime());
    config.setConnectionTimeout(this.configuration.getConnectionTimeout());

    config.setInitializationFailTimeout(-1);

    this.hikari = new HikariDataSource(config);
  }

  @Override
  public void shutdown() {
    if (this.hikari != null) {
      this.hikari.close();
    }
  }

  @Override
  public Connection getConnection() throws SQLException {
    Connection connection = this.hikari.getConnection();
    if (connection == null) {
      throw new SQLException("Unable to get a connection from the pool.");
    }
    return connection;
  }
}
