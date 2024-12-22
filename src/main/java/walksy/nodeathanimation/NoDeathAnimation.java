package walksy.nodeathanimation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NoDeathAnimation implements ModInitializer {

    public int redTickTime = 12;
    private static final Path configDir = FabricLoader.getInstance().getConfigDir();
    private static final File configFile = configDir.resolve("nodeathanimation.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static NoDeathAnimation INSTANCE;

    @Override
    public void onInitialize() {
        if (INSTANCE == null) INSTANCE = this;
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommandManager.literal("walksyredoverlayduration")
                .then(ClientCommandManager.argument("num", IntegerArgumentType.integer())
                    .executes(context -> {
                        INSTANCE.redTickTime = IntegerArgumentType.getInteger(context, "num");
                        context.getSource().sendFeedback(Text.of("Red Overlay Duration set to " + INSTANCE.redTickTime));
                        saveConfig();
                        return 1;
                    })
                )
            )
        );
        loadConfig();
    }

    private void loadConfig() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                Config config = GSON.fromJson(reader, Config.class);
                INSTANCE.redTickTime = config.redTickTime;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveConfig();
        }
    }

    public void saveConfig() {
        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            Config config = new Config(INSTANCE.redTickTime);
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Config {
        public int redTickTime;
        public Config(int redOverlyTime) {
            this.redTickTime = redOverlyTime;
        }
    }
}
