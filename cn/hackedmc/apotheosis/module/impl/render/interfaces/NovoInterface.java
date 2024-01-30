package cn.hackedmc.apotheosis.module.impl.render.interfaces;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.Type;
import cn.hackedmc.apotheosis.component.impl.render.ParticleComponent;
import cn.hackedmc.apotheosis.module.impl.render.Interface;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.KillEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.TickEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.ServerUtils;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.render.particle.Particle;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.util.vector.Vector2f;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.DragValue;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import cn.hackedmc.apotheosis.value.impl.SubMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import util.time.StopWatch;

import java.awt.*;

public class NovoInterface extends Mode<Interface> {

    private final Font productSansRegular = FontManager.getProductSansRegular(18);
    private final Font minecraft = FontManager.getMinecraft();

    private Font arrayListFont = productSansRegular;
    private final Font productSansMedium18 = FontManager.getProductSansMedium(18);
    private final StopWatch stopWatch = new StopWatch();

    private final ModeValue colorMode = new ModeValue("ArrayList Color Mode", this, () -> Client.CLIENT_TYPE != Type.RISE) {{
        add(new SubMode("Static"));
        add(new SubMode("Fade"));
        add(new SubMode("Breathe"));
        setDefault("Fade");
    }};

    private final ModeValue font = new ModeValue("ArrayList Font", this, () -> Client.CLIENT_TYPE != Type.RISE) {{
        add(new SubMode("Product Sans"));
        add(new SubMode("Minecraft"));
        setDefault("Product Sans");
    }};

    private final ModeValue shader = new ModeValue("Shader Effect", this, () -> Client.CLIENT_TYPE != Type.RISE) {{
        add(new SubMode("Glow"));
        add(new SubMode("Shadow"));
        add(new SubMode("None"));
        setDefault("Shadow");
    }};

    private final BooleanValue dropShadow = new BooleanValue("Drop Shadow", this, true, () -> Client.CLIENT_TYPE != Type.RISE);
    private final BooleanValue sidebar = new BooleanValue("Sidebar", this, true, () -> Client.CLIENT_TYPE != Type.RISE);
    private final BooleanValue particles = new BooleanValue("Particles on Kill", this, true, () -> Client.CLIENT_TYPE != Type.RISE);
    private final ModeValue background = new ModeValue("BackGround", this, () -> Client.CLIENT_TYPE != Type.RISE) {{
        add(new SubMode("Off"));
        add(new SubMode("Normal"));
        add(new SubMode("Blur"));
        setDefault("Normal");
    }};
    private final DragValue position = new DragValue("", this.getParent(), new Vector2d(200, 200), true);


    private boolean glow, shadow;
    private boolean normalBackGround, blurBackGround;
    private String username, coordinates;
    private float nameWidth, userWidth, xyzWidth;
    private Color logoColor;

