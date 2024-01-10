package com.example.tshare.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.tshare.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class SearchForRideActivity : AppCompatActivity() {

    private  var rideFrom: EditText?=null
    private  var rideTo: EditText?=null
    private  var rideDate: EditText?=null
    private var searchBtn:Button?=null
    private var closebutton:FloatingActionButton?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_ride)

        rideFrom=findViewById(R.id.rideFrom)
        rideTo=findViewById(R.id.rideTo)
        rideDate=findViewById(R.id.rideDate)
        searchBtn=findViewById(R.id.ridesearchBtn)
        closebutton=findViewById(R.id.close_button)

        closebutton?.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }


            searchBtn?.setOnClickListener {
                if(rideFrom?.text.toString().isEmpty()|| rideTo?.text.toString().isEmpty() || rideDate?.text.toString().isEmpty()){
                    Toast.makeText(applicationContext,"Fill out all the fields", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this, rideSearchResultActivity::class.java)
                    intent.putExtra("rideFrom", rideFrom!!.text.toString())
                    intent.putExtra("rideTo", rideTo!!.text.toString())
                    intent.putExtra("rideDate", rideDate!!.text.toString())
                    startActivity(intent)
                }
            }
    }
    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                val sdf = SimpleDateFormat("dd/MM/yyyy")
                rideDate?.setText(sdf.format(selectedDate.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}