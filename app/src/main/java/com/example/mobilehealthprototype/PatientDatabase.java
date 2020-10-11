package com.example.mobilehealthprototype;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                            PatientDatabase.class, "patient_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                PatientInfoDao dao = INSTANCE.PatientInfoDao();
                dao.deleteAll();

                PatientInfo patient_info = new PatientInfo();
                patient_info.id = "exampleId";
                patient_info.name = "exampleName";

                dao.insert(patient_info);
            });
        }
    };
}
