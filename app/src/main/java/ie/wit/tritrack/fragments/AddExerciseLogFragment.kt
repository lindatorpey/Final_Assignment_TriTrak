package ie.wit.tritrack.fragments

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ie.wit.tritrack.db.ExerciseDataBase
import ie.wit.tritrack.R
import ie.wit.tritrack.models.ExerciseModelClass
import org.w3c.dom.Text
import java.util.*

class AddExerciseLogFragment : Fragment() {

    lateinit var addExerciseLog: Button
    lateinit var addExerciseLogTypeOptions: RadioGroup
    lateinit var optionCycling: RadioButton
    lateinit var optionRunning: RadioButton
    lateinit var optionSwim: RadioButton
    lateinit var inputTime: EditText
    lateinit var exerciseModelClass: ExerciseDataBase
    lateinit var timePicker: NumberPicker
    lateinit var userName: EditText
    lateinit var datePicker:TextView
    var selectedDay = 0
    var selectedMonth = 0
    var selectedYear = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentView =
            inflater.inflate(R.layout.layout_fragment_add_exercise_log, container, false)


        addExerciseLog = fragmentView.findViewById(R.id.addExerciseLog)
        addExerciseLogTypeOptions = fragmentView.findViewById(R.id.addExerciseLogTypeOptions)
        userName = fragmentView.findViewById(R.id.userName)
        datePicker = fragmentView.findViewById(R.id.datePicker)
        inputTime = fragmentView.findViewById(R.id.inputTime)
        optionCycling = fragmentView.findViewById(R.id.optionCycling)
        optionRunning = fragmentView.findViewById(R.id.optionRunning)
        optionSwim = fragmentView.findViewById(R.id.optionSwim)
        timePicker = fragmentView.findViewById(R.id.timePicker)




        timePicker.minValue = 1
        timePicker.maxValue = 60

        exerciseModelClass = ExerciseDataBase(context)

        datePicker.setOnClickListener({
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    selectedYear = year
                    selectedMonth = month + 1
                    selectedDay = dayOfMonth
                    datePicker.setText("" + selectedDay + "-" + selectedMonth + "-" + selectedYear)
                },
                year,
                month,
                day
            )

            dpd.show()
        })



        addExerciseLog.setOnClickListener({
            val nameStr = userName.text.toString().trim()
            if (nameStr.isEmpty()) {
                Toast.makeText(context, "Please enter name of exercise", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val dateStr = datePicker.text.toString().trim()
            if (dateStr.isEmpty()) {
                Toast.makeText(context, "Please select date of exercise", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            var timeStr = inputTime.text.toString().trim()
            if (timeStr.isEmpty()) {
                timeStr = timePicker.value.toString()
            }
/*
            if (timeStr.isEmpty()) {
                Toast.makeText(context, "Please enter time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
*/
            val time = timeStr.toInt()
            if (time < 1 || time > 60) {
                Toast.makeText(context, "Time should be between 1 and 61", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            var typeStr = ""
            if (optionCycling.isChecked) {
                typeStr = "Cycling"
            }
            if (optionRunning.isChecked) {
                typeStr = "Running"
            }
            if (optionSwim.isChecked) {
                typeStr = "Swimming"
            }
            val result = exerciseModelClass.insertExerciseLog(ExerciseModelClass(-1, nameStr,dateStr, typeStr, time))
            if (result > 0) {
                inputTime.setText("")
                Toast.makeText(context, "Exercise Log created", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error while creating Exercise Log", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        return fragmentView;
    }

}