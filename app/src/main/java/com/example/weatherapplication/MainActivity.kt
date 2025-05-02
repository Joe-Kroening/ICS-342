package com.example.weatherapplication

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.MutableState
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
import android.Manifest.permission
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Looper
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import android.location.Address
import androidx.compose.ui.platform.testTag


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

/*
        locationCallback = object:LocationCallback(){
            fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
            }
        } */

        enableEdgeToEdge()



        // Notification function

        fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = getString(R.string.channel_name)
                val descriptionText = getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("A", name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as
                            NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        createNotificationChannel()


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

/*
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
        fun startLocationUpdates(){
            val locationRequest = LocationRequest.BUILDER(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build()
            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())

        }*/



}

fun GetIcon2(id: Double) : String {
    var str: String=""
    if (id < 300.0) {
        str="R.drawable.storm"
    }
    if (id >= 300.0 && id <600.0) {
        str="R.drawable.rain"
    }
    if (id>=600.0 && id<700.0) {
        str="R.drawable.snow"
    }
    if (id==800.0) {
        str="R.drawable.sun"
    }
    if (id>800.0) {
        str="R.drawable.clouds"
    }
    return str
}

@Composable
fun startApp() {
    // NEW!
    val context:Context = LocalContext.current
    val weatherService = createRetrofitService()
    val apiKey = stringResource(R.string.apiKey)
    //val apiKey = resources.getString(R.string.apiKey) not working
    val units = stringResource(R.string.unit)
    //val units = resources.getString(R.string.unit) not working
    var zipCode = 55155

    // Beginning Zip Code

    //var zipCode = remember { mutableStateOf(55113) }

    //val zipTest="55113"

    val geocoder = Geocoder(context)
    val maxResult=1

    @Suppress("DEPRECATION")
    var addressList: MutableList<Address> = geocoder.getFromLocationName(zipCode.toString(), maxResult)!!
    val address = addressList.get(0)

    val latTest = address.getLatitude()
    val longTest = address.getLongitude()

    //Toast.makeText(context,"$latTest   $longTest",Toast.LENGTH_LONG).show()



    val latitude = remember {mutableStateOf(latTest) }
    val longitude = remember {mutableStateOf(longTest)}

    val days = 16
    //val viewModel = WeatherModel(weatherService = weatherService, apiKey=apiKey, zipCodeState=zipCode, unit=units)
    //viewModel.fetchWeather()



    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val notificationService = createRetrofitNoteService()
    val viewNoteModel = notificationModel(
        weatherService = notificationService,
        apiKey = apiKey,
        latitude = latitude,
        longitude = longitude,
        unit = units
    )
    viewNoteModel.fetchWeather()

    val notificationServiceLatLon = createRetrofitNoteServiceLatLon()
    val viewModelForeLatLon = foreCastModelLatLon(
        weatherService = notificationServiceLatLon,
        apiKey = apiKey,
        latitude = latitude,
        longitude = longitude,
        unit = units,
        days=days
    )
    viewModelForeLatLon.fetchWeather()

// printing out values to check that location call worked
        //Toast.makeText(context,"${weatherNoteItems}",Toast.LENGTH_SHORT).show()
        //  Toast.makeText(context,"${location.latitude}  ${location.longitude}",Toast.LENGTH_SHORT).show()


    // NEW VIEW!
    val foreCastService = createForeRetrofitService()
    //val viewModelFore = foreCastModel(weatherService = foreCastService, apiKey=apiKey, zipCode=zipCode, unit=units, days=days)
    //viewModelFore.fetchWeather()


    navigation(viewModel=viewNoteModel,viewModelFore=viewModelForeLatLon)




}

