package com.example.weatherapp_21012021005

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import android.widget.Toast
import androidx.cardview.widget.CardView
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var CITY: String
    lateinit var icon: String

    val API: String = "d6f656559459285b5b0c8b847cd7e8e1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val expandBtn = findViewById<Button>(R.id.expandBtn)
        val expandableLayout = findViewById<LinearLayout>(R.id.expandableLayout)
        val cardView = findViewById<CardView>(R.id.cardView1)
        expandBtn.setOnClickListener {
            if (expandableLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                expandableLayout.visibility = View.VISIBLE
                expandBtn.text = "COLLAPSE"
            } else {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                expandableLayout.visibility = View.GONE
                expandBtn.text = "EXPAND"
            }
        }

        Log.d("TAG", "first executed")

        val takeCity = findViewById<EditText>(R.id.main_searchview)
        val search = findViewById<Button>(R.id.main_searchbutton)

        search.setOnClickListener {
            CITY = takeCity.text.toString()
            Toast.makeText(applicationContext, takeCity.text.toString(), Toast.LENGTH_LONG).show()
            WeatherTask().execute()
            Log.d("TAG", "search executed")
        }
    }

    inner class WeatherTask : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String? {
            var response: String?

            try {
                response = URL("https://api.openweathermap.org/data/2.5/forecast?q=$CITY,us&mode=JSON&appid=$API").readText(Charsets.UTF_8)
            } catch (e: Exception) {
                response = null
                e.printStackTrace()
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            Log.d("TAG", "firstpostex")

            try {
                val jsonObj = JSONObject(result)

                // Access the "list" array
                val list = jsonObj.getJSONArray("list")

                // If you want to access data from the first item in the "list" array
                val firstItem = list.getJSONObject(0)

                val main = firstItem.getJSONObject("main")

                val wind = firstItem.getJSONObject("wind")

                // Now you can access the data in the "main" object
                val temp = main.getDouble("temp")
                val temp_min = main.getDouble("temp_min")
                val temp_max = main.getDouble("temp_max")
                val humidityget = main.getDouble("humidity")
                val sea_level = main.getDouble("sea_level")
                val ground_level = main.getDouble("grnd_level")
                val feelslike = main.getDouble("feels_like")
                val pressure = main.getDouble("pressure")

                //city
                val city = jsonObj.getJSONObject("city")
                val sunrise = city.getLong("sunrise")
                val sunset = city.getLong("sunset")
                val cityname = city.getString("name")
                val countryname = city.getString("country")

                //wind
                val windget = wind.getDouble("speed")
                val winddeg = wind.getInt("deg")

                Log.d("TAG", "jasontemp")

                Log.d("TAG", "Temperature: $temp")

                // Function to convert temperature from Kelvin to Celsius
                fun kelvinToCelsius(kelvin: Double): Double {
                    return kelvin - 273.15
                }

                val tempCelsius = kelvinToCelsius(temp)
                val tempMinCelsius = kelvinToCelsius(temp_min)
                val tempMaxCelsius = kelvinToCelsius(temp_max)

                // Display the temperature in Celsius
                val tempttext = findViewById<TextView>(R.id.main_tempreture)
                tempttext.text = String.format("%.2f °C", tempCelsius)

                val tempmin = findViewById<TextView>(R.id.main_minimumtemp)
                tempmin.text = String.format("%.2f °C", tempMinCelsius)

                val tempmax = findViewById<TextView>(R.id.main_maximum_temp)
                tempmax.text = String.format("%.2f °C", tempMaxCelsius)

                val humidity = findViewById<TextView>(R.id.main_humidity)
                humidity.text = humidityget.toString()

                val seaLevel = findViewById<TextView>(R.id.sea_level)
                seaLevel.text = sea_level.toString()

                val groundLevel = findViewById<TextView>(R.id.grnd_level)
                groundLevel.text = ground_level.toString()

                val humi = findViewById<TextView>(R.id.humiditydescr)
                humi.text = humidityget.toString()

                val humiditycircletext = findViewById<TextView>(R.id.circular_humidity)
                val humidityprogress = findViewById<ProgressBar>(R.id.progress_bar)

                // Assuming humidityget is a value between 0 and 100
                val humidityValue = humidityget.toInt()
                humiditycircletext.text = "$humidityValue%"  // Display humidity as a percentage
                humidityprogress.max = 100  // Set the maximum value of the progress bar
                humidityprogress.progress = humidityValue  // Set the current progress based on humidity

                //wind
                val windtext = findViewById<TextView>(R.id.main_wind)
                windtext.text = windget.toString()

                val winddeg1 = findViewById<TextView>(R.id.main_degree)
                winddeg1.text = winddeg.toString()

                val mainwindspeed = findViewById<TextView>(R.id.main_windspeed)
                mainwindspeed.text = windget.toString()

                //setting sunrise and sunset
                //convert





                val textsunrise = findViewById<TextView>(R.id.main_sunrise)
                val textsunset = findViewById<TextView>(R.id.main_sunset)

                val convertsunrise = sunrise
                val convertsunset = sunset


                val sunriseDate = java.util.Date(convertsunrise * 1000L)
                val sunsetDate = java.util.Date(convertsunset * 1000L)

                val sdf = java.text.SimpleDateFormat("HH:mm:ss") // You can change the format as needed
                val formattedSunrise = sdf.format(sunriseDate)
                val formattedSunset = sdf.format(sunsetDate)


                textsunrise.text = formattedSunrise
                textsunset.text = formattedSunset

                //feels like
                val textfeellike = findViewById<TextView>(R.id.main_feelslikes)
                textfeellike.text = String.format("%.2f °C", kelvinToCelsius(feelslike))

                val textpressure = findViewById<TextView>(R.id.main_pressures)
                textpressure.text = pressure.toString()

                //setting city name and country name
                val textcityname = findViewById<TextView>(R.id.main_city)
                textcityname.text = cityname

                val textcountry = findViewById<TextView>(R.id.main_countryname)
                textcountry.text = countryname
            } catch (e: JSONException) {
                e.printStackTrace()
                Log.d("TAG", "json error")
                // Handle any JSON parsing errors here
            }
        }
    }
}
