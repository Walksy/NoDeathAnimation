package walksy.nodeathanimation;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import walksy.nodeathanimation.config.Config;

import java.util.Locale;
import java.util.Map;

public class NoDeathAnimation implements ModInitializer {

    @Override
    public void onInitialize() {

    }

    public static boolean shouldOverride(Entity entity) {
        EntityType<?> type = entity.getType();
        for (String value : Config.affectedEntityList) {
            EntityType<?> type2 = getEntityTypeFromString(value);
            if (type2 == null) continue;
            if (type2.equals(type)) return true;
        }
        return false;
    }

    private static @Nullable EntityType<?> getEntityTypeFromString(@Nullable String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        String normalized = name.trim()
                .toLowerCase(Locale.ROOT)
                .replace(" ", "_");

        try {
            Identifier id = normalized.contains(":")
                    ? Identifier.parse(normalized)
                    : Identifier.fromNamespaceAndPath("minecraft", normalized);

            EntityType<?> direct = BuiltInRegistries.ENTITY_TYPE.getValue(id);
            if (direct != null && !BuiltInRegistries.ENTITY_TYPE.getKey(direct).equals(BuiltInRegistries.ENTITY_TYPE.getDefaultKey())) {
                return direct;
            }

            for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : BuiltInRegistries.ENTITY_TYPE.entrySet()) {
                String path = entry.getKey().identifier().getPath();
                if (path.contains(normalized)) {
                    return entry.getValue();
                }
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }
}
