package com.minecraftman.tagcore.queue;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class QueueManager {
	TagCore main;
	
	private final ArrayList<Player> queue = new ArrayList<>();
	
	public ArrayList<Player> getQueue() {
		return queue;
	}
	
	public QueueManager(TagCore main) {
		this.main = main;
	}
	
	public boolean addPlayer(Player player) {
		if (!isInQueue(player)) {
			if (Bukkit.getOnlinePlayers().size() > 1) {
				queue.add(player);
				return true;
			}
		}
		return false;
	}
	
	/*
	public boolean insertPlayer(Player player, int pos) {
		if (!isInQueue(player)) {
			if (Bukkit.getOnlinePlayers().size() > 1) {
				queue.add(pos, player);
				return true;
			}
		}
		return false;
	}
	 */
	
	public boolean removePlayer(Player player) {
		return queue.remove(player);
	}
	
	public boolean isInQueue(Player player) {
		return queue.contains(player);
	}
	
	public int getQueueLength() {
		return queue.size();
	}
	
	public void clearQueue() {
			queue.clear();
		}
}