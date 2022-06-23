package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking
import kotlin.Exception


class FakeTestRepository : TasksRepository {

    val tasksServiceData: LinkedHashMap<String,Task> = LinkedHashMap()
    private val observableTasks = MutableLiveData<Result<List<Task>>>()
    private var shouldReturnError = false

    fun setReturnError(value: Boolean){
        shouldReturnError=value
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if(shouldReturnError){
            return Result.Error(Exception("Task exception"))
        }
        val data  = tasksServiceData.values.toList()
       return Result.Success(data)
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runBlocking{ refreshTasks() }
        return observableTasks
    }

    override suspend fun refreshTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
       if(shouldReturnError){
           return Result.Error(Exception("Task exception"))
       }
        tasksServiceData[taskId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("test exception"))
    }

    override suspend fun saveTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = task.copy(isCompleted = true)
        tasksServiceData[task.id] = completedTask
        refreshTasks()
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    fun addTask(vararg tasks: Task){
        for(task in tasks){
            tasksServiceData[task.id]=task
        }
        runBlocking { refreshTasks() }
    }
}