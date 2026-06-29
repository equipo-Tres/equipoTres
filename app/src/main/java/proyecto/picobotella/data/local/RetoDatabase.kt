package proyecto.picobotella.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RetoEntity::class], version = 1, exportSchema = false)
abstract class RetoDatabase : RoomDatabase() {

    abstract fun retoDao(): RetoDao

    companion object {
        @Volatile
        private var INSTANCE: RetoDatabase? = null

        fun getDatabase(context: Context): RetoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RetoDatabase::class.java,
                    "reto_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
