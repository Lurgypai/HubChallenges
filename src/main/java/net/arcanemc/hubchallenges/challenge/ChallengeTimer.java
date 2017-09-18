package net.arcanemc.hubchallenges.challenge;

import org.bukkit.Bukkit;

import net.arcanemc.hubchallenges.HubChallenges;

public class ChallengeTimer {
	
	private Task task = new Task();
	private int taskId;
	private HubChallenges plugin;
	
	public ChallengeTimer(HubChallenges plugin_) {
		this.plugin = plugin_;
	}
	
	public void startTimer() {
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, 0, 1);
	}
	
	public void resetTimer() {
		Bukkit.getScheduler().cancelTask(taskId);
		this.task.timeInTicks = 0;
		this.task.timeInSeconds = 0;
	}
	
	public long getTicks() {
		return this.task.timeInTicks;
	}
	
	public long getSeconds() {
		return this.task.timeInSeconds;
	}
	
	private class Task implements Runnable {

		protected long timeInTicks;
		protected long timeInSeconds;
		
		@Override
		public void run() {
			timeInTicks++;
			if(timeInTicks % 20 == 0) {
				timeInSeconds++;
			}
			
			if (timeInTicks > 9000000000000000000L || timeInSeconds > 9000000000000000000L) {
				timeInTicks = 0;
				timeInSeconds = 0;
			}
		}
		
	}
}
