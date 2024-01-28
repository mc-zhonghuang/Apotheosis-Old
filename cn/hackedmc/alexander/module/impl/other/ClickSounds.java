package cn.hackedmc.alexander.module.impl.other;

import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.input.ClickEvent;
import cn.hackedmc.alexander.util.sound.SoundUtil;
import cn.hackedmc.alexander.value.impl.ModeValue;
import cn.hackedmc.alexander.value.impl.NumberValue;
import cn.hackedmc.alexander.value.impl.SubMode;
import org.apache.commons.lang3.RandomUtils;

@ModuleInfo(name = "module.other.clicksounds.name", description = "module.other.clicksounds.description", category = Category.OTHER)
public final class ClickSounds extends Module {

    private final ModeValue sound = new ModeValue("Sound", this)
            .add(new SubMode("Standard"))
            .add(new SubMode("Double"))
            .add(new SubMode("Alan"))
            .setDefault("Standard");

    private final NumberValue volume = new NumberValue("Volume", this, 0.5, 0.1, 2, 0.1);
    private final NumberValue variation = new NumberValue("Variation", this, 5, 0, 100, 1);

    @EventLink()
    public final Listener<ClickEvent> onClick = event -> {
        String soundName = "alexander.click.standard";

        switch (sound.getValue().getName()) {
            case "Double": {
                soundName = "alexander.click.double";
                break;
            }

            case "Alan": {
                soundName = "alexander.click.alan";
                break;
            }
        }

        SoundUtil.playSound(soundName, volume.getValue().floatValue(), RandomUtils.nextFloat(1.0F, 1 + variation.getValue().floatValue() / 100f));
    };
}