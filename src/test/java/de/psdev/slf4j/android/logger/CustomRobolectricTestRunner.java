package de.psdev.slf4j.android.logger;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.runners.model.InitializationError;

import java.io.File;

/**
 * Custom test runner to tell Robolectric where to find the android files
 */
public class CustomRobolectricTestRunner extends RobolectricTestRunner {

    public CustomRobolectricTestRunner(final Class<?> testClass) throws InitializationError {
        super(testClass, new File("src/test/android"));
    }
}
