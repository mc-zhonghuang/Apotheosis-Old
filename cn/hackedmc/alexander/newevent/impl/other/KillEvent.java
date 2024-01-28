package cn.hackedmc.alexander.newevent.impl.other;


import cn.hackedmc.alexander.newevent.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.Entity;

@Getter
@AllArgsConstructor
public final class KillEvent implements Event {

    Entity entity;

}