package net.enyo.warpsword.client;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RiftBeamHandler {

    public static void applyBeamDamage(Level level, Player player, Vec3 start, Vec3 end) {
        AABB hitBox = new AABB(start, end).inflate(1.0);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, hitBox, e -> !e.is(player));

        for (LivingEntity entity : targets) {
            entity.hurt(player.damageSources().magic(), 6.0F);
        }
    }
}
