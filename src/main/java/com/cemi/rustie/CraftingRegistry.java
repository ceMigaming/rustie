package com.cemi.rustie;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.item.ItemStack;

public class CraftingRegistry {
	
	private static List<List<Pair<ItemStack, Integer>>> RECIPES = new ArrayList<>();
	
	private static List<Triple<ItemStack, Integer, String>> OUTPUTS = new ArrayList<>();
	
	
	
	public static List<Pair<ItemStack, Integer>> getMaterialsFromId(int i) {
		return RECIPES.get(i);
	}
	
	public static Triple<ItemStack, Integer, String> getOutputFromId(int i) {
		return OUTPUTS.get(i);
	}
	
	public static int getCount() {
		return OUTPUTS.size();
	}
	
	public static void registerRecipe(Triple<ItemStack, Integer, String> output, List<Pair<ItemStack, Integer>> pairs) {
		OUTPUTS.add(output);
		RECIPES.add(pairs);
	}
	
	public static void registerRecipe(Pair<ItemStack, Integer> output, List<Pair<ItemStack, Integer>> pairs) {
		OUTPUTS.add(Triple.of(output.getLeft(), output.getRight(), "No description available"));
		RECIPES.add(pairs);
	}
	
	public static void registerRecipes() {
		
	}
	
}
