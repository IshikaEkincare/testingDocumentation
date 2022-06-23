package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

class StatisticsUtilsTest{

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero(){
        val tasks = listOf<Task>(
            Task("title","desc", isCompleted = false)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.activeTasksPercent, `is`(100f))
        assertThat(result.completedTasksPercent,`is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_noActive_returnsZeroHundred(){
        val tasks = listOf<Task>(
            Task("title","desc",isCompleted = true)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.activeTasksPercent,`is`(0f))
        assertThat(result.completedTasksPercent,`is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_twoCompletedThreeActive_returnsFortySixty(){
        val tasks = listOf<Task>(
            Task("title","desc",isCompleted = true),
            Task("completed","completed",isCompleted = true),
            Task("pending","pending",isCompleted = false),
            Task("working","working",isCompleted = false),
            Task("going on","going on",isCompleted = false)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.completedTasksPercent,`is`(40f))
        assertThat(result.activeTasksPercent,`is`(60f))
    }

    @Test
    fun getActiveAndCompletedStats_NoActiveNoCompleted_returnZeroBoth(){
        val tasks = emptyList<Task>()
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.activeTasksPercent,`is`(0f))
        assertThat(result.completedTasksPercent,`is`(0f))
    }
}