package io.github.iclickhd.factions.managers;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

public class UserManager {
	public Optional<User> getUser(UUID userUUID) {
        UserStorageService userStorageService = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        Optional<User> optionalUser = userStorageService.get(userUUID);
        
        return optionalUser.isPresent() ? optionalUser : Optional.empty();
	}
	
	public boolean isUserOnline(UUID userUUID) {
		Optional<User> optionalUser = getUser(userUUID);
		if(optionalUser.isPresent()) {
			return optionalUser.get().isOnline();
		} else {
			return false;
		}
	}
	
	public boolean isUserOnline(User user) {
		return isUserOnline(user.getUniqueId());
	}
	
	public void addUser() {
		
	}
}
