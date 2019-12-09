package com.divan.osmanlicalugat.data.repo;

import android.content.ContentValues;
import android.database.Cursor;

import com.divan.osmanlicalugat.data.domain.Word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by İrfan Öngüç on 29.03.2019.
 */

public class WordRepo extends BaseRepo<Word> {


    @Override
    public void fillContentValues(Word department, ContentValues contentValues) {
        contentValues.put(Word.KEY_ID, department.getId());
        contentValues.put(Word.keyIndex, department.getIndeks());
        contentValues.put(Word.keyKelime, department.getKelime());
        contentValues.put(Word.keyKirpilmis, department.getKirpilmis());
        contentValues.put(Word.keyAnlam, department.getAnlam());
    }

    @Override
    public String[] getColumns() {
        String[] columns = {Word.KEY_ID, Word.keyIndex, Word.keyKelime, Word.keyKirpilmis, Word.keyAnlam};
        return columns;
    }

    @Override
    public Word getObjectByCursor(Cursor cursor) {
        Word word = new Word();
        word.setId(cursor.getString(0));
        word.setIndeks(cursor.getInt(1));
        word.setKelime(cursor.getString(2));
        word.setKirpilmis(cursor.getString(3));
        word.setAnlam(cursor.getString(4));
        return word;
    }

    @Override
    public String getTableName() {
        return Word.TABLE_NAME;
    }

    @Override
    public List<Word> getAll() {
        return gettAllByColumnOrderBy(Word.keyKirpilmis);
    }

    public List<Word> getListByName(String callWord){
        String selection = Word.keyKirpilmis+" LIKE ?";
        String[] selectionArgs = {callWord};
        return getListByColumns(selection, selectionArgs);
    }

    public List<Word> getFavoriteListByName(String callWord){
        String selection = Word.keyIndex +"=? AND "+ Word.keyKirpilmis+" LIKE ?";
        String[] selectionArgs = {"1",callWord};
        return getListByColumns(selection,selectionArgs);
    }

    public List<Word> getFavoriteList(){
        return getListByColumn("indeks","1");
    }
}
