package com.github.zottaa.mastersleep

import android.content.Context
import androidx.room.Room
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.zottaa.mastersleep.core.AppDataBase
import com.github.zottaa.mastersleep.core.DataBaseModule
import com.github.zottaa.mastersleep.main.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Singleton

@RunWith(AndroidJUnit4::class)
@LargeTest
@UninstallModules(DataBaseModule::class)
@HiltAndroidTest
class DiaryTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class TestDataBaseModule {
        companion object {
            @Singleton
            @Provides
            fun provideAppDatabase(@ApplicationContext context: Context): AppDataBase =
                Room.inMemoryDatabaseBuilder(
                    context,
                    AppDataBase::class.java
                )
                    .allowMainThreadQueries()
                    .build()
        }
    }

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private fun goToDiary() {
        onView(
            withId(R.id.action_diary)
        ).perform(click())
    }

    @Before
    fun setup() {
        goToDiary()
    }

    @Test
    fun create_note() {
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