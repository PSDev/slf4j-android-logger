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
import com.xtremelabs.robolectric.Robolectric;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

@RunWith(CustomRobolectricTestRunner.class)
public class AndroidLoggerAdapterTest {

    private Logger mLogger;

    @Before
    public void setUp() throws Exception {
        Robolectric.bindShadowClass(EnhancedShadowLog.class);
        mLogger = LoggerFactory.getLogger(AndroidLoggerAdapterTest.class);
        EnhancedShadowLog.stream = System.out;
    }

    @Test
    public void testInitialization() throws Exception {
        assertEquals("should have read correct log tag from properties", "TestLogTag", AndroidLoggerAdapter.getLogTag());
        assertEquals("should have correct name", AndroidLoggerAdapterTest.class.getName(), mLogger.getName());
        assertEquals("should have correct log level", Log.VERBOSE, AndroidLoggerAdapter.getLogLevel());
    }

    @Test
    public void testIsTraceEnabled() throws Exception {
        assertTrue("trace should be enabled", mLogger.isTraceEnabled());
    }

    @Test
    public void testTrace() throws Exception {
        mLogger.trace("test trace");
        assertLog(Log.VERBOSE, "test trace");
    }

    @Test
    public void testTraceWithArg() throws Exception {
        mLogger.trace("test trace {}", "argument");
        assertLog(Log.VERBOSE, "test trace argument");
    }

    @Test
    public void testTraceWithTwoArgs() throws Exception {
        mLogger.trace("test trace {} {}", "argument", "argument2");
        assertLog(Log.VERBOSE, "test trace argument argument2");
    }

    @Test
    public void testTraceWithVarArgs() throws Exception {
        mLogger.trace("test trace {} {} {}", "argument", "argument2", "argument3");
        assertLog(Log.VERBOSE, "test trace argument argument2 argument3");
    }

    @Test
    public void testTraceWithArgAndThrowable() throws Exception {
        final Exception exception = new Exception("test trace exception");
        mLogger.trace("test trace {} {} {}", "argument", "argument2", "argument3", exception);
        assertLog(Log.VERBOSE, "test trace argument argument2 argument3", exception);
    }

    @Test
    public void testTraceWithThrowable() throws Exception {
        final Exception exception = new Exception("test trace exception");
        mLogger.trace("test trace", exception);
        assertLog(Log.VERBOSE, "test trace", exception);
    }

    @Test
    public void testIsDebugEnabled() throws Exception {
        assertTrue("debug should be enabled", mLogger.isDebugEnabled());
    }

    @Test
    public void testDebug() throws Exception {
        mLogger.debug("test debug");
        assertLog(Log.DEBUG, "test debug");
    }

    @Test
    public void testDebugWithArg() throws Exception {
        mLogger.debug("test debug {}", "argument");
        assertLog(Log.DEBUG, "test debug argument");
    }

    @Test
    public void testDebugWithTwoArgs() throws Exception {
        mLogger.debug("test debug {} {}", "argument", "argument2");
        assertLog(Log.DEBUG, "test debug argument argument2");
    }

    @Test
    public void testDebugWithVarArgs() throws Exception {
        mLogger.debug("test debug {} {} {}", "argument", "argument2", "argument3");
        assertLog(Log.DEBUG, "test debug argument argument2 argument3");
    }

    @Test
    public void testDebugWithArgAndThrowable() throws Exception {
        final Exception exception = new Exception("test debug exception");
        mLogger.debug("test debug {} {} {}", "argument", "argument2", "argument3", exception);
        assertLog(Log.DEBUG, "test debug argument argument2 argument3", exception);
    }

    @Test
    public void testDebugWithThrowable() throws Exception {
        final Exception exception = new Exception("test debug exception");
        mLogger.debug("test debug", exception);
        assertLog(Log.DEBUG, "test debug", exception);
    }

    @Test
    public void testIsInfoEnabled() throws Exception {
        assertTrue("info should be enabled", mLogger.isInfoEnabled());
    }

    @Test
    public void testInfo() throws Exception {
        mLogger.info("test info");
        assertLog(Log.INFO, "test info");
    }

    @Test
    public void testInfoWithArg() throws Exception {
        mLogger.info("test info {}", "argument");
        assertLog(Log.INFO, "test info argument");
    }

    @Test
    public void testInfoWithTwoArgs() throws Exception {
        mLogger.info("test info {} {}", "argument", "argument2");
        assertLog(Log.INFO, "test info argument argument2");
    }

    @Test
    public void testInfoWithVarArgs() throws Exception {
        mLogger.info("test info {} {} {}", "argument", "argument2", "argument3");
        assertLog(Log.INFO, "test info argument argument2 argument3");
    }

