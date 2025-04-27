package com.example.weatherapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.weatherapplication.ui.theme.WeatherApplicationTheme
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

import androidx.compose.material3.Button // Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat

import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        // NEW!
        /* Moved to startApp because state variables must be in a
        composable function
        val weatherService = createRetrofitService()
        val apiKey = resources.getString(R.string.apiKey)
        val units = resources.getString(R.string.unit)
        //var zipCode = 55155

        var zipCodeState = remember {mutableStateOf(55155)}
        val days = 16
        // NEW took out zip code from WeatherModel constructor since it was added as a variable
        //val viewModel = WeatherModel(weatherService = weatherService, apiKey=apiKey, unit=units)
        val viewModel = WeatherModel(weatherService = weatherService, apiKey=apiKey, zipCode=zipCode, unit=units)
        viewModel.fetchWeather()

         //! NEW FOR TESTING GETTER FUNCTION
        //println(viewModel.zipCode)
        viewModel.updateZip(32714)
        //println(viewModel.zip2)


        // NEW!

        // NEW VIEW!
        val foreCastService = createForeRetrofitService()
        val viewModelFore = foreCastModel(weatherService = foreCastService, apiKey=apiKey, zipCode=zipCode, unit=units, days=days)
        viewModelFore.fetchWeather()
        */

        // NEW VIEW!


        setContent {
            WeatherApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier.padding(innerPadding)
                    ){
                        //WeatherScreen(data=viewModel)
                        // ***ADD Second screen***
                        //foreCastScreen(data=viewModelFore)
                        //navigation(viewModel=viewModel,viewModelFore=viewModelFore)
                        startApp()
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
@Composable
fun startApp() {
    // NEW!
    val weatherService = createRetrofitService()
    val apiKey = stringResource(R.string.apiKey)
    //val apiKey = resources.getString(R.string.apiKey) not working
    val units = stringResource(R.string.unit)
    //val units = resources.getString(R.string.unit) not working
    //var zipCode = 55155

    var zipCode = remember { mutableStateOf(55155) }

    val days = 16
    val viewModel = WeatherModel(weatherService = weatherService, apiKey=apiKey, zipCodeState=zipCode, unit=units)
    viewModel.fetchWeather()




    // NEW!


    // NEW VIEW!
    val foreCastService = createForeRetrofitService()
    val viewModelFore = foreCastModel(weatherService = foreCastService, apiKey=apiKey, zipCode=zipCode, unit=units, days=days)
    viewModelFore.fetchWeather()

    navigation(viewModel=viewModel,viewModelFore=viewModelFore)




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
@Composable
fun DialogBox(onDismissRequest: ()->Unit) {
    //var onDismissRequest1: ()->Unit
    Dialog(onDismissRequest= {onDismissRequest()}) {
        val textMesaage = "Enter valid ZipCode"
        Text(text=textMesaage)
    }
}



//  NEW !
@Composable
fun WeatherScreen(data: WeatherModel,onButtonClicked:()->Unit) {
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
    val city = weatherItems?.name
    val zip = data.getZip()

    var text: String = ""

    val weather = weatherItems?.weather  // getting list of weather items
    val firstWeatherID=weather?.first()?.id  //get first element of the list


    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(60.dp).background(Color.Gray)
        ) {
            Text(
                stringResource(R.string.appName), fontSize = 27.sp,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()

            )
        }
// CityState
        Row(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$city", fontSize = 27.sp,
                //stringResource(R.string.cityState), fontSize = 27.sp,
                modifier = Modifier
                    .padding(0.dp)
            )

        }
// Temp
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$tempR", fontSize = 100.sp,
                modifier = Modifier
                    .padding(start = 30.dp)
            )
            Text(
                stringResource(R.string.degreeSym), fontSize = 100.sp,
                modifier = Modifier
                    .padding(end = 100.dp)

            )
// Image
            if (firstWeatherID != null) {
                if(firstWeatherID < 300.0) {
                    Image(
                        painter = painterResource(id = R.drawable.storm),
                        contentDescription = "Thunderstrom"
                    )
                }
            }

// Image

            if (firstWeatherID != null) {
                if(firstWeatherID < 600.0 && firstWeatherID >= 300.0) {
                    Image(
                        painter = painterResource(id = R.drawable.rain),
                        contentDescription = "Rain"
                    )
                }
            }
// Image
            if (firstWeatherID != null) {
                if(firstWeatherID < 700.0 && firstWeatherID > 600.0) {
                    Image(
                        painter = painterResource(id = R.drawable.snow),
                        contentDescription = "Snow"
                    )
                }
            }

