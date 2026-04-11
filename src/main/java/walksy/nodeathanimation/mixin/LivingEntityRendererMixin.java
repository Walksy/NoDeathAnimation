package walksy.nodeathanimation.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import walksy.nodeathanimation.LivingEntityRenderStateSources;
import walksy.nodeathanimation.NoDeathAnimation;
import walksy.nodeathanimation.config.Config;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"), cancellable = true)
    public void getAttributes(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        ((LivingEntityRenderStateSources) livingEntityRenderState).setSourceEntity(livingEntity);
    }


    @Inject(method = "setupRotations", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;sqrt(F)F", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void setupTransforms(S state, PoseStack poseStack, float bodyRot, float entityScale, CallbackInfo ci) {
        LivingEntity target = ((LivingEntityRenderStateSources) state).getSourceEntity();
        if (Config.modEnabled && target != null && NoDeathAnimation.shouldOverride(target)) {
            ci.cancel();
        }
    }

    @Inject(method = "getOverlayCoords", at = @At(value = "HEAD"), cancellable = true)
    private static void getV(LivingEntityRenderState renderState, float whiteOverlayProgress, CallbackInfoReturnable<Integer> cir) {
        LivingEntity entity = ((LivingEntityRenderStateSources) renderState).getSourceEntity();
        if (!Config.modEnabled || entity == null || !NoDeathAnimation.shouldOverride(entity)) return;

        boolean hurt = (entity.hurtTime > 0 && !entity.isDeadOrDying()) || (entity.deathTime > 0 && entity.deathTime <= Config.deathOverlayTime);
        cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(whiteOverlayProgress), OverlayTexture.v(hurt)));
    }
}
