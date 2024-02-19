package cn.hackedmc.apotheosis.newevent;

public class CancellableEvent implements Event {
    public boolean PRE;
    public boolean isPre() {
        return this.PRE;
    }

    public boolean isPost() {
        return !this.PRE;
    }
    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCancelled() {
        this.cancelled = true;
    }
}