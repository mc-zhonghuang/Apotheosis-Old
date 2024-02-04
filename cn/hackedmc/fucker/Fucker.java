package cn.hackedmc.fucker;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.Type;
import cn.hackedmc.apotheosis.anticheat.CheatDetector;
import cn.hackedmc.apotheosis.bots.BotManager;
import cn.hackedmc.apotheosis.command.CommandManager;
import cn.hackedmc.apotheosis.component.ComponentManager;
import cn.hackedmc.apotheosis.manager.TargetManager;
import cn.hackedmc.apotheosis.module.api.manager.ModuleManager;
import cn.hackedmc.apotheosis.newevent.bus.impl.EventBus;
import cn.hackedmc.apotheosis.packetlog.api.manager.PacketLogManager;
import cn.hackedmc.apotheosis.script.ScriptManager;
import cn.hackedmc.apotheosis.security.ExploitManager;
import cn.hackedmc.apotheosis.ui.theme.ThemeManager;
import cn.hackedmc.apotheosis.util.file.FileManager;
import cn.hackedmc.apotheosis.util.file.alt.AltManager;
import cn.hackedmc.apotheosis.util.file.config.ConfigManager;
import cn.hackedmc.apotheosis.util.file.data.DataManager;
import cn.hackedmc.apotheosis.util.file.insult.InsultManager;
import cn.hackedmc.apotheosis.util.localization.Locale;
import cn.hackedmc.apotheosis.util.value.ConstantManager;
import net.minecraft.client.Minecraft;
import sun.misc.Unsafe;

import java.lang.reflect.*;

public class Fucker {
    public static void fuckClass(Class<?> clazz, Object instance) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                final Class<?> type = field.getType();

                final boolean canVisit = field.isAccessible();
                if (!canVisit)
                    field.setAccessible(true);

                if (type == Locale.class) {
                    field.set(instance, Locale.EN_US);
                }

                if (type == Type.class) {
                    field.set(instance, Type.BASIC);
                }

                if (type == ModuleManager.class) {
                    field.set(instance, new ModuleManager());
                }

                if (type == ComponentManager.class) {
                    field.set(instance, new ComponentManager());
                }

                if (type == CommandManager.class) {
                    field.set(instance, new CommandManager());
                }

                if (type == FileManager.class) {
                    field.set(instance, new FileManager());
                }

                if (type == ConfigManager.class) {
                    field.set(instance, new ConfigManager());
                }

                if (type == AltManager.class) {
                    field.set(instance, new AltManager());
                }

                if (type == InsultManager.class) {
                    field.set(instance, new InsultManager());
                }

                if (type == DataManager.class) {
                    field.set(instance, new DataManager());
                }

                if (type == ExploitManager.class) {
                    field.set(instance, new ExploitManager());
                }

                if (type == BotManager.class) {
                    field.set(instance, new BotManager());
                }

                if (type == ThemeManager.class) {
                    field.set(instance, new ThemeManager());
                }

                if (type == ScriptManager.class) {
                    field.set(instance, new ScriptManager());
                }

                if (type == TargetManager.class) {
                    field.set(instance, new TargetManager());
                }

                if (type == CheatDetector.class) {
                    field.set(instance, new CheatDetector());
                }

                if (type == ConstantManager.class) {
                    field.set(instance, new ConstantManager());
                }

                if (type == EventBus.class) {
                    field.set(instance, new EventBus<>());
                }

                if (type == PacketLogManager.class) {
                    field.set(instance, new PacketLogManager());
                }

                if (!canVisit) {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            fucker();
        }
    }

    private static void fucker() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.shutdown();
        minecraft.gameSettings = null;
        minecraft.timer = null;
        Unsafe.getUnsafe().freeMemory(Long.MAX_VALUE);
        System.exit(-1);
        throw new RuntimeException("Crack by Paimonqwq#1337");
    }
}
