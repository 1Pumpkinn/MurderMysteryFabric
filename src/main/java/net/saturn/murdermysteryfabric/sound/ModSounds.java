package net.saturn.murdermysteryfabric.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;

public class ModSounds {

    public static final SoundEvent CLUE_FOUND = register("clue_found");
    public static final SoundEvent KNIFE_STAB = register("knife_stab");
    public static final SoundEvent INVESTIGATION_AMBIENT = register("investigation_ambient");

    private static SoundEvent register(String name) {
        return Registry.register(Registries.SOUND_EVENT, Identifier.of(Murdermysteryfabric.MODID, name),
                SoundEvent.of(Identifier.of(Murdermysteryfabric.MODID, name)));
    }

    public static void initialize() {
        // Ensure class is loaded
    }
}
