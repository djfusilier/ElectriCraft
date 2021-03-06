/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2017
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ElectriCraft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.Libraries.ReikaRecipeHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.IC2Handler;
import Reika.DragonAPI.ModInteract.ItemHandlers.IC2Handler.IC2Stacks;
import Reika.DragonAPI.ModInteract.RecipeHandlers.ThermalRecipeHelper;
import Reika.DragonAPI.ModRegistry.PowerTypes;
import Reika.ElectriCraft.Auxiliary.ElectriStacks;
import Reika.ElectriCraft.Items.ItemWirePlacer;
import Reika.ElectriCraft.Registry.BatteryType;
import Reika.ElectriCraft.Registry.ElectriCrafting;
import Reika.ElectriCraft.Registry.ElectriItems;
import Reika.ElectriCraft.Registry.ElectriOres;
import Reika.ElectriCraft.Registry.ElectriTiles;
import Reika.ElectriCraft.Registry.WireType;
import Reika.ElectriCraft.TileEntities.TileEntityFuse;
import Reika.ElectriCraft.TileEntities.TileEntityWirelessCharger;
import Reika.ElectriCraft.TileEntities.TileEntityWirelessCharger.ChargerTiers;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Auxiliary.RecipeManagers.RecipeHandler.RecipeLevel;
import Reika.RotaryCraft.Auxiliary.RecipeManagers.RecipesGrinder;
import Reika.RotaryCraft.Auxiliary.RecipeManagers.WorktableRecipes;
import Reika.RotaryCraft.Registry.BlockRegistry;
import Reika.RotaryCraft.Registry.ConfigRegistry;
import Reika.RotaryCraft.Registry.DifficultyEffects;
import cpw.mods.fml.common.registry.GameRegistry;

public class ElectriRecipes {

	public static void loadOreDict() {
		for (int i = 0; i < ElectriOres.oreList.length; i++) {
			ElectriOres ore = ElectriOres.oreList[i];
			OreDictionary.registerOre(ore.getDictionaryName(), ore.getOreBlock());
			OreDictionary.registerOre(ore.getProductDictionaryName(), ore.getProduct());
		}

		for (int i = 0; i < ElectriCrafting.craftingList.length; i++) {
			ElectriCrafting c = ElectriCrafting.craftingList[i];
			if (c.hasOreName()) {
				ItemStack is = c.getItem();
				String s = c.oreDictName;
				OreDictionary.registerOre(s, is);
			}
		}

		OreDictionary.registerOre("dustGlowstone", Items.glowstone_dust);
	}

