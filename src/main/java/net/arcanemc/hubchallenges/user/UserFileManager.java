package net.arcanemc.hubchallenges.user;

import java.util.ArrayList;
import java.util.UUID;

public class UserFileManager {
	private ArrayList<UserFile> files;
	
	public UserFileManager() {
		this.files = new ArrayList<UserFile>();
	}
	
	public ArrayList<UserFile> getFiles() {
		return this.files;
	}
	
	public UserFile getFile(UUID player) {
		for (UserFile file : this.files) {
			if (file.getOwner().equals(player)) {
				return file;
			}
		}
		return null;
	}
	
	public void addFile(UserFile file) {
		this.files.add(file);
	}
	
	public void serializeAndDelete(UUID uuid) {
		UserFile file = this.getFile(uuid);
		file.serialize();
		this.files.remove(file);
	}
}
