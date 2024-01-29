package cn.hackedmc.apotheosis.newevent;

@FunctionalInterface
public interface Listener<Event> {
    void call(Event event);
}