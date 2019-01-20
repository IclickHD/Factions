package io.github.iclickhd.factions.configs;

import java.nio.file.Path;

public abstract class AbstractCacheableConfig extends AbstractConfig implements ICacheableConfig {
	public AbstractCacheableConfig(Path configFile) {
		super(configFile);
		this.setup();
		fillCache();
	}
}
