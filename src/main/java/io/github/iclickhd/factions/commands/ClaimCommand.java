package io.github.iclickhd.factions.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

public class ClaimCommand extends AbstractCommand {

	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		if(source instanceof Player) {

		} else {
			return CommandResult.success();
		}
		return null;
	}

}
