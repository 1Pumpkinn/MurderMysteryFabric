package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class RealisticHeadBobMixin {

    private float bobPhase = 0.0f;

    @Inject(method = "bobView", at = @At("TAIL"))
    private void applyHorrorCamera(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        PlayerEntity player = client.player;

        // 🧠 Smooth timer instead of raw velocity chaos
        bobPhase += tickDelta * 0.6f;

        // 🌫️ Breathing effect (slow inhale/exhale)
        float breath = MathHelper.sin(bobPhase * 0.8f) * 0.0025f;

        // 🚶 Movement influence (very soft, not jittery)
        float speed = (float) player.getVelocity().horizontalLength();
        float moveInfluence = MathHelper.clamp(speed * 0.3f, 0.0f, 0.03f);

        // 🌙 subtle sway (NOT shake)
        float swayX = MathHelper.sin(bobPhase * 0.5f) * moveInfluence;
        float swayY = MathHelper.cos(bobPhase * 0.4f) * moveInfluence * 0.5f;

        // apply breathing (vertical float)
        matrices.translate(0.0, -breath, 0.0);

        // apply very subtle sway (horror feel)
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(swayY * 10.0f));
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(swayX * 8.0f));
    }
}