package com.example.screensage.service

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Entity representing a User stored in the local SQLite database.
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int,
    val username: String,
)

/**
 * Data Access Object (DAO) for managing the single stored user.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User) // Saves or updates the only stored user

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getCurrentUser(): User? // Fetches the single stored user

    @Query("DELETE FROM user")
    suspend fun clearUser() // Deletes the current user (for logout)
}


/**
 * Room Database for storing user data.
 */
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Service for handling user storage and retrieval.
 */
object RoomService {

    /**
     * Saves or updates the logged-in user in SQLite.
     */
    suspend fun saveUser(context: Context, userId: Int, username: String) {
        val user = User(id = userId, username = username)
        val db = AppDatabase.getInstance(context)
        db.userDao().saveUser(user)
    }

    /**
     * Retrieves the currently stored user.
     */
    suspend fun getCurrentUser(context: Context): User? {
        val db = AppDatabase.getInstance(context)
        return db.userDao().getCurrentUser()
    }

    /**
     * Clears the stored user (for logout).
     */
    suspend fun clearUser(context: Context) {
        val db = AppDatabase.getInstance(context)
        db.userDao().clearUser()
    }
}