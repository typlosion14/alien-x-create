package com.typlosion.alienxcreate;

import com.typlosion.alienxcreate.worldgen.GeodeTitanium;
import com.typlosion.alienxcreate.worldgen.ModWorldGenProvider;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AlienXCreateMod.MODID)
public class AlienXCreateMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "alienxcreate";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "alienxcreate" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "alienxcreate" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "alienxcreate" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Block> SMALL_TITANIUM_BUD =
            BLOCKS.register("small_titanium_bud",
                    () -> new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.copy(Blocks.SMALL_AMETHYST_BUD)));
    public static final RegistryObject<Block> MEDIUM_TITANIUM_BUD =
            BLOCKS.register("medium_titanium_bud",
                    () -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.copy(Blocks.MEDIUM_AMETHYST_BUD)));
    public static final RegistryObject<Block> LARGE_TITANIUM_BUD =
            BLOCKS.register("large_titanium_bud",
                    () -> new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.copy(Blocks.LARGE_AMETHYST_BUD)));
    public static final RegistryObject<Block> TITANIUM_CLUSTER =
            BLOCKS.register("titanium_cluster",
                    () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)));
    public static final RegistryObject<Block> BUDDING_TITANIUM =
            BLOCKS.register("budding_titanium",
                    () -> new BuddingTitaniumBlock( BlockBehaviour.Properties.copy(Blocks.BUDDING_AMETHYST)));
    // Creates a new BlockItem with the id "alienxcreate:example_block", combining the namespace and path
    public static final RegistryObject<Item> BUDDING_TITANIUM_ITEM = ITEMS.register("budding_titanium", () -> new BlockItem(BUDDING_TITANIUM.get(), new Item.Properties()));
    public static final RegistryObject<Item> TITANIUM_CLUSTER_ITEM = ITEMS.register("titanium_cluster", () -> new BlockItem(TITANIUM_CLUSTER.get(), new Item.Properties()));

    public static final RegistryObject<Block> SMALL_GALVAN_BUD =
            BLOCKS.register("small_galvan_bud",
                    () -> new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.copy(Blocks.SMALL_AMETHYST_BUD)));
    public static final RegistryObject<Block> MEDIUM_GALVAN_BUD =
            BLOCKS.register("medium_galvan_bud",
                    () -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.copy(Blocks.MEDIUM_AMETHYST_BUD)));
    public static final RegistryObject<Block> LARGE_GALVAN_BUD =
            BLOCKS.register("large_galvan_bud",
                    () -> new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.copy(Blocks.LARGE_AMETHYST_BUD)));
    public static final RegistryObject<Block> GALVAN_CLUSTER =
            BLOCKS.register("galvan_cluster",
                    () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)));
    public static final RegistryObject<Block> BUDDING_GALVAN =
            BLOCKS.register("budding_galvan",
                    () -> new BuddingGalvanBlock( BlockBehaviour.Properties.copy(Blocks.BUDDING_AMETHYST)));
    // Creates a new BlockItem with the id "alienxcreate:example_block", combining the namespace and path
    public static final RegistryObject<Item> BUDDING_GALVAN_ITEM = ITEMS.register("budding_galvan", () -> new BlockItem(BUDDING_GALVAN.get(), new Item.Properties()));
    public static final RegistryObject<Item> GALVAN_CLUSTER_ITEM = ITEMS.register("galvan_cluster", () -> new BlockItem(GALVAN_CLUSTER.get(), new Item.Properties()));

    public static final RegistryObject<Item> TITANIUM_NUGGET = ITEMS.register("titanium_nugget", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INCOMPLETE_GALVAN_PLATE = ITEMS.register("incomplete_galvan_plate", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INCOMPLETE_GALVAN_CIRCUIT = ITEMS.register("incomplete_galvan_circuit", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INCOMPLETE_OMNITRIX = ITEMS.register("incomplete_omnitrix", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INCOMPLETE_OMNIVERSE_OMNITRIX = ITEMS.register("incomplete_omniverse_omnitrix", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INCOMPLETE_REINFORCED_GALVAN_PLATE = ITEMS.register("incomplete_reinforced_galvan_plate", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INCOMPLETE_CODE_TABLET = ITEMS.register("incomplete_tablet", () -> new Item(new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> TITANIUM_TAB =
            CREATIVE_MODE_TABS.register("titanium_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("creativetab.alienxcreate.titanium_tab"))
                            .icon(() -> new ItemStack(INCOMPLETE_OMNITRIX.get()))
                            .displayItems((params, output) -> {

                                output.accept(BUDDING_TITANIUM_ITEM.get());
                                output.accept(TITANIUM_CLUSTER_ITEM.get());

                                output.accept(BUDDING_GALVAN_ITEM.get());
                                output.accept(GALVAN_CLUSTER_ITEM.get());

                                output.accept(TITANIUM_NUGGET.get());
                            })
                            .build());



    public AlienXCreateMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);
        MixinBootstrap.init();
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        FEATURES.register(modEventBus);
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // On ajoute le provider de WorldGen
        generator.addProvider(event.includeServer(),
                new ModWorldGenProvider(output, lookupProvider));
    }


    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, MODID);

    public static final RegistryObject<Feature<GeodeConfiguration>> TITANIUM_GEODE =
            FEATURES.register("titanium_geode",
                    () -> new GeodeFeature(GeodeConfiguration.CODEC));
    public static final ResourceKey<PlacedFeature> TITANIUM_GEODE_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(MODID, "titanium_geode_placed"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> TITANIUM_GEODE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(MODID, "titanium_geode"));


    public class ModConfiguredFeatures {



        public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
            GeodeTitanium.init();
            context.register(
                    TITANIUM_GEODE_KEY,
                    new ConfiguredFeature<>(
                            TITANIUM_GEODE.get(),
                            GeodeTitanium.TITANIUM_GEODE_CONFIG
                    )
            );
        }

    }
    public class ModPlacedFeatures  {
        public static void bootstrap(BootstapContext<PlacedFeature> context) {

            HolderGetter<ConfiguredFeature<?, ?>> configured =
                    context.lookup(Registries.CONFIGURED_FEATURE);

            Holder<ConfiguredFeature<?, ?>> geode =
                    configured.getOrThrow(TITANIUM_GEODE_KEY);

            context.register(
                    TITANIUM_GEODE_PLACED,
                    new PlacedFeature(
                            geode,
                            List.of(
                                    RarityFilter.onAverageOnceEvery(50),
                                    InSquarePlacement.spread(),
                                    HeightRangePlacement.uniform(
                                            VerticalAnchor.absolute(-58),
                                            VerticalAnchor.absolute(30)
                                    ),
                                    BiomeFilter.biome()
                            )
                    )
            );
        }
    }



    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        }
    }
}
