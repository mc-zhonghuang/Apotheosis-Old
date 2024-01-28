package cn.hackedmc.alexander.component.impl.player.rotationcomponent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MovementFix {
    OFF("Off"),
    NORMAL("Normal"),
    TRADITIONAL("Only Forward"),
    BACKWARDS_SPRINT("Runnable");

    String name;

    @Override
    public String toString() {
        return name;
    }
}