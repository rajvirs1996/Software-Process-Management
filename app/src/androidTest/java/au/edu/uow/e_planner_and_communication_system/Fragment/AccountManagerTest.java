package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.core.AllOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import au.edu.uow.e_planner_and_communication_system.Activity.LoginActivity;
import au.edu.uow.e_planner_and_communication_system.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Tony on 26/3/2018.
 */

@RunWith(AndroidJUnit4.class)
public class AccountManagerTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void test_AccountManager() {
        logOutAndInIfPossible();

        // Open navigation menu
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.personal_manager));
        // onView(withText(R.id.personal_manager)).perform();
        onView(withId(R.id.account_management_image)).check(matches(isDisplayed()));
        onView(withId(R.id.accountmanager_image_button)).check(matches(isDisplayed()));
        onView(withId(R.id.account_managment_password_button)).check(matches(isDisplayed()));
        onView(withId(R.id.account_managment_button__status)).check(matches(isDisplayed()));
    }

    private void logOutAndInIfPossible() {
        try {
            // Open Navigation drawer
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            // Scroll to item (prevent "at least 90 percent of the view's area is displayed to the user.")
            onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.personal_logout));
            onView(withId(R.id.personal_logout)).perform(click());
        } catch (NoMatchingViewException e) {
            // Ignore exception since we only want to do this operation if it's easy.
            //Log.e("test", e.getMessage());
        }

        try {
            onView(withId(R.id.emailText)).perform(replaceText("test123@test.com"));
            onView(withId(R.id.passwordText)).perform(replaceText("test123"));
            onView(withId(R.id.loginBtn)).perform(click());
        } catch (NoMatchingViewException e) {
            //
        }

    }
}