    public NovoInterface(String name, Interface parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {
        String text = " | " + Client.VERSION + " | " + mc.thePlayer.getDisplayName().getFormattedText() + " | " + ServerUtils.getRemoteIp() + " | " + "Fps:"+ Minecraft.getDebugFPS();
        int sb = 6 * 25 - 5;
        int left = FontManager.getProductSansRegular(20).width(text);
        position.scale = new Vector2d(200, 100);

        if (mc == null || mc.gameSettings.showDebugInfo || mc.theWorld == null || mc.thePlayer == null || Client.CLIENT_TYPE != Type.RISE) {
            return;
        }

        this.getParent().setModuleSpacing(this.productSansRegular.height());
        this.getParent().setWidthComparator(this.arrayListFont);
        this.getParent().setEdgeOffset(10);

        // modules in the top right corner of the screen
        for (final ModuleComponent moduleComponent : this.getParent().getActiveModuleComponents()) {
            if (moduleComponent.animationTime == 0) {
                continue;
            }

            String name = (this.getParent().lowercase.getValue() ? moduleComponent.getTranslatedName().toLowerCase() : moduleComponent.getTranslatedName()).replace(getParent().getRemoveSpaces().getValue() ? " " : "", "");

            String tag = (this.getParent().lowercase.getValue() ? moduleComponent.getTag().toLowerCase() : moduleComponent.getTag())
                    .replace(getParent().getRemoveSpaces().getValue() ? " " : "", "");

            final double x = moduleComponent.getPosition().getX();
            final double y = moduleComponent.getPosition().getY();
            final Color finalColor = moduleComponent.getColor();

            final double widthOffset = arrayListFont == minecraft ? 3.5 : 2;

            if (this.normalBackGround || this.blurBackGround) {
                Runnable backgroundRunnable = () ->
                        RenderUtil.rectangle(x + 0.5 - widthOffset, y - 2.5,
                                (moduleComponent.nameWidth + moduleComponent.tagWidth) + 2 + widthOffset,
                                this.getParent().moduleSpacing, getTheme().getBackgroundShade());

                if (this.normalBackGround) {
                    NORMAL_RENDER_RUNNABLES.add(backgroundRunnable);
                }

                if (this.blurBackGround) {
                    NORMAL_BLUR_RUNNABLES.add(() ->
                            RenderUtil.rectangle(x + 0.5 - widthOffset, y - 2.5,
                                    (moduleComponent.nameWidth + moduleComponent.tagWidth) + 2 + widthOffset,
                                    this.getParent().moduleSpacing, Color.BLACK));
                }

                // Draw the glow/shadow around the module
                NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                    if (glow || shadow) {

                        RenderUtil.rectangle(x + 0.5 - widthOffset, y - 2.5,
                                (moduleComponent.nameWidth + moduleComponent.tagWidth) + 2 + widthOffset,
                                this.getParent().moduleSpacing, glow ? ColorUtil.withAlpha(finalColor, 164) : getTheme().getDropShadow());
                    }
                });
            }

            final boolean hasTag = !moduleComponent.getTag().isEmpty() && this.getParent().suffix.getValue();

            Runnable textRunnable = () -> {
                if (dropShadow.getValue()) {
                    arrayListFont.drawStringWithShadow(name, x, y, finalColor.getRGB());

                    if (hasTag) {
                        arrayListFont.drawStringWithShadow(tag, x + moduleComponent.getNameWidth() + 3, y, 0xFFCCCCCC);
                    }
                } else {
                    arrayListFont.drawString(name, x, y, finalColor.getRGB());

                    if (hasTag) {
                        arrayListFont.drawString(tag, x + moduleComponent.getNameWidth() + 3, y, 0xFFCCCCCC);
                    }
                }
            };

            Runnable shadowRunnable = () -> {
                arrayListFont.drawString(name, x, y, Color.BLACK.getRGB());

                if (hasTag) {
                    arrayListFont.drawString(tag, x + moduleComponent.getNameWidth() + 3, y, Color.BLACK.getRGB());
                }
            };

            NORMAL_RENDER_RUNNABLES.add(textRunnable);

            if (glow) {
                NORMAL_POST_BLOOM_RUNNABLES.add(textRunnable);
            } else if (shadow) {
                NORMAL_POST_BLOOM_RUNNABLES.add(shadowRunnable);
            }

            if (this.sidebar.getValue()) {
                Runnable runnable = () -> RenderUtil.roundedRectangle(x + moduleComponent.getNameWidth() + moduleComponent.getTagWidth() + 2, y - 1.5f,
                        2, 9, 1, finalColor);

                runnable.run();
                NORMAL_POST_BLOOM_RUNNABLES.add(runnable);
            }
        }

        if (coordinates == null || username == null) return;

        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
            if (glow || shadow) {
                // information of user in the bottom right corner of the screen
                final float x = event.getScaledResolution().getScaledWidth();
                final float y = event.getScaledResolution().getScaledHeight() - this.productSansRegular.height() - 1;
                //    this.productSansRegular.drawStringWithShadow("java.lang.NullPointerException", x - userWidth - 5, y, 0xFFCCCCCC);
//                this.productSansMedium18.drawStringWithShadow(username, x - nameWidth - 5, y, 0xFFCCCCCC);

                // coordinates of user in the bottom left corner of the screen
                final float coordX = 5;
                this.productSansRegular.drawStringWithShadow("XYZ:", coordX, y - (mc.currentScreen instanceof GuiChat ? 13 : 0), new Color(-1).getRGB());
                this.productSansMedium18.drawStringWithShadow(coordinates, coordX + xyzWidth, y - (mc.currentScreen instanceof GuiChat ? 13 : 0), 0xFFCCCCCC);
            }

