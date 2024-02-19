package cn.hackedmc.apotheosis.module.impl.combat;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.util.player.PlayerUtil;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

@ModuleInfo(name = "module.combat.teams.name", description = "module.combat.teams.description", category = Category.COMBAT)
public final class Teams extends Module {
    public static Teams INSTANCE;
    private final BooleanValue armorValue = new BooleanValue("ArmorColor", this,true);
    private final BooleanValue colorValue = new BooleanValue("Color",this, true);
    private final BooleanValue scoreboardValue = new BooleanValue("ScoreboardTeam",this, true);
    public Teams(){
        INSTANCE = this;
    }
    public static boolean isSameTeam(Entity entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)entity;
            if (!Objects.requireNonNull(Client.INSTANCE.getModuleManager().get(Teams.class).isEnabled())) {
                return false;
            } else {
                return Teams.INSTANCE.armorValue.getValue() && PlayerUtil.armorTeam(entityplayer) || Teams.INSTANCE.colorValue.getValue() && PlayerUtil.colorTeam(entityplayer) || Teams.INSTANCE.scoreboardValue.getValue() && PlayerUtil.scoreTeam(entityplayer);
            }
        } else {
            return false;
        }
    }
}