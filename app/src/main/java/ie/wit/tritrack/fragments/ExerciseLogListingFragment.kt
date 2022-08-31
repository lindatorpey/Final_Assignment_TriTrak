package ie.wit.tritrack.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.tritrack.db.ExerciseDataBase
import ie.wit.tritrack.R
import ie.wit.tritrack.adapters.ExerciseLogListAdapter
import ie.wit.tritrack.models.ExerciseModelClass

class ExerciseLogListingFragment : Fragment() {

     var exercisesModels = ArrayList<ExerciseModelClass>()
     lateinit var exerciseLogListAdapter: ExerciseLogListAdapter
     lateinit var exerciseDataBase: ExerciseDataBase
     lateinit var recylerViewExerciseLogListing: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentView =
            inflater.inflate(R.layout.layout_fragment_exercise_log_list, container, false)
        recylerViewExerciseLogListing = fragmentView.findViewById(R.id.recylerViewExerciseLogListing)

        exerciseDataBase = ExerciseDataBase(activity)
        exercisesModels = exerciseDataBase.allExercise

        exerciseLogListAdapter =
            ExerciseLogListAdapter(
                requireActivity(),
                exerciseDataBase, exercisesModels
            )
        recylerViewExerciseLogListing.adapter = exerciseLogListAdapter
        recylerViewExerciseLogListing.layoutManager=LinearLayoutManager(context)
        recylerViewExerciseLogListing.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        return fragmentView
    }
}