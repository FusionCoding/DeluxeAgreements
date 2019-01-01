package de.fusion.deluxeagreements.sql.utils;

import de.fusion.deluxeagreements.DeluxeAgreements;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.bukkit.entity.Player;

public class AgreementUtils {


  public static boolean hasAgreed(Player p) {
    String uuid = p.getUniqueId().toString();
    try {
      Connection connection = DeluxeAgreements.getInstance().getDatabase().getConnection();
      PreparedStatement ps = connection
          .prepareStatement("SELECT * FROM `deluxeagreements_data` WHERE `uuid` = ?");
      ps.setString(1, uuid);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        ps.close();
        connection.close();
        return true;
      }
      ps.close();
      connection.close();
      return false;

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  public static void addAgreement(Player p) {
    try {
      Connection connection = DeluxeAgreements.getInstance().getDatabase().getConnection();
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO `deluxeagreements_data` (`uuid`, `ip`, `time`) VALUES (?, ?, ?)");
      ps.setString(1, p.getUniqueId().toString());
      ps.setString(2, p.getAddress().getAddress().getHostAddress());
      ps.setTimestamp(3, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
      ps.execute();
      ps.close();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }


}
