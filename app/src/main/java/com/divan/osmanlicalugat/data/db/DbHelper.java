package com.divan.osmanlicalugat.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by İrfan Öngüç on 28.03.2019.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dblugat.db";
    private Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        new DbCopier(context).createDataBase();
        int x = 0;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//       db= this.getReadableDatabase();
//       db.close();
//        new DbCopier(context).createDataBase();
        int x = 0;
//        this.getWritableDatabase();
        create();
    }

   public void create(){

       try
       {
           File dbFile = context.getDatabasePath(DATABASE_NAME);

           if (dbFile.exists())
           {
               //Toast.makeText(cont, "Database Already Exist..." , Toast.LENGTH_LONG).show();
           }
           else
           {
               this.getReadableDatabase();

               InputStream input = context.getAssets().open(DATABASE_NAME);
               int size = input.available();
               input.close();

               if (size > 0)
               {

                   Log.d("file" , dbFile.getPath());

                   copyDataBase(dbFile);
                   //this.close();
               }
               else
               {
//                   // TODO Auto-generated method stub
//                   db.execSQL("create table " + product_table +" ( "+ order_id + " INTEGER PRIMARY KEY AUTOINCREMENT , " + pname + " TEXT NOT NULL ," + desc + " TEXT ,"
//                           + rating + " INTEGER ," + quantity +" INTEGER );");
               }
           }
       } catch (IOException e) {
           e.printStackTrace();
       }

//       this.ourdb = db;
    }

    private void copyDataBase(File dbFile) throws IOException {

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(dbFile);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0)
        {
            myOutput.write(buffer, 0, length);
            Log.d("buf", "" + length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON"); // on delete cascade vs. aktif olmalı. Standart olarak kapalı geliyor.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
