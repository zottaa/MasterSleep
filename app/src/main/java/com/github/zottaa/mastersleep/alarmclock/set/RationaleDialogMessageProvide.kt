package com.github.zottaa.mastersleep.alarmclock.set

interface RationaleDialogMessageProvide : DialogMessageProvide {
    class PostNotification : RationaleDialogMessageProvide {
        override fun message(): String =
            "This app require post notification permission to alarm clock work."

    }
    class ActivityRecognition : RationaleDialogMessageProvide {
        override fun message(): String =
            "This app require activity recognition permission to track your sleep."

    }
}

