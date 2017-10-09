package net.arcanemc.hubchallenges.challenge;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import net.arcanemc.hubchallenges.HubChallenges;

public class ChallengeManager {
	private ArrayList<Challenge> challenges;
	
	public ChallengeManager() {
		challenges = new ArrayList<Challenge>();
		Challenge underThirty = new Challenge("underThirty", "Under Thirty", "Beat a map in under thirty seconds.", 1, false, 4000);
		Challenge noStop = new Challenge("noStop", "Can't Stop", "Beat every map while sprinting continuously.", 4, true, 1000);
		Challenge noCrouch = new Challenge("noCrouch", "No Crouching", "Beat every map witout crouching.", 4, true, 8000);
		Challenge underThirtyNoCrouch = new Challenge("underThirtyNoCrouch", "Can't Stop, Won't Crouch", "Beat a map in under thirty seconds without crouching.", 1, false, 8000);
		
		this.addChallenge(underThirty);
		this.addChallenge(noStop);
		this.addChallenge(noCrouch);
		this.addChallenge(underThirtyNoCrouch);
	}
	
	public Challenge getChallenge(String id) {
		for (Challenge challenge : this.challenges) {
			if (challenge.getId().equals(id)) {
				return challenge;
			}
		}
		return null;
	}
	
	public ArrayList<Challenge> getChallenges(){
		return this.challenges;
	}
	
	private void addChallenge(Challenge challenge) {
		if (!this.challenges.contains(challenge)) {
			challenges.add(challenge);
		}
	}
	
	public void createTables() {
		for (Challenge ch : this.challenges) {
			String create = "CREATE TABLE IF NOT EXISTS " + ch.getId() + " (uuid VARCHAR(36), completed BIT, challengeid VARCHAR(25), repetitions INT, mapsCompleted VARCHAR(400))";
			try {
				PreparedStatement stmnt = HubChallenges.getConnection().prepareStatement(create);
				stmnt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	Complete a track in 30 seconds. (x/1)
	Complete all 4 tracks, each without stopping. (x/4)
	Complete all 4 tracks without crouching. (x/4)
	Complete a track in 30 seconds without crouching. (x/1)
	Complete a track on a "Windy Day". (x/1)
	Can we throw particles at them randomly?
	
	4000 Coins.
	1000 Coins.
	8000 Coins.
	8000 Coins.
	2000 Coins.
	 */
}
