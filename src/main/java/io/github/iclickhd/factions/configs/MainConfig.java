package io.github.iclickhd.factions.configs;

import java.io.IOException;
import java.nio.file.Path;

public class MainConfig extends AbstractMappedConfig<MainConfigFields> {
	@Override
	public void reload() {
		super.reload();
	}
	
	@Override
	public void load() {
		try {
			configNode = configLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MainConfig(Path configFile) {
		super(configFile, new MainConfigFields());
	}
}
