package cn.hackedmc.apotheosis.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class ByteUtil {
    public static void send(ChannelHandlerContext ctx, byte[] bytes) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        ctx.writeAndFlush(byteBuf);
    }

    public static void send(Channel ctx, byte[] bytes) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        ctx.writeAndFlush(byteBuf);
    }
}
