package com.github.zottaa.mastersleep.alarmclock.set

interface DenialDialogMessageProvide : DialogMessageProvide {
    class PostNotification : DenialDialogMessageProvide {
        override fun message(): String =
            "This feature is unavailable because it requires post notification permission\n" +
                                    "Please allow post notification permission from settings to proceed further"

    }
    class ActivityRecognition : DenialDialogMessageProvide {
        override fun message(): String =
            "This feature is unavailable because it requires activity recognition permission\n" +
                    "Please allow activity recognition permission from settings to proceed further"

    }
}