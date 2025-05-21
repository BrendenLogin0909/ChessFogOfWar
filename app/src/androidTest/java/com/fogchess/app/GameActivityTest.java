package com.fogchess.app;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class GameActivityTest {

    @Rule
    public ActivityTestRule<GameActivity> activityRule = new ActivityTestRule<>(GameActivity.class);

    @Test
    public void testBoardIsDisplayed() {
        // Verify the chess board is displayed
        onView(withId(R.id.chessBoard)).check(matches(isDisplayed()));
    }

    @Test
    public void testNextTurnButtonIsDisplayed() {
        // Verify the next turn button is displayed
        onView(withId(R.id.nextTurnButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testBlackoutScreenAppears() {
        // Click the next turn button
        onView(withId(R.id.nextTurnButton)).perform(click());
        
        // Verify the blackout screen appears
        onView(withId(R.id.blackoutOverlay)).check(matches(isDisplayed()));
    }
}
