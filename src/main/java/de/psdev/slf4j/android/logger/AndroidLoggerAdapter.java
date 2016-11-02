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
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * <p>A simple implementation that delegates all log requests to the Google Android
 * logging facilities. Note that this logger does not support {@link Marker}.
 * Methods taking marker data as parameter simply invoke the eponymous method
 * without the Marker argument, discarding any marker data in the process.</p>
 * <p/>
 * <p>The logging levels specified for SLF4J can be almost directly mapped to
 * the levels that exist in the Google Android platform. The following table
 * shows the mapping implemented by this logger.</p>
 * <p/>
 * <table border="1">
 * <tr><th><b>SLF4J<b></th><th><b>Android</b></th></tr>
 * <tr><td>TRACE</td><td>{@link Log#VERBOSE}</td></tr>
 * <tr><td>DEBUG</td><td>{@link Log#DEBUG}</td></tr>
 * <tr><td>INFO</td><td>{@link Log#INFO}</td></tr>
 * <tr><td>WARN</td><td>{@link Log#WARN}</td></tr>
 * <tr><td>ERROR</td><td>{@link Log#ERROR}</td></tr>
 * </table>
 * <p/>
 * <p>Use loggers as usual:
 * <ul>
 * <li>
 * Declare a logger<br/>
 * <code>private static final Logger logger = LoggerFactory.getLogger(MyClass.class);</code>
 * </li>
 * <li>
 * Invoke logging methods, e.g.,<br/>
 * <code>logger.debug("Some log message. Details: {}", someObject);</code><br/>
 * <code>logger.debug("Some log message with varargs. Details: {}, {}, {}", someObject1, someObject2, someObject3);</code>
 * </li>
 * </ul>
 * </p>
 * <p/>
 * <p>Logger instances created using the LoggerFactory are named according to the fully qualified
 * class name of the class given as a parameter.
 *
 * @author Andrey Korzhevskiy <a.korzhevskiy@gmail.com>
 * @author Philip Schiffer <philip.schiffer@gmail.com
 */
public class AndroidLoggerAdapter extends MarkerIgnoringBase {
    private static final long serialVersionUID = -1227274521521287937L;

    private static final String NO_MESSAGE = "";
    private static final StackTraceElement NOT_FOUND = new StackTraceElement(NO_MESSAGE, NO_MESSAGE, NO_MESSAGE, 0);

    // Properties
    private static final String CONFIGURATION_FILE = "logger.properties";
    private static final Properties ANDROID_LOGGER_PROPERTIES = new Properties();

    // Current log level and tag
    private static LogLevel sLogLevel = LogLevel.INFO;
    private static String sLogTag = "Slf4jAndroidLogger";

    /**
     * All system properties used by {@code AndroidLogger} start with this prefix
     */
    public static final String SYSTEM_PREFIX = "de.psdev.slf4j.android.logger.";
    public static final String DEFAULT_LOG_LEVEL_KEY = SYSTEM_PREFIX + "defaultLogLevel";
    public static final String LOG_TAG_KEY = SYSTEM_PREFIX + "logTag";

    /**
     * Initialize properties read from properties file
     */
    static {
        InputStream propertiesInputStream = null;
        final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        if (threadClassLoader != null) {
            propertiesInputStream = threadClassLoader.getResourceAsStream(CONFIGURATION_FILE);
        } else {
            propertiesInputStream = ClassLoader.getSystemResourceAsStream(CONFIGURATION_FILE);
        }
        if (propertiesInputStream != null) {
            try {
                ANDROID_LOGGER_PROPERTIES.load(propertiesInputStream);
                propertiesInputStream.close();
            } catch (IOException ignored) {
                // ignored
            }
        }

        // Init properties
        final String defaultLogLevelString = getStringProperty(DEFAULT_LOG_LEVEL_KEY, null);
        if (defaultLogLevelString != null) {
            setLogLevel(stringToLevel(defaultLogLevelString));
        }
        setLogTag(getStringProperty(LOG_TAG_KEY, "Slf4jAndroidLogger"));
    }

    private final Pattern mClassNamePattern;

    /**
     * Package access allows only {@link AndroidLoggerFactory} to instantiate
     * SimpleLogger instances.
     */
    AndroidLoggerAdapter(final String tag) {
        name = tag;
        mClassNamePattern = Pattern.compile(name + "(\\$+.*)?");
    }

    public static LogLevel getLogLevel() {
        return sLogLevel;
    }

    public static void setLogLevel(final LogLevel logLevel) {
        sLogLevel = logLevel;
    }

    public static String getLogTag() {
        return sLogTag;
    }

    public static void setLogTag(final String logTag) {
        sLogTag = logTag;
    }

    /**
     * Is this logger instance enabled for the VERBOSE level?
     *
     * @return True if this Logger is enabled for level VERBOSE, false otherwise.
     */
    @Override
    public boolean isTraceEnabled() {
        return isLevelEnabled(LogLevel.TRACE);
    }

    /**
     * Log a message object at level VERBOSE.
     *
     * @param msg - the message object to be logged
     */
    @Override
    public void trace(final String msg) {
        log(LogLevel.TRACE, msg, null);
    }

    /**
     * Log a message at level VERBOSE according to the specified format and
     * argument.
     * <p/>
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for level VERBOSE.
     * </p>
     *
     * @param format the format string
     * @param arg    the argument
     */
    @Override
    public void trace(final String format, final Object arg) {
        formatAndLog(LogLevel.TRACE, format, arg);
    }

    /**
     * Log a message at level VERBOSE according to the specified format and
     * arguments.
     * <p/>
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the VERBOSE level.
     * </p>
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    @Override
    public void trace(final String format, final Object arg1, final Object arg2) {
        formatAndLog(LogLevel.TRACE, format, arg1, arg2);
    }

    /**
     * Log a message at level VERBOSE according to the specified format and
     * arguments.
     * <p/>
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the VERBOSE level.
     * </p>
     *
     * @param format   the format string
     * @param argArray an array of arguments
     */
    @Override
    public void trace(final String format, final Object... argArray) {
        formatAndLog(LogLevel.TRACE, format, argArray);
    }

    /**
     * Log an exception (throwable) at level VERBOSE with an accompanying message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    @Override
    public void trace(final String msg, final Throwable t) {
        log(LogLevel.TRACE, msg, t);
    }

    /**
     * Is this logger instance enabled for the DEBUG level?
     *
     * @return True if this Logger is enabled for level DEBUG, false otherwise.
     */
    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(LogLevel.DEBUG);
    }

    /**
     * Log a message object at level DEBUG.
     *
     * @param msg - the message object to be logged
     */
    @Override
    public void debug(final String msg) {
        log(LogLevel.DEBUG, msg, null);
    }

    /**
     * Log a message at level DEBUG according to the specified format and argument.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for level DEBUG.
     * </p>
     *
     * @param format the format string
     * @param arg    the argument
     */
    @Override
    public void debug(final String format, final Object arg) {
        formatAndLog(LogLevel.DEBUG, format, arg);
    }

    /**
     * Log a message at level DEBUG according to the specified format and
     * arguments.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the DEBUG level.
     * </p>
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    @Override
    public void debug(final String format, final Object arg1, final Object arg2) {
        formatAndLog(LogLevel.DEBUG, format, arg1, arg2);
    }

    /**
     * Log a message at level DEBUG according to the specified format and
     * arguments.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the DEBUG level.
     * </p>
     *
     * @param format   the format string
     * @param argArray an array of arguments
     */
    @Override
    public void debug(final String format, final Object... argArray) {
        formatAndLog(LogLevel.DEBUG, format, argArray);
    }

    /**
     * Log an exception (throwable) at level DEBUG with an accompanying message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    @Override
    public void debug(final String msg, final Throwable t) {
        log(LogLevel.DEBUG, msg, t);
    }

    /**
     * Is this logger instance enabled for the INFO level?
     *
     * @return True if this Logger is enabled for the INFO level, false otherwise.
     */
    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(LogLevel.INFO);
    }

    /**
     * Log a message object at the INFO level.
     *
     * @param msg - the message object to be logged
     */
    @Override
    public void info(final String msg) {
        log(LogLevel.INFO, msg, null);
    }

    /**
     * Log a message at level INFO according to the specified format and argument.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the INFO level.
     * </p>
     *
     * @param format the format string
     * @param arg    the argument
     */
    @Override
    public void info(final String format, final Object arg) {
        formatAndLog(LogLevel.INFO, format, arg);
    }

    /**
     * Log a message at the INFO level according to the specified format and
     * arguments.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the INFO level.
     * </p>
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    @Override
    public void info(final String format, final Object arg1, final Object arg2) {
        formatAndLog(LogLevel.INFO, format, arg1, arg2);
    }

    /**
     * Log a message at level INFO according to the specified format and
     * arguments.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the INFO level.
     * </p>
     *
     * @param format   the format string
     * @param argArray an array of arguments
     */
    @Override
    public void info(final String format, final Object... argArray) {
        formatAndLog(LogLevel.INFO, format, argArray);
    }

    /**
     * Log an exception (throwable) at the INFO level with an accompanying
     * message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    @Override
    public void info(final String msg, final Throwable t) {
        log(LogLevel.INFO, msg, t);
    }

    /**
     * Is this logger instance enabled for the WARN level?
     *
     * @return True if this Logger is enabled for the WARN level, false
     * otherwise.
     */
    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(LogLevel.WARN);
    }

    /**
     * Log a message object at the WARN level.
     *
     * @param msg - the message object to be logged
     */
    @Override
    public void warn(final String msg) {
        log(LogLevel.WARN, msg, null);
    }

    /**
     * Log a message at the WARN level according to the specified format and
     * argument.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the WARN level.
     * </p>
     *
     * @param format the format string
     * @param arg    the argument
     */
    @Override
    public void warn(final String format, final Object arg) {
        formatAndLog(LogLevel.WARN, format, arg);
    }

    /**
     * Log a message at the WARN level according to the specified format and
     * arguments.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the WARN level.
     * </p>
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    @Override
    public void warn(final String format, final Object arg1, final Object arg2) {
        formatAndLog(LogLevel.WARN, format, arg1, arg2);
    }

    /**
     * Log a message at level WARN according to the specified format and
     * arguments.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the WARN level.
     * </p>
     *
     * @param format   the format string
     * @param argArray an array of arguments
     */
    @Override
    public void warn(final String format, final Object... argArray) {
        formatAndLog(LogLevel.WARN, format, argArray);
    }

    /**
     * Log an exception (throwable) at the WARN level with an accompanying
     * message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    @Override
    public void warn(final String msg, final Throwable t) {
        log(LogLevel.WARN, msg, t);
    }

    /**
     * Is this logger instance enabled for level ERROR?
     *
     * @return True if this Logger is enabled for level ERROR, false otherwise.
     */
    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(LogLevel.ERROR);
    }

    /**
     * Log a message object at the ERROR level.
     *
     * @param msg - the message object to be logged
     */
    @Override
    public void error(final String msg) {
        log(LogLevel.ERROR, msg, null);
    }

    /**
     * Log a message at the ERROR level according to the specified format and
     * argument.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the ERROR level.
     * </p>
     *
     * @param format the format string
     * @param arg    the argument
     */
    @Override
    public void error(final String format, final Object arg) {
        formatAndLog(LogLevel.ERROR, format, arg);
    }

    /**
     * Log a message at the ERROR level according to the specified format and
     * arguments.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the ERROR level.
     * </p>
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    @Override
    public void error(final String format, final Object arg1, final Object arg2) {
        formatAndLog(LogLevel.ERROR, format, arg1, arg2);
    }

    /**
     * Log a message at level ERROR according to the specified format and
     * arguments.
     * <p>
     * This form avoids superfluous object creation when the logger is disabled
     * for the ERROR level.
     * </p>
     *
     * @param format   the format string
     * @param argArray an array of arguments
     */
    @Override
    public void error(final String format, final Object... argArray) {
        formatAndLog(LogLevel.ERROR, format, argArray);
    }

    /**
     * Log an exception (throwable) at the ERROR level with an accompanying
     * message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    @Override
    public void error(final String msg, final Throwable t) {
        log(LogLevel.ERROR, msg, t);
    }

    private void formatAndLog(final LogLevel logLevel, final String format, final Object... argArray) {
        if (isLevelEnabled(logLevel)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            log(logLevel, ft.getMessage(), ft.getThrowable());
        }
    }

    private void log(final LogLevel logLevel, final String message, final Throwable throwable) {
        if (isLevelEnabled(logLevel)) {
            switch (logLevel.getAndroidLogLevel()) {
                case Log.VERBOSE:
                    logAndroidVerbose(message, throwable);
                    break;
                case Log.DEBUG:
                    logAndroidDebug(message, throwable);
                    break;
                case Log.INFO:
                    logAndroidInfo(message, throwable);
                    break;
                case Log.WARN:
                    logAndroidWarn(message, throwable);
                    break;
                case Log.ERROR:
                    logAndroidError(message, throwable);
                    break;
                default:
                    // nop
                    break;
            }
        }
    }

    private void logAndroidVerbose(final String message, final Throwable throwable) {
        if (throwable != null) {
            Log.v(getLogTag(), enhanced(message), throwable);
        } else {
            Log.v(getLogTag(), enhanced(message));
        }
    }

    private void logAndroidDebug(final String message, final Throwable throwable) {
        if (throwable != null) {
            Log.d(getLogTag(), enhanced(message), throwable);
        } else {
            Log.d(getLogTag(), enhanced(message));
        }
    }

    private void logAndroidInfo(final String message, final Throwable throwable) {
        if (throwable != null) {
            Log.i(getLogTag(), enhanced(message), throwable);
        } else {
            Log.i(getLogTag(), enhanced(message));
        }
    }

    private void logAndroidWarn(final String message, final Throwable throwable) {
        if (throwable != null) {
            Log.w(getLogTag(), enhanced(message), throwable);
        } else {
            Log.w(getLogTag(), enhanced(message));
        }
    }

    private void logAndroidError(final String message, final Throwable throwable) {
        if (throwable != null) {
            Log.e(getLogTag(), enhanced(message), throwable);
        } else {
            Log.e(getLogTag(), enhanced(message));
        }
    }

    // Property getter

    private static String getStringProperty(final String propertyName) {
        String propertyValue = null;
        try {
            propertyValue = System.getProperty(propertyName);
        } catch (SecurityException ignored) {
        }
        return propertyValue == null ? ANDROID_LOGGER_PROPERTIES.getProperty(propertyName) : propertyValue;
    }

    private static String getStringProperty(final String propertyName, final String defaultValue) {
        final String prop = getStringProperty(propertyName);
        return prop == null ? defaultValue : prop;
    }

    private static boolean getBooleanProperty(final String propertyName, final boolean defaultValue) {
        final String prop = getStringProperty(propertyName);
        return prop == null ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    private String enhanced(final String message) {
        final StackTraceElement caller = determineCaller();
        final String classNameOnly = getClassNameOnly(caller.getClassName());
        final String methodName = caller.getMethodName();
        final int lineNumber = caller.getLineNumber();
        final Thread thread = Thread.currentThread();
        return String.format(Locale.ENGLISH, "%s [%s:%s:%s] %s", message, classNameOnly, methodName, lineNumber, thread);
    }

    private StackTraceElement determineCaller() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (final StackTraceElement element : stackTrace) {
            if (mClassNamePattern.matcher(element.getClassName()).matches()) {
                return element;
            }
        }
        return NOT_FOUND;
    }

    private static String getClassNameOnly(final String classNameWithPackage) {
        final int lastDotPos = classNameWithPackage.lastIndexOf('.');
        if (lastDotPos == -1) {
            return classNameWithPackage;
        }
        return classNameWithPackage.substring(lastDotPos + 1);
    }

    /**
     * Is the given log level currently enabled?
     *
     * @param logLevel is this level enabled?
     */
    protected static boolean isLevelEnabled(final LogLevel logLevel) {
        // log level are numerically ordered so can use simple numeric comparison
        return logLevel.getAndroidLogLevel() >= sLogLevel.getAndroidLogLevel();
    }

    private static LogLevel stringToLevel(final String levelStr) {
        if ("trace".equalsIgnoreCase(levelStr)) {
            return LogLevel.TRACE;
        }
        if ("verbose".equalsIgnoreCase(levelStr)) {
            return LogLevel.TRACE;
        }
        if ("debug".equalsIgnoreCase(levelStr)) {
            return LogLevel.DEBUG;
        }
        if ("info".equalsIgnoreCase(levelStr)) {
            return LogLevel.INFO;
        }
        if ("warn".equalsIgnoreCase(levelStr)) {
            return LogLevel.WARN;
        }
        if ("error".equalsIgnoreCase(levelStr)) {
            return LogLevel.ERROR;
        }
        // assume INFO by default
        return LogLevel.INFO;
    }
}
