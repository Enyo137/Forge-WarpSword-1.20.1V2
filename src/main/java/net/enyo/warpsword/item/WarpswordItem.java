package net.enyo.warpsword.item;

import net.enyo.warpsword.effect.ModEffects;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.enyo.warpsword.sound.ModSounds;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.UUID;

public class WarpswordItem extends SwordItem {

    private static final HashMap<UUID, Long> riftCooldowns = new HashMap<>();

    public WarpswordItem(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    private final HashMap<UUID, Boolean> wasSwingingLastTick = new HashMap<>();

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
        if (level.isClientSide || !(entity instanceof Player player)) return;

        // Only if item is in main hand and selected
        if (player.getMainHandItem() != stack || !selected) return;

        UUID id = player.getUUID();
        boolean isSwinging = player.swinging;
        boolean wasSwinging = wasSwingingLastTick.getOrDefault(id, false);

        if (player.isCrouching() && isSwinging && !wasSwinging && !player.getCooldowns().isOnCooldown(this)) {
            // Just started swinging this tick
            player.getCooldowns().addCooldown(this, 40); // 2 seconds
            System.out.println("Firing Rift Slash!");

            // Spawn arrow server-side
            if (!level.isClientSide()) {
                Vec3 look = player.getLookAngle();
                SmallFireball fireball = new SmallFireball(level, player, look.x, look.y, look.z);
                fireball.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());

                level.addFreshEntity(fireball);
            }
        }

        // Update swing state
        wasSwingingLastTick.put(id, isSwinging);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        UUID playerId = player.getUUID();
        long currentTime = System.currentTimeMillis();

        if (!world.isClientSide()) {

            // If in rift, exit early
            if (player.hasEffect(ModEffects.BLACK_WHITE.get())) {
                player.removeEffect(ModEffects.BLACK_WHITE.get());
                player.removeEffect(MobEffects.NIGHT_VISION);
                player.removeEffect(MobEffects.BLINDNESS);
                player.removeEffect(MobEffects.INVISIBILITY);
                player.setInvisible(false);
                return InteractionResultHolder.success(stack);
            }

            // Prevent entering if within cooldown window
            if (riftCooldowns.containsKey(playerId)) {
                long lastUsed = riftCooldowns.get(playerId);
                if (currentTime - lastUsed < 5000) { // 5 seconds
                    return InteractionResultHolder.fail(stack);
                }
            }

            // Enter the rift
            world.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.RIFT_JUMP.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    0.85F
            );

            player.addEffect(new MobEffectInstance(ModEffects.BLACK_WHITE.get(), 100, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 0, false, false));

            player.setInvisible(true);

            // Save cooldown time
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

