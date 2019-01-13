package io.github.iclickhd.factions.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import io.github.iclickhd.factions.FactionsPlugin;

public class FactionNameArgument extends CommandElement {

	public FactionNameArgument(@Nullable Text key) {
		super(key);
	}

	@Nullable
	@Override
	protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
		if (args.hasNext()) {
			return args.next();
		} else {
			return null;
		}
	}

	@Override
	public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
		Set<String> factionNames = FactionsPlugin.getPlugin().getFactionLogic().getFactions().stream()
				.map(faction -> faction.getName()).collect(Collectors.toSet());
		List<String> list = new ArrayList<String>(factionNames);
		Collections.sort(list);

		if (args.hasNext()) {
			String charSequence = args.nextIfPresent().get().toLowerCase();
			return list.stream().filter(x -> x.contains(charSequence)).collect(Collectors.toList());
		}
		
		return list;
	}

}
