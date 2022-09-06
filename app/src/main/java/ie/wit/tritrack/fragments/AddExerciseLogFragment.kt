package ie.wit.tritrack.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import ie.wit.tritrack.db.ExerciseDataBase
import ie.wit.tritrack.R
import ie.wit.tritrack.helpers.FilePathHelper
import ie.wit.tritrack.models.ExerciseModelClass
import org.w3c.dom.Text
import java.io.File
import java.io.IOException
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
    var exerciseImagePath: String = ""
    lateinit var imageExercise: ImageView
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
        imageExercise = fragmentView.findViewById(R.id.imageExercise)
        datePicker = fragmentView.findViewById(R.id.datePicker)
        inputTime = fragmentView.findViewById(R.id.inputTime)
        optionCycling = fragmentView.findViewById(R.id.optionCycling)
        optionRunning = fragmentView.findViewById(R.id.optionRunning)
        optionSwim = fragmentView.findViewById(R.id.optionSwim)
        timePicker = fragmentView.findViewById(R.id.timePicker)




        timePicker.minValue = 1
        timePicker.maxValue = 60

        exerciseModelClass = ExerciseDataBase(context)

        imageExercise.setOnClickListener({
            val alert = AlertDialog.Builder(context)
            val inputTime = EditText(context)
            inputTime.inputType = InputType.TYPE_CLASS_NUMBER
            alert.setTitle("Select Picture Mode")

            alert.setPositiveButton("Camera",
                { dialog, whichButton -> //What ever you want to do with the value
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(
                                requireActivity(),
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(
                                requireActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            dispatchTakePictureIntent()
                        } else {
                            requestPermissions(
                                listOf<String>(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ).toTypedArray(), 111
                            )
                        }
                    }
                })

            alert.setNegativeButton("Gallery",
                { dialog, whichButton ->
                    val gallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(gallery, 333)
                })
            alert.show()
        })

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
            val result = exerciseModelClass.insertExerciseLog(ExerciseModelClass(-1, nameStr,dateStr,exerciseImagePath, typeStr, time))
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            grantResults[1] == PackageManager.PERMISSION_GRANTED &&
            grantResults[2] == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(context, "Please allow all permissions", Toast.LENGTH_SHORT)
                .show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        ) as File
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            exerciseImagePath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "ie.wit.tritrack.new.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 222)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 222) {
            imageExercise.setImageURI(Uri.fromFile(File(exerciseImagePath)))
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 333) {
            val uri = data!!.data
            exerciseImagePath = FilePathHelper.getPathFromUri(requireContext(), uri!!) as String
            imageExercise.setImageURI(uri)
        }
    }
}

