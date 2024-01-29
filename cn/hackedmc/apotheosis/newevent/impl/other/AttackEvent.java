package cn.hackedmc.apotheosis.newevent.impl.other;

import cn.hackedmc.apotheosis.newevent.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;

@Getter
@Setter
@AllArgsConstructor
public final class AttackEvent extends CancellableEvent {
    private Entity target;
}