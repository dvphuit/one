package dvp.data.youtube.extension

fun Long?.orZero() = this ?: 0L
fun Double?.orZero() = this ?: 0.0
fun Boolean?.orFalse() = this ?: false
fun String?.orEmpty() = this ?: ""

fun String?.longOrZero() = this?.toLongOrNull() ?: 0L

fun Boolean?.isTrue() = this == true