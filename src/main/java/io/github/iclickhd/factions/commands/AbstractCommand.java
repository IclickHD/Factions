package io.github.iclickhd.factions.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import io.github.iclickhd.factions.objects.AbstractPluginObject;

public abstract class AbstractCommand extends AbstractPluginObject implements CommandExecutor {
    @Override
    public abstract CommandResult execute(CommandSource source, CommandContext context) throws CommandException;
}
