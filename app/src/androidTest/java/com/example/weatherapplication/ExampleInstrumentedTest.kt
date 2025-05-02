package com.example.weatherapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.weatherapplication", appContext.packageName)
    }

    fun testZipCodeButton(){
        val composeTestRule=createComposeRule()
        val newZipCode="abc"
        val newZipCodeInt:Int
        composeTestRule.onNodeWithText("zip field").assertIsDisplayed()
        composeTestRule.onNodeWithText("zip button").assertIsDisplayed()

       // assertTrue(newZipCode)
    }

    @Test
    fun icontest() {

    }
}