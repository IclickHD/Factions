package io.github.iclickhd.factions.listeners;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import io.github.iclickhd.factions.models.Faction;

public class PlayerMoveListener extends AbstractListener {
	@Listener
	public void onPlayerMove(MoveEntityEvent event, @Root Player player) {
		Location<World> playerLocation = player.getLocation();
		Optional<Faction> optionalChunkFaction = getPlugin().getFactionLogic().getFactionByClaim(player.getWorld(), playerLocation.getChunkPosition());
		if(optionalChunkFaction.isPresent()) {
			
		}
	}
}
