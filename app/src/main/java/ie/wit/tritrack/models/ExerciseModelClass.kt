package ie.wit.tritrack.models

class ExerciseModelClass {
    var exerciseLogID: Long = -1
    var exerciseType = ""
    var exerciseTime: Int = 0

    constructor() {}
    constructor(
        exerciseLogID: Long,
        exerciseType: String,
        exerciseTime: Int,
    ) {
        this.exerciseLogID = exerciseLogID
        this.exerciseType = exerciseType
        this.exerciseTime = exerciseTime
    }
}