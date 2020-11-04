package org.example.netty_demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable // 表示它可以被多个channel安全地共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 从channel中读取消息时
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf in = (ByteBuf) msg;
        System.out.println(
                "Server received:" + in.toString(CharsetUtil.UTF_8));
        ctx.write(in); // 将处理发送给下一个远程节点
    }

    //
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    // 处理异常方法
    // 打印了异常并关闭处理线程
    /*
    每个Channel都
     */

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
