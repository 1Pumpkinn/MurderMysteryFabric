package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.LightmapTextureManager;
import net.saturn.murdermysteryfabric.game.GameManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public class CustomLightmapMixin {

    // 0.0 = no brightness boost whatsoever — combined with midnight this is
    // pitch black. Raise toward 1.0 if you want torches to glow more.
    private static final double GAME_GAMMA = 0.0;

    @Unique private double murdermysteryfabric$savedGamma = Double.NaN;
    @Unique private static Field murdermysteryfabric$gammaField = null;
    @Unique private static Field murdermysteryfabric$valueField = null;

    @Inject(method = "update", at = @At("HEAD"))
    private void darkGammaHead(float tickProgress, CallbackInfo ci) {
        if (!GameManager.getInstance().isGameRunning()) {
            if (!Double.isNaN(murdermysteryfabric$savedGamma)) {
                murdermysteryfabric$write(murdermysteryfabric$savedGamma);
                murdermysteryfabric$savedGamma = Double.NaN;
            }
            return;
        }
        double current = murdermysteryfabric$read();
        if (Double.isNaN(current)) return;
        if (Double.isNaN(murdermysteryfabric$savedGamma)) {
            murdermysteryfabric$savedGamma = current;
        }
        murdermysteryfabric$write(GAME_GAMMA);
    }

    @Inject(method = "update", at = @At("TAIL"))
    private void restoreGammaTail(float tickProgress, CallbackInfo ci) {
        if (!GameManager.getInstance().isGameRunning()) return;
        if (Double.isNaN(murdermysteryfabric$savedGamma)) return;
        murdermysteryfabric$write(murdermysteryfabric$savedGamma);
    }

    @Unique
    private static double murdermysteryfabric$read() {
        try {
            SimpleOption<Double> opt = murdermysteryfabric$option();
            if (opt == null) return Double.NaN;
            Double v = opt.getValue();
            return v == null ? Double.NaN : v;
        } catch (Exception e) { return Double.NaN; }
    }

    @Unique
    private static void murdermysteryfabric$write(double value) {
        try {
            SimpleOption<Double> opt = murdermysteryfabric$option();
            if (opt == null) return;
            if (murdermysteryfabric$valueField == null) {
                for (Field f : SimpleOption.class.getDeclaredFields()) {
                    if (f.getType() == Object.class || f.getName().equals("value")) {
                        f.setAccessible(true);
                        // confirm it holds a Double
                        Object current = f.get(opt);
                        if (current instanceof Double) {
                            murdermysteryfabric$valueField = f;
                            break;
                        }
                    }
                }
            }
            if (murdermysteryfabric$valueField != null) {
                murdermysteryfabric$valueField.set(opt, value);
            }
        } catch (Exception ignored) {}
    }

    @Unique
    private static SimpleOption<Double> murdermysteryfabric$option() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.options == null) return null;
            if (murdermysteryfabric$gammaField == null) {
                murdermysteryfabric$gammaField = client.options.getClass().getDeclaredField("gamma");
                murdermysteryfabric$gammaField.setAccessible(true);
            }
            //noinspection unchecked
            return (SimpleOption<Double>) murdermysteryfabric$gammaField.get(client.options);
        } catch (Exception e) { return null; }
    }
}