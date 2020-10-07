package com.example.mobilehealthprototype;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PatientInfo.class}, version = 1, exportSchema = false)
public abstract class PatientDatabase extends RoomDatabase {
    public abstract PatientInfoDao PatientInfoDao();
    private static volatile PatientDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PatientDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PatientDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PatientDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
