package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.Result.Success
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.hamcrest.core.IsEqual
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class DefaultTasksRepositoryTest{

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val task1 = Task("Title1","desc1")
    private val task2 = Task("Title2","desc2")
    private val task3 = Task("Title3","desc3")
    private val remoteTasks = listOf(task1,task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }

    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource

    private lateinit var tasksRepository: DefaultTasksRepository

    @Before
    fun createRepository(){
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
        tasksRepository = DefaultTasksRepository(
            tasksRemoteDataSource,
            tasksLocalDataSource,
            Dispatchers.Main
        )
    }


    @Test
    fun getTasks_requestAllTaskFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        val tasks = tasksRepository.getTasks(true) as Success
        assertThat(tasks.data,IsEqual(remoteTasks))
    }
}