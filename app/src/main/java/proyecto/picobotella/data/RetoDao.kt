package proyecto.picobotella.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import proyecto.picobotella.model.RetoEntity

@Dao
interface RetoDao {

    @Query("SELECT * FROM retos ORDER BY id DESC")
    fun getAll(): LiveData<List<RetoEntity>>

    @Query("SELECT COUNT(*) FROM retos")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reto: RetoEntity)

    @Delete
    suspend fun delete(reto: RetoEntity)
}
