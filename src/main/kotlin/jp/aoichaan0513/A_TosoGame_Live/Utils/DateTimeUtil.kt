package jp.aoichaan0513.A_TosoGame_Live.Utils

class DateTimeUtil {
    companion object {

        fun formatTimestamp(i: Int, isMilliSeconds: Boolean = false): DataClass.Timestamp {
            return formatTimestamp(i.toLong(), isMilliSeconds)
        }

        fun formatTimestamp(l: Long, isMilliSeconds: Boolean = false): DataClass.Timestamp {
            return DataClass.Timestamp(
                    if (isMilliSeconds) l / (1000 * 60 * 60) % 24 else l / 3600,
                    if (isMilliSeconds) l / (1000 * 60) % 60 else (l % 3600) / 60,
                    if (isMilliSeconds) l / 1000 % 60 else l % 60
            )
        }
    }
}