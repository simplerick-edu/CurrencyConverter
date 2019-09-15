package com.example.simplerick.currencyconverter

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.content.res.Resources
import com.example.simplerick.currencyconverter.DigitsInputFilter
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject







class MainActivity : AppCompatActivity() {

    private lateinit var source_spinner: Spinner
    private lateinit var target_spinner:Spinner
    private lateinit var source_value: EditText
    private lateinit var target_value: EditText
    private var cache: HashMap<String,Double> = HashMap()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initilize()
        getExchangeRates()
        addTextWatcher()
//        spinnerListener()
    }

    private fun initilize() {
        source_spinner = findViewById(R.id.source_spinner)
        target_spinner = findViewById(R.id.target_spinner)
        source_value = findViewById(R.id.source_value)
        target_value = findViewById(R.id.target_value)

        val currencies = resources.getStringArray(R.array.currencies_array)
        for (c1 in currencies) {
            for (c2 in currencies) {
                cache.put(c1+"_"+c2, 0.0)
            }
        }

        source_value.setFilters(arrayOf<InputFilter>(DigitsInputFilter(2)))
        target_value.setEnabled(false)

        target_spinner.setSelection(1)
        source_spinner.setSelection(0)

    }



    private fun spinnerListener() {
        source_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                convertAndSet(source_value, target_value)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
        target_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                convertAndSet(source_value, target_value)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
    }





    private fun addTextWatcher() {
        source_value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                convertAndSet(source_value, target_value)
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }





    private fun getExchangeRates() {
        val url1 = "https://free.currconv.com/api/v7/convert?q="
        val url2 = "&compact=ultra&apiKey=d50bb1b173479d255536"

        val url = url1+cache.keys.joinToString(",")+url2


        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url,null,
            Response.Listener<JSONObject> { response ->
                for(currencies in cache.keys) {
                    val rate = response.getString(currencies).toDouble()
                    cache.put(currencies, rate)
                }
            },
            Response.ErrorListener {  })

        queue.add(request)
    }



    private fun convertAndSet(first: EditText, second: EditText) {
        val currencies = source_spinner.selectedItem.toString() + "_" + target_spinner.selectedItem.toString()
        val first_text = first.text.toString()

        if (first_text == "") {
            second.setText("0")
        }
        else {
            val first_value = first_text.toDouble()
            val rate = cache.getOrDefault(currencies,0.0)
            val result = String.format("%.2f",(rate * first_value))
            second.setText(result)
        }

    }



    }
