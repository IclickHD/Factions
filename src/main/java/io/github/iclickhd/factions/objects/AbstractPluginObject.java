package io.github.iclickhd.factions.objects;

import io.github.iclickhd.factions.FactionsPlugin;

public abstract class AbstractPluginObject extends AbstractObject {
	public FactionsPlugin getPlugin() {
		return FactionsPlugin.getPlugin();
	}
}
