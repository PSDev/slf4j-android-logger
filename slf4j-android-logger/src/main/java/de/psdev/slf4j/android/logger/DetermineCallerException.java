package de.psdev.slf4j.android.logger;

public class DetermineCallerException extends RuntimeException {

    public DetermineCallerException() {
    }

    public DetermineCallerException(final String message) {
        super(message);
    }

    public DetermineCallerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DetermineCallerException(final Throwable cause) {
        super(cause);
    }

    public DetermineCallerException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
