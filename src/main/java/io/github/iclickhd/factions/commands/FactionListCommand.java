package io.github.iclickhd.factions.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.models.FactionMember;

public class FactionListCommand extends AbstractCommand {
	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		PaginationList.Builder builder = PaginationList.builder();
		List<Text> contents = new ArrayList<Text>();
		for(Faction faction : getPlugin().getFactionLogic().getFactions())
		{
			contents.add(Text.of(faction.getName() + " " + getPlugin().getFactionLogic().getOnlineMembers(faction.getName()).orElse(new HashSet<FactionMember>()).size()));
		}
		builder.title(Text.of("Faction List"))
		.contents(contents)
		.padding(Text.of("="))
		.sendTo(source);
		return CommandResult.success();
	}

}
