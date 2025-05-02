package com.example.weatherapplication

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import org.junit.Test

import org.junit.Assert.*

class MainActivityKtTest {


    @Test
    fun CheckGetIcon() {
        val id=210.0
        val result = GetIcon2(id)
        println(result)
        assertEquals(/* expected = */ result, /* actual = */ "R.drawable.storm")
    }



}