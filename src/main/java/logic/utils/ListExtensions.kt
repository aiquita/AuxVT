package logic.utils

fun <T> List<T>.with(element: T): List<T> {
    val mutable = this.toMutableList()
    mutable.add(element)
    return mutable
}

fun <T> List<T>.without(pos: Int): List<T> {
    val mutable = this.toMutableList()
    mutable.removeAt(pos)
    return mutable
}

fun <T> List<T>.findDuplicate(): T? {
    val previous = HashSet<T>()
    forEach {
        if (previous.contains(it))
            return it

        previous.add(it)
    }
    return null
}