@Composable
fun getIcon(id: Double) : String {
    var str: String=""
    if (id < 300.0) {
        str="R.drawable.storm"
    }
    if (id >= 300.0 && id <600.0) {
        str="R.drawable.rain"
    }
    if (id>=600.0 && id<700.0) {
        str="R.drawable.snow"
    }
    if (id==800.0) {
        str="R.drawable.sun"
    }
    if (id>800.0) {
        str="R.drawable.clouds"
    }
    return str
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


@Composable
fun MinimalDialogExample(onDismissRequest: ()->Unit) {
    val message = stringResource(R.string.notAllowed)
    Dialog(onDismissRequest={onDismissRequest()}) {
        Card(modifier=Modifier) {
            Text(
                text=message
            )
            Row{
                Button(modifier=Modifier.testTag("NoPermission"),
                    onClick={
                        onDismissRequest()
                    }) {
                    Text(text="Confirm")
                }
                Button(modifier=Modifier.testTag("Confirm"),
                    onClick={
                        onDismissRequest()
                    }) {
                    Text(text="Dismiss")
                }
            }


        }
    }
}

@Composable
//fun RunDialog() {
    fun RunDialog(showDialog: MutableState<Boolean>) {
   // val showDialog=remember{mutableStateOf(true)}
    if(showDialog.value==true) {
        MinimalDialogExample(
            onDismissRequest={showDialog.value=false}
        )
    }
}



//  NEW !
@Composable
fun WeatherScreen(data: notificationModel, onButtonClicked:()->Unit) {
    val context:Context = LocalContext.current

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
    //val zip = data.getZip()
    val degSym = stringResource(R.string.degreeSym)

    var text: String = ""

    val showDialog = remember{mutableStateOf(false)}

    val weather = weatherItems?.weather  // getting list of weather items
    val firstWeatherID=weather?.first()?.id  //get first element of the list
    val weatherCond=weather?.first()?.main

    val apiKey=data.getApi()
    val latitude: Double



    // Notification

    val intent = Intent(context, MainActivity::class.java).apply{
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK}


    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    if (firstWeatherID != null) {
        if (firstWeatherID < 300.0) {
            val builder = NotificationCompat.Builder(context, "A")
                .setSmallIcon(R.drawable.storm)
                .setContentTitle("$city")
                .setContentText("$tempR$degSym   $weatherCond")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(0, builder.build())
            }
        }
    }

    if (firstWeatherID != null) {
        if (firstWeatherID >= 300.0 && firstWeatherID<600.0) {
            val builder = NotificationCompat.Builder(context, "A")
                .setSmallIcon(R.drawable.rain)
                .setContentTitle("$city")
                .setContentText("$tempR$degSym   $weatherCond")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(0, builder.build())
            }
        }
    }

    if (firstWeatherID != null) {
        if (firstWeatherID >= 600.0 && firstWeatherID<700.0) {
            val builder = NotificationCompat.Builder(context, "A")
                .setSmallIcon(R.drawable.snow)
                .setContentTitle("$city")
                .setContentText("$tempR$degSym   $weatherCond")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(0, builder.build())
            }
        }
    }

    if (firstWeatherID == 800.0) {
        val builder = NotificationCompat.Builder(context, "A")
            .setSmallIcon(R.drawable.sun)
            .setContentTitle("$city")
            .setContentText("$tempR$degSym   $weatherCond")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(0, builder.build())
        }
    }

    if (firstWeatherID != null) {
        if (firstWeatherID >= 800.0) {
            val builder = NotificationCompat.Builder(context, "A")
                .setSmallIcon(R.drawable.clouds)
                .setContentTitle("$city")
                .setContentText("$tempR$degSym   $weatherCond")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(0, builder.build())
            }
        }
    }
    {
    //with(NotificationManagerCompat.from(context)){
        //notify(0, builder.build())
    }

    @Composable
    fun getIcon(id: Double) : String {
        var str: String=""
        if (id < 300.0) {
            str="R.drawable.storm"
        }
        if (id >= 300.0 && id <600.0) {
            str="R.drawable.rain"
        }
        if (id>=600.0 && id<700.0) {
            str="R.drawable.snow"
        }
        if (id==800.0) {
            str="R.drawable.sun"
        }
        if (id>800.0) {
            str="R.drawable.clouds"
        }
        return str
    }
    val icon = firstWeatherID?.let { getIcon(it) }




    Column {
        Box(
            modifier = Modifier.testTag("AppNameBox")
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.Gray)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$city", fontSize = 27.sp,
                //stringResource(R.string.cityState), fontSize = 27.sp,
                modifier = Modifier.testTag("City")
                    .padding(0.dp)
            )

        }
// Temp
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$tempR", fontSize = 100.sp,
                modifier = Modifier.testTag("TempR")
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
                        contentDescription = stringResource(R.string.Thunderstrom)
                    )
                }
            }

// Image

            if (firstWeatherID != null) {
                if(firstWeatherID < 600.0 && firstWeatherID >= 300.0) {
                    Image(
                        painter = painterResource(id = R.drawable.rain),
                        contentDescription = stringResource(R.string.Rain)
                    )
                }
            }
// Image
            if (firstWeatherID != null) {
                if(firstWeatherID < 700.0 && firstWeatherID > 600.0) {
                    Image(
                        painter = painterResource(id = R.drawable.snow),
                        contentDescription = stringResource(R.string.Snow)
                    )
                }
            }

// Image
            if(firstWeatherID==800.0) {
                Image(
                    painter = painterResource(id = R.drawable.sun),
                    contentDescription = stringResource(R.string.Sunny)
                )
            }
