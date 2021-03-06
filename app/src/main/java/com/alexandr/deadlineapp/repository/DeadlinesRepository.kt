package com.alexandr.deadlineapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexandr.deadlineapp.repository.database.AppDatabase
import com.alexandr.deadlineapp.repository.database.entity.Deadline
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DeadlinesRepository(appDatabase: AppDatabase): CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    val foundData: MutableLiveData<List<Deadline>> = MutableLiveData()

    private val job : Job = Job()

    private var deadlineDAO = appDatabase.getDeadlinesDAO()

    fun getDeadlines(): LiveData<List<Deadline>> {
        return deadlineDAO.getAllLive()
    }

    fun getDeadlinesCurrent(): LiveData<List<Deadline>> {
        return deadlineDAO.getAllCurrentLive()
    }

    fun findItem(name: String) {
        launch(Dispatchers.Main) {
            foundData.value = withContext(Dispatchers.IO) {
                deadlineDAO.findDeadline(name)
            }
        }
    }

    fun insertDeadline(deadline: Deadline) {
        launch(Dispatchers.IO) {
            deadlineDAO.insert(deadline)
        }
    }

    fun deleteDeadline(deadline: Deadline) {
        launch(Dispatchers.IO) {
            deadlineDAO.delete(deadline)
        }
    }

    fun updateDeadline(deadline: Deadline) {
        launch(Dispatchers.IO) {
            deadlineDAO.update(deadline)
        }
    }

    fun cancel() {
        job.cancel()
    }
}