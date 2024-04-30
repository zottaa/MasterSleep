package com.github.zottaa.mastersleep

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.textfield.TextInputEditText
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf.allOf

class CreateNotePage {
    private val root = R.id.diaryCreateRoot

    private fun root() = onView(
        CoreMatchers.allOf(
            withId(root)
        )
    )

    fun checkVisibleNow() {
        root().check(matches(isDisplayed()))
    }

    fun inputTitle(title: String) {
        onView(
            allOf(
                isAssignableFrom(TextInputEditText::class.java),
                withId(R.id.titleTextInputEditText)
            )
        ).perform(typeText(title), closeSoftKeyboard())
    }

    fun inputContent(content: String) {
        onView(
            allOf(
                isAssignableFrom(TextInputEditText::class.java),
                withId(R.id.contentTextInputEditText)
            )
        ).perform(typeText(content), closeSoftKeyboard())
    }

    fun save() {
        pressBack()
    }

    fun delete() {
        onView(
            withId(R.id.delete_menu_item)
        ).perform(click())
        onView(withText(R.string.delete))
            .inRoot(isDialog())
            .perform(click())
    }
}