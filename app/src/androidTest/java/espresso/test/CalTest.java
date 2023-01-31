package espresso.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import androidx.test.filters.SdkSuppress;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Basic sample for unbundled UiAutomator.
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class CalTest {

    private static final String BASIC_SAMPLE_PACKAGE = "com.example.new_sample";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String SUB_OPR_RESULT = "706";
    private static final String ADD_OPR_RESULT = "919";

    private UiDevice mDevice;

    @Before
    public void startTheCalculatorApp() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }


    @Test
    public void add_operation() {
        // Press buttons
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button7")).click();
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button9")).click();
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button5")).click();

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button_add")).click();

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button1")).click();
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button2")).click();
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button4")).click();

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button_equal")).click();

        //Verify result
        UiObject2 changedText = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "output")), 500 );
        assertThat(changedText.getText(), is(equalTo(ADD_OPR_RESULT)));
    }

    @Test
    public void sub_operation() {

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button9")).click();
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button1")).click();
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button2")).click();

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button_sub")).click();

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button2")).click();
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button0")).click();
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button6")).click();

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "button_equal")).click();

        UiObject2 changedText = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE,
                "output")), 500 );
        assertThat(changedText.getText(), is(equalTo(SUB_OPR_RESULT)));

    }

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
