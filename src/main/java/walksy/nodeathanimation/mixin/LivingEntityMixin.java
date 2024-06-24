package walksy.nodeathanimation.mixin;

import net.minecraft.entity.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import walksy.nodeathanimation.NoDeathAnimation;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    @Shadow public int deathTime;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "updatePostDeath", at = @At("HEAD"), cancellable = true)
    public void onDeathTicks(CallbackInfo ci) {
        ci.cancel();
        ++this.deathTime;
        if (this.deathTime >= 22 && !this.getWorld().isClient() && !this.isRemoved()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            this.remove(RemovalReason.KILLED);
        }
    }
}
