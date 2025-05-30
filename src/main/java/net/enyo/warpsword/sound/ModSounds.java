package net.enyo.warpsword.sound;

import net.enyo.warpsword.warpsword;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, warpsword.MOD_ID);

    // rift pop in sound
    public static final RegistryObject<SoundEvent> RIFT_JUMP = SOUNDS.register("rift_jump",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(warpsword.MOD_ID, "rift_jump"))
    );


    // rift pop out sound
    public static final RegistryObject<SoundEvent> RIFT_POP_OUT = SOUNDS.register("rift_pop_out",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(warpsword.MOD_ID, "rift_pop_out"))
    );
}
