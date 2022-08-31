package ie.wit.tritrack.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ie.wit.tritrack.db.ExerciseDataBase
import ie.wit.tritrack.R
import ie.wit.tritrack.models.ExerciseModelClass

class AddExerciseLogFragment : Fragment() {

    lateinit var addExerciseLog: Button
    lateinit var addExerciseLogTypeOptions: RadioGroup
    lateinit var optionCycling: RadioButton
    lateinit var optionRunning: RadioButton
    lateinit var optionSwim: RadioButton
    lateinit var inputTime: EditText
    lateinit var exerciseModelClass: ExerciseDataBase
    lateinit var timePicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentView =
            inflater.inflate(R.layout.layout_fragment_add_exercise_log, container, false)

        addExerciseLog = fragmentView.findViewById(R.id.addExerciseLog)
        addExerciseLogTypeOptions = fragmentView.findViewById(R.id.addExerciseLogTypeOptions)
        inputTime = fragmentView.findViewById(R.id.inputTime)
        optionCycling = fragmentView.findViewById(R.id.optionCycling)
        optionRunning = fragmentView.findViewById(R.id.optionRunning)
        optionSwim = fragmentView.findViewById(R.id.optionSwim)
        timePicker = fragmentView.findViewById(R.id.timePicker)

        timePicker.minValue = 1
        timePicker.maxValue = 60

        exerciseModelClass = ExerciseDataBase(context)

        addExerciseLog.setOnClickListener({
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
            val result = exerciseModelClass.insertExerciseLog(ExerciseModelClass(-1, typeStr, time))
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