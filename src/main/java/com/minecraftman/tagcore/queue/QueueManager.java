package com.minecraftman.tagcore.queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class QueueManager {
// ONLINE QUEUE MANAGEMENT
	public static class onlineManager {
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
	
/*
// Commented out since not used as of now
// I made this because it seemed logical, but it has literally no use for my purpose
// (there will only be a couple of players in the queue at any given moment, so if they log off it doesn't matter)

// OFFLINE QUEUE MANAGEMENT
	public static class offlineManager {
		private static final HashMap<OfflinePlayer, Integer> offlineQueue = new HashMap<>();
		
		public static boolean addOfflinePlayer(OfflinePlayer player, int postion) {
			if (!isInOfflineQueue(player)) {
				offlineQueue.put(player, postion);
				return true;
			}
			return false;
		}
		
		public static boolean removeOfflinePlayer(OfflinePlayer player) {
			if (isInOfflineQueue(player)) {
				offlineQueue.remove(player);
				return true;
			}
			return false;
		}
		
		public static boolean isInOfflineQueue(OfflinePlayer player) {
			return offlineQueue.containsKey(player);
		}
		
		public static int getPreviousPosition(OfflinePlayer player) {
			return offlineQueue.get(player);
		}
	}
*/
}