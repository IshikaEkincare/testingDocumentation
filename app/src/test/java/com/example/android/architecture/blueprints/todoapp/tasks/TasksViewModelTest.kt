package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.annotation.Config

@Config(sdk = [28])
class TasksViewModelTest{

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var tasksRepository: FakeTestRepository


    @Before
    fun setUpViewModel(){
        tasksRepository = FakeTestRepository()
        val task1 = Task("title1","desc1")
        val task2 = Task("Title2","desc2")
        val task3 = Task("title3","desc3")
        tasksRepository.addTask(task1,task2,task3)
        tasksViewModel = TasksViewModel(tasksRepository)
    }


    @Test
    fun addNewTask_setsNewTaskEvent(){
        tasksViewModel.addNewTask()
        val value= tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible(){
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(),`is`(true))
    }

    @Test
    fun completeTask_snackBarAndDataUpdated(){
        val task = Task("title1","desc1")
        tasksRepository.addTask(task)

        tasksViewModel.completeTask(task,true)

        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        val snackBarText: Event<Int> = tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackBarText.getContentIfNotHandled(),`is`(R.string.task_marked_complete))

    }
}