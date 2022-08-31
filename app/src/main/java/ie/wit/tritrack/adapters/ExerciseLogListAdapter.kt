package ie.wit.tritrack.adapters

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ie.wit.tritrack.db.ExerciseDataBase
import ie.wit.tritrack.models.ExerciseModelClass
import ie.wit.tritrack.R

class ExerciseLogListAdapter(
    private val context: Context,
    private val exerciseDataBase: ExerciseDataBase,
    private val exerciseModelsList: ArrayList<ExerciseModelClass>
) : RecyclerView.Adapter<
        ExerciseLogListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_log_list_template, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exerciseModelClass = exerciseModelsList[position]
        holder.exerciseType.text =
            "Exercise Type: " + exerciseModelClass.exerciseType

        holder.exerciseTime.text =
            "Exercise Time: " + exerciseModelClass.exerciseTime.toString() + " Minutes"

        holder.tvDelete.setTag(position)
        holder.tvEdit.setTag(position)

        holder.tvDelete.setOnClickListener {
            val clickedPosition = it.getTag() as Int
            exerciseDataBase.deleteExerciseLog(exerciseModelsList.get(clickedPosition).exerciseLogID)
            exerciseModelsList.removeAt(position)
            notifyDataSetChanged()
        }
        holder.tvEdit.setOnClickListener {
            val clickedPosition = it.getTag() as Int
            val alert = AlertDialog.Builder(context)
            val inputTime = EditText(context)
            inputTime.inputType=InputType.TYPE_CLASS_NUMBER
            alert.setTitle("Edit Exercise Time (Minutes)")

            alert.setView(inputTime)
            inputTime.setText(exerciseModelsList.get(clickedPosition).exerciseTime.toString())

            alert.setPositiveButton("Update",
                { dialog, whichButton -> //What ever you want to do with the value
                    val timeStr = inputTime.text.toString().trim()
                    if (timeStr.isEmpty()) {
                        Toast.makeText(context, "Please enter time", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    val time = timeStr.toInt()
                    if (time < 1) {
                        Toast.makeText(context, "Please enter valid time", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    exerciseModelsList.get(clickedPosition).exerciseTime = time
                    exerciseDataBase.updateExerciseLog(exerciseModelsList.get(clickedPosition))
                    notifyDataSetChanged()
                })

            alert.setNegativeButton("Cancel",
                { dialog, whichButton ->
                    dialog.dismiss()
                })
            alert.show()
        }
    }

    override fun getItemCount(): Int {
        return exerciseModelsList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val exerciseType: TextView = itemView.findViewById(R.id.exerciseType)
        val exerciseTime: TextView = itemView.findViewById(R.id.exerciseTime)
        val tvEdit: TextView = itemView.findViewById(R.id.tvEdit)
        val tvDelete: TextView = itemView.findViewById(R.id.tvDelete)
    }
}
