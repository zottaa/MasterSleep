package com.github.zottaa.mastersleep

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.hamcrest.CoreMatchers.allOf

class ListNotePage {
    private val recyclerViewMatcher = RecyclerViewMatcher(R.id.notesRecyclerView)
    private val root = R.id.diaryListRoot

    private fun root() = onView(
        allOf(
            withId(root)
        )
    )

    fun checkVisibleNow() {
        root().check(matches(isDisplayed()))
    }

    fun clickAddButton() {
        onView(
            allOf(
                withId(R.id.addButton),
                isAssignableFrom(FloatingActionButton::class.java),
                withParent(isAssignableFrom(ConstraintLayout::class.java)),
                withParent(withId(R.id.diaryListRoot))
            )
        ).perform(click())
    }

    fun checkNote(position: Int, title: String, content: String) {
        val noteRoot = R.id.noteRoot
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(withId(noteRoot)),
                recyclerViewMatcher.atPosition(position, R.id.titleTextView)
            )
        ).check(matches(withText(title)))
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(withId(noteRoot)),
                recyclerViewMatcher.atPosition(position, R.id.contentTextView)
            )
        ).check(matches(withText(content)))
    }

    fun checkNoNote(position: Int) {
        val noteRoot = R.id.noteRoot
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(withId(noteRoot)),
                recyclerViewMatcher.atPosition(position, R.id.titleTextView)
            )
        ).check(doesNotExist())
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(withId(noteRoot)),
                recyclerViewMatcher.atPosition(position, R.id.contentTextView)
            )
        ).check(doesNotExist())
    }

    fun clickAtNote(position: Int) {
        onView(recyclerViewMatcher.atPosition(position)).perform(click())
    }
}