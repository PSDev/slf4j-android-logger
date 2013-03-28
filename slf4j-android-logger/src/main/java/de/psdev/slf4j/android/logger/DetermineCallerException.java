package de.psdev.slf4j.android.logger;

public class DetermineCallerException extends RuntimeException {
    private static final long serialVersionUID = -6987929757213786107L;

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
}
