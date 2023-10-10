package com.minecraftman.tagcore.queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class QueueManager {
	private static final ArrayList<Player> queue = new ArrayList<>();
	
	public static ArrayList<Player> getQueue() {
		return queue;
	}
	
	public static boolean addPlayer(Player player) {
		if (!isInQueue(player)) {
			if (Bukkit.getOnlinePlayers().size() > 1) {
				queue.add(player);
				return true;
			}
		}
		return false;
	}
	
	/*
	public static boolean insertPlayer(Player player, int pos) {
		if (!isInQueue(player)) {
			if (Bukkit.getOnlinePlayers().size() > 1) {
				queue.add(pos, player);
				return true;
			}
		}
		return false;
	}
	 */
	
	public static boolean removePlayer(Player player) {
		if (isInQueue(player)) {
			queue.remove(player);
			return true;
		}
		return false;
	}
	
	public static boolean isInQueue(Player player) {
		return queue.contains(player);
	}
	
	public static int getQueuePosition(Player player) {
		if (isInQueue(player)) {
			return queue.indexOf(player) + 1;
		}
		return -1;
	}
	
	public static int getQueueLength() {
		return queue.size();
	}
	
	public static void clearQueue() {
			queue.clear();
		}
}