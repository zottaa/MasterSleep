package com.github.zottaa.mastersleep.core

import androidx.navigation.NavController

interface Screen {
    fun show(navController: NavController)

    abstract class Navigate(
        private val fragmentId: Int
    ) : Screen {
        override fun show(navController: NavController) {
            navController.navigate(fragmentId)
        }
    }
}