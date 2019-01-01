package de.fusion.deluxeagreements.listener;

import de.fusion.deluxeagreements.DeluxeAgreements;
import de.fusion.deluxeagreements.sql.utils.AgreementUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

  @EventHandler
  public void on(PlayerJoinEvent e) {
    if (!AgreementUtils.hasAgreed(e.getPlayer())) {
      e.getPlayer().sendMessage(DeluxeAgreements.getConfiguration().getPath("General.Messages.Agreement").getStringList());
    }
  }

}
