package cn.hackedmc.apotheosis.module.impl.movement.phase;

import cn.hackedmc.apotheosis.module.impl.movement.Phase;
import cn.hackedmc.apotheosis.value.Mode;

public class ClipPhase extends Mode<Phase> {
    public ClipPhase(String name, Phase parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ);
    }
}
