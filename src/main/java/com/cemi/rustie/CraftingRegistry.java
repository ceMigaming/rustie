package com.cemi.rustie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.cemi.rustie.item.RustieItems;

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
	
	@SafeVarargs
	public static void registerRecipe(Triple<ItemStack, Integer, String> output, Pair<ItemStack, Integer> ... pairs) {
		OUTPUTS.add(output);
		RECIPES.add(Arrays.asList(pairs));
	}
	
	@SafeVarargs
	public static void registerRecipe(Pair<ItemStack, Integer> output, Pair<ItemStack, Integer> ... pairs) {
		OUTPUTS.add(Triple.of(output.getLeft(), output.getRight(), "No description available"));
		RECIPES.add(Arrays.asList(pairs));
	}
	
	public static void registerRecipes() {
		registerRecipe(Pair.of(new ItemStack(RustieItems.bandage), 1),  Pair.of(new ItemStack(RustieItems.cloth), 4));
		registerRecipe(Pair.of(new ItemStack(RustieItems.lowGradeFuel), 4),  Pair.of(new ItemStack(RustieItems.animalFat), 3), Pair.of(new ItemStack(RustieItems.cloth), 1));
	}
	
}
