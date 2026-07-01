package proyecto.picobotella.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RetoDao {

    @Query("SELECT * FROM retos ORDER BY id DESC") //criterio 6 HU-6 ordenar los retos en la parte superior
    fun getAll(): LiveData<List<RetoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reto: RetoEntity)

    @Delete
    suspend fun delete(reto: RetoEntity)
}
