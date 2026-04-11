package walksy.nodeathanimation;

import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityRenderStateSources {
    LivingEntity getSourceEntity();
    void setSourceEntity(LivingEntity entity);
}
