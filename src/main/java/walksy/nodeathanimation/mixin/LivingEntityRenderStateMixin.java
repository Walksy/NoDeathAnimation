package walksy.nodeathanimation.mixin;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import walksy.nodeathanimation.LivingEntityRenderStateSources;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateSources {

    @Unique
    private LivingEntity source;

    @Override
    public LivingEntity getSourceEntity() {
        return source;
    }

    @Override
    public void setSourceEntity(LivingEntity entity) {
        this.source = entity;
    }
}