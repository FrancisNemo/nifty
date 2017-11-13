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

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Set;

/**
 * A lifecycle object that manages starting up and shutting down multiple core channels.
 */
public class NiftyBootstrap
{
    private final NettyServerConfig nettyServerConfig;
    private final Map<ThriftServerDef, NettyServerTransport> transports;
    /**
     * This takes a Set of ThriftServerDef. Use Guice Multibinder to inject.
     */
    @Inject
    public NiftyBootstrap(
            Set<ThriftServerDef> thriftServerDefs,
            NettyServerConfig nettyServerConfig)
    {
        ImmutableMap.Builder<ThriftServerDef, NettyServerTransport> builder = new ImmutableMap.Builder<>();
        this.nettyServerConfig = nettyServerConfig;
        for (ThriftServerDef thriftServerDef : thriftServerDefs) {
            builder.put(thriftServerDef, new NettyServerTransport(thriftServerDef, nettyServerConfig));
        }
        transports = builder.build();
    }

    @PostConstruct
    public void start()
    {
        for (NettyServerTransport transport : transports.values()) {
            transport.start();
        }
    }

    @PreDestroy
    public void stop()
    {
        for (NettyServerTransport transport : transports.values()) {
            try {
                transport.stop();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Map<ThriftServerDef, Integer> getBoundPorts()
    {
        ImmutableMap.Builder<ThriftServerDef, Integer> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<ThriftServerDef, NettyServerTransport> entry : transports.entrySet()) {
            builder.put(entry.getKey(), entry.getValue().getPort());
        }
        return builder.build();
    }

    public Map<ThriftServerDef, NiftyMetrics> getNiftyMetrics()
    {
        ImmutableMap.Builder<ThriftServerDef, NiftyMetrics> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<ThriftServerDef, NettyServerTransport> entry : transports.entrySet()) {
            builder.put(entry.getKey(), entry.getValue().getMetrics());
        }
        return builder.build();
    }
}
