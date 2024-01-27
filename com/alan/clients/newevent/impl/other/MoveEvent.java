package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class MoveEvent extends CancellableEvent {

    private double posX, posY, posZ;

    public void zeroXZ() {
        this.posX = this.posZ = 0;
    }
}
