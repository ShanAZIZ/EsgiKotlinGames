package fr.esgi.games.utils
class Utils {
    companion object{
        fun convertArrayList(arrayList: ArrayList<*>): ArrayList<Int> {
            val intArrayList = ArrayList<Int>()
            for (item in arrayList) {
                val i = item as Long
                intArrayList.add(i.toInt())
            }
            return intArrayList
        }
    }
}