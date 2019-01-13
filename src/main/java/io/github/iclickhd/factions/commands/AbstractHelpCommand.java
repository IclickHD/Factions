package io.github.iclickhd.factions.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public abstract class AbstractHelpCommand extends AbstractCommand {
	private String parentCommand;
	private Map<List<String>, CommandSpec> subCommands;
	
	public AbstractHelpCommand(String parentCommand, Map<List<String>, CommandSpec> subCommands) {
		this.parentCommand = parentCommand;
		this.subCommands = subCommands;
	}
	
	public void sendHelpListTo(CommandSource receiver) {
		List<Text> helpList = new ArrayList<Text>();
		for (Entry<List<String>, CommandSpec> entry : subCommands.entrySet()) {
			CommandSpec commandSpec = entry.getValue();
			Text helpTextBuilder = Text.builder()
					.append(Text.of(TextColors.AQUA,
							"/" + parentCommand + " " + StringUtils.join(entry.getKey(), ",")))
					.append(commandSpec.getShortDescription(receiver).isPresent()
							? Text.of(TextColors.YELLOW, " " + commandSpec.getShortDescription(receiver).get().toPlain())
							: Text.EMPTY)
					.append(Text.of(commandSpec.getUsage(receiver)))
					.build();

			helpList.add(helpTextBuilder);
		}
		
		helpList.sort(Text::compareTo);
		PaginationList.Builder paginationListBuilder = PaginationList.builder();
		paginationListBuilder.contents(helpList).title(Text.of(TextColors.DARK_GREEN, "Help for command \"" + parentCommand + "\"")).linesPerPage(10).sendTo(receiver);
	}
}
