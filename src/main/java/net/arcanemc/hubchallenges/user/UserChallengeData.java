package net.arcanemc.hubchallenges.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import net.arcanemc.hubchallenges.HubChallenges;

public class UserChallengeData {
	private String challengeId;
	private UUID player;
	private int currentRepetitions;
	private ArrayList<String> mapsCompleted;
	private boolean completed;
	private boolean runCompletable;
	
	public UserChallengeData(UUID player_, String challengeId_) {
		this.player = player_;
		this.challengeId = challengeId_;
		this.currentRepetitions = 0;
		this.mapsCompleted = new ArrayList<String>();
		this.completed = false;
		this.runCompletable = false;
	}
	
	private UserChallengeData(String player_, int completed_, String challengeId_, int repetitions_, String mapsCompleted) {
		this.player = UUID.fromString(player_);
		this.completed = (completed_ == 1);
		this.challengeId = challengeId_;
		this.currentRepetitions = repetitions_;
		this.mapsCompleted = new ArrayList<String>(Arrays.asList(mapsCompleted.split(", ")));
		this.runCompletable = false;
	}
	
	public String getChallengeId() {
		return this.challengeId;
	}
	
	public UUID getPlayer() {
		return this.player;
	}
	
	public int getCurrentRepetitions() {
		return this.currentRepetitions;
	}
	
	public ArrayList<String> getMapsCompletedOn() {
		return this.mapsCompleted;
	}
	
	public void addMapCompletedOn(String mapId) {
		this.mapsCompleted.add(mapId);
	}
	
	public boolean isCompletedOn(String mapID) {
		return this.mapsCompleted.contains(mapID);
	}
	
	public boolean isCompleted() {
		return this.completed;
	}
	
	public boolean isCompleteableThisMap() {
		return this.runCompletable;
	}
	
	public void setCompleted(boolean completed_) {
		this.completed = completed_;
	}
	
	public void setCompleteableThisMap(boolean completable) {
		this.runCompletable = completable;
	}
	
	public void addRepeat() {
		this.currentRepetitions++;
	}
	
	public void serialize() {
		String select = "SELECT * FROM " + this.challengeId + " WHERE uuid=?";
		String update = "UPDATE " + this.challengeId + " SET completed=?, challengeid=?, repetitions=?, mapsCompleted=? WHERE uuid=?";
		String insert = "INSERT INTO " + this.challengeId + "(uuid, completed, challengeid, repetitions, mapsCompleted) VALUES (?,?,?,?,?)";
		try {
			PreparedStatement stmnt = HubChallenges.getConnection().prepareStatement(select);
			stmnt.setString(1, this.player.toString());
			ResultSet rs = stmnt.executeQuery();
			if(rs.next()) {
				PreparedStatement updateStmnt = HubChallenges.getConnection().prepareStatement(update);
				updateStmnt.setInt(1, this.completed ? 1 : 0);
				updateStmnt.setString(2, this.challengeId);
				updateStmnt.setInt(3, this.currentRepetitions);
				updateStmnt.setString(4, String.join(", ", this.mapsCompleted));
				updateStmnt.setString(5, this.player.toString());
				updateStmnt.executeUpdate();
			} else {
				PreparedStatement insertStmnt = HubChallenges.getConnection().prepareStatement(insert);
				insertStmnt.setString(1, this.player.toString());
				insertStmnt.setInt(2, this.completed ? 1 : 0);
				insertStmnt.setString(3, this.challengeId);
				insertStmnt.setInt(4, this.currentRepetitions);
				insertStmnt.setString(5, String.join(", ", this.mapsCompleted));
				insertStmnt.executeUpdate();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	
	public static UserChallengeData unserialize(UUID uuid, String challengeId) {
		String select = "SELECT * FROM " + challengeId + " WHERE uuid=?";
		try {
			PreparedStatement stmnt = HubChallenges.getConnection().prepareStatement(select);
			stmnt.setString(1, uuid.toString());
			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {
				return new UserChallengeData(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getString(5));
			} else {
				return new UserChallengeData(uuid, challengeId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
