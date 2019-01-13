package io.github.iclickhd.factions.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;

public class FactionHelpCommand extends AbstractHelpCommand {
	public FactionHelpCommand(String parentCommand, Map<List<String>, CommandSpec> commands) {
		super(parentCommand, commands);
	}

	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		sendHelpListTo(source);		
		return CommandResult.success();
	}
	
	public String joining(Iterator<String> iterator, String separator) {	
        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        
        final String first = iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        for (Iterator<String> iterator2 = iterator; iterator2.hasNext();) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
	}
}
