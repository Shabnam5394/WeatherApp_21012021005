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
                response = URL("https://api.openweathermap.org/data/2.5/forecast?q=$CITY,us&mode=jason&appid=$API").readText(Charsets.UTF_8)
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
//                val cityjason = firstItem.getJSONObject("city")

                // Now you can access the data in the "main" object
                val temp = main.getDouble("temp")
                val temp_min = main.getDouble("temp_min")
                val temp_max = main.getDouble("temp_max")
                val humidityget = main.getDouble("humidity")


                // Now you can access the data in the "wind" object

                val windget = wind.getDouble("speed")









                Log.d("TAG", "jasontemp")

                Log.d("TAG", "Temperature: $temp")

                //main

                val tempttext = findViewById<TextView>(R.id.main_tempreture)
                tempttext.text = temp.toString()
                val tempmin = findViewById<TextView>(R.id.main_minimumtemp)
                tempmin.text = temp_min.toString()
                val tempmax = findViewById<TextView>(R.id.main_maximum_temp)
                tempmax.text = temp_max.toString()

                val humidity = findViewById<TextView>(R.id.main_humidity)
                humidity.text = humidityget.toString()


                //wind

                val windtext = findViewById<TextView>(R.id.main_wind)
                windtext.text = windget.toString()


                //city
//
//                val city = cityjason.getString("name")
//                val country = cityjason.getString("country")
//
//
//                val citytext = findViewById<TextView>(R.id.main_city)
//                citytext.text = city
//
//                val countryname = findViewById<TextView>(R.id.main_countryname)
//                countryname.text = country




                // You can use these variables as needed in your code
            } catch (e: JSONException) {
                e.printStackTrace()
                Log.d("TAG", "json error")
                // Handle any JSON parsing errors here
            }
        }
    }
}
