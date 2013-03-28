/*
 * Copyright 2013 Philip Schiffer
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * This file incorporates code covered by the following terms:
 * Copyright (c) 2004-2013 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.psdev.slf4j.android.logger;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * AndroidLoggerFactory is an implementation of {@link ILoggerFactory} returning
 * the appropriately named {@link AndroidLoggerFactory} instance.
 *
 * @author Andrey Korzhevskiy <a.korzhevskiy@gmail.com>
 * @author Philip Schiffer <philip.schiffer@gmail.com>
 */
public class AndroidLoggerFactory implements ILoggerFactory {
    private final ConcurrentMap<String, Logger> loggerMap;

    public AndroidLoggerFactory() {
        loggerMap = new ConcurrentHashMap<String, Logger>();
    }

    /**
     * Return an appropriate {@link AndroidLoggerAdapter} instance by name.
     */
    @Override
    public Logger getLogger(final String name) {
        synchronized (loggerMap) {
            if (!loggerMap.containsKey(name)) {
                loggerMap.put(name, new AndroidLoggerAdapter(name));
            }
            return loggerMap.get(name);
        }
    }
}
