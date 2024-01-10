package com.example.tshare.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tshare.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class SearchForCompanionActivity : AppCompatActivity() {

    private  var taxiFrom: EditText?=null
    private  var taxiTo: EditText?=null
    private  var taxiDate: EditText?=null
    private var searchBtn: Button?=null
    private var closebutton: FloatingActionButton?=null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_companion)

        taxiFrom=findViewById(R.id.taxiFrom)
        taxiTo=findViewById(R.id.taxiTo)
        taxiDate=findViewById(R.id.taxiDate)
        searchBtn=findViewById(R.id.taxisearchBtn)
        closebutton=findViewById(R.id.close_button)

        closebutton?.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        searchBtn?.setOnClickListener {
            if(taxiFrom?.text.toString().isEmpty()|| taxiTo?.text.toString().isEmpty() || taxiDate?.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Fill out all the fields", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, taxiSearchResultActivity::class.java)
                intent.putExtra("taxiFrom", taxiFrom!!.text.toString())
                intent.putExtra("taxiTo", taxiTo!!.text.toString())
                intent.putExtra("taxiDate", taxiDate!!.text.toString())
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
                taxiDate?.setText(sdf.format(selectedDate.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}