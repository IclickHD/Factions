package io.github.iclickhd.factions;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

import io.github.iclickhd.factions.caching.FactionsCache;
import io.github.iclickhd.factions.caching.PlayersCache;
import io.github.iclickhd.factions.commands.ClaimCommand;
import io.github.iclickhd.factions.commands.CreateCommand;
import io.github.iclickhd.factions.commands.FactionHelpCommand;
import io.github.iclickhd.factions.commands.FactionInfoCommand;
import io.github.iclickhd.factions.commands.FactionListCommand;
import io.github.iclickhd.factions.commands.FactionRankDemote;
import io.github.iclickhd.factions.commands.FactionRankPromote;
import io.github.iclickhd.factions.commands.PlayerInfoCommand;
import io.github.iclickhd.factions.commands.ReloadCommand;
import io.github.iclickhd.factions.configs.AbstractConfig;
import io.github.iclickhd.factions.configs.FactionConfig;
import io.github.iclickhd.factions.configs.FactionSetting;
import io.github.iclickhd.factions.configs.MainConfig;
import io.github.iclickhd.factions.configs.PlayerConfig;
import io.github.iclickhd.factions.listeners.PlayerJoinListener;
import io.github.iclickhd.factions.listeners.PlayerMoveListener;
import io.github.iclickhd.factions.logic.FactionLogic;
import io.github.iclickhd.factions.logic.MainLogic;
import io.github.iclickhd.factions.logic.PlayerLogic;
import io.github.iclickhd.factions.managers.PowerManager;
import io.github.iclickhd.factions.managers.UserManager;
import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.models.FactionClaim;
import io.github.iclickhd.factions.models.FactionMember;
import io.github.iclickhd.factions.models.FactionPlayer;
import io.github.iclickhd.factions.parsers.FactionNameArgument;
import io.github.iclickhd.factions.serializers.FactionClaimSerializer;
import io.github.iclickhd.factions.serializers.FactionMemberSerializer;
import io.github.iclickhd.factions.serializers.FactionPlayerSerializer;
import io.github.iclickhd.factions.serializers.FactionSerializer;
import io.github.iclickhd.factions.serializers.Vector3iSerializer;
import io.github.iclickhd.factions.statictext.CommandArgumentKeys;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION)
public class FactionsPlugin {
	private static FactionsPlugin plugin;
	public Map<List<String>, CommandSpec> factionCommands;
	
	@Inject
	private Logger logger;
	
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
	
	private MainConfig mainConfig;
	private MainLogic mainLogic;
	
	private FactionConfig factionConfig;
	private FactionsCache factionsCache;
	private FactionLogic factionLogic;
	
	private PlayerConfig playerConfig;
	private PlayersCache playersCache;
	private PlayerLogic playerLogic;
	
	private UserManager userManager;
	private PowerManager powerManager;

