package com.example.weatherapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class foreCastScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Composable
    fun makezip(): MutableState<Int> {
        var zipCodeState = remember { mutableStateOf(55155) }
        return zipCodeState
    }


    @Composable
    fun makelat(): MutableState<Double> {
        var lat = remember { mutableStateOf(55155) }
        return lat
    }

    @Composable
    fun makelong(): MutableState<Double> {
        var long = remember { mutableStateOf(55155) }
        return long
    }

    // I am having trouble getting my test data set up but I have listed what I want to run below
    @Composable
    fun setup(): WeatherModel {
        var lat = makelat()
        var long = makelong()
        val viewModel = notificationModel(apiKey = "", lat, long, unit = "")
        return viewModel
    }

    @Test
    fun checkItemsDisplayOnScreen() {
        composeTestRule.setContent {
            val viewModel = setup()
            WeatherScreen(viewModel, ()->Unit)
        }
        composeTestRule.onNodeWithText("AppNameBox2").assertIsDisplayed()
        composeTestRule.onNodeWithText("AppName").assertIsDisplayed()
        composeTestRule.onNodeWithText("ForeCastFor").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date").assertIsDisplayed()
        composeTestRule.onNodeWithText("lowTemp2").assertIsDisplayed()
        composeTestRule.onNodeWithText("lowTempR2").assertIsDisplayed()
        composeTestRule.onNodeWithText("highTemp2").assertIsDisplayed()
        composeTestRule.onNodeWithText("highTempR2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hum2").assertIsDisplayed()
        composeTestRule.onNodeWithText("HumR2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Press2").assertIsDisplayed()
        composeTestRule.onNodeWithText("PressR2").assertIsDisplayed()

        composeTestRule.onNodeWithText("WeatherButton").assertIsDisplayed()
    }
}