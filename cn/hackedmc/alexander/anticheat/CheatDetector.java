package cn.hackedmc.alexander.anticheat;

import cn.hackedmc.alexander.anticheat.alert.AlertManager;
import cn.hackedmc.alexander.anticheat.check.manager.CheckManager;
import cn.hackedmc.alexander.anticheat.data.PlayerData;
import cn.hackedmc.alexander.anticheat.listener.RegistrationListener;
import lombok.Getter;
import net.minecraft.client.Minecraft;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class CheatDetector {

    public final Map<UUID, PlayerData> playerMap = new ConcurrentHashMap<>();

    private final RegistrationListener registrationListener = new RegistrationListener();
    private final AlertManager alertManager = new AlertManager();

    public CheatDetector() {
        CheckManager.setup();
    }

    public void incrementTick() {
        for (PlayerData data : playerMap.values()) {
            if (Minecraft.getMinecraft().theWorld.playerEntities.contains(data.getPlayer())) {
                data.incrementTick();
            } else {
                registrationListener.handleDestroy(data.getPlayer().getUniqueID());
            }
        }
    }
}
