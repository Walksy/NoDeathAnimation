package walksy.nodeathanimation.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import walksy.nodeathanimation.NoDeathAnimation;
import walksy.nodeathanimation.config.Config;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public int deathTime;

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tickDeath", at = @At(value = "HEAD"), cancellable = true)
    public void updateDeathTime(CallbackInfo ci) {
        if (!Config.modEnabled || !NoDeathAnimation.shouldOverride(LivingEntity.class.cast(this))) return;
        ci.cancel();
        ++this.deathTime;
        if (this.deathTime >= Config.deathTime && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte)60); //redundant client-side
            this.remove(Entity.RemovalReason.KILLED);
        }
    }
}
