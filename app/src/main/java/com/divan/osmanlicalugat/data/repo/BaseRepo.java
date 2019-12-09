package com.divan.osmanlicalugat.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.divan.osmanlicalugat.data.db.DbManager;
import com.divan.osmanlicalugat.data.domain.BaseModel;
import com.divan.osmanlicalugat.data.domain.CustomQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by İrfan Öngüç on 28.03.2019.
 */

public abstract class BaseRepo<T extends BaseModel> {

    SQLiteDatabase db;
    DbManager dbManager;

    protected BaseRepo() {
        dbManager = DbManager.getDbManager();
        db = dbManager.getDatabase();
    }


    /**
     * @param t             ekleme ya da güncelleme yapılacak olan obje'yi simgeler. Ekleme yaparken BaseModel içindeki alanlar zaten eklendiği için bir daha eklemeye gerek yoktur
     * @param contentValues ekleme ve güncelleme hangi kolonlar için yapılacaksa o kolonların bu metod içinde doldurulması gerekmektedir
     */
    public abstract void fillContentValues(T t, ContentValues contentValues);

    /**
     * Sorgu esnasında görmek istediğimiz kolonları bu metodda beliliyoruz.
     *
     * @return
     */
    public abstract String[] getColumns();

    /**
     * Sqlite sorgusunda gelen kolonların nesnenin alanlarıyla eşleştirilmesi için cursor kullanılır.
     *
     * @param cursor
     * @return
     */
    public abstract T getObjectByCursor(Cursor cursor);

    /**
     * İşlem yapmak istediğimiz tablonun adını dönmelidir.
     *
     * @return
     */
    public abstract String getTableName();

    public ContentValues getContentValuesBaseModel(BaseModel baseModel) {
        ContentValues values = new ContentValues();
//        values.put(baseModel.KEY_RecordDateTime, TranslateDateFormat.getConvertedStringFromDate(baseModel.getRecordDateTime()));
//        values.put(baseModel.KEY_UpdateDateTime, TranslateDateFormat.getConvertedStringFromDate(baseModel.getUpdateDateTime()));
        return values;
    }

    private ContentValues getContentValues(T t) {
        ContentValues contentValues = getContentValuesBaseModel(t);
        fillContentValues(t, contentValues);
        return contentValues;
    }


    public long add(T t) {
        t.fillRecordAndUpdateDate();
        ContentValues contentValues = getContentValues(t);
        long value = db.insert(getTableName(), null, contentValues);
        if (value == -1) {
            throw new IllegalStateException("Kaydedilemedi " + t.getClass().getSimpleName() + ": " + t.toString());
        }
        return value;
    }

    public List<T> addAll(List<T> tList) {

        db.beginTransaction();
        List<T> notSaved = new ArrayList<>();
        for (T t : tList) {
            try {
                long id = add(t);
            } catch (Exception e) {
                notSaved.add(t);
                Log.e(this.getClass().getSimpleName(), "exception : " + e.getClass().getSimpleName() + "message = " + e.getMessage() + "object t = " + t);
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        String collect = Stream.of(notSaved).map(p -> p.getIdString()).collect(Collectors.joining(","));
        Log.i(this.getClass().getSimpleName(), "Kaydedilmeyen Nesneler(idList): " + collect);
        tList.removeAll(notSaved);
        return tList;
    }

    public void synchronizeAll(List<T> tList) {
        if (tList == null) return;
        db.beginTransaction();
        try {
            for (T t : tList) {
                synchronize(t);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public int synchronize(T t) {
        int returnedValue = update(t);
        if (returnedValue == 0) {
            returnedValue = (int) add(t);
        }
        return returnedValue;
    }

    public void delete(T t) {
        db.delete(getTableName(), "rowId=?", new String[]{t.getIdString()});
//        dbManager.closeDatabase();
    }

    public void updateByQery(String query) {
        db.execSQL(query);
    }

    public int update(T t) {
        t.fillUpdateDates();
        ContentValues contentValues = getContentValues(t);
        String id = null;
        if (t.getIdString() == null)
            return 0;
        else
            id = t.getIdString();

        int updatedId = db.update(getTableName(), contentValues, "rowId=?", new String[]{id});
        return updatedId == 0 ? 0 : Integer.parseInt(id);
    }

    public T getById(String id) {
        Cursor cursor = db.query(getTableName(), getColumns(), "rowId=?", new String[]{id}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                T objectByCursor = getObjectByCursor(cursor);
                cursor.close();
                return objectByCursor;
            }
        }
        return null;
    }

    public List<T> getListByQuery(String query, String[] list) {
        Cursor cursor = db.rawQuery(query, list);
        return getListByCursor(cursor);
    }

    public List<T> getListByQuery(String query) {
        return getListByQuery(query, null);
    }

    public List<T> getListByColumn(String columnName, String columnValue) {
        Cursor cursor = db.query(getTableName(), getColumns(), columnName + "=?", new String[]{columnValue}, null, null, null, null);
        return getListByCursor(cursor);
    }

    public List<T> getListByColumns(String selection, String[] selectionArgs) {
        try {
            Cursor cursor = db.query(getTableName(), getColumns(), selection, selectionArgs, null, null, null, null);
            return getListByCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    public List<T> getListByCustomQuery(CustomQuery customQuery) {
        try {
            Cursor cursor = db.query(customQuery.getTable(), customQuery.getColumns(), customQuery.getSelection(), customQuery.getSelectionArgs(), customQuery.getGroupBy(), customQuery.getHaving(), customQuery.getOrderBy(), customQuery.getLimit());
            return getListByCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }


    public List<T> getAll() {
        Cursor cursor = db.query(getTableName(), getColumns(), null, null, null, null, null, null);
        return getListByCursor(cursor);
    }

    public List<T> gettAllByColumnOrderBy(String column) {
        List<T> objectList = new ArrayList<>();
        Cursor cursor = db.query(getTableName(), getColumns(), null, null, null, null, column + " COLLATE NOCASE ASC", null);
        if (cursor.moveToFirst()) {
            do {
                T t = getObjectByCursor(cursor);
                objectList.add(t);
            } while (cursor.moveToNext());
        }
        return objectList;
    }

    private List<T> getListByCursor(Cursor cursor) {
        List<T> objectList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    T t = getObjectByCursor(cursor);
                    objectList.add(t);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return objectList;
    }

    @Override
    protected void finalize() throws Throwable {
        dbManager.closeDatabase();
        super.finalize();
    }
}
