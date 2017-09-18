package net.arcanemc.hubchallenges.user;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

import net.arcanemc.core.Core;
import net.arcanemc.core.api.user.User;
import net.arcanemc.hubchallenges.HubChallenges;
import net.arcanemc.hubchallenges.challenge.Challenge;
import net.arcanemc.hubchallenges.challenge.ChallengeManager;
import net.arcanemc.hubchallenges.parkour.ParkourMap;

public class UserFile {
	private UUID player;
	private ArrayList<UserChallengeData> data;
	
	public UserFile(UUID player_, ChallengeManager manager) {
		this.data = new ArrayList<UserChallengeData>();
		this.player = player_;
		for (Challenge challenge : manager.getChallenges()) {
			this.data.add(UserChallengeData.unserialize(player, challenge.getId()));
		}
	}
	
	public void serialize() {
		for (UserChallengeData data_ : this.data) {
			data_.serialize();
		}
	}
	
	public UserChallengeData getData(String challengeId) {
		for (UserChallengeData data : this.data) {
			if(data.getChallengeId().equals(challengeId)) {
				return data;
			}
		}
		return null;
	}
	
	public ArrayList<UserChallengeData> getData() {
		return this.data;
	}
	
	public void setData(UserChallengeData challenge) {
		int index = -1;
		for (UserChallengeData data : this.data) {
			if (data.getChallengeId().equals(challenge.getChallengeId())) {
				index = this.data.indexOf(data);
			}
		}
		if (index >= 0)
		this.data.set(index, challenge);
	}
	
	public void setAllCompleteable(boolean completeable) {
		for (UserChallengeData challenge : this.data) {
			challenge.setCompleteableThisMap(completeable);
		}
	}
	
	public UUID getOwner() {
		return this.player;
	}
	
	public void checkChallenges(ParkourMap map, ChallengeManager chManager) {
		for (UserChallengeData chData : this.data) {
			if (chData.isCompleteableThisMap()) {
				if (!chData.isCompleted()) {
					if (chData.getCurrentRepetitions() < chManager.getChallenge(chData.getChallengeId()).getNeededRepetitions()) {
						if (chManager.getChallenge(chData.getChallengeId()).needsDifferentMap() && !chData.isCompletedOn(map.getId())) {
						chData.addRepeat();
						chData.addMapCompletedOn(map.getId());
						} else if (!chManager.getChallenge(chData.getChallengeId()).needsDifferentMap()){
							chData.addRepeat();
							chData.addMapCompletedOn(map.getId());
						}
					}
					if (chData.getCurrentRepetitions() == chManager.getChallenge(chData.getChallengeId()).getNeededRepetitions()) {
						if (Core.getUserManager().getUser(this.player).isPresent()) {
							User user = Core.getUserManager().getUser(this.player).get();
							user.setCoins(user.getCoins() + chManager.getChallenge(chData.getChallengeId()).getCoinAmount());
						} else {
							Bukkit.getPlayer(player).sendMessage(HubChallenges.formatTextForHub("Uh Oh, we couldn't find you! Contact an ADMIN if you see this!"));
						}
						Bukkit.getPlayer(player).sendMessage(HubChallenges.formatTextForHub("You got: " + chManager.getChallenge(chData.getChallengeId()).getCoinAmount() + " coins for beating: " + chManager.getChallenge(chData.getChallengeId()).getName()));
						chData.setCompleted(true);
					}
				}
			}
		}
	}
}
