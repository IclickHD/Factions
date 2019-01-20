package io.github.iclickhd.factions.listeners;

import java.math.BigDecimal;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import io.github.iclickhd.factions.models.FactionPlayer;

public class PlayerJoinListener extends AbstractListener {
    @Listener(order = Order.POST)
    public void onPlayerJoin(ClientConnectionEvent.Join event)
    {
        if(event.getCause().root() instanceof Player)
        {
            Player player = (Player) event.getCause().root();
            
            if (!getPlugin().getPlayersCache().getCache().containsKey(player.getUniqueId())) {
                FactionPlayer factionPlayer = new FactionPlayer(player.getUniqueId());
                factionPlayer.setPower(getPlugin().getMainConfig().getFields().StartingPower);
                factionPlayer.setPowerBoost(BigDecimal.valueOf(0));
                getPlugin().getPlayerLogic().addOrUpdate(factionPlayer);
            }

            getPlugin().getPowerManager().startIncreasingPower(player.getUniqueId());
        }
    }
}
