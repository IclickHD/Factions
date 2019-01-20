package io.github.iclickhd.factions.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

import io.github.iclickhd.factions.models.Faction;

public class ClaimCommand extends AbstractCommand {

	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		if(source instanceof Player) {
			Player player = (Player)source;
			Optional<Faction> optionalPlayerFaction = getPlugin().getFactionLogic().getFactionByPlayer(player);
			if(optionalPlayerFaction.isPresent()) {
				Faction playerFaction = optionalPlayerFaction.get();
				Optional<Faction> optionalChunkFaction = getPlugin().getFactionLogic().getFactionByClaim(player.getWorld(), player.getLocation().getChunkPosition());
				if(optionalChunkFaction.isPresent()) {
					Faction chunkFaction = optionalChunkFaction.get();
					if(getPlugin().getFactionLogic().canOverClaim(playerFaction, chunkFaction)) {
						getPlugin().getFactionLogic().claimChunk(playerFaction, player.getWorld(), player.getLocation().getChunkPosition());	
					}
				} else {
					if(getPlugin().getFactionLogic().canClaim(playerFaction)) {
						getPlugin().getFactionLogic().claimChunk(playerFaction, player.getWorld(), player.getLocation().getChunkPosition());	
					}
				}
			} else {
				
			}
		} else {
			
		}		
		return CommandResult.success();
	}
}
