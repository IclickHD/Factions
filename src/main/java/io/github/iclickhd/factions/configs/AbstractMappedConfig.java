package io.github.iclickhd.factions.configs;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractMappedConfig<MappedModel> extends AbstractConfig {
	public MappedModel mappedModel;

	public AbstractMappedConfig(Path configFile, MappedModel mappedModel) {
		this(configFile);
		this.mappedModel = mappedModel;
		setup();
		fill();
	}
	
	private AbstractMappedConfig(Path configFile) {
		super(configFile);
	}

	public MappedModel getFields() {
		return mappedModel;
	}

	@Override
	public void setup() {
		if (!Files.exists(configFile)) {
			try {
				Files.createFile(configFile);
				load();
				populate();
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			load();
		}
	}

	@Override
	public void populate() {
		for (Field field : getFields().getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(FactionSetting.class)) {
				FactionSetting factionSetting = field.getAnnotation(FactionSetting.class);
				try {
					get().getNode((Object[]) factionSetting.path()).setComment(factionSetting.comment()).setValue(field.get(getFields()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		save();
	}

	public void fill() {
		for (Field field : getFields().getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(FactionSetting.class)) {
				FactionSetting factionSetting = field.getAnnotation(FactionSetting.class);
				if (!get().getNode((Object[]) factionSetting.path()).isVirtual()) {
					try {
						if(field.getType().equals(BigDecimal.class)) {
							field.set(getFields(), BigDecimal.valueOf(get().getNode((Object[]) factionSetting.path()).getDouble()));
						} else {
							field.set(getFields(), get().getNode((Object[]) factionSetting.path()).getValue());
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} else {
					try {
						get().getNode((Object[]) factionSetting.path()).setComment(factionSetting.comment()).setValue(field.get(getFields()));
						save();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
