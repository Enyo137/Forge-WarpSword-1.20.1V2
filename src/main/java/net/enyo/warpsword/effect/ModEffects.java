package net.enyo.warpsword.effect;

import net.enyo.warpsword.warpsword;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, warpsword.MOD_ID);

    // Register your black and white effect here
    public static final RegistryObject<MobEffect> BLACK_WHITE = EFFECTS.register("black_white", BlackWhiteEffect::new);



}