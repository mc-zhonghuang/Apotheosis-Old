package cn.hackedmc.apotheosis.newevent.impl.other;


import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.newevent.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ModuleToggleEvent implements Event {
    private Module module;
}