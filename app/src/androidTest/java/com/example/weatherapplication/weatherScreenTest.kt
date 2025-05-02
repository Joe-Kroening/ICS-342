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

class weatherScreenTest {

    @get:Rule
    val composeTestRule= createComposeRule()

    @Composable
    fun makezip(): MutableState<Int> {
        var zipCodeState = remember {mutableStateOf(55155)}
        return zipCodeState
    }


    @Composable
    fun makelat(): MutableState<Double> {
        var lat = remember {mutableStateOf(55155)}
        return lat
    }

    @Composable
    fun makelong(): MutableState<Double> {
        var long = remember {mutableStateOf(55155)}
        return long
    }

// I am having trouble getting my test data set up but I have listed what I want to run below
    @Composable
    fun setup():WeatherModel {
        var lat=makelat()
        var long=makelong()
        val viewModel=notificationModel(apiKey="", lat, long, unit="")
        return viewModel
    }



    @Test
    fun testZipCodeButton(){
        composeTestRule.setContent{
            val viewModel=setup()
            weatherScreenTest()
        }
        val newZipCode="abc"
        val newZipCodeInt:Int
        composeTestRule.onNodeWithText("zip field").assertIsDisplayed()
        composeTestRule.onNodeWithText("zip button").assertIsDisplayed()

        // assertTrue(newZipCode)
    }
/*
    @Test
    fun testWeatherButton() {
        val composeTestRule = createComposeRule()
        composeTestRule.onNodeWithText("Go To Forecast Button").performClick()
        composeTestRule.onNodeWithText("ForecastAppName").assertIsDisplayed()
    }
    */
    @Test
    fun checkItemsDisplayOnScreen () {
        composeTestRule.setContent{
            val viewModel=setup()
        WeatherScreen(viewModel,()->Unit)
    }
        composeTestRule.onNodeWithText("NoPermission").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Confirm").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("AppNameBox").assertIsDisplayed()
        composeTestRule.onNodeWithText("City").assertIsDisplayed()
        composeTestRule.onNodeWithText("TempR").assertIsDisplayed()
        composeTestRule.onNodeWithText("Feel").assertIsDisplayed()
        composeTestRule.onNodeWithText("FeelR").assertIsDisplayed()
        composeTestRule.onNodeWithText("DegSym").assertIsDisplayed()
        composeTestRule.onNodeWithText("lowTemp").assertIsDisplayed()
        composeTestRule.onNodeWithText("lowTempR").assertIsDisplayed()
        composeTestRule.onNodeWithText("highTemp").assertIsDisplayed()
        composeTestRule.onNodeWithText("highTempR").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hum").assertIsDisplayed()
        composeTestRule.onNodeWithText("HumR").assertIsDisplayed()
        composeTestRule.onNodeWithText("Press").assertIsDisplayed()
        composeTestRule.onNodeWithText("PressR").assertIsDisplayed()
        composeTestRule.onNodeWithText("TextField").assertIsDisplayed()
        composeTestRule.onNodeWithText("zipButton").assertIsDisplayed()
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("ForeButton").assertIsDisplayed()



    }



}