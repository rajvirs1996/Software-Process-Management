package au.edu.uow.e_planner_and_communication_system;

import android.support.annotation.NonNull;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import au.edu.uow.e_planner_and_communication_system.Activity.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Tony on 19/3/2018.
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public IntentsTestRule<LoginActivity> mLoginActivityRule = new IntentsTestRule<>(LoginActivity.class);

    @Test
    public void test_failedLoginWithValidInput() throws InterruptedException {
        // Wait until all toast disappear
        Thread.sleep(2000);
        // Generate random user
        String username = "user" + randomDigits();
        String email = username + "@test.com";
        String password = username;

        // Go back to the sign in screen if we're logged in from a previous test
        logOutIfPossible();

        // Select email field
        ViewInteraction viewInteractionEmailText = onView(
                withId(R.id.emailText))
                .check(matches(isDisplayed()));
        viewInteractionEmailText.perform(click());
        // Enter email address
        viewInteractionEmailText.perform(replaceText(email));

        // Enter password
        ViewInteraction viewInteractionPasswordText = onView(
                withId(R.id.passwordText))
                .check(matches(isDisplayed()));
        viewInteractionPasswordText.perform(replaceText(password));

        // Click login in
        ViewInteraction viewInteractionLoginBtn = onView(
                withId(R.id.loginBtn))
                .check(matches(isDisplayed()));
        viewInteractionLoginBtn.perform(click());

        // It should show an error
        onView(withText("Authentication failed. \nUser does not exist!"))
                .inRoot(withDecorView(not(is(mLoginActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_failedLoginWithInvalidEmail() throws InterruptedException {
        // Wait until all toast disappear
        Thread.sleep(2000);
        // Generate random user
        String username = "user" + randomDigits();
        String email = username + "test.com";
        String password = username;

        // Go back to the sign in screen if we're logged in from a previous test
        logOutIfPossible();

        // Select email field
        ViewInteraction viewInteractionEmailText = onView(
                withId(R.id.emailText))
                .check(matches(isDisplayed()));
        viewInteractionEmailText.perform(click());
        // Enter email address
        viewInteractionEmailText.perform(replaceText(email));

        // Enter password
        ViewInteraction viewInteractionPasswordText = onView(
                withId(R.id.passwordText))
                .check(matches(isDisplayed()));
        viewInteractionPasswordText.perform(replaceText(password));

        // Click login in
        ViewInteraction viewInteractionLoginBtn = onView(
                withId(R.id.loginBtn))
                .check(matches(isDisplayed()));
        viewInteractionLoginBtn.perform(click());

        // It should should show an error
        viewInteractionEmailText.check(matches(hasErrorText("Wrong email!")));
    }

    @Test
    public void test_failedLoginWithWrongPassword() throws InterruptedException {
        // Wait until all toast disappear
        Thread.sleep(2000);
        // Generate random user
        String email = "uowfirst@uowmail.edu.au";
        String password = "123465789";

        // Go back to the sign in screen if we're logged in from a previous test
        logOutIfPossible();

        // Select email field
        ViewInteraction viewInteractionEmailText = onView(
                withId(R.id.emailText))
                .check(matches(isDisplayed()));
        viewInteractionEmailText.perform(click());
        // Enter email address
        viewInteractionEmailText.perform(replaceText(email));

        // Create password ViewInteraction
        ViewInteraction viewInteractionPasswordText = onView(
                withId(R.id.passwordText))
                .check(matches(isDisplayed()));
        viewInteractionPasswordText.perform(replaceText(password));

        // Click login in
        ViewInteraction viewInteractionLoginBtn = onView(
                withId(R.id.loginBtn))
                .check(matches(isDisplayed()));
        viewInteractionLoginBtn.perform(click());

        // Wait previous toast disappear
        //onView(isRoot()).perform(waitId(16908299, TimeUnit.SECONDS.toMillis(1)));

        // It should should show an error
        onView(withText("Authentication failed. \nThe password is invalid."))
                .inRoot(withDecorView(not(is(mLoginActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_failedLoginWithInvalidPassword() throws InterruptedException {
        // Wait until all toast disappear
        Thread.sleep(2000);
        // Generate random user
        String username = "user" + randomDigits();
        String email = username + "@test.com";
        String password = "";

        // Go back to the sign in screen if we're logged in from a previous test
        logOutIfPossible();

        // Select email field
        ViewInteraction viewInteractionEmailText = onView(
                withId(R.id.emailText))
                .check(matches(isDisplayed()));
        viewInteractionEmailText.perform(click());
        // Enter email address
        viewInteractionEmailText.perform(replaceText(email));

        // Create password ViewInteraction
        ViewInteraction viewInteractionPasswordText = onView(
                withId(R.id.passwordText))
                .check(matches(isDisplayed()));

        // Click login in
        ViewInteraction viewInteractionLoginBtn = onView(
                withId(R.id.loginBtn))
                .check(matches(isDisplayed()));
        viewInteractionLoginBtn.perform(click());

        // It should should show an error
        viewInteractionPasswordText.check(matches(hasErrorText("Wrong password")));
    }

    @Test
    public void test_successLogin() {
        // Generate random user
        String username = "uowfirst";
        String email = username + "@uowmail.edu.au";
        String password = username;

        // Go back to the sign in screen if we're logged in from a previous test
        logOutIfPossible();

        // Select email field
        ViewInteraction viewInteractionEmailText = onView(
                withId(R.id.emailText))
                .check(matches(isDisplayed()));
        viewInteractionEmailText.perform(click());
        // Enter email address
        viewInteractionEmailText.perform(replaceText(email));

        // Enter password
        ViewInteraction viewInteractionPasswordText = onView(
                withId(R.id.passwordText))
                .check(matches(isDisplayed()));
        viewInteractionPasswordText.perform(replaceText(password));

        // Click login in
        ViewInteraction viewInteractionLoginBtn = onView(
                withId(R.id.loginBtn))
                .check(matches(isDisplayed()));
        viewInteractionLoginBtn.perform(click());

        // It should enter main activity
        intended(toPackage("au.edu.uow.prototype1"));
    }

    private void logOutIfPossible() {
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
    }

    @NonNull
    private String randomDigits() {
        Random random = new Random();
        return String.valueOf(random.nextInt(99999999));
    }
}