	public static FactionsPlugin getPlugin() {
		return plugin;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
    public Path getConfigDir()
    {
        return configDir;
    }
    
    public MainConfig getMainConfig() {
    	return mainConfig;
    }
    
    public MainLogic getMainLogic()
    {
        return mainLogic;
    }
    
    public FactionConfig getFactionConfig() {
    	return factionConfig;
    }
    
	public FactionsCache getFactionsCache() {
		return factionsCache;
	}
	
    public FactionLogic getFactionLogic()
    {
        return factionLogic;
    }
    
    public PlayerConfig getPlayerConfig() {
    	return playerConfig;
    }
    
    public PlayersCache getPlayersCache() {
    	return playersCache;
    }
    
    public PlayerLogic getPlayerLogic() {
    	return playerLogic;
    }
    
    public UserManager getUserManager() {
    	return userManager;
    }
    
    public PowerManager getPowerManager()
    {
        return powerManager;
    }
    
	@Listener
	public void onPreInit(GamePreInitializationEvent event) {
		plugin = this;
	}

	@Listener
	public void onInit(GameInitializationEvent event) {
		factionCommands = new HashMap<List<String>, CommandSpec>();
		factionsCache = new FactionsCache();
		playersCache = new PlayersCache();
		
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Faction.class), new FactionSerializer());
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(FactionMember.class), new FactionMemberSerializer());
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(FactionPlayer.class), new FactionPlayerSerializer());
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(FactionClaim.class), new FactionClaimSerializer());
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Vector3i.class), new Vector3iSerializer());
		
		SetupConfigs();

		Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.AQUA, "Configs loaded..."));
		
		InitializeManagers();
		
		InitializeLogic();		
		
		InitializeCommands();

		Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.AQUA, "Commands loaded..."));

		RegisterListeners();
	}
	
	private void InitializeManagers() {
		powerManager = new PowerManager();
		userManager = new UserManager();
	}

	private void RegisterListeners() {
		Sponge.getEventManager().registerListeners(this, new PlayerMoveListener());
		Sponge.getEventManager().registerListeners(this, new PlayerJoinListener());
	}

	private void InitializeCommands() {
		getLogger().info("Initializing commands...");

		factionCommands.put(Collections.singletonList("help"), CommandSpec.builder()
				.description(Text.of("Displays the list of faction command"))
				.permission("")
				.executor(new FactionHelpCommand("f", factionCommands))
				.build());

		factionCommands.put(Collections.singletonList("list"), CommandSpec.builder()
				.description(Text.of("Displays the list of faction"))
				.permission("")
				.executor(new FactionListCommand())
				.build());

		factionCommands.put(Collections.singletonList("create"), CommandSpec.builder()
				.description(Text.of("Create a faction"))
				.permission("")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of(CommandArgumentKeys.FACTION_NAME_COMMAND_ARGUMENT))))
				.executor(new CreateCommand()).build());

		factionCommands.put(Arrays.asList("f", "show", "who"), CommandSpec.builder()
				.description(Text.of("Display the faction information"))
				.permission("")
				.arguments(new FactionNameArgument(Text.of(CommandArgumentKeys.FACTION_NAME_COMMAND_ARGUMENT)))
				.executor(new FactionInfoCommand())
				.build());

		factionCommands.put(Arrays.asList("p", "player"), CommandSpec.builder()
				.description(Text.of("Display the player information"))
				.permission("")
				.arguments(GenericArguments.optional(GenericArguments.user(Text.of(CommandArgumentKeys.PLAYER_NAME_COMMAND_ARGUMENT))))
				.executor(new PlayerInfoCommand())
				.build());
		
		factionCommands.put(Collections.singletonList("promote"), CommandSpec.builder()
				.description(Text.of("Promote a player"))
				.executor(new FactionRankPromote())
				.build());
		
		factionCommands.put(Collections.singletonList("demote"), CommandSpec.builder()
				.description(Text.of("Demote a player"))
				.executor(new FactionRankDemote())
				.build());
		
		factionCommands.put(Collections.singletonList("claim"), CommandSpec.builder()
				.description(Text.of("Claim a land"))
				.executor(new ClaimCommand())
				.build());
		
        factionCommands.put(Collections.singletonList("reload"), CommandSpec.builder()
				.description(Text.of("Reload configuration"))
				.permission("")
				.executor(new ReloadCommand()).build());
		
        CommandSpec commandFaction = CommandSpec.builder()
				.description(Text.of("Displays the list of faction command"))
                .executor(new FactionHelpCommand("f", factionCommands))
                .children(factionCommands)
                .build();
		
		Sponge.getCommandManager().register(this, commandFaction, "faction", "f");
	}

	private void SetupConfigs() {
        try
        {
            Files.createDirectories(configDir);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (!Files.exists(configDir.resolve("data")))
        {
            try
            {
                Files.createDirectories(configDir.resolve("data"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        factionConfig = new FactionConfig(Paths.get(getConfigDir().resolve("data") + "/factions.conf"));
        playerConfig = new PlayerConfig(Paths.get(getConfigDir().resolve("data") + "/players.conf"));
        mainConfig = new MainConfig(Paths.get(getConfigDir() + "/main.conf"));
	}
	
	private void InitializeLogic() 
	{
		mainLogic = new MainLogic();
		factionLogic = new FactionLogic();
		playerLogic = new PlayerLogic();
	}
	
	public void registerMappedConfig(AbstractConfig config, Class<?> mappedClass) {
		if(mappedClass.isAnnotationPresent(FactionSetting.class)) {
			for (Field field : config.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(FactionSetting.class)) {
					FactionSetting factionSetting = field.getAnnotation(FactionSetting.class);
					if (config.get().getNode((Object[]) factionSetting.path()).isVirtual()) {
						try {
							field.set(this, config.get().getNode((Object[]) factionSetting.path()).getValue());
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
}
