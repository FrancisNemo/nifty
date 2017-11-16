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


import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import javax.annotation.PreDestroy;

import java.io.Closeable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class NiftyTimer
    implements Closeable, Timer
{
//    public NiftyTimer(String prefix, long tickDuration, TimeUnit unit, int ticksPerWheel)
//    {
//        super(new ThreadFactoryBuilder().setNameFormat(prefix + "-timer-%s").setDaemon(true).build(),
//              ThreadNameDeterminer.CURRENT,
//              tickDuration,
//              unit,
//              ticksPerWheel);
//    }
//
//    public NiftyTimer(String prefix)
//    {
//        this(prefix, 100, TimeUnit.MILLISECONDS, 512);
//    }

    @PreDestroy
    @Override
    public void close()
    {
        //TODO
        // stop();
    }

    @Override
    public Timeout newTimeout(TimerTask timerTask, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public Set<Timeout> stop() {
        return null;
    }
}
