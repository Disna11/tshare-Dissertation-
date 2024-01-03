package com.example.tshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class bookTaxiActivity : AppCompatActivity() {

    private lateinit var closebutton: FloatingActionButton
    private lateinit var fromEDT: EditText
    private lateinit var toEDT: EditText
    private lateinit var dateEDT: EditText
    private lateinit var timeEDT: EditText
    private lateinit var preferenceEDT: EditText
    private lateinit var publishBTN: Button
    var privacy: String?=null
    var timeZone: String?=null
    var timeOfTravel:String?=null


    val tw: TextWatcher = object : TextWatcher {
        private var current = ""
        private val ddmmyyyy = "DDMMYYYY"
        private val cal = Calendar.getInstance()
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // No specific implementation for beforeTextChanged
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString() != current) {
                var clean = s.toString().replace("[^\\d.]|\\.".toRegex(), "")
                val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")

                var cl = clean.length
                var sel = cl
                var i = 2
                while (i <= cl && i < 6) {
                    sel++
                    i += 2
                }
                // Fix for pressing delete next to a forward slash
                if (clean == cleanC) sel--

                if (clean.length < 8) {
                    clean += ddmmyyyy.substring(clean.length)
                } else {
                    // This part makes sure that when we finish entering numbers
                    // the date is correct, fixing it otherwise
                    var day = clean.substring(0, 2).toInt()
                    var mon = clean.substring(2, 4).toInt()
                    var year = clean.substring(4, 8).toInt()

                    mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                    cal[Calendar.MONTH] = mon - 1
                    year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                    cal[Calendar.YEAR] = year
                    // ^ first set year for the line below to work correctly
                    // with leap years - otherwise, date e.g. 29/02/2012
                    // would be automatically corrected to 28/02/2012

                    day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                        Calendar.DATE) else day
                    clean = String.format("%02d%02d%02d", day, mon, year)
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8))

                sel = if (sel < 0) 0 else sel
                current = clean
                dateEDT.setText(current)
                dateEDT.setSelection(if (sel < current.length) sel else current.length)
            }
        }


        override fun afterTextChanged(s: Editable) {
            // Implementation of afterTextChanged
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_taxi)

        closebutton= findViewById(R.id.close_button)
        fromEDT= findViewById(R.id.tFrom)
        toEDT=findViewById(R.id.tTo)
        dateEDT=findViewById(R.id.tDate)
        timeEDT=findViewById(R.id.tTime)
        preferenceEDT=findViewById(R.id.tWho)
        publishBTN=findViewById(R.id.tPublishButton)

        closebutton?.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        dateEDT.addTextChangedListener(tw)

        val timeSpinner=findViewById<Spinner>(R.id.amOrpm)
        val times= arrayOf("AM","PM")
        val arrayAdp= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,times)
        timeSpinner.adapter=arrayAdp
        timeSpinner?.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                timeZone=  times[p2]
                val txt=findViewById<TextView>(R.id.tSelectedAmPm)
                txt?.setText(timeZone)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@bookTaxiActivity, "Please select AM or PM", Toast.LENGTH_SHORT).show()
            }
        }

        // Set a TextWatcher to monitor text changes
        timeEDT.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No specific implementation beforeTextChanged
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No specific implementation onTextChanged
            }

            override fun afterTextChanged(s: Editable?) {
                validateTimeFormat(s.toString())
            }
        })

        publishBTN?.setOnClickListener {
            val from = fromEDT.text.toString()
            val to = toEDT.text.toString()
            val date = dateEDT.text.toString()
            val time = timeOfTravel
            val preference = preferenceEDT.text.toString()

            if (from.isEmpty() || to.isEmpty() || date.isEmpty() || time.isNullOrEmpty() || preference.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedDate = Calendar.getInstance()
            selectedDate.time = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date)

            val currentDate = Calendar.getInstance()

            if (selectedDate.before(currentDate)) {
                // Show an error message if the selected date is before the current date
                Toast.makeText(this, "Selected date must be greater than or equal to the current date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val database = FirebaseDatabase.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                val userReference = database.getReference("taxiRequests")

                // Create a data class or use a Map to store values
                val bookingData = taxi(from, to, date, time, preference,  timeZone ,userId)

                // Use setValue or updateChildren to push data to the database
                userReference.push().setValue(bookingData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Booking data added to the database", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to add booking data", Toast.LENGTH_SHORT).show()
                        }
                    }
            }


        }
    }

    private fun validateTimeFormat(time: String) {
        val timePattern = Regex("^([0-1]?[0-9]|2[0-3]):([0-5][0-9])$")

        if (timePattern.matches(time)) {
            val hours = time.substring(0, 2).toInt()
            val minutes = time.substring(3, 5).toInt()

            if ((timeZone == "AM" && (hours < 1 || hours > 12)) ||
                (timeZone == "PM" && (hours < 1 || hours > 12))) {
                // Invalid hours for the selected AM/PM
                Toast.makeText(this, "Invalid hours for the selected AM/PM. Please use HH:MM", Toast.LENGTH_SHORT).show()
            } else if (minutes < 0 || minutes > 59) {
                // Invalid minutes
                Toast.makeText(this, "Invalid minutes. Please use HH:MM", Toast.LENGTH_SHORT).show()
            } else {
                // Valid time format
                this.timeOfTravel = time
            }
        } else {
            // Invalid time format
            Toast.makeText(this, "Invalid time format. Please use HH:MM", Toast.LENGTH_SHORT).show()
        }
    }
}