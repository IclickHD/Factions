package io.github.iclickhd.factions.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

public class TaxCommand extends AbstractCommand {
	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		source.sendMessage(Text.of("Tax Command"));
		return CommandResult.success();
	}

}
