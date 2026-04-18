package net.saturn.murdermysteryfabric.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;

public class ModItems {

    // Murderer items
    public static final Item KNIFE = register("knife",
            new Item(new Item.Settings()
                    .registryKey(key("knife"))
                    .attributeModifiers(createKnifeModifiers())));

    public static final Item EVIDENCE_FILE = register("evidence_file",
            new Item(new Item.Settings()
                    .registryKey(key("evidence_file"))
                    .maxCount(1)));

    private static RegistryKey<Item> key(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Murdermysteryfabric.MODID, name));
    }

    private static AttributeModifiersComponent createKnifeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(Identifier.of(Murdermysteryfabric.MODID, "knife_attack_damage"),
                                5.0, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(Identifier.of(Murdermysteryfabric.MODID, "knife_attack_speed"),
                                -2.4, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Murdermysteryfabric.MODID, name), item);
    }

    public static void initialize() {
        // Ensure class is loaded
    }
}