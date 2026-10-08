package io.github.bakedlibs.dough.skins;

import java.net.URL;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

/**
 * Patched copy of dough's {@code CustomGameProfile} (MIT License, Copyright (c) 2019 TheBusyBiscuit).
 * <p>
 * The original class extended {@code com.mojang.authlib.GameProfile}, which is final since
 * Minecraft 1.21.9. This version no longer extends it and uses Bukkit's {@link PlayerProfile} API instead.
 */
public final class CustomGameProfile {

    private static final String PLAYER_NAME = "CS-CoreLib";

    private final UUID uuid;
    private final URL skinUrl;
    private final String texture;

    CustomGameProfile(@Nonnull UUID uuid, @Nullable String texture, @Nonnull URL url) {
        this.uuid = uuid;
        this.skinUrl = url;
        this.texture = texture;
    }

    public @Nonnull UUID getId() {
        return uuid;
    }

    public @Nonnull String getName() {
        return PLAYER_NAME;
    }

    /**
     * This creates a new Bukkit {@link PlayerProfile} carrying this skin.
     *
     * @return A {@link PlayerProfile} with this skin applied
     */
    public @Nonnull PlayerProfile toPlayerProfile() {
        PlayerProfile playerProfile = Bukkit.createPlayerProfile(uuid, PLAYER_NAME);
        PlayerTextures playerTextures = playerProfile.getTextures();
        playerTextures.setSkin(skinUrl);
        playerProfile.setTextures(playerTextures);
        return playerProfile;
    }

    void apply(@Nonnull SkullMeta meta) {
        meta.setOwnerProfile(toPlayerProfile());
    }

    @Nullable
    public String getBase64Texture() {
        return this.texture;
    }
}
