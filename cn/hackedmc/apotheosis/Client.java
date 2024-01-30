package cn.hackedmc.apotheosis;

import by.radioegor146.nativeobfuscator.Native;
import cn.hackedmc.apotheosis.anticheat.CheatDetector;
import cn.hackedmc.apotheosis.api.Hidden;
import cn.hackedmc.apotheosis.bots.BotManager;
import cn.hackedmc.apotheosis.command.Command;
import cn.hackedmc.apotheosis.command.CommandManager;
import cn.hackedmc.apotheosis.component.Component;
import cn.hackedmc.apotheosis.component.ComponentManager;
import cn.hackedmc.apotheosis.creative.RiseTab;
import cn.hackedmc.apotheosis.manager.TargetManager;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.manager.ModuleManager;
import cn.hackedmc.apotheosis.newevent.bus.impl.EventBus;
import cn.hackedmc.apotheosis.packetlog.Check;
import cn.hackedmc.apotheosis.packetlog.api.manager.PacketLogManager;
import cn.hackedmc.apotheosis.script.ScriptManager;
import cn.hackedmc.apotheosis.security.ExploitManager;
import cn.hackedmc.apotheosis.ui.click.clover.CloverClickGUI;
import cn.hackedmc.apotheosis.ui.click.dropdown.DropdownClickGUI;
import cn.hackedmc.apotheosis.ui.click.standard.RiseClickGUI;
import cn.hackedmc.apotheosis.ui.menu.impl.alt.AltManagerMenu;
import cn.hackedmc.apotheosis.ui.theme.ThemeManager;
import cn.hackedmc.apotheosis.util.ReflectionUtil;
import cn.hackedmc.apotheosis.util.file.FileManager;
import cn.hackedmc.apotheosis.util.file.FileType;
import cn.hackedmc.apotheosis.util.file.alt.AltManager;
import cn.hackedmc.apotheosis.util.file.config.ConfigFile;
import cn.hackedmc.apotheosis.util.file.config.ConfigManager;
import cn.hackedmc.apotheosis.util.file.data.DataManager;
import cn.hackedmc.apotheosis.util.file.insult.InsultFile;
import cn.hackedmc.apotheosis.util.file.insult.InsultManager;
import cn.hackedmc.apotheosis.util.localization.Locale;
import cn.hackedmc.apotheosis.util.math.MathConst;
import cn.hackedmc.apotheosis.util.value.ConstantManager;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.viamcp.ViaMCP;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public static Type CLIENT_TYPE = Type.RISE;


    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Setter
    private Locale locale = Locale.EN_US; // The language of the client

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
    private RiseClickGUI standardClickGUI;
    private DropdownClickGUI dropdownClickGUI;
    private AltManagerMenu altManagerMenu;

    private RiseTab creativeTab;

    /**
     * The main method when the Minecraft#startGame method is about
     * finish executing our client gets called and that's where we
     * can start loading our own classes and modules.
     */
    public void initRise() {
        // Crack Protection
//        if (!this.validated && !DEVELOPMENT_SWITCH) {
//            return;
//        }

        // Init
        Minecraft mc = Minecraft.getMinecraft();
        MathConst.calculate();

        // Compatibility
        mc.gameSettings.guiScale = 2;
        mc.gameSettings.ofFastRender = false;
        mc.gameSettings.ofShowGlErrors = DEVELOPMENT_SWITCH;

        // Performance
        mc.gameSettings.ofSmartAnimations = true;
        mc.gameSettings.ofSmoothFps = false;
        mc.gameSettings.ofFastMath = false;

        this.moduleManager = new ModuleManager();
        this.componentManager = new ComponentManager();
        this.commandManager = new CommandManager();
        this.fileManager = new FileManager();
        this.configManager = new ConfigManager();
        this.altManager = new AltManager();
        this.insultManager = new InsultManager();
        this.dataManager = new DataManager();
        this.securityManager = new ExploitManager();
        this.botManager = new BotManager();
        this.themeManager = new ThemeManager();
//        this.networkManager = new NetworkManager();
        this.scriptManager = new ScriptManager();
        this.targetManager = new TargetManager();
        this.cheatDetector = new CheatDetector();
        this.constantManager = new ConstantManager();
        this.eventBus = new EventBus<>();
        this.packetLogManager = new PacketLogManager();

        // Register
        String[] paths = {
                "cn.hackedmc.apotheosis.",
                "hackclient."
        };

        for (String path : paths) {
            if (!ReflectionUtil.dirExist(path)) {
                continue;
            }

            Class<?>[] classes = ReflectionUtil.getClassesInPackage(path);

            for (Class<?> clazz : classes) {
                try {
                    if (clazz.isAnnotationPresent(Hidden.class)) continue;

                    if (Component.class.isAssignableFrom(clazz) && clazz != Component.class) {
                        this.componentManager.add((Component) clazz.getConstructor().newInstance());
                    } else if (Module.class.isAssignableFrom(clazz) && clazz != Module.class) {
                        this.moduleManager.add((Module) clazz.getConstructor().newInstance());
                    } else if (Command.class.isAssignableFrom(clazz) && clazz != Command.class) {
                        this.commandManager.add((Command) clazz.getConstructor().newInstance());
                    } else if (Check.class.isAssignableFrom(clazz) && clazz != Check.class) {
                        this.packetLogManager.add((Check) clazz.getConstructor().newInstance());
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException ignored) {}
            }

            break;
        }

        // Init Managers
        this.targetManager.init();
        this.dataManager.init();
        this.moduleManager.init();
        this.securityManager.init();
        this.botManager.init();
        this.componentManager.init();
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

        this.standardClickGUI = new RiseClickGUI();
        this.dropdownClickGUI = new DropdownClickGUI();
        this.altManagerMenu = new AltManagerMenu();

        this.creativeTab = new RiseTab();

        ViaMCP.staticInit();

        Display.setTitle(NAME + " " + VERSION_FULL);
    }

    /**
     * The terminate method is called when the Minecraft client is shutting
     * down, so we can cleanup our stuff and ready ourselves for the client quitting.
     */
    public void terminate() {
        this.configFile.write();
    }
}

