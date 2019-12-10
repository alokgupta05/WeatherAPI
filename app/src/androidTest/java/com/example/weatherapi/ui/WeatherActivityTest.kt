package com.example.weatherapi.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.weatherapi.R
import org.hamcrest.CoreMatchers.containsString


@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class WeatherActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(WeatherActivity::class.java)


    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_COARSE_LOCATION"
        )

    @Test
    fun weatherLabelCityTest() {
        Thread.sleep(2000)
        onView(withId(R.id.labelCity))
            .check(matches(isDisplayed()))
        onView(withId(R.id.labelCity)).check(matches(withText(containsString("City"))))

    }

    @Test
    fun weatherTemperatureTest() {
        Thread.sleep(2000)
        onView(withId(R.id.labelTemperature))
            .check(matches(isDisplayed()))
        onView(withId(R.id.labelTemperature)).check(matches(withText(containsString("Temperature"))))

    }

    @Test
    fun weatherCloudsTest() {
        Thread.sleep(2000)
        onView(withId(R.id.labelCloud))
            .check(matches(isDisplayed()))
        onView(withId(R.id.labelCloud)).check(matches(withText(containsString("Cloud"))))

    }

}
