package io.github.bakedlibs.dough.skins;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Patched copy of dough's {@code PlayerHead} (MIT License, Copyright (c) 2019 TheBusyBiscuit).
 * <p>
 * The original class set block textures through NMS reflection using {@code com.mojang.authlib.GameProfile}.
 * This version uses Bukkit's {@link Skull#setOwnerProfile} instead, which works on Minecraft 1.21.9+.
 */
public final class PlayerHead {

    private PlayerHead() {}

    public static @Nonnull ItemStack getItemStack(@Nonnull OfflinePlayer player) {
        Validate.notNull(player, "The player can not be null!");

        return getItemStack(meta -> meta.setOwningPlayer(player));
    }

    public static @Nonnull ItemStack getItemStack(@Nonnull PlayerSkin skin) {
        Validate.notNull(skin, "The skin can not be null!");

        return getItemStack(meta -> skin.getProfile().apply(meta));
    }

    private static @Nonnull ItemStack getItemStack(@Nonnull Consumer<SkullMeta> consumer) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        consumer.accept(meta);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * This applies the given {@link PlayerSkin} to a player head {@link Block}.
     *
     * @param block
     *            The player head {@link Block}
     * @param skin
     *            The {@link PlayerSkin} to apply
     * @param sendBlockUpdate
     *            Kept for compatibility, the {@link Block} is always updated for clients
     */
    @ParametersAreNonnullByDefault
    public static void setSkin(Block block, PlayerSkin skin, boolean sendBlockUpdate) {
        Material material = block.getType();

        if (material != Material.PLAYER_HEAD && material != Material.PLAYER_WALL_HEAD) {
            throw new IllegalArgumentException("Cannot update a head texture. Expected a Player Head, received: " + material);
        }

        BlockState state = block.getState();

        if (state instanceof Skull skull) {
            skull.setOwnerProfile(skin.getProfile().toPlayerProfile());
            skull.update(true, false);
        }
    }

}
