package net.enyo.warpsword.item;

import net.enyo.warpsword.warpsword;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Tiers;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, warpsword.MOD_ID);

    public static final RegistryObject<Item> warpshard = ITEMS.register("warpshard",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WARPSWORD = ITEMS.register("warpsword",
            () -> new WarpswordItem(
                    Tiers.NETHERITE, // Or your own custom tier
                    5,               // Attack damage
                    -2.4F,           // Attack speed
                    new Item.Properties().stacksTo(1).durability(6031).fireResistant()
            )
    );
    public static void register(IEventBus eventBus)  {
        ITEMS.register(eventBus);
    }
}
