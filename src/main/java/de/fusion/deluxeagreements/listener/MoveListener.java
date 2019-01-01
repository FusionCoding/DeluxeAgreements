package de.fusion.deluxeagreements.listener;

import de.fusion.deluxeagreements.sql.utils.AgreementUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

  private List<String> allow = new ArrayList<>();

  @EventHandler
  public void on(PlayerMoveEvent e) {
    if (allow.contains(e.getPlayer().getName())) {

    } else {
      if (AgreementUtils.hasAgreed(e.getPlayer())) {
        allow.add(e.getPlayer().getName());
      } else {
        e.setTo(e.getFrom());
      }
    }
  }


}
