/*
 * Copyright 2002-2015 by bafeimao.net, The umbrella Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bafeimao.umbrella.support.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import net.bafeimao.umbrella.support.generated.CommonProto.Packet;
import net.bafeimao.umbrella.support.server.message.HandlerContext;
import net.bafeimao.umbrella.support.server.message.MessageDispatcher;
import net.bafeimao.umbrella.support.server.message.NettyBasedChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultServerHandler extends SimpleChannelInboundHandler<Packet> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServerHandler.class);
    private MessageDispatcher<Packet> messageDispatcher ;
    private  AttributeKey<HandlerContext> key = AttributeKey.newInstance("neutral_context");

    public DefaultServerHandler(MessageDispatcher<Packet> dispatcher) {
        messageDispatcher = dispatcher;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        LOGGER.info("Handling message: {} [type:{}]", packet, packet.getType());

        try {
            ctx.attr(key).setIfAbsent(new NettyBasedChannelHandlerContext(ctx));
            HandlerContext context =  ctx.attr(key).get();

            messageDispatcher.dispatch(context, packet);
        } catch (Exception e) {
            LOGGER.error("{}", e);

            // TODO 处理未处理的异常
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof IOException) {
            LOGGER.error("Client disconnected, id: {}, remote: {}", ctx.channel().id(), ctx.channel().remoteAddress());
        } else {
            LOGGER.error("{}", cause);
        }

        ctx.close();
    }
}
