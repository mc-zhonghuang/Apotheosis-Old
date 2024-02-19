package cn.hackedmc.apotheosis.manager;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.combat.AntiBot;
import cn.hackedmc.apotheosis.module.impl.combat.KillAura;
import cn.hackedmc.apotheosis.module.impl.combat.Teams;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.TickEvent;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.fucker.Fucker;
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

/**
 * @author Alan
 * @since 3/03/2022
 */
public class TargetManager extends ConcurrentLinkedQueue<Entity> implements InstanceAccess {

    boolean players = true;
    boolean ircs = true;
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
        return (players && entity instanceof EntityPlayer) || (animals && (entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem ||
                entity instanceof EntityBat)) || (mobs && (entity instanceof EntityMob || entity instanceof EntityVillager || entity instanceof EntitySlime ||
                entity instanceof EntityGhast || entity instanceof EntityDragon));
    }

    public void updateTargets() {
        try {
            KillAura killAura = getModule(KillAura.class);
            players = killAura.player.getValue();
            ircs = killAura.irc.getValue();
            invisibles = killAura.invisibles.getValue();
            animals = killAura.animals.getValue();
            mobs = killAura.mobs.getValue();
            teams = Teams.INSTANCE.isEnabled();
            this.clear();
            mc.theWorld.loadedEntityList.stream()
                    .filter(entity -> entity != mc.thePlayer && checker(entity) && (invisibles || !entity.isInvisible()) && (!(entity instanceof EntityLivingBase) || ((EntityLivingBase) entity).getHealth() > 0) && (!teams || Teams.isSameTeam(entity)) && (!ircs || Fucker.usernames.get(entity.getCommandSenderName()) == null))
                    .forEach(this::add);
        } catch (Exception e) {
            // Don't give crackers clues...
            if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
        }
    }

    public List<Entity> getTargets(final double range) {
        if (this.isEmpty()) {
            return new ArrayList<>();
        }
        return this.stream()
                .filter(entity -> mc.thePlayer.getDistanceToEntity(entity) < range && mc.theWorld.loadedEntityList.contains(entity) && !Client.INSTANCE.getBotManager().contains(entity))
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))
                .collect(Collectors.toList());
    }
}