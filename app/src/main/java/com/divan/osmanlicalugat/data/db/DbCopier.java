package com.divan.osmanlicalugat.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DbCopier {
    private final String DB_PATH = "/data/data/com.divan.osmanlicalugat/databases/";
    static final String DATABASE_NAME = "dblugat.db";
    private Context context;

    public DbCopier(Context context) {
        this.context = context;
        context.deleteDatabase(DATABASE_NAME);
    }

    public void createDataBase() {
//        boolean dbExist = checkDataBase();
//        if (!dbExist) {
//            this.getReadableDatabase();
        copyDataBase();

    }
//    }

    private void copyDataBase() {
        InputStream myInput = null;
        try {
            myInput = context.getAssets().open(DATABASE_NAME);
            String outFileName = DB_PATH + DbHelper.DATABASE_NAME;
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(),e.getMessage());
        }

    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        } finally {
            if (checkDB != null) {
                checkDB.close();
            }
        }

        return checkDB != null ? true : false;
    }
}
