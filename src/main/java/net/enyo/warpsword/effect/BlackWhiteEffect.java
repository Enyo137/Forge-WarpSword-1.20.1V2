package net.enyo.warpsword.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BlackWhiteEffect extends MobEffect {

    public BlackWhiteEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x7F7F7F); // gray color
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // No direct effect needed here; just the tint
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}