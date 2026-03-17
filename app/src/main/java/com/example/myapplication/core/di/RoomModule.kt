package com.example.myapplication.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.models.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    companion object {
        // Миграция с версии 1 на версию 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    ALTER TABLE products 
                    ADD COLUMN updatedAt INTEGER
                    """
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    ALTER TABLE products
                    ADD COLUMN description TEXT NOT NULL DEFAULT ''
                """.trimIndent()
                )
            }
        }

        @Provides
        @Singleton
        fun providesRoomDatabase(@ApplicationContext context: Context): AppDatabase {
            lateinit var database: AppDatabase

            database = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "room_database"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        CoroutineScope(Dispatchers.IO).launch {

                            database.getUserDao().insertAll(
                                listOf(
                                    User(
                                        name = "Alex",
                                        email = "alex@mail.com",
                                        password = "11111111"
                                    ),
                                    User(
                                        name = "Maria",
                                        email = "maria@mail.com",
                                        password = "22222222"
                                    ),
                                    User(
                                        name = "John",
                                        email = "john@mail.com",
                                        password = "33333333"
                                    ),
                                    User(
                                        name = "Jane",
                                        email = "jane@mail.com",
                                        password = "44444444"
                                    ),
                                    User(
                                        name = "Bob",
                                        email = "bob@mail.com",
                                        password = "55555555"
                                    )
                                )
                            )
                        }
                    }

                })
                .build()

            return database
        }
    }
}