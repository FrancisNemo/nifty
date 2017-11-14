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
package com.facebook.nifty.client;

import io.netty.channel.*;


import java.net.SocketAddress;

import static com.google.common.base.Preconditions.checkNotNull;

public final class TimeoutHandler implements ChannelInboundHandler, ChannelOutboundHandler
{
    private static final String NAME = "_TIMEOUT_HANDLER";

    private volatile long lastMessageReceivedNanos = 0L;
    private volatile long  lastMessageSentNanos = 0L;

    public static synchronized void addToPipeline(ChannelPipeline cp)
    {
        checkNotNull(cp, "cp is null");
        if (cp.get(NAME) == null) {
            cp.addFirst(NAME, new TimeoutHandler());
        }
    }

    public static TimeoutHandler findTimeoutHandler(ChannelPipeline cp)
    {
        return (TimeoutHandler) cp.get(NAME);
    }

//    private TimeoutHandler()
//    {
//    }
//
//    @Override
//    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
//        throws Exception
//    {
//        if (e instanceof MessageEvent) {
//            lastMessageReceivedNanos = System.nanoTime();
//        }
//        ctx.sendUpstream(e);
//    }
//
//    @Override
//    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
//        throws Exception
//    {
//        if (e instanceof MessageEvent) {
//            lastMessageSentNanos = System.nanoTime();
//        }
//        ctx.sendDownstream(e);
//    }

    public long getLastMessageReceivedNanos()
    {
        return lastMessageReceivedNanos;
    }

    public long getLastMessageSentNanos()
    {
        return lastMessageSentNanos;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

    }

    @Override
    public void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {

    }

    @Override
    public void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) throws Exception {

    }

    @Override
    public void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {

    }

    @Override
    public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {

    }

    @Override
    public void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {

    }

    @Override
    public void read(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {

    }

    @Override
    public void flush(ChannelHandlerContext channelHandlerContext) throws Exception {

    }
}