// Image
            if(firstWeatherID==800.0) {
                Image(
                    painter = painterResource(id = R.drawable.sun),
                    contentDescription = "Sunny"
                )
            }
// Image
            if (firstWeatherID != null) {
                if(firstWeatherID > 800.0) {
                    Image(
                        painter = painterResource(id = R.drawable.clouds),
                        contentDescription = "Clouds"
                    )
                }
            }


        }
        // Feel
        Row(modifier = Modifier.padding(bottom = 20.dp)) {
            Text(
                stringResource(R.string.feel), fontSize = 15.sp,
                modifier = Modifier.padding(start = 40.dp)
            )
            Text(
                text = "$feelR", fontSize = 15.sp,
                modifier = Modifier
            )
            Text(
                stringResource(R.string.degreeSym), fontSize = 30.sp,
                modifier = Modifier
            )
        }

        Card(modifier = Modifier.fillMaxWidth().padding(15.dp)) {

            Row(){
                Column(){
                    Row(modifier = Modifier.padding(15.dp)) {
                        Column(modifier = Modifier.padding(10.dp)) { Text(
                            text= stringResource(R.string.lowTemp),
                            fontSize =  20.sp
                        )
                            Row {
                                Text(
                                    text = "$lowTempR",
                                    fontSize =  20.sp
                                )
                                Text(
                                    stringResource(R.string.degreeSym),
                                    fontSize =  20.sp
                                )
                            }

                        }

                        Column(modifier = Modifier.padding(10.dp)) { Text(
                            text= stringResource(R.string.highTemp),
                            fontSize =  20.sp
                        )
                            Row {
                                Text(
                                    text = "$highTempR",
                                    fontSize =  20.sp
                                )
                                Text(
                                    stringResource(R.string.degreeSym),
                                    fontSize =  20.sp
                                )
                            }

                        }


                    }
                    Row(modifier = Modifier.padding(15.dp)) {
                        Column(modifier = Modifier.padding(10.dp)) { Text(
                            text= stringResource(R.string.humidity),
                            fontSize =  20.sp
                        )
                            Row {
                                Text(
                                    text = "$humidityR",
                                    fontSize =  20.sp
                                )
                                Text(
                                    stringResource(R.string.humidityUnit),
                                    fontSize =  20.sp
                                )
                            }

                        }

                        Column(modifier = Modifier.padding(15.dp)) { Text(
                            text= stringResource(R.string.pressure),
                            fontSize =  20.sp
                        )
                            Row {
                                Text(
                                    text = "$pressureR",
                                    fontSize =  20.sp
                                )
                                Text(
                                    stringResource(R.string.pressureUnit),
                                    fontSize =  20.sp
                                )
                            }

                        }
                    }
                }

            }

        }
// low temp
/*
        Row {
            Text(
                stringResource(R.string.lowTemp), fontSize = 30.sp,
                modifier = Modifier
                    .padding(start = 15.dp)
            )
            Text(
                text = "$lowTempR", fontSize = 30.sp,
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
                text = "$highTempR", fontSize = 30.sp,
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
                text = "$humidityR", fontSize = 30.sp,
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
                text = "$pressureR", fontSize = 30.sp,
                modifier = Modifier
            )
            Text(
                stringResource(R.string.pressureUnit), fontSize = 30.sp,
                modifier = Modifier
            )

        }
*/



        @Composable
        fun text1() {
            var zipCodeState by data.zipCodeState
            var newZipCode by remember {mutableStateOf("")}
            var newZipCodeInt by remember {mutableStateOf(0)}
            val context=LocalContext.current
            val message=stringResource(R.string.zipError)



            OutlinedTextField(
                value = newZipCode,
                onValueChange = { newZipCode = it },
                label = { Text("ZipCode") },
                keyboardOptions = KeyboardOptions(keyboardType= KeyboardType.Number)
            )
            Button(onClick={
                if(newZipCode.length==5 && newZipCode.toInt()!=null && !newZipCode.contains('.') && newZipCode[0]!='-')   {
                    newZipCodeInt = newZipCode.toInt()
                    data.updateZip(newZipCodeInt)
                }
                else {
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

                }
            }){
                Text(text=stringResource(R.string.search))
            }
        }

        Row {
            // Text Field
            text1()
        }
        Row{
            // Button
            Button(onClick=onButtonClicked) {
                Text(text=stringResource(R.string.goFor))
            }
        }


