package au.edu.uow.e_planner_and_communication_system;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import au.edu.uow.e_planner_and_communication_system.Activity.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by Tony on 19/3/2018.
 */

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void test_failedLoginWithValidInput() {
        // Generate random user
        String username = "user" + randomDigits();
        String email = username + "@test.com";
        String password = "12345678";

        // Go back to the sign in screen if we're logged in from a previous test
        logOutIfPossible();

        // Select email field
        ViewInteraction appCompatEditText = onView(
                withId(R.id.emailText))
                .check(matches(isDisplayed()));
        appCompatEditText.perform(click());

        // Enter email address
        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.emailText))
                .check(matches(isDisplayed()));
        appCompatEditText2.perform(replaceText(email));

        // Enter password
        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.passwordText))
                .check(matches(isDisplayed()));
        appCompatEditText3.perform(replaceText(password));

        // Click login in
        ViewInteraction appCompatButton = onView(
                withId(R.id.loginBtn))
                .check(matches(isDisplayed()));
        appCompatButton.perform(click());
    }

    private void logOutIfPossible() {
        try {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
            onView(withText(R.string.logout)).perform(click());
        } catch (NoMatchingViewException e) {
            // Ignore exception since we only want to do this operation if it's easy.
        }
    }

    private String randomDigits() {
        Random random = new Random();
        return String.valueOf(random.nextInt(99999999));
    }
}
