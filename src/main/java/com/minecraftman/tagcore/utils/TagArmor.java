package com.minecraftman.tagcore.utils;

//import com.mojang.authlib.GameProfile;
//import com.mojang.authlib.properties.Property;
//import org.bukkit.Color;
//import org.bukkit.Material;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.inventory.ItemFlag;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.LeatherArmorMeta;
//import org.bukkit.inventory.meta.SkullMeta;
//
//import java.lang.reflect.Field;
//import java.util.UUID;
//
public class TagArmor {
//	public static ItemStack getTaggerSkull() {
//		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
//		SkullMeta meta = (SkullMeta) skull.getItemMeta();
//
//		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
//		profile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxMzE1MTU0OTA5OSwKICAicHJvZmlsZUlkIiA6ICI2NjEwNjRmMjk1MDU0ZTU3OTZkYTM0MDljNTQzNjY1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJFdGVybmFsIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FiZTRiODg4ODE3YTkyOTUzNTgyNTk3ZTA2NDU5Y2I4OTJkMjdjZjI1MzliOTRmZTgwZGY3OTA0ZTAxOTRiNGEiCiAgICB9CiAgfQp9"));
//		Field field;
//		try {
//			field = meta.getClass().getDeclaredField("profile");
//			field.setAccessible(true);
//			field.set(meta, profile);
//		} catch (NoSuchFieldException | IllegalAccessException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//
//		skull.setItemMeta(meta);
//		skull.addEnchantment(Enchantment.BINDING_CURSE, 1);
//
//		return skull;
//	}
//
//	public static ItemStack getTaggerChestplate() {
//		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
//		LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
//		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//		meta.setColor(Color.RED);
//		meta.setDisplayName(Chat.translate("&cTagger clothes"));
//		chestplate.setItemMeta(meta);
//		chestplate.addEnchantment(Enchantment.BINDING_CURSE, 1);
//		return chestplate;
//	}
//
//	public static ItemStack getTaggerLeggings() {
//		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
//		LeatherArmorMeta meta = (LeatherArmorMeta) leggings.getItemMeta();
//		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//		meta.setColor(Color.RED);
//		meta.setDisplayName(Chat.translate("&cTagger clothes"));
//		leggings.setItemMeta(meta);
//		leggings.addEnchantment(Enchantment.BINDING_CURSE, 1);
//		return leggings;
//	}
//
//	public static ItemStack getTaggerBoots() {
//		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
//		LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();
//		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//		meta.setColor(Color.RED);
//		meta.setDisplayName(Chat.translate("&cTagger clothes"));
//		boots.setItemMeta(meta);
//		boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
//		return boots;
//	}
}