//Zip Button
            @Composable
            fun validateButton() {
                Button(onClick = {
                     if (text.length != 5) {
                         //DialogBox()
            }
        })

    // Button call
    //validateButton()
    {
        Text(text = "Search")
    }
}
            }





}

// Second Screen
@Composable
fun foreCastScreen(data: foreCastModel,onButtonClicked:()->Unit) {
    val foreCastItems by data.foreCastItems.observeAsState()
    val city = foreCastItems?.city?.name
    val city2 = foreCastItems?.city
    val list = foreCastItems?.list

    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(60.dp).background(Color.Gray)
        ) {
            Text(
                stringResource(R.string.appName), fontSize = 27.sp,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            )
        }


        Row (modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
           // Box(modifier = Modifier.fillMaxWidth()) {
                Row(modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Text(
                        stringResource(R.string.foreCastFor), fontSize = 48.sp,
                        modifier = Modifier
                            .padding(25.dp)
                    )
                }
               // Row(modifier=Modifier.fillMaxWidth()) {
            /*
                    Text(
                        text = "$city2", fontSize = 24.sp,
                        modifier = Modifier
                            .padding(15.dp)
                    )
                    */

             //   }


           // }
        }

        Box(modifier=Modifier.fillMaxWidth()
                            .height(600.dp)) {
            LazyColumn(modifier=Modifier.fillMaxWidth(),
                userScrollEnabled = true
            ) {
                list?.forEach {
                    val dt=it.dt
                    val sdf = SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                    val date = dt?.let { it1 -> Date(it1*1000) }
                    val dateS: String = date?.let { it1 -> sdf.format(it1) }.toString()
                    val lowTemp=it.temp.min?.toInt()
                    val highTemp=it.temp.max?.toInt()
                    val humidity=it.humidity?.toInt()
                    val pressure=it.pressure?.toInt()
                    val firstWeatherID=it.weather.first().id


                    item{
                        // Row(modifier=Modifier.fillMaxWidth()){
                        Card(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
                            Text(
                            text = "$dateS",
                            modifier = Modifier.padding(15.dp),
                            fontSize = 45.sp
                        )
                            Row(){
                                Column(){
                                    Row(modifier = Modifier.padding(15.dp)) {
                                        Column(modifier = Modifier.padding(10.dp)) { Text(
                                            text= stringResource(R.string.lowTemp),
                                            fontSize =  20.sp
                                        )
                                            Row {
                                                Text(
                                                    text = "$lowTemp",
                                                    fontSize =  20.sp
                                                )
                                                Text(
                                                    stringResource(R.string.degreeSym),
                                                    fontSize =  20.sp
                                                )
                                            }

                                        }

                                        Column(modifier = Modifier.padding(10.dp)) { Text(
                                            text= stringResource(R.string.highTemp),
                                            fontSize =  20.sp
                                        )
                                            Row {
                                                Text(
                                                    text = "$highTemp",
                                                    fontSize =  20.sp
                                                )
                                                Text(
                                                    stringResource(R.string.degreeSym),
                                                    fontSize =  20.sp
                                                )
                                            }

                                        }


                                    }
                                    Row(modifier = Modifier.padding(15.dp)) {
                                        Column(modifier = Modifier.padding(10.dp)) { Text(
                                            text= stringResource(R.string.humidity),
                                            fontSize =  20.sp
                                        )
                                            Row {
                                                Text(
                                                    text = "$humidity",
                                                    fontSize =  20.sp
                                                )
                                                Text(
                                                    stringResource(R.string.humidityUnit),
                                                    fontSize =  20.sp
                                                )
                                            }

                                        }

                                        Column(modifier = Modifier.padding(10.dp)) { Text(
                                            text= stringResource(R.string.pressure),
                                            fontSize =  20.sp
                                        )
                                            Row {
                                                Text(
                                                    text = "$pressure",
                                                    fontSize =  20.sp
                                                )
                                                Text(
                                                    stringResource(R.string.pressureUnit),
                                                    fontSize =  20.sp
                                                )
                                            }

                                        }
                                    }
                                }

                                Column(modifier = Modifier.padding(15.dp)) { Text(
                                    text= stringResource(R.string.Weather)
                                )
                                    // Image
                                    if (firstWeatherID != null) {
                                        if(firstWeatherID < 300.0) {
                                            Image(
                                                painter = painterResource(id = R.drawable.storm),
                                                contentDescription = "Thunderstrom"
                                            )
                                        }
                                    }

// Image

                                    if (firstWeatherID != null) {
                                        if(firstWeatherID < 600.0 && firstWeatherID >= 300.0) {
                                            Image(
                                                painter = painterResource(id = R.drawable.rain),
                                                contentDescription = "Rain"
                                            )
                                        }
                                    }
// Image
                                    if (firstWeatherID != null) {
                                        if(firstWeatherID < 700.0 && firstWeatherID > 600.0) {
                                            Image(
                                                painter = painterResource(id = R.drawable.snow),
                                                contentDescription = "Snow"
                                            )
                                        }
                                    }

// Image
                                    if(firstWeatherID==800.0) {
                                        Image(
                                            painter = painterResource(id = R.drawable.sun),
                                            contentDescription = "Sunny"
                                        )
                                    }
// Image
                                    if (firstWeatherID != null) {
                                        if(firstWeatherID > 800.0) {
                                            Image(
                                                painter = painterResource(id = R.drawable.clouds),
                                                contentDescription = "Clouds"
                                            )
                                        }
                                    }
                                }
                            }

                        }



                    }

                }
            }
        }



