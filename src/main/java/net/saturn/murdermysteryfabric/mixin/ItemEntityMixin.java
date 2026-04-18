package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.saturn.murdermysteryfabric.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Makes the gun glow when dropped on the ground.
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getStack();

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        
        // Make gun glow when dropped
        if (getStack().getItem() == ModItems.GUN) {
            self.setGlowing(true);
        }
    }
}
