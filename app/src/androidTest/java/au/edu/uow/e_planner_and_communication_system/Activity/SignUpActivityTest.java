package au.edu.uow.e_planner_and_communication_system.Activity;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import au.edu.uow.e_planner_and_communication_system.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Tony on 21/3/2018.
 */
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> mSignUpActivityRules = new ActivityTestRule<>(SignUpActivity.class);

    //ViewInteraction viewInteractionBackBtn = onView(withId(R.id.back)).check(matches(isDisplayed()));

    @Test
    public void test_emptyName() {
        ViewInteraction viewInteractionNameText = onView(withId(R.id.nameSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSignUpBtn = onView(withId(R.id.signUp)).check(matches(isDisplayed()));

        viewInteractionNameText.perform(replaceText(""));
        viewInteractionSignUpBtn.perform(click());
        viewInteractionNameText.check(matches(hasErrorText("Please enter your name!")));
    }

    @Test
    public void test_emptyEmail() {
        ViewInteraction viewInteractionNameText = onView(withId(R.id.nameSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionEmailText = onView(withId(R.id.emailSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSignUpBtn = onView(withId(R.id.signUp)).check(matches(isDisplayed()));

        String name = "test" + randomDigit();

        viewInteractionNameText.perform(replaceText(name));
        viewInteractionEmailText.perform(replaceText(""));
        viewInteractionSignUpBtn.perform(click());
        viewInteractionEmailText.check(matches(hasErrorText("Please enter your email!")));
    }

    @Test
    public void test_invalidEmail() {
        ViewInteraction viewInteractionNameText = onView(withId(R.id.nameSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionEmailText = onView(withId(R.id.emailSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSignUpBtn = onView(withId(R.id.signUp)).check(matches(isDisplayed()));

        String name = "test" + randomDigit();

        viewInteractionNameText.perform(replaceText(name));
        viewInteractionEmailText.perform(replaceText(name));
        viewInteractionSignUpBtn.perform(click());
        viewInteractionEmailText.check(matches(hasErrorText("Please enter your email!")));
    }

    @Test
    public void test_emptySID() {
        ViewInteraction viewInteractionNameText = onView(withId(R.id.nameSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionEmailText = onView(withId(R.id.emailSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSIDText = onView(withId(R.id.studentIDSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSignUpBtn = onView(withId(R.id.signUp)).check(matches(isDisplayed()));

        String name = "test" + randomDigit();
        String email = name + "@test.com";

        viewInteractionNameText.perform(replaceText(name));
        viewInteractionEmailText.perform(replaceText(email));
        viewInteractionSIDText.perform(replaceText(""));
        viewInteractionSignUpBtn.perform(click());
        viewInteractionSIDText.check(matches(hasErrorText("Please enter your student ID!")));
    }

    @Test
    public void test_emptyPassword() {
        ViewInteraction viewInteractionNameText = onView(withId(R.id.nameSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionEmailText = onView(withId(R.id.emailSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSIDText = onView(withId(R.id.studentIDSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionPasswordText = onView(withId(R.id.passwordSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSignUpBtn = onView(withId(R.id.signUp)).check(matches(isDisplayed()));

        String name = "test" + randomDigit();
        String email = name + "@test.com";

        viewInteractionNameText.perform(replaceText(name));
        viewInteractionEmailText.perform(replaceText(email));
        viewInteractionSIDText.perform(replaceText(name));
        viewInteractionPasswordText.perform(replaceText(""));
        viewInteractionSignUpBtn.perform(click());
        viewInteractionPasswordText.check(matches(hasErrorText("Please enter the password!")));
    }

    @Test
    public void test_emptyConfPassword() {
        ViewInteraction viewInteractionNameText = onView(withId(R.id.nameSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionEmailText = onView(withId(R.id.emailSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSIDText = onView(withId(R.id.studentIDSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionPasswordText = onView(withId(R.id.passwordSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionConfPasswordText = onView(withId(R.id.confPasswordText)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSignUpBtn = onView(withId(R.id.signUp)).check(matches(isDisplayed()));

        String name = "test" + randomDigit();
        String email = name + "@test.com";
        String password = name;

        viewInteractionNameText.perform(replaceText(name));
        viewInteractionEmailText.perform(replaceText(email));
        viewInteractionSIDText.perform(replaceText(name));
        viewInteractionPasswordText.perform(replaceText(password));
        viewInteractionConfPasswordText.perform(replaceText(""));
        viewInteractionSignUpBtn.perform(click());
        viewInteractionConfPasswordText.check(matches(hasErrorText("Please enter the password again!")));
    }

    @Test
    public void test_diffConfPassword() {
        ViewInteraction viewInteractionNameText = onView(withId(R.id.nameSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionEmailText = onView(withId(R.id.emailSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSIDText = onView(withId(R.id.studentIDSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionPasswordText = onView(withId(R.id.passwordSignUp)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionConfPasswordText = onView(withId(R.id.confPasswordText)).check(matches(isDisplayed()));
        ViewInteraction viewInteractionSignUpBtn = onView(withId(R.id.signUp)).check(matches(isDisplayed()));

        String name = "test" + randomDigit();
        String email = name + "@test.com";
        String password = name;

        viewInteractionNameText.perform(replaceText(name));
        viewInteractionEmailText.perform(replaceText(email));
        viewInteractionSIDText.perform(replaceText(name));
        viewInteractionPasswordText.perform(replaceText(randomDigit()));
        viewInteractionConfPasswordText.perform(replaceText(randomDigit()));
        viewInteractionSignUpBtn.perform(click());
        viewInteractionConfPasswordText.check(matches(hasErrorText("Password does not match the confirm password")));
    }

    @Test
    public void test_successSignUp(){

    }

    private String randomDigit() {
        Random random = new Random();
        return String.valueOf(random.nextInt(99999));
    }
}