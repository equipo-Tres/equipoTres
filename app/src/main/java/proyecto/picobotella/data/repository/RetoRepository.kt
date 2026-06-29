package proyecto.picobotella.data.repository

import androidx.lifecycle.LiveData
import proyecto.picobotella.data.local.RetoDao
import proyecto.picobotella.data.local.RetoEntity

class RetoRepository(private val retoDao: RetoDao) {

    val allRetos: LiveData<List<RetoEntity>> = retoDao.getAll()

    suspend fun insert(reto: RetoEntity) {
        retoDao.insert(reto)
    }

    suspend fun delete(reto: RetoEntity) {
        retoDao.delete(reto)
    }
}
