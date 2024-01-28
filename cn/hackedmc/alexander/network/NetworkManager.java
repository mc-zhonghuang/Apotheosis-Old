package cn.hackedmc.alexander.network;

import by.radioegor146.nativeobfuscator.Native;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import lombok.Getter;
import lombok.Setter;

@Getter
@Native
public final class NetworkManager implements InstanceAccess {
    @Setter
    public String username;
    public String message;

    public void init(String username) {
        this.username = username;
    }

    public static boolean a() {
        return true;
    }
}
