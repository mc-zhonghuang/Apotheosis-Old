package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.render.interfaces.ModernInterface;
import cn.hackedmc.apotheosis.module.impl.render.interfaces.ModuleComponent;
import cn.hackedmc.apotheosis.module.impl.render.interfaces.NovoInterface;
import cn.hackedmc.apotheosis.module.impl.render.interfaces.WurstInterface;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.localization.Localization;
import cn.hackedmc.apotheosis.Type;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.math.MathUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.util.vector.Vector2f;
import cn.hackedmc.apotheosis.value.Value;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import cn.hackedmc.apotheosis.value.impl.SubMode;
import cn.hackedmc.fucker.Fucker;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiChat;
import util.time.StopWatch;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Getter
@Setter
@ModuleInfo(name = "module.render.interface.name", description = "module.render.interface.description", category = Category.RENDER, autoEnabled = true)
public final class Interface extends Module {
    public static Interface INSTANCE;
    private final ModeValue mode = new ModeValue("Mode", this, () -> Client.CLIENT_TYPE != Type.BASIC) {{
        add(new ModernInterface("Modern", (Interface) this.getParent()));
        add(new NovoInterface("Novo", (Interface) this.getParent()));
        add(new WurstInterface("Wurst", (Interface) this.getParent()));
        setDefault("Modern");
    }};

    private final ModeValue modulesToShow = new ModeValue("Modules to Show", this, () -> Client.CLIENT_TYPE != Type.BASIC) {{
        add(new SubMode("All"));
        add(new SubMode("Exclude render"));
        add(new SubMode("Only bound"));
        setDefault("Exclude render");
    }};

    public final BooleanValue irc = new BooleanValue("Show IRC Message", this, true);
    public final BooleanValue limitChatWidth = new BooleanValue("Limit Chat Width", this, false);
    public final BooleanValue smoothHotBar = new BooleanValue("Smooth Hot Bar", this, true);

    public BooleanValue suffix = new BooleanValue("Suffix", this, true, () -> Client.CLIENT_TYPE != Type.BASIC);
    public BooleanValue lowercase = new BooleanValue("Lowercase", this, false, () -> Client.CLIENT_TYPE != Type.BASIC);
    public BooleanValue removeSpaces = new BooleanValue("Remove Spaces", this, false, () -> Client.CLIENT_TYPE != Type.BASIC);

    public BooleanValue shaders = new BooleanValue("Shaders", this, true);
    private ArrayList<ModuleComponent> allModuleComponents = new ArrayList<>(),
            activeModuleComponents = new ArrayList<>();
    private SubMode lastFrameModulesToShow = (SubMode) modulesToShow.getValue();

    private final StopWatch stopwatch = new StopWatch();
    private final StopWatch updateTags = new StopWatch();
    private final Font productSansMedium18 = FontManager.getProductSansMedium(18);

    public Font widthComparator = nunitoNormal;
    public float moduleSpacing = 12, edgeOffset;

    public Interface() {
        createArrayList();

        INSTANCE = this;
    }

    public void createArrayList() {
        allModuleComponents.clear();
        Client.INSTANCE.getModuleManager().getAll().stream()
                .sorted(Comparator.comparingDouble(module -> -widthComparator.width(Localization.get(module.getDisplayName()))))
                .forEach(module -> allModuleComponents.add(new ModuleComponent(module)));
    }

