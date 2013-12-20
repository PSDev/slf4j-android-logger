package de.psdev.slf4j.android.logger;

import android.util.Log;

public enum LogLevel {
    TRACE(Log.VERBOSE),
    DEBUG(Log.DEBUG),
    INFO(Log.INFO),
    WARN(Log.WARN),
    ERROR(Log.ERROR);

    private final int mAndroidLogLevel;

    LogLevel(final int androidLogLevel) {
        mAndroidLogLevel = androidLogLevel;
    }

    int getAndroidLogLevel() {
        return mAndroidLogLevel;
    }
}