	public static void addRecipes() {
		for (int i = 0; i < WireType.wireList.length; i++) {
			WireType wire = WireType.wireList[i];
			wire.addCrafting();
		}
		for (int i = 0; i < BatteryType.batteryList.length; i++) {
			BatteryType bat = BatteryType.batteryList[i];
			bat.addCrafting();
		}
		for (int i = 0; i < TileEntityWirelessCharger.ChargerTiers.tierList.length; i++) {
			ChargerTiers pad = TileEntityWirelessCharger.ChargerTiers.tierList[i];
			ItemStack is = ElectriTiles.WIRELESSPAD.getCraftedProduct();
			is.stackTagCompound = new NBTTagCompound();
			is.stackTagCompound.setInteger("tier", i);
			GameRegistry.addRecipe(new ShapedOreRecipe(is, pad.getRecipe()));
		}
		for (int i = 0; i < ElectriOres.oreList.length; i++) {
			ElectriOres ore = ElectriOres.oreList[i];
			ReikaRecipeHelper.addSmelting(ore.getOreBlock(), ore.getProduct(), ore.xpDropped);
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(ElectriItems.BOOK.getStackOf(), "RSR", "PPP", "PPP", 'R', "ingotGold", 'S', ItemStacks.steelingot, 'P', Items.paper));

		RecipesGrinder.getRecipes().addRecipe(new ItemStack(Items.diamond), ElectriCrafting.DIAMONDDUST.getItem());
		RecipesGrinder.getRecipes().addRecipe(ReikaItemHelper.lapisDye, ElectriCrafting.BLUEDUST.getItem());
		RecipesGrinder.getRecipes().addRecipe(new ItemStack(Items.quartz), ElectriCrafting.QUARTZDUST.getItem());

		ReikaRecipeHelper.addSmelting(ElectriCrafting.CRYSTALDUST.getItem(), ElectriItems.CRYSTAL.getStackOf(), 1F);
		GameRegistry.addRecipe(new ShapelessOreRecipe(ElectriCrafting.CRYSTALDUST.getItem(2), ElectriCrafting.BLUEDUST.oreDictName, ElectriCrafting.DIAMONDDUST.oreDictName, ElectriCrafting.QUARTZDUST.oreDictName, "dustGlowstone", "dustRedstone", "dustRedstone", "dustRedstone", "dustRedstone"));

		Object[] ctr = {Blocks.glowstone, Blocks.lapis_block, Items.ender_eye, Blocks.emerald_block, Items.nether_star};
		for (int i = 1; i < BatteryType.batteryList.length; i++) {
			ItemStack cry = ElectriItems.CRYSTAL.getStackOfMetadata(i-1);
			ItemStack is = ElectriItems.CRYSTAL.getStackOfMetadata(i);
			GameRegistry.addRecipe(is, "RCR", "CIC", "RCR", 'R', Items.redstone, 'C', cry, 'I', ctr[i-1]);
		}

		ItemStack w = ReikaItemHelper.getSizedItemStack(WireType.SUPERCONDUCTOR.getCraftedProduct(), DifficultyEffects.PIPECRAFT.getInt());
		ItemStack w2 = ReikaItemHelper.getSizedItemStack(WireType.SUPERCONDUCTOR.getCraftedInsulatedProduct(), 3);
		ShapedOreRecipe ir = new ShapedOreRecipe(w, "IGI", "SRS", "tgt", 't', ItemStacks.tungsteningot, 'I', ItemStacks.steelingot, 'G', BlockRegistry.BLASTGLASS.getBlockInstance(), 'S', "ingotSilver", 'g', "ingotGold", 'R', Items.redstone);
		Object[] obj2 = {"WwW", "WwW", "WwW", 'W', Blocks.wool, 'w', w};
		WorktableRecipes.getInstance().addRecipe(ir, RecipeLevel.CORE);
		WorktableRecipes.getInstance().addRecipe(w2, RecipeLevel.CORE, obj2);
		if (ConfigRegistry.TABLEMACHINES.getState()) {
			GameRegistry.addRecipe(ir);
			GameRegistry.addRecipe(w2, obj2);
		}

		ElectriTiles.GENERATOR.addOreCrafting("gts", "iGn", "ppp", 'n', "ingotNickel", 't', "ingotTin", 'p', ItemStacks.basepanel, 'g', "ingotCopper", 's', ItemStacks.steelingot, 'G', ItemStacks.generator, 'i', ItemStacks.impeller);
		ElectriTiles.MOTOR.addOreCrafting("scs", "gCg", "BcB", 'g', ItemStacks.goldcoil, 'c', "ingotCopper", 's', "ingotSilver", 'S', ItemStacks.steelingot, 'B', ItemStacks.basepanel, 'C', ItemStacks.shaftcore);
		ElectriTiles.RELAY.addSizedOreCrafting(4, "SCS", "CPC", 'C', "ingotCopper", 'P', ItemStacks.basepanel, 'S', ItemStacks.steelingot);
		ElectriTiles.RESISTOR.addSizedOreCrafting(4, "SCS", "PCP", 'C', "dustCoal", 'S', ItemStacks.steelingot, 'P', ItemStacks.basepanel);
		ElectriTiles.METER.addCrafting("SsS", "wCw", "SbS", 'S', ItemStacks.steelingot, 'w', WireType.SILVER.getCraftedProduct(), 'C', ItemStacks.pcb, 's', ItemStacks.screen, 'b', ItemStacks.basepanel);
		ElectriTiles.TRANSFORMER.addCrafting("SSS", "I I", "SSS", 'S', ItemStacks.basepanel, 'I', ItemStacks.redgoldingot);

		ItemStack[] FUSE_INGOTS = {
				ItemStacks.coaldust.copy(),
				ItemStacks.steelingot.copy(),
				ElectriStacks.copperIngot.copy(),
				new ItemStack(Items.gold_ingot)
		};

		for (int i = 0; i < FUSE_INGOTS.length; i++) {
			Object ingot = FUSE_INGOTS[i];
			NBTTagCompound tag = new TileEntityFuse(TileEntityFuse.TIERS[i]).getTagsToWriteToStack();
			ElectriTiles.FUSE.addSizedOreNBTCrafting(8, tag, " G ", "GCG", "PPP", 'G', Blocks.glass, 'C', ingot, 'P', ItemStacks.basepanel, 'S', ItemStacks.steelingot);
		}
	}

	public static void addPostLoadRecipes() {
		if (PowerTypes.RF.isLoaded()) {
			ElectriTiles.CABLE.addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), "RDR", "BGB", "RER", 'D', Items.diamond, 'R', Blocks.redstone_block, 'G', Blocks.gold_block, 'E', Items.ender_pearl, 'B', BlockRegistry.BLASTGLASS.getStackOf());

