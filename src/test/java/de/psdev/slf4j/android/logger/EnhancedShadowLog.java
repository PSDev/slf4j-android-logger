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

import android.util.Log;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Implements(Log.class)
public class EnhancedShadowLog {

    private static Map<String,List<LogItem>> logsByTag = new HashMap<String,List<LogItem>>();
    private static List<LogItem> logs = new ArrayList<LogItem>();
    public static PrintStream stream;

    @Implementation
    public static void e(final String tag, final String msg) {
        e(tag, msg, null);
    }

    @Implementation
    public static void e(final String tag, final String msg, final Throwable throwable) {
        addLog(Log.ERROR, tag, msg, throwable);
    }

    @Implementation
    public static void d(final String tag, final String msg) {
        d(tag, msg, null);
    }

    @Implementation
    public static void d(final String tag, final String msg, final Throwable throwable) {
        addLog(Log.DEBUG, tag, msg, throwable);
    }

    @Implementation
    public static void i(final String tag, final String msg) {
        i(tag, msg, null);
    }

    @Implementation
    public static void i(final String tag, final String msg, final Throwable throwable) {
        addLog(Log.INFO, tag, msg, throwable);
    }

    @Implementation
    public static void v(final String tag, final String msg) {
        v(tag, msg, null);
    }

    @Implementation
    public static void v(final String tag, final String msg, final Throwable throwable) {
        addLog(Log.VERBOSE, tag, msg, throwable);
    }

    @Implementation
    public static void w(final String tag, final String msg) {
        w(tag, msg, null);
    }

    @Implementation
    public static void w(final String tag, final Throwable throwable) {
        w(tag, null, throwable);
    }


    @Implementation
    public static void w(final String tag, final String msg, final Throwable throwable) {
        addLog(Log.WARN, tag, msg, throwable);
    }

    @Implementation
    public static void wtf(final String tag, final String msg) {
        wtf(tag, msg, null);
    }

    @Implementation
    public static void wtf(final String tag, final String msg, final Throwable throwable) {
        addLog(Log.ASSERT, tag, msg, throwable);
    }

    @Implementation
    public static int println(final int priority, final String tag, final String msg) {
        addLog(priority, tag, msg, null);
        return msg.length();
    }

    @Implementation
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    @Implementation
    public static boolean isLoggable(final String tag, final int level) {
        return stream != null || level >= Log.VERBOSE;
    }

    private static void addLog(final int level, final String tag, final String msg, final Throwable throwable) {
        if (stream != null) {
            logToStream(stream, level, tag, msg, throwable);
        }

        final LogItem item = new LogItem(level, tag, msg, throwable);
        List<LogItem> itemList = null;

        if (!logsByTag.containsKey(tag)) {
            itemList = new ArrayList<LogItem>();
            logsByTag.put(tag, itemList);
        } else {
            itemList = logsByTag.get(tag);
        }

        itemList.add(item);
        logs.add(item);
    }

    private static void logToStream(final PrintStream ps, final int level, final String tag, final String msg, final Throwable throwable) {
        final char c;
        switch (level) {
            case Log.ASSERT: c = 'A'; break;
            case Log.DEBUG:  c = 'D'; break;
            case Log.ERROR:  c = 'E'; break;
            case Log.WARN:   c = 'W'; break;
            case Log.INFO:   c = 'I'; break;
            case Log.VERBOSE:c = 'V'; break;
            default:         c = '?';
        }
        ps.println(c + "/" + tag + ": " + msg);
        if (throwable != null) {
            throwable.printStackTrace(ps);
        }
    }

    /**
     * Non-Android accessor.  Returns ordered list of all log entries.
     * @return
     */
    public static List<LogItem> getLogs() {
        return logs;
    }

    /**
     * Non-Android accessor.  Returns ordered list of all log items for a specific tag.
     *
     * @param tag
     * @return
     */
    public static List<LogItem> getLogsForTag( final String tag ) {
        return logsByTag.get(tag);
    }

    public static void reset() {
        logs.clear();
        logsByTag.clear();
    }

    public static class LogItem {
        public final int type;
        public final String tag;
        public final String msg;
        public final Throwable throwable;

        public LogItem(final int type, final String tag, final String msg, final Throwable throwable) {
            this.type = type;
            this.tag = tag;
            this.msg = msg;
            this.throwable = throwable;
        }
    }

}
