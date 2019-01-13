package io.github.iclickhd.factions.models;

import java.util.List;
import java.util.UUID;

import com.google.common.reflect.TypeToken;

public class FactionMember extends AbstractModel {
	@SuppressWarnings("serial")
	public static TypeToken<List<FactionMember>> ListTypeToken = new TypeToken<List<FactionMember>>() {
	};
	
	private UUID uniqueId;
	
	public FactionMember(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}
	
	public void setUniqueId(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}
}
