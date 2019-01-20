package io.github.iclickhd.factions.configs;

import java.math.BigDecimal;

public class MainConfigFields {
	@FactionSetting(path = { "power", "max-power" }, comment = "Maximum amount of power a player can have. Default: 10.0")
	public BigDecimal MaxPower = BigDecimal.valueOf(10.0);
	
	@FactionSetting(path = { "power", "min-power" }, comment = "Minimum amount of power a player can have. Default: -10.0")
	public BigDecimal MinPower = BigDecimal.valueOf(-10.0);

	@FactionSetting(path = { "power", "starting-power" }, comment = "Starting amount of power. Default: 5.0")
	public BigDecimal StartingPower = BigDecimal.valueOf(5.0);
	
	@FactionSetting(path = { "power", "increment" }, comment = "Amount of power added after a certain amount of time playing. (See power.increment-interval). Default: 0.2")
	public BigDecimal PowerIncrement = BigDecimal.valueOf(0.2);
	
	@FactionSetting(path = { "power", "decrement" }, comment = "Amount of power removed on player death. Default: 2.0")
	public BigDecimal PowerDecrement = BigDecimal.valueOf(2.0);
	
	@FactionSetting(path = { "power", "increment-interval" }, comment = "Time in seconds of player playing for incrementing its power. Default: 300")
	public int PowerIncrementInterval = 300;
}