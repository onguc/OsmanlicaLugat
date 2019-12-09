package com.divan.osmanlicalugat.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by İrfan Öngüç on 22.03.2019.
 */

public class DbManager {
    private int mOpenCounter = 0;
    private static DbManager dbManager;
    private static SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(Context context) {
        if (dbManager == null) {
            sqLiteOpenHelper = new DbHelper(context);
            dbManager = new DbManager();
        }
    }

    public static synchronized DbManager getDbManager() {
        if (dbManager == null) {
            throw new IllegalStateException(DbManager.class.getSimpleName() + " Başlatılamadı! initializeInstance(helper) çağrılmalı!");
        }
        return dbManager;
    }

    public synchronized SQLiteDatabase getDatabase() {
        mOpenCounter++;
        if (mOpenCounter == 1)
            mDatabase = sqLiteOpenHelper.getWritableDatabase();
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if (mDatabase != null && mOpenCounter == 0)
            mDatabase.close();
    }
}
