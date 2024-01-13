package com.example.tshare.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.tshare.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class updateRideActivity : AppCompatActivity() {

    private lateinit var closebutton: FloatingActionButton
    private lateinit var fromEDT: EditText
    private lateinit var toEDT: EditText
    private lateinit var dateEDT: EditText
    private lateinit var timeEDT: EditText
    private lateinit var preferenceEDT: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var publishBTN: Button
    var privacy: String?=null
    var timeZone: String?=null
    var timeOfTravel:String?=null
    private var isTimeValid = false

    private fun isDateValid(selectedDate: Calendar): Boolean {
        val currentDate = Calendar.getInstance()

        // Set both calendars to midnight to compare dates only, not time
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)

        selectedDate.set(Calendar.HOUR_OF_DAY, 0)
        selectedDate.set(Calendar.MINUTE, 0)
        selectedDate.set(Calendar.SECOND, 0)
        selectedDate.set(Calendar.MILLISECOND, 0)

        return !selectedDate.before(currentDate)
    }

    private fun parseSelectedDate(dateString: String): Calendar? {
        val selectedDate = Calendar.getInstance()
        try {
            selectedDate.time = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString)
        } catch (e: ParseException) {
            // Handle the parse exception, e.g., log it or show an error message
            return null
        }
        return selectedDate
    }



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
        setContentView(R.layout.activity_update_ride)

        closebutton= findViewById(R.id.close_button)
        fromEDT= findViewById(R.id.from)
        toEDT=findViewById(R.id.to)
        dateEDT=findViewById(R.id.date)
        timeEDT=findViewById(R.id.Time)
        preferenceEDT=findViewById(R.id.who)
        checkBox=findViewById(R.id.visibility)
        publishBTN=findViewById(R.id.publishButton)

        closebutton?.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                privacy = "yes"
            } else {
                privacy = "no"
            }
        }
        dateEDT.addTextChangedListener(tw)


        val timeSpinner=findViewById<Spinner>(R.id.amOrpm)
        val times= arrayOf("AM","PM")
        val arrayAdp= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,times)
        timeSpinner.adapter=arrayAdp
        timeSpinner?.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                timeZone=  times[p2]
                val txt=findViewById<TextView>(R.id.selectedAmPm)
                txt?.setText(timeZone)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@updateRideActivity, "Please select AM or PM", Toast.LENGTH_SHORT).show()
            }
        }

        dateEDT.setOnClickListener {
            showDatePicker()
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
            val privacy=privacy

            if (time != null) {
                if (from.isEmpty() || to.isEmpty() || date.isEmpty() || time.isEmpty() || preference.isEmpty()) {
                    Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val selectedDate = parseSelectedDate(date)
            if (selectedDate != null && !isDateValid(selectedDate)) {
                // Show an error message if the selected date is before the current date
                Toast.makeText(this, "Selected date must be greater than or equal to the current date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            validateTimeFormat(timeEDT.text.toString())
            if (!isTimeValid) {
                Toast.makeText(this, "Invalid time. Please use HH:MM", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userId=intent.getStringExtra("userId")
            val itemId=intent.getStringExtra("itemId")

            if (itemId != null) {
                val databaseReference = FirebaseDatabase.getInstance().getReference("seatAvailability").child(itemId)
                val updatedData = mapOf(
                    "from" to from,
                    "to" to to,
                    "date" to date,
                    "time" to time,
                    "preference" to preference,
                    "timeZone" to timeZone,
                    "userId" to userId,
                    "privacy" to privacy
                    // Add other fields as needed
                )

                databaseReference.setValue(updatedData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
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
                isTimeValid = false
            } else if (minutes < 0 || minutes > 59) {
                // Invalid minutes
                isTimeValid = false
            } else {
                // Valid time format
                this.timeOfTravel = time
                isTimeValid = true
            }
        } else {
            // Invalid time format
            isTimeValid = false
        }
    }

    private fun showDatePicker() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Check if the selected date is not before the current date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                if (!selectedDate.before(currentDate)) {
                    // Format the selected date and update the EditText
                    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                    dateEDT.setText(formattedDate)
                } else {
                    // Show an error message or handle the invalid date
                    Toast.makeText(this, "Please select a date on or after the current date", Toast.LENGTH_SHORT).show()
                }
            },
            year,
            month,
            day
        )

        datePicker.show()
    }
}