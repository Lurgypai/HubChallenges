package net.arcanemc.hubchallenges.challenge;

import java.util.ArrayList;

public class Challenge {

	private String id;
	private String name;
	private String desc;
	private ArrayList<String> flags = new ArrayList<String>();
	private final int TOTAL_REPETITIONS;
	private boolean differentMap;
	private int coins;
	
	public Challenge(String id_, String name_, String desc_, int totalReps_, boolean diffMaps, int coins_) {
		this.id = id_;
		this.name = name_;
		this.desc = desc_;
		this.TOTAL_REPETITIONS = totalReps_;
		this.differentMap = diffMaps;
		this.coins = coins_;
	}
	
	public Challenge addFlag(String flagName_) {
		if (flags.contains(flagName_)) 
			flags.add(flagName_);
		return this;
	}
	
	public boolean needsDifferentMap() {
		return this.differentMap;
	}
	
	public int getNeededRepetitions() {
		return this.TOTAL_REPETITIONS;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.desc;
	}
	
	public int getCoinAmount() {
		return this.coins;
	}
}
