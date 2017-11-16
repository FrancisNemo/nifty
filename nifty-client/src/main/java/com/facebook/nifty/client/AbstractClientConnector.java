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

import com.facebook.nifty.duplex.TDuplexProtocolFactory;
import com.google.common.net.HostAndPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.apache.thrift.protocol.TBinaryProtocol;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public abstract class AbstractClientConnector<T extends NiftyClientChannel>
        implements NiftyClientConnector<T>
{
    protected final SocketAddress address;
    private final TDuplexProtocolFactory protocolFactory;
    private volatile SocketAddress resolvedAddress;

    public AbstractClientConnector(SocketAddress address, TDuplexProtocolFactory protocolFactory)
    {
        this.address = address;
        this.protocolFactory = protocolFactory;
    }

    @Override
    public ChannelFuture connect(Bootstrap bootstrap)
    {
//        if (bootstrap instanceof Socks4ClientBootstrap) {
//            return bootstrap.connect(address);
//        }

        if (resolvedAddress == null) {
            resolvedAddress = resolvedAddress((InetSocketAddress) address);
        }
        return bootstrap.connect(resolvedAddress);
    }

    @Override
    public String toString()
    {
        return address.toString();
    }

    protected TDuplexProtocolFactory getProtocolFactory()
    {
        return protocolFactory;
    }

    protected static SocketAddress toSocketAddress(HostAndPort address)
    {
        return InetSocketAddress.createUnresolved(address.getHostText(), address.getPort());
    }

    protected static TDuplexProtocolFactory defaultProtocolFactory()
    {
        return TDuplexProtocolFactory.fromSingleFactory(new TBinaryProtocol.Factory());
    }

    private static InetSocketAddress resolvedAddress(InetSocketAddress address)
    {
        if (!address.isUnresolved()) {
            return address;
        }
        return new InetSocketAddress(address.getHostString(), address.getPort());
    }
}