/*
            if (list != null) {
                list.forEach {
                    val dt = it.dt
                    val lowTemp = it.temp.min?.toInt()
                    val highTemp = it.temp.max?.toInt()
                    val humidity = it.humidity
                    val pressure = it.pressure
                    Row {
/*
                    Text(
                        text = "$dt",
                        modifier = Modifier.padding(15.dp)
                    )

 */
                        // LOW

                        Text(
                            text = "$lowTemp",
                            modifier = Modifier.padding(start = 40.dp)
                                .width(25.dp), fontSize = 15.sp
                        )
                        Text(
                            stringResource(R.string.degreeSym), fontSize = 15.sp,
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                        // HIGH

                        Text(
                            text = "$highTemp",
                            modifier = Modifier.padding(start = 55.dp, bottom = 15.dp)
                        )
                        Text(
                            stringResource(R.string.degreeSym), fontSize = 15.sp,
                            modifier = Modifier.padding(start = 1.dp, bottom = 15.dp)
                        )
                        // HUMIDITY

                        Text(
                            text = "$humidity",
                            modifier = Modifier.padding(start = 75.dp, bottom = 15.dp)
                        )
                        Text(
                            stringResource(R.string.humidityUnit),
                            modifier = Modifier.padding(start = 1.dp, bottom = 15.dp)
                        )

                        // PRESSURE

                        Text(
                            text = "$pressure",
                            modifier = Modifier.padding(start = 55.dp, bottom = 15.dp)
                        )
                        Text(
                            stringResource(R.string.pressureUnit),
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                    }
                }
*/

// Text Field


// Button
            Button(onClick = onButtonClicked) {
                Text(text = stringResource(R.string.goWeath))
            }

        }
    //}
}

/*
@Composable
fun start(apiKey) {
    // NEW!
    val weatherService = createRetrofitService()
    //val apiKey = resources.getString(R.string.apiKey)
   //val units = resources.getString(R.string.unit)
    val zipCode: Int by remember { mutableStateOf(55155) }
    val days = 16
    val viewModel = WeatherModel(weatherService = weatherService, apiKey=apiKey, zipCode=zipCode, unit=units)
    viewModel.fetchWeather()
    // NEW!

    // NEW VIEW!
    val foreCastService = createForeRetrofitService()
    val viewModelFore = foreCastModel(weatherService = foreCastService, apiKey=apiKey, zipCode=zipCode, unit=units, days=days)
    viewModelFore.fetchWeather()
    // NEW VIEW!
    navigation(viewModel= androidx.lifecycle.viewmodel.compose.viewModel,viewModelFore=viewModelFore)
}
*/

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


// NEW RETROFIT!
fun createForeRetrofitService(): foreCastInterface {
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
        .create(foreCastInterface::class.java)
}

// Navigation
@Composable
fun navigation(viewModel: WeatherModel, viewModelFore: foreCastModel) {

    val navController = rememberNavController()

    @Serializable
    data class Screen(val name:String)

    var WeatherScn=Screen(name="Current")
    var ForecastScn=Screen(name="SixteenDay")


    NavHost(navController= navController,
            startDestination = WeatherScn.name) {
        composable(route = WeatherScn.name){
            WeatherScreen(data = viewModel,
            onButtonClicked={navController.navigate(ForecastScn.name)})
        }

        composable(route=ForecastScn.name) {
            foreCastScreen(data = viewModelFore,
                onButtonClicked= { navController.navigate(WeatherScn.name) })
        }
    }
}
