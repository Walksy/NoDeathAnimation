package walksy.nodeathanimation.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import walksy.nodeathanimation.NoDeathAnimation;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "updatePostDeath", at = @At("HEAD"), cancellable = true)
    public void onDeathTicks(CallbackInfo ci) {
        ci.cancel();
        ++this.deathTime;
        if (this.deathTime == 22) {
            this.remove(RemovalReason.KILLED);
        }
    }
}