    @Test
    public void testInfoWithArgAndThrowable() throws Exception {
        final Exception exception = new Exception("test info exception");
        mLogger.info("test info {} {} {}", "argument", "argument2", "argument3", exception);
        assertLog(Log.INFO, "test info argument argument2 argument3", exception);
    }

    @Test
    public void testInfoWithThrowable() throws Exception {
        final Exception exception = new Exception("test info exception");
        mLogger.info("test info", exception);
        assertLog(Log.INFO, "test info", exception);
    }

    @Test
    public void testIsWarnEnabled() throws Exception {
        assertTrue("warn should be enabled", mLogger.isWarnEnabled());
    }

    @Test
    public void testWarn() throws Exception {
        mLogger.warn("test info");
        assertLog(Log.WARN, "test info");
    }

    @Test
    public void testWarnWithArg() throws Exception {
        mLogger.warn("test warn {}", "argument");
        assertLog(Log.WARN, "test warn argument");
    }

    @Test
    public void testWarnWithTwoArgs() throws Exception {
        mLogger.warn("test warn {} {}", "argument", "argument2");
        assertLog(Log.WARN, "test warn argument argument2");
    }

    @Test
    public void testWarnWithVarArgs() throws Exception {
        mLogger.warn("test warn {} {} {}", "argument", "argument2", "argument3");
        assertLog(Log.WARN, "test warn argument argument2 argument3");
    }

    @Test
    public void testWarnWithArgAndThrowable() throws Exception {
        final Exception exception = new Exception("test warn exception");
        mLogger.warn("test warn {} {} {}", "argument", "argument2", "argument3", exception);
        assertLog(Log.WARN, "test warn argument argument2 argument3", exception);
    }

    @Test
    public void testWarnWithThrowable() throws Exception {
        final Exception exception = new Exception("test warn exception");
        mLogger.warn("test warn", exception);
        assertLog(Log.WARN, "test warn", exception);
    }

    @Test
    public void testIsErrorEnabled() throws Exception {
        assertTrue("error should be enabled", mLogger.isErrorEnabled());
    }

    @Test
    public void testError() throws Exception {
        mLogger.error("test error");
        assertLog(Log.ERROR, "test error");
    }

    @Test
    public void testErrorWithArg() throws Exception {
        mLogger.error("test error {}", "argument");
        assertLog(Log.ERROR, "test error argument");
    }

    @Test
    public void testErrorWithTwoArgs() throws Exception {
        mLogger.error("test error {} {}", "argument", "argument2");
        assertLog(Log.ERROR, "test error argument argument2");
    }

    @Test
    public void testErrorWithVarArgs() throws Exception {
        mLogger.error("test error {} {} {}", "argument", "argument2", "argument3");
        assertLog(Log.ERROR, "test error argument argument2 argument3");
    }

    @Test
    public void testErrorWithArgAndThrowable() throws Exception {
        final Exception exception = new Exception("test error exception");
        mLogger.error("test error {} {} {}", "argument", "argument2", "argument3", exception);
        assertLog(Log.ERROR, "test error argument argument2 argument3", exception);
    }

    @Test
    public void testErrorWithThrowable() throws Exception {
        final Exception exception = new Exception("test error exception");
        mLogger.error("test error", exception);
        assertLog(Log.ERROR, "test error", exception);
    }

    @Test
    public void testInnerclassMatching() throws Exception {
        final InnerClassTest innerClassTest = new InnerClassTest();
        innerClassTest.doSomething();
        assertLog(Log.INFO, "inner class match");
        assertThat("should contain correct class name", EnhancedShadowLog.getLogs().get(0).msg, containsString("InnerClassTest"));
    }

    @After
    public void tearDown() throws Exception {
        EnhancedShadowLog.reset();
    }

    // Helper

    private static void assertLog(final int expectedLogLevel, final String expectedContainedText) {
        assertLog(expectedLogLevel, expectedContainedText, null);
    }

    private static void assertLog(final int expectedLogLevel, final String expectedContainedText, final Throwable expectedThrowable) {
        assertEquals("should have logged 1 message", 1L, EnhancedShadowLog.getLogs().size());
        final EnhancedShadowLog.LogItem logItem = EnhancedShadowLog.getLogs().get(0);
        assertEquals("should have correct type", expectedLogLevel, logItem.type);
        assertThat("should contain message", logItem.msg, containsString(expectedContainedText));
        assertThat("should contain class", logItem.msg, containsString(AndroidLoggerAdapterTest.class.getSimpleName()));
        assertEquals("should have correct log tag", "TestLogTag", logItem.tag);
        if (expectedThrowable != null) {
            assertEquals("should have logged the correct throwable", expectedThrowable, logItem.throwable);
        }
    }

    class InnerClassTest {
        public void doSomething() {
            mLogger.info("inner class match");
        }
    }
}
