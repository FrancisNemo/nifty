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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.Closeable;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This already has a built in TFramedTransport. No need to wrap.
 */
@NotThreadSafe
class TNiftyAsyncClientTransport extends TTransport
        implements ChannelInboundHandler, ChannelOutboundHandler, Closeable
{
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    // this is largely a guess. there shouldn't really be more than 2 write buffers at any given time.
    private static final int MAX_BUFFERS_IN_QUEUE = 3;
    private final Channel channel;
    private final Queue<ByteBuf> writeBuffers;
    private volatile TNiftyClientListener listener;

    public TNiftyAsyncClientTransport(Channel channel)
    {
        this.channel = channel;
        this.writeBuffers = new ConcurrentLinkedQueue<ByteBuf>();
    }

    public void setListener(TNiftyClientListener listener)
    {
        this.listener = listener;
    }

    @Override
    public boolean isOpen()
    {
        return channel.isOpen();
    }

    @Override
    public void open()
            throws TTransportException
    {
        // no-op
    }

    @Override
    public void close()
    {
        channel.close();
    }

    @Override
    public int read(byte[] bytes, int offset, int length)
            throws TTransportException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(byte[] bytes, int offset, int length)
            throws TTransportException
    {
        getWriteBuffer().writeBytes(bytes, offset, length);
    }

    @Override
    public void flush()
            throws TTransportException
    {
        // all these is to re-use the write buffer. We can only clear
        // and re-use a write buffer when the write operation completes,
        // which is an async operation in netty. the future listener
        // down here will be invoked by Netty I/O thread.
        if (!writeBuffers.isEmpty()) {
            final ByteBuf channelBuffer = writeBuffers.remove();
            channel.write(channelBuffer).addListener(
                    new ChannelFutureListener()
                    {
                        @Override
                        public void operationComplete(ChannelFuture future)
                                throws Exception
                        {
                            if (future.isSuccess()) {
                                channelBuffer.clear();
                                if (writeBuffers.size() < MAX_BUFFERS_IN_QUEUE) {
                                    writeBuffers.add(channelBuffer);
                                }
                            }
                        }
                    }
            );
        }
    }

//    @Override
//    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
//            throws Exception
//    {
//        if (e instanceof MessageEvent) {
//            messageReceived(ctx, (MessageEvent) e);
//        }
//        else if (e instanceof ChannelStateEvent) {
//            ChannelStateEvent evt = (ChannelStateEvent) e;
//            switch (evt.getState()) {
//                case OPEN:
//                    if (Boolean.FALSE.equals(evt.getValue())) {
//                        listener.onChannelClosedOrDisconnected(ctx.getChannel());
//                    }
//                    break;
//                case CONNECTED:
//                    if (evt.getValue() == null) {
//                        listener.onChannelClosedOrDisconnected(ctx.getChannel());
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//        else if (e instanceof ExceptionEvent) {
//            listener.onExceptionEvent((ExceptionEvent) e);
//        }
//        ctx.sendUpstream(e);
//        // for all other stuff we drop it on the floor
//    }
//
//    private void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
//    {
//        if (e.getMessage() instanceof ByteBuf && listener != null) {
//            listener.onFrameRead(ctx.getChannel(), (ByteBuf) e.getMessage());
//        }
//        // drop it
//    }
//
//    @Override
//    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
//            throws Exception
//    {
//        ctx.sendDownstream(e);
//    }
//
    public ByteBuf getWriteBuffer()
    {
        if (writeBuffers.isEmpty()) {
            writeBuffers.add(Unpooled.buffer(DEFAULT_BUFFER_SIZE));
        }
        return writeBuffers.peek();
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
