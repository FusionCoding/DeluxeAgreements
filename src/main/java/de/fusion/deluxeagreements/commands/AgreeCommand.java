package de.fusion.deluxeagreements.commands;

import de.fusion.deluxeagreements.DeluxeAgreements;
import de.fusion.deluxeagreements.sql.utils.AgreementUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AgreeCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s,
      String[] strings) {
    if (commandSender instanceof Player) {
      if (!AgreementUtils.hasAgreed((Player) commandSender)) {
        AgreementUtils.addAgreement((Player) commandSender);
        commandSender.sendMessage(
            DeluxeAgreements.getConfiguration().getPath("General.Messages.Agree").getString());
      }
    }

    return true;
  }
}
