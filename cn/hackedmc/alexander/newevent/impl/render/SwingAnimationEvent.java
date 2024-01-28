package cn.hackedmc.alexander.newevent.impl.render;

import cn.hackedmc.alexander.newevent.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class SwingAnimationEvent extends CancellableEvent {

    private int animationEnd;

}
