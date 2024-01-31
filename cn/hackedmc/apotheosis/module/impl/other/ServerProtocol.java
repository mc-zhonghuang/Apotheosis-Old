package cn.hackedmc.apotheosis.module.impl.other;

import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.module.impl.other.protocols.HuaYuTingProtocol;
import cn.hackedmc.apotheosis.value.impl.ModeValue;

@ModuleInfo(name = "module.other.protocol.name", description = "module.other.protocol.description", category = Category.OTHER)
public class ServerProtocol extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new HuaYuTingProtocol("HuaYuTing", this))
            .setDefault("HuaYuTing");
}
