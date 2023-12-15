package com.minecraftman.tagcore.utils;

import org.bukkit.inventory.Inventory;

import java.io.*;

public class InventoryUtils {
	public static byte[] serializeInventory(Inventory inventory) {
		// Serialize the inventory to a byte array
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		     ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
			objectOutputStream.writeObject(inventory);
			return outputStream.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Inventory deserializePlayerInventory(byte[] data) {
		if (data == null) return null;
		// Deserialize the byte array into a PlayerInventory object
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		     ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
			return (Inventory) objectInputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}


/*
Original Code:

public class DataProcessor {
    public static byte[] serializePlayerInventory(PlayerInventory inventory) throws IOException {
        // Serialize the inventory to a byte array
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(inventory);
            return outputStream.toByteArray();
        }
    }

    public static void deserializePlayerInventory(PlayerInventory inventory, byte[] data) throws IOException, ClassNotFoundException {
        // Deserialize the byte array into a PlayerInventory object
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            PlayerInventory deserializedInventory = (PlayerInventory) objectInputStream.readObject();

            // Clear the current inventory and set it with the deserialized inventory
            inventory.clear();
            inventory.setContents(deserializedInventory.getContents());
            inventory.setArmorContents(deserializedInventory.getArmorContents());
        }
    }
}
 */