// Image
            if (firstWeatherID != null) {
                if(firstWeatherID > 800.0) {
                    Image(
                        painter = painterResource(id = R.drawable.clouds),
                        contentDescription = stringResource(R.string.Clouds)
                    )
                }
            }


        }
        // Feel
        Row(modifier = Modifier.padding(bottom = 20.dp)) {
            Text(
                stringResource(R.string.feel), fontSize = 15.sp,
                modifier = Modifier.padding(start = 40.dp)
                    .testTag("Feel")
            )
            Text(
                text = "$feelR", fontSize = 15.sp,
                modifier = Modifier
                    .testTag("FeelR")
            )
            Text(
                stringResource(R.string.degreeSym), fontSize = 30.sp,
                modifier = Modifier.testTag("degSym")
            )
        }

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)) {

            Row(){
                Column(){
                    Row(modifier = Modifier.padding(15.dp)) {
                        Column(modifier = Modifier.padding(10.dp)
                            .testTag("lowTemp")) { Text(
                            text= stringResource(R.string.lowTemp),
                            fontSize =  20.sp
                        )
                            Row {
                                Text(modifier=Modifier.testTag("lowTempR"),
                                    text = "$lowTempR",
                                    fontSize =  20.sp
                                )
                                Text(
                                    stringResource(R.string.degreeSym),
                                    fontSize =  20.sp
                                )
                            }

                        }

                        Column(modifier = Modifier.padding(10.dp).testTag("highTemp")) { Text(
                            text= stringResource(R.string.highTemp),
                            fontSize =  20.sp
                        )
                            Row {
                                Text(modifier=Modifier.testTag("highTempR"),
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
                        Column(modifier = Modifier.padding(10.dp)
                            .testTag("Hum")) { Text(
                            text= stringResource(R.string.humidity),
                            fontSize =  20.sp
                        )
                            Row {
                                Text(modifier=Modifier.testTag("HumR"),
                                    text = "$humidityR",
                                    fontSize =  20.sp
                                )
                                Text(
                                    stringResource(R.string.humidityUnit),
                                    fontSize =  20.sp
                                )
                            }

                        }

                        Column(modifier = Modifier.padding(15.dp)
                            .testTag("Press")) { Text(
                            text= stringResource(R.string.pressure),
                            fontSize =  20.sp
                        )
                            Row {
                                Text(modifier=Modifier.testTag("PressR"),
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
            //var zipCodeState by data.zipCodeState
            var newZipCode by remember {mutableStateOf("")}
            var newZipCodeInt by remember {mutableStateOf(0)}

            val context=LocalContext.current
            val message=stringResource(R.string.zipError)
            val permission = arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()
            ) { permissionMap ->
                val isGranted = permissionMap.values.reduce{acc, next -> acc && next}
                if(isGranted) {

                    //createLocation()
                    //Create Location object
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                    val result=fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            it.latitude
                            it.longitude
                        }

                        val latitude = location.latitude
                        val longitude = location.longitude

                        data.updateLat(latitude)
                        data.updateLong(longitude)

                    // Notification
/*
                    val intent = Intent(context, MainActivity::class.java).apply{
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK}


                    val pendingIntent: PendingIntent = PendingIntent.getActivity(
                        context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


                    val builder = NotificationCompat.Builder( context, "A")
                        .setSmallIcon(R.drawable.sun)
                        .setContentTitle("$data.weatherItems.name")
                        .setContentText("$tempR$degSym   $weatherCond")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)

                    with(NotificationManagerCompat.from(context)){

                        notify(0, builder.build())
                    }
                    */

                    //Toast.makeText(context,"Hello  World",Toast.LENGTH_SHORT).show()



                        // printing out values to check that location call worked
                        //Toast.makeText(context,"${weatherNoteItems}",Toast.LENGTH_SHORT).show()
                        //Toast.makeText(context,"${location.latitude}  ${location.longitude}",Toast.LENGTH_SHORT).show()
                    }


                }
                else {
                    // show dialog box
                    showDialog.value=true
                    //val message2="Permission was denied!"
                    //Toast.makeText(context,message2,Toast.LENGTH_SHORT).show()
                }
            }



            fun checkAndRequestPermission(
                context: Context,
                permission: Array<String>,
                launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
            ) {
                if (
                    permission.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }
                ) {
                    //createLocation()

                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                    val result=fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            it.latitude
                            it.longitude
                        }

                        val latitude = location.latitude
                        val longitude = location.longitude

                        data.updateLat(latitude)
                        data.updateLong(longitude)

                    // Notification

/*
                    val intent = Intent(context, MainActivity::class.java).apply{
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK}


                    val pendingIntent: PendingIntent = PendingIntent.getActivity(
                        context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


                    val builder = NotificationCompat.Builder( context, "A")
                        .setSmallIcon(R.drawable.sun)
                        .setContentTitle("$city")
                        .setContentText("$tempR$degSym  $weatherCond")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)

                    with(NotificationManagerCompat.from(context)){

                        notify(0, builder.build())
                    }
*/




// printing out values to check that location call worked
                        //Toast.makeText(context,"${weatherNoteItems}",Toast.LENGTH_SHORT).show()
                          //Toast.makeText(context,"${location.latitude}  ${location.longitude}",Toast.LENGTH_SHORT).show()
                    }


                } else {
                    launcher.launch(permission)
                }
            }

            OutlinedTextField(
                modifier=Modifier.width(130.dp).testTag("TextField"),
                value = newZipCode,
                onValueChange = { newZipCode = it },
                label = { Text("ZipCode") },
                keyboardOptions = KeyboardOptions(keyboardType= KeyboardType.Number)
            )
            Button(modifier=Modifier.testTag("zipButton"),onClick={
                if(newZipCode.length==5 && newZipCode.toInt()!=null && !newZipCode.contains('.') && newZipCode[0]!='-')   {
                    newZipCodeInt = newZipCode.toInt()
                    //data.updateZip(newZipCodeInt)
                }
                else {
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

                }
            }){
                Text(text=stringResource(R.string.search))
            }

            // Home Image

            Spacer(modifier= Modifier.width(85.dp))
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    modifier=Modifier.testTag("Home")
                        .height(60.dp)
                        .clickable { //showDialog.value=true
                            checkAndRequestPermission(
                                context = context,
                                permission = permission,
                                launcher = launcher
                            )
                        }
                        //.padding(start = 85.dp),

                    )
          //  }

        }

        Row {
            // Text Field
            text1()
        }
        Row{
            // Button
            Button(modifier=Modifier.testTag("ForeButton"),onClick=onButtonClicked) {
                Text(text=stringResource(R.string.goFor))
            }

        }

/*
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
        Text(text = stringResource(R.string.search))
    }
}

 */
        Column(modifier=Modifier.height(30.dp)){

            }

}

// Show dialog box associated with home
RunDialog(showDialog)

}

