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

import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.channel.socket.SocketChannelConfig;

import java.lang.reflect.Proxy;
import java.util.concurrent.ThreadFactory;

/*
 * Hooks for configuring various parts of Netty.
 */
public class NettyServerConfigBuilder extends NettyConfigBuilderBase<NettyServerConfigBuilder>
{
    private final SocketChannelConfig socketChannelConfig = (SocketChannelConfig) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class<?>[]{SocketChannelConfig.class},
            new Magic("child.")
    );
    private final ServerSocketChannelConfig serverSocketChannelConfig = (ServerSocketChannelConfig) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class<?>[]{ServerSocketChannelConfig.class},
            new Magic(""));

    @Inject
    public NettyServerConfigBuilder()
    {
        // Thrift turns TCP_NODELAY by default, and turning it off can have latency implications
        // so let's turn it on by default as well. It can still be switched off by explicitly
        // calling setTcpNodelay(false) after construction.
        getSocketChannelConfig().setTcpNoDelay(true);
    }

    public SocketChannelConfig getSocketChannelConfig()
    {
        return socketChannelConfig;
    }


    public ServerSocketChannelConfig getServerSocketChannelConfig()
    {
        return serverSocketChannelConfig;
    }

    public NettyServerConfig build()
    {
        //Timer timer = getTimer();
        EventLoopGroup bossGroup = getBossExecutor();
        int bossThreadCount = getBossThreadCount();
        EventLoopGroup workerGroup = getWorkerExecutor();
        int workerThreadCount = getWorkerThreadCount();

        return new NettyServerConfig(
                getBootstrapOptions(),
                bossGroup,
                bossThreadCount,
                workerGroup,
                workerThreadCount
        );
    }

    private String threadNamePattern(String suffix)
    {
        String niftyName = getNiftyName();
        return "nifty-server" + (Strings.isNullOrEmpty(niftyName) ? "" : "-" + niftyName) + suffix;
    }

    private ThreadFactory renamingThreadFactory(String nameFormat)
    {
        return new ThreadFactoryBuilder().setNameFormat(nameFormat).build();
    }
}
