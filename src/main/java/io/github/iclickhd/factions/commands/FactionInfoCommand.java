package io.github.iclickhd.factions.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColors;

import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.models.FactionMember;
import io.github.iclickhd.factions.statictext.CommandArgumentKeys;
import io.github.iclickhd.factions.utils.TextUtils;

public class FactionInfoCommand extends AbstractCommand {
	@Override
	public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
		Optional<String> optionalFactionName = context.<String>getOne(CommandArgumentKeys.FACTION_NAME_COMMAND_ARGUMENT);
		if (optionalFactionName.isPresent()) {
			Optional<Faction> optionalFaction = getPlugin().getFactionLogic().getFaction(optionalFactionName.get());
			if (optionalFaction.isPresent()) {
				ShowFactionInfo(source, optionalFaction.get());
			} else {
				source.sendMessage(Text.of("This faction does not exist"));
			}
		} else {
			if (source instanceof Player) {
				Player player = (Player) source;
				Optional<Faction> faction = getPlugin().getFactionLogic().getFactionByPlayerUUID(player.getUniqueId());
				
				if (faction.isPresent()) {
					ShowFactionInfo(source, faction.get());
				} else {
					player.sendMessage(Text.of("You do not belong to any faction"));
				}
			}
		}

		
		return CommandResult.success();
	}

	public void ShowFactionInfo(MessageReceiver source, Faction faction) {
		Set<Text> onlineMembersName = new HashSet<Text>();
		Set<Text> offlineMembersName = new HashSet<Text>();
		for (FactionMember factionMember : faction.getMembers()) {
			Optional<User> optionalUser = getPlugin().getUserManager().getUser(factionMember.getUniqueId());
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				if (getPlugin().getUserManager().isUserOnline(user)) {
					onlineMembersName.add(Text.of(user.getName()));
				} else {
					offlineMembersName.add(Text.of(user.getName()));
				}
			}
		}

		List<Text> contents = new ArrayList<Text>();
		contents.add(Text.builder().append(Text.of("Online members (" + onlineMembersName.size() + ") : ")).append(TextUtils.join(onlineMembersName, Text.of(", "))).build());
		contents.add(Text.builder().append(Text.of("Offline members (" + offlineMembersName.size() + ") : ")).append(TextUtils.join(offlineMembersName, Text.of(", "))).build());
		contents.add(Text.of("Land / Power / Max power : " + faction.getClaims().size() + " / " + getPlugin().getFactionLogic().getFactionRoundedPower(faction.getUniqueId()) + " / " + getPlugin().getFactionLogic().getFactionRoundedMaxPower(faction.getUniqueId())));
		PaginationList.builder().title(Text.of(TextColors.GOLD, TextColors.DARK_GREEN, faction.getName(), TextColors.GOLD)).contents(contents).padding(Text.of("=")).sendTo(source);
	}
}
