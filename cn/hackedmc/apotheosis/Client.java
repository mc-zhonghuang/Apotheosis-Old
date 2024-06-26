package cn.hackedmc.apotheosis;

import by.radioegor146.nativeobfuscator.Native;
import cn.hackedmc.apotheosis.anticheat.CheatDetector;
import cn.hackedmc.apotheosis.bots.BotManager;
import cn.hackedmc.apotheosis.command.CommandManager;
import cn.hackedmc.apotheosis.command.impl.*;
import cn.hackedmc.apotheosis.component.ComponentManager;
import cn.hackedmc.apotheosis.component.impl.event.EntityKillEventComponent;
import cn.hackedmc.apotheosis.component.impl.event.EntityTickComponent;
import cn.hackedmc.apotheosis.component.impl.hud.AdaptiveRefreshRateComponent;
import cn.hackedmc.apotheosis.component.impl.hud.DragComponent;
import cn.hackedmc.apotheosis.component.impl.hypixel.APIKeyComponent;
import cn.hackedmc.apotheosis.component.impl.hypixel.InventoryDeSyncComponent;
import cn.hackedmc.apotheosis.component.impl.module.teleportaura.TeleportAuraComponent;
import cn.hackedmc.apotheosis.component.impl.packetlog.PacketLogComponent;
import cn.hackedmc.apotheosis.component.impl.patches.GuiClosePatchComponent;
import cn.hackedmc.apotheosis.component.impl.performance.ParticleDistanceComponent;
import cn.hackedmc.apotheosis.component.impl.player.*;
import cn.hackedmc.apotheosis.component.impl.render.*;
import cn.hackedmc.apotheosis.component.impl.viamcp.BlockHitboxFixComponent;
import cn.hackedmc.apotheosis.component.impl.viamcp.HitboxFixComponent;
import cn.hackedmc.apotheosis.component.impl.viamcp.MinimumMotionFixComponent;
import cn.hackedmc.apotheosis.creative.RiseTab;
import cn.hackedmc.apotheosis.manager.TargetManager;
import cn.hackedmc.apotheosis.module.api.manager.ModuleManager;
import cn.hackedmc.apotheosis.module.impl.combat.*;
import cn.hackedmc.apotheosis.module.impl.exploit.*;
import cn.hackedmc.apotheosis.module.impl.ghost.*;
import cn.hackedmc.apotheosis.module.impl.movement.*;
import cn.hackedmc.apotheosis.module.impl.other.Insults;
import cn.hackedmc.apotheosis.module.impl.other.*;
import cn.hackedmc.apotheosis.module.impl.player.*;
import cn.hackedmc.apotheosis.module.impl.render.*;
import cn.hackedmc.apotheosis.newevent.bus.impl.EventBus;
import cn.hackedmc.apotheosis.packetlog.api.manager.PacketLogManager;
import cn.hackedmc.apotheosis.packetlog.impl.FlyingCheck;
import cn.hackedmc.apotheosis.script.ScriptManager;
import cn.hackedmc.apotheosis.security.ExploitManager;
import cn.hackedmc.apotheosis.ui.click.clover.CloverClickGUI;
import cn.hackedmc.apotheosis.ui.click.standard.RiseClickGUI;
import cn.hackedmc.apotheosis.ui.menu.impl.alt.AltManagerMenu;
import cn.hackedmc.apotheosis.ui.music.MusicMenu;
import cn.hackedmc.apotheosis.ui.theme.ThemeManager;
import cn.hackedmc.apotheosis.util.file.FileManager;
import cn.hackedmc.apotheosis.util.file.FileType;
import cn.hackedmc.apotheosis.util.file.alt.AltManager;
import cn.hackedmc.apotheosis.util.file.config.ConfigFile;
import cn.hackedmc.apotheosis.util.file.config.ConfigManager;
import cn.hackedmc.apotheosis.util.file.data.DataManager;
import cn.hackedmc.apotheosis.util.file.insult.InsultFile;
import cn.hackedmc.apotheosis.util.file.insult.InsultManager;
import cn.hackedmc.apotheosis.util.localization.Locale;
import cn.hackedmc.apotheosis.util.value.ConstantManager;
import cn.hackedmc.fucker.Fucker;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.viamcp.ViaMCP;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("UnusedDeclaration")
@Getter
@Native
public enum Client {

    /**
     * Simple enum instance for our client as enum instances
     * are immutable and are very easy to create and use.
     */
    INSTANCE;

    public static String NAME = "Apotheosis";
    public static String VERSION = "1.0";
    public static final String VERSION_FULL = "1.0"; // Used to give more detailed build info on beta builds
    public static final String VERSION_DATE = "August 1, 2023";

