fun classesNeededFor75(
    conducted: Int,
    attended: Int
): Int {

    if (conducted == 0) return 0

    var x = 0

    while (
        ((attended + x).toFloat() /
                (conducted + x)) * 100 < 75
    ) {
        x++
    }

    return x
}

fun safeBunks(
    conducted: Int,
    attended: Int
): Int {

    if (conducted == 0) return 0

    var x = 0

    while (
        (attended.toFloat() /
                (conducted + x + 1)) * 100 >= 75
    ) {
        x++
    }

    return x
}