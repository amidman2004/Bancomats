package com.example.bancomats

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.size
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.w3c.dom.Text
import java.net.CacheResponse
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private var scope = MainScope()
    private var counter = 0
    fun getJson(response: String, i: Int) {
        val bancomatArray = JSONArray(response)
        val mItemObject = bancomatArray.getJSONObject(i)
        val name = mItemObject.getString("name")
        val timeOt = mItemObject.getString("time_from")
        val timeDo = mItemObject.getString("time_to")
        val status = mItemObject.getString("is_working")
        val formatter = SimpleDateFormat("hh:mm")
        val time1 = formatter.format(Date(timeOt.toLong()))
        val time2 = formatter.format(Date(timeDo.toLong()))
        val time0 = "$time1-$time2"

        val dateTime = Calendar.getInstance().time
        val Long = dateTime.time

        val adress_text = findViewById<TextView>(R.id.adress)
        val period_text = findViewById<TextView>(R.id.period)
        val status_text = findViewById<TextView>(R.id.status)

        val spisok = findViewById<LinearLayout>(R.id.line)


        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.activity_bancomatic, null)
        spisok!!.addView(rowView, spisok!!.childCount)
        rowView.findViewById<TextView>(R.id.status).text = Long.toString()
        rowView.findViewById<TextView>(R.id.adress).text = name
        rowView.findViewById<TextView>(R.id.period).text = time0

        if (Long>timeOt.toLong() && Long<timeDo.toLong()){
            rowView.findViewById<TextView>(R.id.status).text = "Работает"

        }else{
            rowView.findViewById<TextView>(R.id.status).text = "Закрыто"
            rowView.findViewById<TextView>(R.id.status).setTextColor(-0x2100000)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scope.launch(Dispatchers.IO) {
            (URL("http://10.0.3.2:3000/bankomats").openConnection() as HttpURLConnection).run {
                requestMethod = "GET"
                doInput = true
                connect()
                val stream = inputStream
                val response = stream.bufferedReader().readText()
                val knopka = findViewById<AppCompatButton>(R.id.suda)
                val spisok = findViewById<LinearLayout>(R.id.line)
                knopka.setOnClickListener {
                    if (counter<9) {
                        getJson(response, counter)
                        counter++
                    }else {
                        Toast.makeText(applicationContext, "банкоматы кончились", Toast.LENGTH_SHORT).show()
                    }
                }
                knopka.isLongClickable = true

            }
        }
    }
}