// Second Screen
@Composable
fun foreCastScreen(data: foreCastModelLatLon,onButtonClicked:()->Unit) {
    val foreCastItems by data.foreCastItems.observeAsState()
    val city = foreCastItems?.city?.name
    val city2 = foreCastItems?.city
    val list = foreCastItems?.list

    Column {
        Box(
            modifier = Modifier.testTag("AppNameBox2")
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.Gray)
        ) {
            Text(
                stringResource(R.string.appName), fontSize = 27.sp,
                modifier = Modifier.testTag("AppName2")
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
                        modifier = Modifier.testTag("ForeCastFor")
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

        Box(modifier=Modifier
            .fillMaxWidth()
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
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)) {
                            Text(
                            text = "$dateS",
                            modifier = Modifier.padding(15.dp)
                                .testTag("Date"),
                            fontSize = 45.sp
                        )
                            Row(){
                                Column(){
                                    Row(modifier = Modifier.padding(15.dp)) {
                                        Column(modifier = Modifier.padding(10.dp).
                                        testTag("lowTemp2")) { Text(
                                            text= stringResource(R.string.lowTemp),
                                            fontSize =  20.sp
                                        )
                                            Row {
                                                Text(modifier=Modifier.testTag("lowTempR2"),
                                                    text = "$lowTemp",
                                                    fontSize =  20.sp
                                                )
                                                Text(
                                                    stringResource(R.string.degreeSym),
                                                    fontSize =  20.sp
                                                )
                                            }

                                        }

                                        Column(modifier = Modifier.padding(10.dp).testTag("highTemp2")) { Text(
                                            text= stringResource(R.string.highTemp),
                                            fontSize =  20.sp
                                        )
                                            Row {
                                                Text(modifier=Modifier.testTag("highTempR2"),
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
                                        Column(modifier = Modifier.padding(10.dp).testTag("Hum2")) { Text(
                                            text= stringResource(R.string.humidity),
                                            fontSize =  20.sp
                                        )
                                            Row {
                                                Text(modifier=Modifier.testTag("HumR2"),
                                                    text = "$humidity",
                                                    fontSize =  20.sp
                                                )
                                                Text(
                                                    stringResource(R.string.humidityUnit),
                                                    fontSize =  20.sp
                                                )
                                            }

                                        }

                                        Column(modifier = Modifier.padding(10.dp).testTag("Press2")) { Text(
                                            text= stringResource(R.string.pressure),
                                            fontSize =  20.sp
                                        )
                                            Row {
                                                Text(modifier=Modifier.testTag("PressR2"),
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
            Button(modifier=Modifier.testTag("WeatherButton"),onClick = onButtonClicked) {
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
// Retrofit service based on lat lon
fun createRetrofitNoteServiceLatLon(): foreCastInterfaceLatLon {
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
        .create(foreCastInterfaceLatLon::class.java)
}

// notification
fun createRetrofitNoteService(): notificationInterface {
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
        .create(notificationInterface::class.java)
}

// Navigation
@Composable
fun navigation(viewModel: notificationModel, viewModelFore: foreCastModelLatLon) {

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
