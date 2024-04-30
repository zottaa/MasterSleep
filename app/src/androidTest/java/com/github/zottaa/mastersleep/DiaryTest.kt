package com.github.zottaa.mastersleep

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.zottaa.mastersleep.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiaryTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private fun goToDiary() {
        onView(
            withId(R.id.action_diary)
        ).perform(click())
    }

    @Test
    fun create_note() {
        goToDiary()
        val noteListPage = ListNotePage()
        noteListPage.checkVisibleNow()
        noteListPage.clickAddButton()
        val createNotePage = CreateNotePage()
        createNotePage.checkVisibleNow()

        createNotePage.inputTitle("Title")
        createNotePage.inputContent("Content")

        activityScenarioRule.scenario.recreate()

        createNotePage.save()

        noteListPage.checkVisibleNow()
        noteListPage.checkNote(0, "Title", "Content")

        activityScenarioRule.scenario.recreate()

        noteListPage.checkNote(0, "Title", "Content")

    }

    @Test
    fun cancel_note_creation() {
        goToDiary()
        val noteListPage = ListNotePage()
        noteListPage.checkVisibleNow()
        noteListPage.clickAddButton()
        val createNotePage = CreateNotePage()
        createNotePage.checkVisibleNow()

        createNotePage.inputTitle("Title")
        createNotePage.inputContent("Content")
        createNotePage.delete()

        noteListPage.checkVisibleNow()
        noteListPage.checkNoNote(0)
    }

    @Test
    fun create_two_notes() {
        goToDiary()
        val noteListPage = ListNotePage()
        noteListPage.checkVisibleNow()
        noteListPage.clickAddButton()
        val createNotePage = CreateNotePage()
        createNotePage.checkVisibleNow()

        createNotePage.inputTitle("Title")
        createNotePage.inputContent("Content")
        createNotePage.save()

        noteListPage.checkVisibleNow()

        noteListPage.clickAddButton()

        createNotePage.checkVisibleNow()
        createNotePage.inputTitle("Title 2")
        createNotePage.inputContent("Content 2")
        createNotePage.save()

        noteListPage.checkVisibleNow()
        noteListPage.checkNote(0, "Title", "Content")
        noteListPage.checkNote(1, "Title 2", "Content 2")
    }

    @Test
    fun create_update_note() {
        goToDiary()
        val noteListPage = ListNotePage()
        noteListPage.checkVisibleNow()
        noteListPage.clickAddButton()
        val createNotePage = CreateNotePage()
        createNotePage.checkVisibleNow()

        createNotePage.inputTitle("Title")
        createNotePage.inputContent("Content")

        activityScenarioRule.scenario.recreate()

        createNotePage.save()

        noteListPage.checkVisibleNow()
        noteListPage.checkNote(0, "Title", "Content")

        activityScenarioRule.scenario.recreate()

        noteListPage.checkNote(0, "Title", "Content")

        noteListPage.clickAtNote(0)

        val editNotePage = EditNotePage()
        editNotePage.checkVisibleNow()
        editNotePage.inputTitle("New Title")
        editNotePage.inputContent("New Content")

        activityScenarioRule.scenario.recreate()

        editNotePage.save()

        noteListPage.checkVisibleNow()
        noteListPage.checkNote(0, "New Title", "New Content")
    }

    @Test
    fun create_delete_note() {
        goToDiary()
        val noteListPage = ListNotePage()
        noteListPage.checkVisibleNow()
        noteListPage.clickAddButton()
        val createNotePage = CreateNotePage()
        createNotePage.checkVisibleNow()

        createNotePage.inputTitle("Title")
        createNotePage.inputContent("Content")

        activityScenarioRule.scenario.recreate()

        createNotePage.save()

        noteListPage.checkVisibleNow()
        noteListPage.checkNote(0, "Title", "Content")

        activityScenarioRule.scenario.recreate()

        noteListPage.checkNote(0, "Title", "Content")

        noteListPage.clickAtNote(0)

        val editNotePage = EditNotePage()
        editNotePage.checkVisibleNow()
        editNotePage.delete()

        noteListPage.checkVisibleNow()
        noteListPage.checkNoNote(0)
    }
}