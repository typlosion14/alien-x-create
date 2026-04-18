package com.typlosion.alienxcreate;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;

// import create
import com.simibubi.create.content.kinetics.crank.HandCrankBlock;

import java.lang.reflect.Method;
import java.util.Set;


@Mod.EventBusSubscriber
public class ModEvents {
    public static final String AlienTransformedTag = "AlienEvo.Transformation";
    public static final String FourArmsTag = "Tetramand";
    public static final String HumungousaurTag = "Vaxasaurian"; // Didn't find the tags while being transform
    public static final String SwampfireTag = "afomni.vineprot"; // Should be Methanosian but find afomni.vineprot
    public static final String HeatBlastTag = "Pyronite";
    public static final String[] HeatAlien = {HeatBlastTag, SwampfireTag};
    public static final String[] StrongAlien = {FourArmsTag, HumungousaurTag};

    @SubscribeEvent
    public static void isUsingWhileBeingAlien(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide) return;

        BlockState state = level.getBlockState(event.getPos());
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        BlockEntity be = level.getBlockEntity(pos);
        Set<String> playerTags = player.getTags();

        if(playerTags.contains(AlienTransformedTag)){
            if (state.getBlock() instanceof HandCrankBlock && setContainsArray(playerTags, StrongAlien)) {
                onCrankUse(event, player);
            }else if (state.getBlock() instanceof BlazeBurnerBlock && be instanceof BlazeBurnerBlockEntity burner
                    && hasBlazeAndCanBeFuel(burner) && setContainsArray(playerTags, HeatAlien)) {
                onBlazeBurnerUse(burner, player, pos);
            }
        }


    }
    public static void onCrankUse(PlayerInteractEvent.RightClickBlock event, Player player) {
        player.getFoodData().setExhaustion(0.0F);
    }

    public static void onBlazeBurnerUse(BlazeBurnerBlockEntity burner, Player player, BlockPos pos) {
        ItemStack fuel = new ItemStack(Items.BLAZE_ROD);
        try {
            Method method = BlazeBurnerBlockEntity.class.getDeclaredMethod("tryUpdateFuel", ItemStack.class, boolean.class, boolean.class);
            method.setAccessible(true);
            boolean result = (boolean) method.invoke(burner, fuel, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean setContainsArray(Set<String> set1, String[] strArray) {
        boolean r = false;
        for (String key : strArray) {
            if(set1.contains(key)){
                r = set1.contains(key);
                break;
            }
        }
        return r;
    }

    private static boolean hasBlazeAndCanBeFuel(BlazeBurnerBlockEntity burner) {
        return burner.getRemainingBurnTime() < BlazeBurnerBlockEntity.MAX_HEAT_CAPACITY;
    }
}