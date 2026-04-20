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
import net.saturn.murdermysteryfabric.item.custom.GunItem;
import net.saturn.murdermysteryfabric.item.custom.KnifeItem;
import net.saturn.murdermysteryfabric.item.custom.TomahawkItem;

import java.util.function.Function;

public class ModItems {

    public static final Item TOMAHAWK = registerItem("tomahawk",
            setting -> new TomahawkItem(setting.maxCount(16)));


    public static final Item KNIFE = registerItem("knife",
            settings -> new KnifeItem(settings
                    .registryKey(key("knife"))
                    .attributeModifiers(createKnifeModifiers())));

    public static final Item GUN = registerItem("gun",
            settings -> new GunItem(settings
                    .registryKey(key("gun"))
                    .maxCount(1)));

    public static final Item EVIDENCE_FILE = registerItem("evidence_file",
            settings -> new Item(settings
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


    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(Murdermysteryfabric.MODID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Murdermysteryfabric.MODID, name)))));
    }

    public static void registerModItems() {
        // Ensure class is loaded
    }
}