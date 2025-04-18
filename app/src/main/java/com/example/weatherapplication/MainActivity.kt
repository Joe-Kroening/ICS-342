package com.example.weatherapplication

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.weatherapplication.ui.theme.WeatherApplicationTheme
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // NEW!
        val weatherService = createRetrofitService()
        val apiKey = resources.getString(R.string.apiKey)
        val units = resources.getString(R.string.unit)
        val zipCode = 55155
        val viewModel = WeatherModel(weatherService = weatherService, apiKey=apiKey, zipCode=zipCode, unit=units)
        viewModel.fetchWeather()

        // NEW!


        setContent {
            WeatherApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier.padding(innerPadding)
                    ){
                        WeatherScreen(data=viewModel)
                    }

                    /*
                    Greeting(
                        name = "Application",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                }
            }
        }


    }
}
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Weather $name!",
        modifier = modifier
    )
}

 */
/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherApplicationTheme {
        Greeting("Android")
    }
}
*/

//  NEW !
@Composable
fun WeatherScreen(data: WeatherModel) {
    val weatherItems by data.weatherItems.observeAsState()
    val temp = weatherItems?.main?.temp
    val tempR=temp?.toInt()
    val lowTemp = weatherItems?.main?.minTemp
    val lowTempR = lowTemp?.toInt()
    val highTemp = weatherItems?.main?.maxTemp
    val highTempR = highTemp?.toInt()
    val humidity = weatherItems?.main?.humidity
    val humidityR = humidity?.toInt()
    val pressure = weatherItems?.main?.pressure
    val pressureR = pressure?.toInt()
    val feel = weatherItems?.main?.feelsLike
    val feelR = feel?.toInt()
    Column {
        Box(modifier = Modifier.fillMaxWidth()
            .height(60.dp) .background(Color.Gray)) {
            Text(
                stringResource(R.string.appName), fontSize = 27.sp,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()

            )
        }
// CityState
        Row(modifier = Modifier.fillMaxWidth() .padding(15.dp),
            horizontalArrangement=Arrangement.Center) {
            Text(
                stringResource(R.string.cityState), fontSize = 27.sp,
                modifier = Modifier
                    .padding(0.dp)
            )

        }
// Temp
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text="$tempR", fontSize = 100.sp,
                modifier = Modifier
                    .padding(start = 30.dp)
            )
            Text(
                stringResource(R.string.degreeSym), fontSize = 100.sp,
                modifier = Modifier
                    .padding(end=100.dp)

            )
// Image
            Image(painter = painterResource(id=R.drawable.sun),
                contentDescription = "The Sun")


        }
        // Feel
        Row(modifier = Modifier.padding(bottom = 20.dp)) {
            Text(
                stringResource(R.string.feel), fontSize = 15.sp,
                modifier = Modifier.padding(start=40.dp)
            )
            Text(
                text="$feelR", fontSize = 15.sp,
                modifier = Modifier
            )
            Text(
                stringResource(R.string.degreeSym), fontSize = 30.sp,
                modifier = Modifier
            )
// low temp
        }
        Row {
            Text(
                stringResource(R.string.lowTemp), fontSize = 30.sp,
                modifier = Modifier
                    .padding(start = 15.dp)
            )
            Text(
                text="$lowTempR", fontSize = 30.sp,
                modifier = Modifier

            )
            Text(
                stringResource(R.string.degreeSym), fontSize = 30.sp,
                modifier = Modifier
            )

        }
// High Temp
        Row {
            Text(
                stringResource(R.string.highTemp), fontSize = 30.sp,
                modifier = Modifier
                    .padding(start = 15.dp)
            )
            Text(
                text="$highTempR", fontSize = 30.sp,
                modifier = Modifier

            )
            Text(
                stringResource(R.string.degreeSym), fontSize = 30.sp,
                modifier = Modifier
            )

        }
// Humidity
        Row {
            Text(
                stringResource(R.string.humidity), fontSize = 30.sp,
                modifier = Modifier
                    .padding(start = 15.dp)
            )
            Text(
                text="$humidityR", fontSize = 30.sp,
                modifier = Modifier
            )
            Text(
                stringResource(R.string.humidityUnit), fontSize = 30.sp,
                modifier = Modifier
            )
// Pressure
        }
        Row {
            Text(
                stringResource(R.string.pressure), fontSize = 30.sp,
                modifier = Modifier
                    .padding(start = 15.dp)
            )
            Text(
                text="$pressureR", fontSize = 30.sp,
                modifier = Modifier
            )
            Text(
                stringResource(R.string.pressureUnit), fontSize = 30.sp,
                modifier = Modifier
            )

        }

    }

}




// NEW!
fun createRetrofitService(): weatherInterface {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    return Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .client(client)
        .addConverterFactory(
            Json.asConverterFactory(
            "application/json".toMediaType()
        ))
        .build()
        .create(weatherInterface::class.java)
}


