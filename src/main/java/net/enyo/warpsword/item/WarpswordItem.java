package net.enyo.warpsword.item;

import net.enyo.warpsword.effect.ModEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.enyo.warpsword.sound.ModSounds;

import java.util.HashMap;
import java.util.UUID;

import static net.enyo.warpsword.effect.RiftParticleHandler.spawnParticleExplosion;

public class WarpswordItem extends SwordItem {

    private static final HashMap<UUID, Long> riftCooldowns = new HashMap<>();

    public WarpswordItem(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        UUID playerId = player.getUUID();
        long currentTime = System.currentTimeMillis();

        if (!world.isClientSide()) {

            // ✅ If in rift, exit early
            if (player.hasEffect(ModEffects.BLACK_WHITE.get())) {
                player.removeEffect(ModEffects.BLACK_WHITE.get());
                player.removeEffect(MobEffects.NIGHT_VISION);
                player.removeEffect(MobEffects.BLINDNESS);
                player.removeEffect(MobEffects.INVISIBILITY);
                player.setInvisible(false);
                return InteractionResultHolder.success(stack);
            }

            // ⛔ Prevent entering if within cooldown window
            if (riftCooldowns.containsKey(playerId)) {
                long lastUsed = riftCooldowns.get(playerId);
                if (currentTime - lastUsed < 5000) { // 5 seconds
                    return InteractionResultHolder.fail(stack);
                }
            }

            // ✅ Enter the rift
            world.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.RIFT_JUMP.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    0.85F
            );

            player.addEffect(new MobEffectInstance(ModEffects.BLACK_WHITE.get(), 100, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 0, false, false));
            player.setInvisible(true);

            // ⏱ Save cooldown time
            riftCooldowns.put(playerId, currentTime);

            // Optional cleanup for invisibility
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player.setInvisible(false);
            }).start();
        }

        return InteractionResultHolder.success(stack);
    }
}