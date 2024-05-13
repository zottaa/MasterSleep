package com.github.zottaa.mastersleep

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.textfield.TextInputEditText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.AllOf

class EditNotePage {
    private val root = R.id.diaryEditRoot

    private fun root() = onView(
        allOf(
            withId(root)
        )
    )

    fun checkVisibleNow() {
        root().check(matches(isDisplayed()))
    }

    fun inputTitle(title: String) {
        onView(
            AllOf.allOf(
                isAssignableFrom(TextInputEditText::class.java),
                withId(R.id.titleEditTextInputEditText)
            )
        ).perform(clearText(), typeText(title), closeSoftKeyboard())
    }

    fun inputContent(content: String) {
        onView(
            AllOf.allOf(
                isAssignableFrom(TextInputEditText::class.java),
                withId(R.id.contentEditTextInputEditText)
            )
        ).perform(clearText(), typeText(content), closeSoftKeyboard())
    }

    fun save() {
        Espresso.pressBack()
    }

    fun delete() {
        onView(
            withId(R.id.delete_menu_item)
        ).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.delete))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click())
    }
}