			Object[] obj = {"ScS", "WCW", "tPt", 't', ItemStacks.tungsteningot, 'W', Blocks.wool, 'c', ItemStacks.redgoldingot, 'C', ElectriItems.CRYSTAL.getStackOfMetadata(BatteryType.batteryList.length), 'P', ItemStacks.bedingot, 'S', ItemStacks.steelingot};
			ShapedOreRecipe ir2 = new ShapedOreRecipe(ElectriTiles.RFBATTERY.getCraftedProduct(), obj);
			WorktableRecipes.getInstance().addRecipe(ir2, RecipeLevel.CORE);
			if (ConfigRegistry.TABLEMACHINES.getState()) {
				GameRegistry.addRecipe(ir2);
			}

			ItemStack cry = ElectriItems.CRYSTAL.getStackOfMetadata(BatteryType.batteryList.length-1);
			ItemStack out = ElectriItems.CRYSTAL.getStackOfMetadata(BatteryType.batteryList.length);
			GameRegistry.addRecipe(out, "RRR", "RCR", "RRR", 'R', Blocks.redstone_block, 'C', cry);
		}

		if (PowerTypes.EU.isLoaded() && ModList.IC2.isLoaded()) {
			ElectriTiles.EUSPLIT.addOreCrafting("PCP", "CcC", "PCP", 'P', ItemStacks.basepanel, 'C', "ingotCopper", 'c', ItemStacks.goldcoil);
			ItemStack dust = IC2Handler.getInstance().isIC2Classic() ? IC2Handler.IC2Stacks.ADVANCEDALLOY.getItem() : IC2Handler.IC2Stacks.ENERGIUM.getItem();
			ElectriTiles.EUCABLE.addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), "RDR", "BGB", "tEt", 'D', Items.diamond, 'R', dust, 't', ItemStacks.tungsteningot, 'G', Blocks.gold_block, 'E', Items.ender_pearl, 'B', BlockRegistry.BLASTGLASS.getStackOf());

			ItemStack plate = IC2Handler.IC2Stacks.ADVANCEDALLOY.getItem();
			Object[] obj = {"ScS", "WCW", "tPt", 't', ItemStacks.tungsteningot, 'W', plate, 'c', ItemStacks.redgoldingot, 'C', ElectriItems.CRYSTAL.getStackOfMetadata(BatteryType.batteryList.length+1), 'P', ItemStacks.bedingot, 'S', ItemStacks.steelingot};
			ShapedOreRecipe ir2 = new ShapedOreRecipe(ElectriTiles.EUBATTERY.getCraftedProduct(), obj);
			WorktableRecipes.getInstance().addRecipe(ir2, RecipeLevel.CORE);
			if (ConfigRegistry.TABLEMACHINES.getState()) {
				GameRegistry.addRecipe(ir2);
			}

			ItemStack cry = ElectriItems.CRYSTAL.getStackOfMetadata(BatteryType.batteryList.length-1);
			ItemStack out = ElectriItems.CRYSTAL.getStackOfMetadata(BatteryType.batteryList.length+1);
			GameRegistry.addRecipe(out, "fLf", "LCL", "RLG", 'f', IC2Stacks.CARBONFIBER.getItem(), 'R', Blocks.redstone_block, 'G', Blocks.glowstone, 'C', cry, 'L', ReikaItemHelper.getAnyMetaStack(IC2Handler.IC2Stacks.LAPOTRON.getItem()));
		}

		if (ModList.THERMALFOUNDATION.isLoaded()) {
			ItemStack is = WireType.SUPERCONDUCTOR.getCraftedProduct();
			ItemStack is2 = WireType.SUPERCONDUCTOR.getCraftedInsulatedProduct();
			ItemWirePlacer item = (ItemWirePlacer)ElectriItems.WIRE.getItemInstance();
			FluidStack f1 = new FluidStack(FluidRegistry.getFluid("rc liquid nitrogen"), item.getCapacity(is));
			FluidStack f2 = new FluidStack(FluidRegistry.getFluid("cryotheum"), item.getCapacity(is));
			ThermalRecipeHelper.addFluidTransposerFill(is, item.getFilledSuperconductor(false), 200, f1);
			ThermalRecipeHelper.addFluidTransposerFill(is, item.getFilledSuperconductor(false), 200, f2);
			ThermalRecipeHelper.addFluidTransposerFill(is2, item.getFilledSuperconductor(true), 200, f1);
			ThermalRecipeHelper.addFluidTransposerFill(is2, item.getFilledSuperconductor(true), 200, f2);
		}
	}

}