            if (!stopWatch.finished(2000)) {
                ParticleComponent.render();
            }
        });

        // information of user in the bottom right corner of the screen
        final float x = event.getScaledResolution().getScaledWidth();
        final float y = event.getScaledResolution().getScaledHeight() - this.productSansRegular.height() - 1;
        final float coordX = 5;

        // Don't draw if the F3 menu is open
        if (mc.gameSettings.showDebugInfo) return;
        Color color1 = ColorUtil.mixColors(getTheme().getFirstColor(), getTheme().getSecondColor(), getTheme().getBlendFactor(new Vector2d(0, position.position.y)));
        Color color2 = ColorUtil.mixColors(getTheme().getFirstColor(), getTheme().getSecondColor(), getTheme().getBlendFactor(new Vector2d(0, position.position.y + position.scale.y * 8.75)));

        //background
        RenderUtil.roundedRectangle(position.position.x + 10, position.position.y + 10, left + position.scale.x - sb, position.scale.y - 80, 0, new Color(0,0,0,100));
        //sb
        FontManager.getProductSansRegular(20).drawStringWithShadow(Client.NAME, position.position.x + 13, position.position.y + 17, logoColor.getRGB());
        //client name
        FontManager.getProductSansRegular(20).drawStringWithShadow(text, position.position.x + 62, position.position.y + 17, -1);
        //lift
        RenderUtil.drawRoundedGradientRect(position.position.x + 10, position.position.y + 8, left + position.scale.x - sb, position.scale.y - 96, 0, color1,color2,true);
        //lift
        RenderUtil.drawRoundedGradientRect(position.position.x + 10, position.position.y + 8, left + position.scale.x - sb, position.scale.y - 96, 0, color2,color1,true);

        this.productSansRegular.drawStringWithShadow("XYZ:", coordX, y - (mc.currentScreen instanceof GuiChat ? 13 : 0), new Color(-1).getRGB());

        this.productSansMedium18.drawStringWithShadow(coordinates, coordX + xyzWidth, y - (mc.currentScreen instanceof GuiChat ? 13 : 0), 0xFFCCCCCC);


        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
            //shadow
            FontManager.getProductSansRegular(20).drawStringWithShadow(Client.NAME, position.position.x + 13 , position.position.y + 17, logoColor.getRGB());
        });

        if (mc.thePlayer.ticksExisted % 150 == 0) {
            stopWatch.reset();
        }
    };

    @EventLink()
    public final Listener<KillEvent> onKill = event -> {
        if (Client.CLIENT_TYPE != Type.RISE) return;

        if (!stopWatch.finished(2000) && this.particles.getValue()) {
            for (int i = 0; i <= 10; i++) {
                ParticleComponent.add(new Particle(new Vector2f(0, 0),
                        new Vector2f((float) Math.random(), (float) Math.random())));
            }
        }

        stopWatch.reset();
    };

    @EventLink()
    public final Listener<TickEvent> onTick = event -> {
        if (Client.CLIENT_TYPE != Type.RISE || mc.thePlayer == null || !mc.getNetHandler().doneLoadingTerrain) return;

        threadPool.execute(() -> {
            glow = this.shader.getValue().getName().equals("Glow");
            shadow = this.shader.getValue().getName().equals("Shadow");
            arrayListFont = this.font.getValue().getName().equals("Minecraft") ? minecraft : productSansRegular;

            username = mc.getSession() == null || mc.getSession().getUsername() == null ? "null" : mc.getSession().getUsername();
            nameWidth = this.productSansMedium18.width(username);
            userWidth = this.productSansRegular.width("java.lang.NullPointerException") + 2;
            coordinates = (int) mc.thePlayer.posX + ", " + (int) mc.thePlayer.posY + ", " + (int) mc.thePlayer.posZ;
            xyzWidth = this.productSansMedium18.width("XYZ:") + 2;

            logoColor = this.getTheme().getFirstColor();

            normalBackGround = background.getValue().getName().equals("Normal");
            blurBackGround = normalBackGround || background.getValue().getName().equals("Blur");

            // modules in the top right corner of the screen
            for (final ModuleComponent moduleComponent : this.getParent().getActiveModuleComponents()) {
                if (moduleComponent.animationTime == 0) {
                    continue;
                }

                final boolean hasTag = !moduleComponent.getTag().isEmpty() && this.getParent().suffix.getValue();

                String name = (this.getParent().lowercase.getValue() ? moduleComponent.getTranslatedName().toLowerCase() : moduleComponent.getTranslatedName()).replace(getParent().getRemoveSpaces().getValue() ? " " : "", "");

                String tag = (this.getParent().lowercase.getValue() ? moduleComponent.getTag().toLowerCase() : moduleComponent.getTag())
                        .replace(getParent().getRemoveSpaces().getValue() ? " " : "", "");

                Color color = this.getTheme().getFirstColor();
                switch (this.colorMode.getValue().getName()) {
                    case "Breathe": {
                        double factor = this.getTheme().getBlendFactor(new Vector2d(0, 0));
                        color = ColorUtil.mixColors(this.getTheme().getFirstColor(), this.getTheme().getSecondColor(), factor);
                        break;
                    }
                    case "Fade": {
                        color = this.getTheme().getAccentColor(new Vector2d(0, moduleComponent.getPosition().getY()));
                        break;
                    }
                }

                moduleComponent.setColor(color);

                moduleComponent.setNameWidth(arrayListFont.width(name));
                moduleComponent.setTagWidth(hasTag ? (arrayListFont.width(tag) + 4) : 0);
            }
        });
    };
}