    public void sortArrayList() {
//        ArrayList<ModuleComponent> components = new ArrayList<>();
//        Client.INSTANCE.getModuleManager().getAll().forEach(module -> components.add(new ModuleComponent(module)));
//
        activeModuleComponents = allModuleComponents.stream()
                .filter(moduleComponent -> moduleComponent.getModule().shouldDisplay(this))
                .sorted(Comparator.comparingDouble(module -> -module.getNameWidth() - module.getTagWidth()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    StopWatch lastUpdate = new StopWatch();

    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        if (lastUpdate.finished(1000)) {
            threadPool.execute(() -> {
                for (final ModuleComponent moduleComponent : allModuleComponents) {
                    moduleComponent.setTranslatedName(Localization.get(moduleComponent.getModule().getDisplayName()));
                }
            });
        }
    };

    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {
        final String name = "Username:" + Fucker.name;
        final String rank = "Rank:" + Fucker.rank.getDisplayName();
        final String online = "Online:" + Fucker.usernames.size();
        this.productSansMedium18.drawStringWithShadow(name, event.getScaledResolution().getScaledWidth() - this.productSansMedium18.width(name) - 2, event.getScaledResolution().getScaledHeight() - this.productSansMedium18.height() * 3 - 2, 0xFFCCCCCC);
        this.productSansMedium18.drawStringWithShadow(rank, event.getScaledResolution().getScaledWidth() - this.productSansMedium18.width("Rank:" + Fucker.rank.name) - 2, event.getScaledResolution().getScaledHeight() - this.productSansMedium18.height() * 2 - 2, 0xFFCCCCCC);
        this.productSansMedium18.drawStringWithShadow(online, event.getScaledResolution().getScaledWidth() - this.productSansMedium18.width(online) - 2, event.getScaledResolution().getScaledHeight() - this.productSansMedium18.height() - 2, 0xFFCCCCCC);

        for (final ModuleComponent moduleComponent : allModuleComponents) {
            if (moduleComponent.getModule().isEnabled()) {
                moduleComponent.animationTime = Math.min(moduleComponent.animationTime + stopwatch.getElapsedTime() / 100.0F, 10);
            } else {
                moduleComponent.animationTime = Math.max(moduleComponent.animationTime - stopwatch.getElapsedTime() / 100.0F, 0);
            }
        }

        threadPool.execute(() -> {
            if (updateTags.finished(50)) {
                updateTags.reset();

                for (final ModuleComponent moduleComponent : activeModuleComponents) {
                    if (moduleComponent.animationTime == 0) {
                        continue;
                    }

                    for (final Value<?> value : moduleComponent.getModule().getValues()) {
                        if (value instanceof ModeValue) {
                            final ModeValue modeValue = (ModeValue) value;

                            moduleComponent.setTag(modeValue.getValue().getName());
                            break;
                        }

                        moduleComponent.setTag("");
                    }
                }

                this.sortArrayList();
            }

            final float screenWidth = event.getScaledResolution().getScaledWidth();
            final Vector2f position = new Vector2f(0, 0);
            for (final ModuleComponent moduleComponent : activeModuleComponents) {
                if (moduleComponent.animationTime == 0) {
                    continue;
                }

                moduleComponent.targetPosition = new Vector2d(screenWidth - moduleComponent.getNameWidth() - moduleComponent.getTagWidth(), position.getY());

                if (!moduleComponent.getModule().isEnabled() && moduleComponent.animationTime < 10) {
                    moduleComponent.targetPosition = new Vector2d(screenWidth + moduleComponent.getNameWidth() + moduleComponent.getTagWidth(), position.getY());
                } else {
                    position.setY(position.getY() + moduleSpacing);
                }

                float offsetX = edgeOffset;
                float offsetY = edgeOffset;

                moduleComponent.targetPosition.x -= offsetX;
                moduleComponent.targetPosition.y += offsetY;

                if (Math.abs(moduleComponent.getPosition().getX() - moduleComponent.targetPosition.x) > 0.5 || Math.abs(moduleComponent.getPosition().getY() - moduleComponent.targetPosition.y) > 0.5 || (moduleComponent.animationTime != 0 && moduleComponent.animationTime != 10)) {
                    for (int i = 0; i < stopwatch.getElapsedTime(); ++i) {
                        moduleComponent.position.x = MathUtil.lerp(moduleComponent.position.x, moduleComponent.targetPosition.x, 1.5E-2F);
                        moduleComponent.position.y = MathUtil.lerp(moduleComponent.position.y, moduleComponent.targetPosition.y, 1.5E-2F);
                    }
                } else {
                    moduleComponent.position = moduleComponent.targetPosition;
                }
            }

            stopwatch.reset();
        });
    };
}