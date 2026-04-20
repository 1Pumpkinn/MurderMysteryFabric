package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes the private {@code client} field of {@link GameRenderer}
 * so our {@link RealisticHeadBobMixin} can read the local player.
 */
@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public interface GameRendererAccessor {

    /**
     * Returns the {@link MinecraftClient} instance held by the GameRenderer.
     * In 1.21.x the field is named {@code client}.
     */
    @Accessor("client")
    MinecraftClient getClient();
}