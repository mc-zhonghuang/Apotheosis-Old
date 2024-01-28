package cn.hackedmc.alexander.newevent.impl.other;


import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.newevent.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ModuleToggleEvent implements Event {
    private Module module;
}