/*
 * Copyright (C) 2012-2016 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.nifty.core;

import com.facebook.nifty.ssl.SslSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConnectionContextHandler extends ChannelInboundHandlerAdapter
{
    //TODO
//    @Override
//    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
//    {
//        super.channelConnected(ctx, e);
//
//        NiftyConnectionContext context = new NiftyConnectionContext();
//        context.setRemoteAddress(ctx.getChannel().getRemoteAddress());
//
//        ctx.setAttachment(context);
//    }
//
//    @Override
//    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//        if (e.getMessage() instanceof SslSession) {
//            NiftyConnectionContext context = (NiftyConnectionContext) ctx.getAttachment();
//            context.setSslSession((SslSession) e.getMessage());
//        } else {
//            super.messageReceived(ctx, e);
//        }
//    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
        ((ByteBuf) msg).release(); // (3)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
};
