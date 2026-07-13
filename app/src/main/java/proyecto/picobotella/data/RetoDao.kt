package proyecto.picobotella.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import proyecto.picobotella.model.RetoEntity

@Dao
interface RetoDao {

    @Query("SELECT * FROM retos ORDER BY id DESC")
    fun getAll(): LiveData<List<RetoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM retos WHERE LOWER(TRIM(description)) = LOWER(TRIM(:description)))")
    suspend fun existsByDescription(description: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM retos WHERE LOWER(TRIM(description)) = LOWER(TRIM(:description)) AND id != :retoId)")
    suspend fun existsByDescriptionExceptId(description: String, retoId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reto: RetoEntity)

    @Update
    suspend fun update(reto: RetoEntity)

    @Delete
    suspend fun delete(reto: RetoEntity)
}
