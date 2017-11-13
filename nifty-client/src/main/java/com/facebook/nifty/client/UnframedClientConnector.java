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

import com.facebook.nifty.core.ThriftUnframedDecoder;
import com.facebook.nifty.duplex.TDuplexProtocolFactory;
import com.google.common.net.HostAndPort;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;


import java.net.InetSocketAddress;
import java.nio.channels.Channels;

public class UnframedClientConnector extends AbstractClientConnector<UnframedClientChannel> {
    public UnframedClientConnector(InetSocketAddress address)
    {
        this(address, defaultProtocolFactory());
    }

    public UnframedClientConnector(HostAndPort address)
    {
        this(address, defaultProtocolFactory());
    }

    public UnframedClientConnector(InetSocketAddress address, TDuplexProtocolFactory protocolFactory)
    {
        super(address, protocolFactory);
    }

    public UnframedClientConnector(HostAndPort address, TDuplexProtocolFactory protocolFactory)
    {
        super(toSocketAddress(address), protocolFactory);
    }

    @Override
    public UnframedClientChannel newThriftClientChannel(Channel nettyChannel, NettyClientConfig clientConfig)
    {
        UnframedClientChannel channel = new UnframedClientChannel(nettyChannel, clientConfig.getTimer(), getProtocolFactory());
        ChannelPipeline cp = nettyChannel.pipeline();
        TimeoutHandler.addToPipeline(cp);
        cp.addLast("thriftHandler", channel);
        return channel;
    }

    @Override
    public ChannelPipelineFactory newChannelPipelineFactory(final int maxFrameSize, NettyClientConfig clientConfig)
    {
        return new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline()
                    throws Exception {
                ChannelPipeline cp = Channels.pipeline();
                TimeoutHandler.addToPipeline(cp);
                cp.addLast("thriftUnframedDecoder", new ThriftUnframedDecoder());
                if (clientConfig.sslClientConfiguration() != null) {
                    cp.addFirst("ssl", clientConfig.sslClientConfiguration().createHandler(address));
                }
                return cp;
            }
        };
    }
}
