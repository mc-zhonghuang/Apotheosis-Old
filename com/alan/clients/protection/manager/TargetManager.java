package com.alan.clients.protection.manager;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.sun.org.apache.xpath.internal.operations.String;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alan
 * @since 3/03/2022
 */
public class TargetManager extends ConcurrentLinkedQueue<Entity> implements InstanceAccess {

    boolean players = true;
    boolean invisibles = false;
    boolean animals = false;
    boolean mobs = false;
    boolean teams = false;

    private int loadedEntitySize;

    public void init() {
        Client.INSTANCE.getEventBus().register(this);
    }

    @EventLink()
    public final Listener<TickEvent> onTick = event -> {
        if (mc.thePlayer.ticksExisted % 150 == 0 || loadedEntitySize != mc.theWorld.loadedEntityList.size()) {
            this.updateTargets();
            loadedEntitySize = mc.theWorld.loadedEntityList.size();
        }
    };

    private boolean checker(Entity entity) {
        return (players && entity instanceof EntityPlayer && ((EntityPlayer) entity).getHealth() > 0 && (!teams || !((EntityPlayer) entity).isOnSameTeam(mc.thePlayer))) || (invisibles && entity.isInvisible()) || (animals && (entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem ||
                entity instanceof EntityBat)) || (mobs && (entity instanceof EntityMob || entity instanceof EntityVillager || entity instanceof EntitySlime ||
                entity instanceof EntityGhast || entity instanceof EntityDragon));
    }

    public void updateTargets() {
        try {
            KillAura killAura = getModule(KillAura.class);
            players = killAura.player.getValue();
            invisibles = killAura.invisibles.getValue();
            animals = killAura.animals.getValue();
            mobs = killAura.mobs.getValue();
            teams = killAura.teams.getValue();

            this.clear();
            mc.theWorld.loadedEntityList.stream().filter(entity -> entity != mc.thePlayer && checker(entity) && !entity.isDead && !Client.INSTANCE.getBotManager().contains(entity)).forEach(this::add);
        } catch (Exception ignored) {
            // Don't give crackers clues...
            if (Client.DEVELOPMENT_SWITCH) ignored.printStackTrace();
        }
    }

    public List<Entity> getTargets(final double range) {
        if (this.isEmpty()) {
            return new ArrayList<>();
        }
        return this.stream()
                .filter(entity -> mc.thePlayer.getDistanceToEntity(entity) < range)
                .filter(entity -> mc.theWorld.loadedEntityList.contains(entity))
                .filter(entity -> !Client.INSTANCE.getBotManager().contains(entity))
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))
                .collect(Collectors.toList());
    }
}