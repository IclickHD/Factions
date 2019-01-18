package io.github.iclickhd.factions;

import java.io.IOException;
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
import io.github.iclickhd.factions.commands.ClaimCommand;
import io.github.iclickhd.factions.commands.CreateCommand;
import io.github.iclickhd.factions.commands.FactionHelpCommand;
import io.github.iclickhd.factions.commands.FactionInfoCommand;
import io.github.iclickhd.factions.commands.FactionListCommand;
import io.github.iclickhd.factions.commands.FactionRankDemote;
import io.github.iclickhd.factions.commands.FactionRankPromote;
import io.github.iclickhd.factions.commands.PlayerInfoCommand;
import io.github.iclickhd.factions.configs.FactionConfig;
import io.github.iclickhd.factions.configs.MainConfig;
import io.github.iclickhd.factions.listeners.PlayerMoveListener;
import io.github.iclickhd.factions.logic.FactionLogic;
import io.github.iclickhd.factions.logic.MainLogic;
import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.models.FactionClaim;
import io.github.iclickhd.factions.models.FactionMember;
import io.github.iclickhd.factions.parsers.FactionNameArgument;
import io.github.iclickhd.factions.serializers.FactionClaimSerializer;
import io.github.iclickhd.factions.serializers.FactionMemberSerializer;
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
    
	private FactionConfig factionConfig;
	private FactionsCache factionsCache;
	private FactionLogic factionLogic;
	
	private MainConfig mainConfig;
	private MainLogic mainLogic;

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
    
    public MainConfig getMainConfig() {
    	return mainConfig;
    }
    
    public MainLogic getMainLogic()
    {
        return mainLogic;
    }
    
	@Listener
	public void onPreInit(GamePreInitializationEvent event) {
		plugin = this;
	}

	@Listener
	public void onInit(GameInitializationEvent event) {
		factionCommands = new HashMap<List<String>, CommandSpec>();
		factionsCache = new FactionsCache();
		
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Faction.class), new FactionSerializer());
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(FactionMember.class), new FactionMemberSerializer());
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

	}

	private void RegisterListeners() {
		Sponge.getEventManager().registerListeners(this, new PlayerMoveListener());
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
		
        CommandSpec commandNation = CommandSpec.builder()
				.description(Text.of("Displays the list of faction command"))
                .executor(new FactionHelpCommand("f", factionCommands))
                .children(factionCommands)
                .build();
		
		Sponge.getCommandManager().register(this, commandNation, "faction", "f");
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

        // Create data directory for EagleFactions
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

        if (!Files.exists(configDir.resolve("players")))
        {
            try
            {
                Files.createDirectories(configDir.resolve("players"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        factionConfig = new FactionConfig(Paths.get(getConfigDir().resolve("data") + "/factions.conf"));
        mainConfig = new MainConfig(Paths.get(getConfigDir() + "/main.conf"));
	}
	
	private void InitializeLogic() 
	{
		mainLogic = new MainLogic();
		factionLogic = new FactionLogic();
	}
}
