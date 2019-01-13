package io.github.iclickhd.factions.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.User;

import io.github.iclickhd.factions.statictext.CommandArgumentKeys;

public class PlayerInfoCommand extends AbstractCommand 
{
	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException 
	{
		Optional<User> optionalUser = context.<User>getOne(CommandArgumentKeys.PLAYER_NAME_COMMAND_ARGUMENT);
		if (optionalUser.isPresent()) {

		} else {

		}
		return CommandResult.success();
	}
}
