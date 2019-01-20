package io.github.iclickhd.factions.managers;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import io.github.iclickhd.factions.models.FactionPlayer;

public class PowerManager extends AbstractManager {
	public void startIncreasingPower(UUID playerUUID) {
		Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();

		taskBuilder.interval(getPlugin().getMainConfig().getFields().PowerIncrementInterval, TimeUnit.SECONDS).execute(new Consumer<Task>() {
			@Override
			public void accept(Task task) {
				if (!getPlugin().getUserManager().isUserOnline(playerUUID)) task.cancel();
				
				FactionPlayer factionPlayer = getPlugin().getPlayersCache().getCache().get(playerUUID);
				BigDecimal maxPower = getMaxPower(factionPlayer.getUniqueId());
				if (factionPlayer.getPower().compareTo(maxPower) < 0) {
					if (factionPlayer.getPower().compareTo(maxPower.subtract(getPlugin().getMainConfig().getFields().PowerIncrement)) <= 0) {
						factionPlayer.setPower(factionPlayer.getPower().add(getPlugin().getMainConfig().getFields().PowerIncrement));
					} else {
						factionPlayer.setPower(maxPower);
					}
					getPlugin().getPlayerLogic().addOrUpdate(factionPlayer);
				}
			}
		}).async().submit(getPlugin());
	}
	
	public BigDecimal getMaxPower(UUID playerUUID) {
		FactionPlayer factionPlayer = getPlugin().getPlayersCache().getCache().get(playerUUID);
		if(factionPlayer != null) {
			return getPlugin().getMainConfig().getFields().MaxPower.add(factionPlayer.getPowerBoost());
		} else {
			return BigDecimal.valueOf(0);
		}
	}
	
	public BigDecimal getPower(UUID playerUUID) {
		FactionPlayer factionPlayer = getPlugin().getPlayersCache().getCache().get(playerUUID);
		return factionPlayer.getPower();
	}
}
