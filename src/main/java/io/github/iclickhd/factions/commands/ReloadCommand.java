package io.github.iclickhd.factions.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

public class ReloadCommand extends AbstractCommand {
	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		getPlugin().getMainConfig().reload();
		
		return CommandResult.success();
	}
}
