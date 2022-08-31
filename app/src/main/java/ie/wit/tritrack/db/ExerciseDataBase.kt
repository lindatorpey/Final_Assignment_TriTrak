package ie.wit.tritrack.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ie.wit.tritrack.models.ExerciseModelClass

class ExerciseDataBase(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "Create Table " + EXERCISE_LOG_TABLE + "(" + EXERCISE_ID_COL + " integer primary key autoincrement," +
                    EXERCISE_TYPE_COL + " text," + EXERCISE_TIME_COL + " integer)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_LOG_TABLE)
        onCreate(db)
    }

    fun insertExerciseLog(exerciseModelClass: ExerciseModelClass): Long {
        val db = writableDatabase
        val values = ContentValues()
        var result: Long = 0
        values.put(EXERCISE_TYPE_COL, exerciseModelClass.exerciseType)
        values.put(EXERCISE_TIME_COL, exerciseModelClass.exerciseTime)
        result = db.insert(EXERCISE_LOG_TABLE, null, values)
        db.close()
        return result
    }

    fun updateExerciseLog(exerciseModelClass: ExerciseModelClass): Long {
        val db = writableDatabase
        val values = ContentValues()
        var result: Long = 0
        values.put(EXERCISE_TYPE_COL, exerciseModelClass.exerciseType)
        values.put(EXERCISE_TIME_COL, exerciseModelClass.exerciseTime)
        result = db.update(
            EXERCISE_LOG_TABLE,
            values,
            EXERCISE_ID_COL + "= ?",
            arrayOf(exerciseModelClass.exerciseLogID.toString())
        )
            .toLong()
        db.close()
        return result
    }

    val allExercise: ArrayList<ExerciseModelClass>
        get() {
            val db = writableDatabase
            val exerciseModels = ArrayList<ExerciseModelClass>()
            var exerciseModelClass: ExerciseModelClass? = null
            val itemsCursor = db.rawQuery(
                "SELECT * FROM " + EXERCISE_LOG_TABLE + " Order BY " + EXERCISE_ID_COL + " DESC",
                null
            )
            while (itemsCursor.moveToNext()) {
                exerciseModelClass = ExerciseModelClass()
                exerciseModelClass.exerciseLogID = itemsCursor.getLong(0)
                exerciseModelClass.exerciseType = itemsCursor.getString(1)
                exerciseModelClass.exerciseTime = itemsCursor.getInt(2)
                exerciseModels.add(exerciseModelClass)
            }
            itemsCursor.close()
            db.close()
            return exerciseModels
        }

    fun deleteExerciseLog(exerciseLogID: Long): Boolean {
        val db = writableDatabase
        val effectedRowsCount =
            db.delete(
                EXERCISE_LOG_TABLE,
                EXERCISE_ID_COL + "= ?",
                arrayOf(exerciseLogID.toString())
            )
        db.close()
        return effectedRowsCount > 0
    }

    companion object {
        //database
        private const val DATABASE_NAME = "ExerciseLog.db"

        //tables
        private const val EXERCISE_LOG_TABLE = "exerciseLog"

        //columns
        private const val EXERCISE_ID_COL = "exerciseID"
        private const val EXERCISE_TYPE_COL = "exerciseType"
        private const val EXERCISE_TIME_COL = "exerciseTime"
    }
}