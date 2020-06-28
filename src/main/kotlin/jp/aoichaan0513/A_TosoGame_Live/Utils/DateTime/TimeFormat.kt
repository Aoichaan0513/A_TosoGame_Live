package jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime

class TimeFormat {
    companion object {

        fun format(time: Int): String {
            var m = 0
            var s = time
            if (s >= 60) {
                m = s / 60
                s -= m * 60
            }
            var sm = m.toString()
            var ss = s.toString()
            if (m <= 9) sm = "0$sm"
            if (s <= 9) ss = "0$ss"
            return "$sm:$ss"
        }

        fun formatMin(time: Int): Int {
            val splited = format(time).split(":")
            return (if (splited[0].startsWith("0")) splited[0].substring(splited[0].length - 1) else splited[0]).toInt()
        }

        fun formatSec(time: Int): Int {
            val splited = format(time).split(":")
            return (if (splited[1].startsWith("0")) splited[1].substring(splited[1].length - 1) else splited[1]).toInt()
        }

        fun formatJapan(time: Int): String {
            return "${format(time).replace(":", "分")}秒"
        }
    }
}