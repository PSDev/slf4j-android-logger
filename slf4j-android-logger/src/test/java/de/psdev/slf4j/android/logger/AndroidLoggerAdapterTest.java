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
 */

package de.psdev.slf4j.android.logger;

import android.util.Log;
import org.junit.Assert;
import org.junit.Test;

public class AndroidLoggerAdapterTest {

    @Test
    public void testInitialization() throws Exception {
        final AndroidLoggerAdapter test = new AndroidLoggerAdapter("TEST");
        Assert.assertEquals("TestLogTag", AndroidLoggerAdapter.sLogTagString);
        Assert.assertEquals("TEST", test.getName());
        Assert.assertEquals(Log.VERBOSE, AndroidLoggerAdapter.sDefaultLogLevel);
    }
}
