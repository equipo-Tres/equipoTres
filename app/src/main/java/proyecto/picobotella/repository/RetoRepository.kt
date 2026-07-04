package proyecto.picobotella.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import proyecto.picobotella.data.RetoDao
import proyecto.picobotella.model.RetoEntity

class RetoRepository(private val retoDao: RetoDao) {

    val allRetos: LiveData<List<RetoEntity>> = retoDao.getAll()

    suspend fun insert(reto: RetoEntity) {
        withContext(Dispatchers.IO) {
            retoDao.insert(reto)
        }
    }

    suspend fun delete(reto: RetoEntity) {
        withContext(Dispatchers.IO) {
            retoDao.delete(reto)
        }
    }

    suspend fun seedSampleRetosIfEmpty(descriptions: List<String>) {
        withContext(Dispatchers.IO) {
            if (retoDao.getCount() == 0) {
                descriptions.forEach { description ->
                    retoDao.insert(RetoEntity(description = description))
                }
            }
        }
    }
}
