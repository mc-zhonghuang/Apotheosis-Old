package cn.hackedmc.apotheosis.packetlog.impl;

import cn.hackedmc.apotheosis.packetlog.Check;
import cn.hackedmc.apotheosis.util.CryptUtil;
import cn.hackedmc.apotheosis.util.vantage.HWIDUtil;
import cn.hackedmc.fucker.Fucker;

public class FlyingCheck extends Check {
    @Override
    public boolean run() {
        return Fucker.uuid.equals(CryptUtil.Base64Crypt.encrypt(HWIDUtil.getUUID()));
    }
}
