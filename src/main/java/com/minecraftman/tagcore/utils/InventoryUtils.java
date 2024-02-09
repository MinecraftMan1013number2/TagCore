package com.minecraftman.tagcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InventoryUtils {
	/*
	This class was taken from here:
	https://gist.github.com/graywolf336/8153678
	
	Actual version used:
	https://web.archive.org/web/20231106034541/https://gist.github.com/graywolf336/8153678
	 */
	
	/**
	 * Converts the player inventory to a String array of Base64 strings. First string is the content and second string is the armor.
	 * Special thanks to Comphenix in the Bukkit forums or also known
	 * as aadnk on GitHub.
	 * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
	 *
	 * @param inventory to turn into a string.
	 */
	public static String serializeInventory(Inventory inventory) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			
			// Remove 5 from the size (4 armor slots ) since they cant be put into a Bukkit inventory and we dont need it
			// OffHand is handled separately
			int size = inventory.getSize();
			if (inventory instanceof PlayerInventory) {
				size -= 5;
			}
			
			// Write the size of the inventory
			dataOutput.writeInt(size);
			
			// Save every element in the list
			for (int i = 0; i < size; i++) {
				dataOutput.writeObject(inventory.getItem(i));
			}
			
			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save inventory.", e);
		}
	}
	
	/**
	 *
	 * A method to get an {@link Inventory} from an encoded, Base64, string.
	 * Special thanks to Comphenix in the Bukkit forums or also known
	 * as aadnk on GitHub.
	 * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
	 *
	 * @param data Base64 string of data containing an inventory.
	 * @return Inventory created from the Base64 string.
	 */
	public static Inventory deserializeInventory(String data) throws IOException {
		if (data == null || data.isEmpty()) return null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
			
			// Read the serialized inventory
			for (int i = 0; i < inventory.getSize(); i++) {
				inventory.setItem(i, (ItemStack) dataInput.readObject());
			}
			
			dataInput.close();
			return inventory;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}
	
	
	
	/**
	 *
	 * A method to serialize an {@link ItemStack} array to Base64 String.
	 *
	 * @param items to turn into a Base64 String.
	 * @return Base64 string of the items.
	 * @throws IllegalStateException
	 */
	public static String serializeItem(ItemStack[] items) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			
			// Write the size of the inventory
			dataOutput.writeInt(items.length);
			
			// Save every element in the list
			for (ItemStack item : items) {
				dataOutput.writeObject(item);
			}
			
			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}
	
	/**
	 * Gets an array of ItemStacks from Base64 string.
	 *
	 * @param data Base64 string to convert to ItemStack array.
	 * @return ItemStack array created from the Base64 string.
	 * @throws IOException
	 */
	public static ItemStack[] deserializeItem(String data) throws IOException {
		if (data == null || data.isEmpty()) return new ItemStack[]{null};
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack[] items = new ItemStack[dataInput.readInt()];
			
			// Read the serialized inventory
			for (int i = 0; i < items.length; i++) {
				items[i] = (ItemStack) dataInput.readObject();
			}
			
			dataInput.close();
			return items;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}
}