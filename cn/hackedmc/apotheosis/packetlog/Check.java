package cn.hackedmc.apotheosis.packetlog;


import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import lombok.Getter;
import lombok.Setter;


/**
 * @author Alan
 * @since 10/19/2021
 */
@Getter
@Setter
public abstract class Check implements InstanceAccess {
    public abstract boolean run();
}