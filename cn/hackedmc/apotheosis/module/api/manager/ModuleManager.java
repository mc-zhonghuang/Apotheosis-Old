package cn.hackedmc.apotheosis.module.api.manager;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.render.Interface;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.Priorities;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.util.localization.Localization;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.newevent.impl.input.KeyboardInputEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patrick
 * @since 10/19/2021
 */
public final class ModuleManager extends ArrayList<Module> {

    /**
     * Called on client start and when for some reason when we reinitialize (idk custom modules?)
     */
    public void init() {
//        final Reflections reflections = new Reflections("com.riseclient.apotheosis.module.impl");
//
//        reflections.getSubTypesOf(Module.class).forEach(clazz -> {
//            try {
//                if (!Modifier.isAbstract(clazz.getModifiers()) && clazz != Interface.class && (clazz != Test.class || Rise.DEVELOPMENT_SWITCH)) {
//                    this.add(clazz.newInstance());
//                }
//            } catch (final Exception e) {
//                e.printStackTrace();
//            }
//        });

        // Automatic initializations
        this.stream().filter(module -> module.getModuleInfo().autoEnabled()).forEach(module -> module.setEnabled(true));

        // Has to be a listener to handle the key presses
        Client.INSTANCE.getEventBus().register(this);
    }

    public List<Module> getAll() {
        return new ArrayList<>(this);
    }

    public <T extends Module> T get(final String name) {
        // noinspection unchecked
        return (T) this.stream()
                .filter(module -> Localization.get(module.getDisplayName()).replace(" ", "").equalsIgnoreCase(name.replace(" ", "")))
                .findAny().orElse(null);
    }

    public <T extends Module> T get(final Class<T> clazz) {
        // noinspection unchecked
        return (T) this.stream()
                .filter(module -> module.getClass() == clazz)
                .findAny().orElse(null);
    }

    public List<Module> get(final Category category) {
        return this.stream()
                .filter(module -> module.getModuleInfo().category() == category)
                .collect(Collectors.toList());
    }

    @EventLink(value = Priorities.VERY_HIGH)
    public final Listener<KeyboardInputEvent> onKey = event -> {

        if (event.getGuiScreen() != null) return;

        this.stream()
                .filter(module -> module.getKeyCode() == event.getKeyCode())
                .forEach(Module::toggle);
    };

    @Override
    public boolean add(final Module module) {
        final boolean result = super.add(module);
        this.updateArraylistCache();
        return result;
    }

    @Override
    public void add(final int i, final Module module) {
        super.add(i, module);
        this.updateArraylistCache();
    }

    @Override
    public Module remove(final int i) {
        final Module result = super.remove(i);
        this.updateArraylistCache();
        return result;
    }

    @Override
    public boolean remove(final Object o) {
        final boolean result = super.remove(o);
        this.updateArraylistCache();
        return result;
    }


    private void updateArraylistCache() {
        final Interface interfaceModule = Interface.INSTANCE;
        if (interfaceModule == null) return;

        interfaceModule.createArrayList();
    }
}