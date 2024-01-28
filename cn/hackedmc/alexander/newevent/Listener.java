package cn.hackedmc.alexander.newevent;

@FunctionalInterface
public interface Listener<Event> {
    void call(Event event);
}