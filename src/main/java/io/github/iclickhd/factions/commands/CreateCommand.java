package io.github.iclickhd.factions.commands;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.models.FactionMember;
import io.github.iclickhd.factions.statictext.CommandArgumentKeys;

public class CreateCommand extends AbstractCommand {
	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		if(source instanceof Player) {
			Player player = (Player) source;
			Optional<String> optionalFactionName = context.<String>getOne(CommandArgumentKeys.FACTION_NAME_COMMAND_ARGUMENT);
			if(optionalFactionName.isPresent()) {
				String factionName = optionalFactionName.get();
				Optional<Faction> optionalFaction = getPlugin().getFactionLogic().getFaction(factionName);
				if(!optionalFaction.isPresent()) {
					FactionMember leader = new FactionMember(player.getUniqueId());

					Faction faction = new Faction(UUID.randomUUID(), factionName);
					faction.getMembers().add(leader);
					
					getPlugin().getFactionLogic().addOrUpdate(faction);
				} else {
					player.sendMessage(Text.of("Faction already exists"));
				}		
			} else {
				player.sendMessage(Text.of("You must specify a faction name"));
			}
		} else {
			source.sendMessage(Text.of("You must be a player to use this command"));
		}
		return CommandResult.success();
	}

}
