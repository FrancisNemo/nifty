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

import com.google.inject.ProvidedBy;
import io.netty.channel.EventLoopGroup;

import java.util.Map;

@ProvidedBy(DefaultNettyServerConfigProvider.class)
public class NettyServerConfig
{
    private final Map<String, Object> bootstrapOptions;
    private final EventLoopGroup bossExecutor;
    private final int bossThreadCount;
    private final EventLoopGroup workerExecutor;
    private final int workerThreadCount;

    public NettyServerConfig(Map<String, Object> bootstrapOptions,
                             EventLoopGroup bossExecutor,
                             int bossThreadCount,
                             EventLoopGroup workerExecutor,
                             int workerThreadCount)
    {
        this.bootstrapOptions = bootstrapOptions;
        this.bossExecutor = bossExecutor;
        this.bossThreadCount = bossThreadCount;
        this.workerExecutor = workerExecutor;
        this.workerThreadCount = workerThreadCount;
    }

    public EventLoopGroup getBossExecutor()
    {
        return bossExecutor;
    }

    public Map<String, Object> getBootstrapOptions()
    {
        return bootstrapOptions;
    }

    public int getBossThreadCount()
    {
        return bossThreadCount;
    }

    public EventLoopGroup getWorkerExecutor()
    {
        return workerExecutor;
    }

    public int getWorkerThreadCount()
    {
        return workerThreadCount;
    }

    public static NettyServerConfigBuilder newBuilder()
    {
        return new NettyServerConfigBuilder();
    }
}