    public static boolean DEVELOPMENT_SWITCH = true;
    public static boolean BETA_SWITCH = true;
    public static boolean FIRST_LAUNCH = true;
    public static Type CLIENT_TYPE;


    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Setter
    private Locale locale; // The language of the client

    private EventBus eventBus;
    private ModuleManager moduleManager;
    private ComponentManager componentManager;
    private CommandManager commandManager;
    private ExploitManager securityManager;
    private BotManager botManager;
    private ThemeManager themeManager;
    @Setter
    private ScriptManager scriptManager;
    private DataManager dataManager;
    private CheatDetector cheatDetector;

    private FileManager fileManager;

    private ConfigManager configManager;
    private AltManager altManager;
    private InsultManager insultManager;
    private TargetManager targetManager;
    private ConstantManager constantManager;
    private PacketLogManager packetLogManager;

    private ConfigFile configFile;

    private CloverClickGUI cloverClickGUI;
    public RiseClickGUI standardClickGUI;
    public AltManagerMenu altManagerMenu;
    public MusicMenu musicMenu;

    private RiseTab creativeTab;

    /**
     * The main method when the Minecraft#startGame method is about
     * finish executing our client gets called and that's where we
     * can start loading our own classes and modules.
     */
    public void initClient() {
        // Crack Protection
//        if (!this.validated && !DEVELOPMENT_SWITCH) {
//            return;
//        }

        // Init
        Minecraft mc = Minecraft.getMinecraft();

        // Compatibility
        mc.gameSettings.guiScale = 2;
        mc.gameSettings.ofFastRender = false;
        mc.gameSettings.ofShowGlErrors = DEVELOPMENT_SWITCH;

        // Performance
        mc.gameSettings.ofSmartAnimations = true;
        mc.gameSettings.ofSmoothFps = false;
        mc.gameSettings.ofFastMath = false;

        Fucker.fuckClass(this.getClass(), this);

        // Register
//        String[] paths = {
//                "cn.hackedmc.apotheosis.",
//                "hackclient."
//        };
//
//        for (String path : paths) {
//            if (!ReflectionUtil.dirExist(path)) {
//                continue;
//            }
//
//            Class<?>[] classes = ReflectionUtil.getClassesInPackage(path);
//
//            for (Class<?> clazz : classes) {
//                try {
//                    if (clazz.isAnnotationPresent(Hidden.class)) continue;
//
//                    if (Component.class.isAssignableFrom(clazz) && clazz != Component.class) {
//                        this.componentManager.add((Component) clazz.getConstructor().newInstance());
//                    } else if (Module.class.isAssignableFrom(clazz) && clazz != Module.class) {
//                        this.moduleManager.add((Module) clazz.getConstructor().newInstance());
//                    } else if (Command.class.isAssignableFrom(clazz) && clazz != Command.class) {
//                        this.commandManager.add((Command) clazz.getConstructor().newInstance());
//                    } else if (Check.class.isAssignableFrom(clazz) && clazz != Check.class) {
//                        this.packetLogManager.add((Check) clazz.getConstructor().newInstance());
//                    }
//                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
//                         NoSuchMethodException ignored) {}
//            }
//
//            break;
//        }

        this.moduleManager.addAll(
                // Combat
                AntiBot.class,
                ComboOneHit.class,
                Criticals.class,
                KillAura.class,
                NewTeleportAura.class,
                Regen.class,
                TeleportAura.class,
                Velocity.class,
                Teams.class,
                // Exploit
                ConsoleSpammer.class,
                Crasher.class,
                Disabler.class,
                GodMode.class,
                KeepContainer.class,
                LightningTracker.class,
                NoRotate.class,
                PingSpoof.class,
                StaffDetector.class,

                // Ghost
                AimAssist.class,
                AimBacktrack.class,
                AutoClicker.class,
                AutoPlace.class,
                Backtrack.class,
                ClickAssist.class,
                Eagle.class,
                FastPlace.class,
                GuiClicker.class,
                HitBox.class,
                KeepSprint.class,
                NoClickDelay.class,
                Reach.class,
                SafeWalk.class,
                WTap.class,

                // Movement
                Flight.class,
                InventoryMove.class,
                Jesus.class,
                LongJump.class,
                NoClip.class,
                NoWeb.class,
                NoSlow.class,
                Phase.class,
                PotionExtender.class,
                Sneak.class,
                Speed.class,
                Sprint.class,
                Step.class,
                Strafe.class,
                TargetStrafe.class,
                Teleport.class,
                WallClimb.class,

                // Other
                AntiAFK.class,
                AntiExploit.class,
                AutoGG.class,
                AutoGroomer.class,
                cn.hackedmc.apotheosis.module.impl.other.CheatDetector.class,
                ClickSounds.class,
                ClientSpoofer.class,
                Debugger.class,
                DiscordPresence.class,
                HypixelAutoPlay.class,
                Insults.class,
                MurderMystery.class,
                NoGuiClose.class,
                NoPitchLimit.class,
                Nuker.class,
                PlayerNotifier.class,
                ServerProtocol.class,
                Spammer.class,
                Spotify.class,
                Timer.class,
                Translator.class,

                // Player
                AntiSuffocate.class,
                AntiVoid.class,
                AutoHead.class,
                AutoPot.class,
                AutoSoup.class,
                AutoTool.class,
                Blink.class,
                Breaker.class,
                ChestAura.class,
                FastBreak.class,
                FastUse.class,
                AntiFireBall.class,
                FlagDetector.class,
                InventorySync.class,
                Manager.class,
                NoFall.class,
                Scaffold.class,
                Stealer.class,
                Twerk.class,

                // Render
                Ambience.class,
                Animations.class,
                AppleSkin.class,
                BPSCounter.class,
                ChestESP.class,
                ClickGUI.class,
                CPSCounter.class,
                ESP.class,
                Footprint.class,
                FPSCounter.class,
                FreeCam.class,
                FreeLook.class,
                FullBright.class,
                Glint.class,
                Gui.class,
                InventoryHUD.class,
                KeyBinds.class,
                HotBar.class,
                HurtCamera.class,
                HurtColor.class,
                Interface.class,
                ItemPhysics.class,
                KeyStrokes.class,
                KillEffect.class,
//                MusicPlayer.class,
                NameTags.class,
                NoCameraClip.class,
                OffscreenESP.class,
                Particles.class,
                ProjectionESP.class,
                Radar.class,
                ScoreBoard.class,
                SessionStats.class,
                SniperOverlay.class,
                Streamer.class,
                TargetInfo.class,
                Tracers.class,
                UnlimitedChat.class,
                ViewBobbing.class
        );

        this.commandManager.addAll(
                Bind.class,
                Clip.class,
                Config.class,
                DeveloperReload.class,
                Friend.class,
                Help.class,
                cn.hackedmc.apotheosis.command.impl.Insults.class,
                IRC.class,
                Name.class,
                Panic.class,
                Say.class,
                Script.class,
                Stuck.class,
                Toggle.class
        );

        this.packetLogManager.addAll(
            FlyingCheck.class
        );
        /*
        componentManager.add(new EntityKillEventComponent());
        componentManager.add(new EntityTickComponent());
        componentManager.add(new AdaptiveRefreshRateComponent());
        componentManager.add(new DragComponent());
        componentManager.add(new APIKeyComponent());
        componentManager.add(new InventoryDeSyncComponent());
        componentManager.add(new TeleportAuraComponent());
        componentManager.add(new PacketLogComponent());
        componentManager.add(new GuiClosePatchComponent());
        componentManager.add(new ParticleDistanceComponent());
        componentManager.add(new BadPacketsComponent());
        componentManager.add(new BlinkComponent());
        componentManager.add(new FallDistanceComponent());
        componentManager.add(new GUIDetectionComponent());
        componentManager.add(new ItemDamageComponent());
        componentManager.add(new LastConnectionComponent());
        componentManager.add(new PacketlessDamageComponent());
        componentManager.add(new PingSpoofComponent());
        componentManager.add(new RotationComponent());
        componentManager.add(new SelectorDetectionComponent());
        componentManager.add(new SlotComponent());
        componentManager.add(new ESPComponent());
        componentManager.add(new NotificationComponent());
        componentManager.add(new ParticleComponent());
        componentManager.add(new ProjectionComponent());
        componentManager.add(new SmoothCameraComponent());
        componentManager.add(new BlockHitboxFixComponent());
        componentManager.add(new HitboxFixComponent());
        componentManager.add(new MinimumMotionFixComponent());
        this.componentManager.init();
        
         */
        // Init Managers
        this.targetManager.init();
        this.dataManager.init();
        this.moduleManager.init();
        this.securityManager.init();
        this.botManager.init();
        this.commandManager.init();
        this.fileManager.init();
        this.configManager.init();
        this.altManager.init();
        this.insultManager.init();
        this.scriptManager.init();
        this.packetLogManager.init();

        final File file = new File(ConfigManager.CONFIG_DIRECTORY, "latest.json");
        this.configFile = new ConfigFile(file, FileType.CONFIG);
        this.configFile.allowKeyCodeLoading();
        this.configFile.read();

        this.insultManager.update();
        this.insultManager.forEach(InsultFile::read);

        this.creativeTab = new RiseTab();

        ViaMCP.staticInit();
    }

    /**
     * The terminate method is called when the Minecraft client is shutting
     * down, so we can cleanup our stuff and ready ourselves for the client quitting.
     */
    public void terminate() {
        this.configFile.write();
    }
}

