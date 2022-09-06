package ie.wit.tritrack.models

class ExerciseModelClass {
    var exerciseLogID: Long = -1

    var exerciseType = ""
    var exerciseTime: Int = 0
    var userName = ""
    var exerciseDate = ""

    constructor() {}
    constructor(
        exerciseLogID: Long,
        userName: String,
        exerciseDate: String,
        exerciseType: String,
        exerciseTime: Int,
    ) {
        this.exerciseLogID = exerciseLogID

        this.exerciseType = exerciseType
        this.exerciseTime = exerciseTime
        this.userName = userName
        this.exerciseDate = exerciseDate
    }
}