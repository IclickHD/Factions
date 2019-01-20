package io.github.iclickhd.factions.models;

import java.math.BigDecimal;
import java.util.UUID;

public class FactionPlayer {
	private UUID uniqueId;
	private BigDecimal power;
	private BigDecimal powerBoost;
	
	public FactionPlayer(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}
	
	public void setUniqueId(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public BigDecimal getPower() {
		return power;
	}
	
	public void setPower(BigDecimal power) {
		this.power = power;
	}

	public BigDecimal getPowerBoost() {
		return powerBoost;
	}

	public void setPowerBoost(BigDecimal powerBoost) {
		this.powerBoost = powerBoost;
	}
}
