package com.typlosion.alienxcreate.worldgen;

import com.typlosion.alienxcreate.AlienXCreateMod;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public class GeodeTitanium {

    public static GeodeConfiguration TITANIUM_GEODE_CONFIG;

    public static void init() {
        TITANIUM_GEODE_CONFIG = new GeodeConfiguration(

                new GeodeBlockSettings(
                        BlockStateProvider.simple(Blocks.ANDESITE), // fillingProvider
                        BlockStateProvider.simple(Blocks.AIR), // innerLayerProvider
                        BlockStateProvider.simple(Blocks.AIR), // alternateInnerLayerProvider
                        BlockStateProvider.simple(Blocks.AIR), // middleLayerProvider
                        BlockStateProvider.simple(Blocks.ANDESITE), // outerLayerProvider

                        List.of(
                                AlienXCreateMod.BUDDING_TITANIUM.get().defaultBlockState(),
                                AlienXCreateMod.BUDDING_GALVAN.get().defaultBlockState()
                        ),

                        BlockTags.FEATURES_CANNOT_REPLACE,
                        BlockTags.GEODE_INVALID_BLOCKS
                ),

                new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D),
                new GeodeCrackSettings(0.95D, 2.0D, 2),

                0.35D,
                0.083D,
                true,

                UniformInt.of(4, 6),
                UniformInt.of(3, 4),
                UniformInt.of(1, 2),

                -16,
                16,

                0.05D,
                1
        );
    }
}