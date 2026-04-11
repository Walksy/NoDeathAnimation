package walksy.nodeathanimation.config;

import main.walksy.lib.api.WalksyLibConfig;
import main.walksy.lib.core.config.impl.LocalConfig;
import main.walksy.lib.core.config.local.Category;
import main.walksy.lib.core.config.local.Option;
import main.walksy.lib.core.config.local.options.BooleanOption;
import main.walksy.lib.core.config.local.options.NumericalOption;
import main.walksy.lib.core.config.local.options.StringListOption;
import main.walksy.lib.core.config.local.options.groups.OptionGroup;
import main.walksy.lib.core.utils.PathUtils;

import java.util.List;

public class Config implements WalksyLibConfig {

    public static boolean modEnabled = true;

    public static int deathTime = 22;
    public static int deathOverlayTime = 22;
    public static List<String> affectedEntityList = List.of("player");



    private final Option<Boolean> modEnabledOption = BooleanOption.createBuilder("Mod Enabled", () -> modEnabled, modEnabled, newValue -> modEnabled = newValue)
        .build();

    private final Option<List<String>> affectedEntityListOption = StringListOption.createBuilder("Affected Entities", () -> affectedEntityList, affectedEntityList, newValue -> affectedEntityList = newValue)
        .build();
    private final Option<Integer> deathTimeOption = NumericalOption.createBuilder("Death Time", () -> deathTime, deathTime, newValue -> deathTime = newValue)
        .values(0, 100, 1)
        .build();
    private final Option<Integer> deathOverlayTimeOption = NumericalOption.createBuilder("Death Overlay Time", () -> deathOverlayTime, deathOverlayTime, newValue -> deathOverlayTime = newValue)
        .values(0, 100, 1)
        .build();

    private final Category generalCategory = Category.createBuilder("General")
        .group(OptionGroup.createBuilder("General Options")
            .addOption(modEnabledOption)
            .build())
        .group(OptionGroup.createBuilder("Additional Options")
            .addOption(affectedEntityListOption)
            .addOption(deathTimeOption)
            .addOption(deathOverlayTimeOption)
            .build())
        .build();

    @Override
    public LocalConfig define() {
        return LocalConfig.createBuilder("No Death Animation")
            .path(PathUtils.ofConfigDir("nodeathanimation"))
            .category(generalCategory)
            .build();
    }
}
