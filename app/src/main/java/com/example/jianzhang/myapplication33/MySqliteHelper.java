package com.example.jianzhang.myapplication33;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: AlexSai
 * Time: 16/11/15 13:33
 * Introduction:
 */
public class MySqliteHelper extends SQLiteOpenHelper {

    private final static String DBNAME = "Ceshi.db";
    private final static int version = 2;
    private SQLiteDatabase db = null;

    public MySqliteHelper(Context context) {
        super(context, DBNAME, null, version);
        getConnection();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not exists dot_tower(gtId INTEGER PRIMARY KEY AUTOINCREMENT , gtName varchar , "
                + "gtAddressText varchar,"
                + "gtAddressJd float"
                + ")");

        Log.e("MySqliteHelper", "onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getDataBase() {
        if (db == null) {
            return db;
        }
        return null;
    }

    public void ceshi1() {
//        String sql1 = "insert into dot_tower(gtName,gtAddressText,gtAddressJd) values('测试1','11111',0)";
//        String sql2 = "insert into dot_tower(gtName,gtAddressText,gtAddressJd) values('测试2','22222',0)";
//        String sql3 = "insert into dot_tower(gtName,gtAddressText,gtAddressJd) values('测试3','33333',0)";
//        db.execSQL(sql1);
//        db.execSQL(sql2);
//        db.execSQL(sql3);
        Log.e("ceshi1", "ceshi1");
        String sql4 = "replace into dot_tower(gtName,gtAddressText,gtAddressJd) values('测试3','33333',0)";
        db.execSQL(sql4);

        Cursor cursor = db.rawQuery("select last_insert_rowid() from dot_tower", null);
        int strid = 0;
        if (cursor.moveToFirst())
            strid = cursor.getInt(0);
        Log.e("strid", strid + "");
    }


    public void ceshi2() {
        String sql1 = "insert into dot_tower(gtId,gtName,gtAddressText,gtAddressJd) values(6,'测试1','11111',0)";
        String sql2 = "insert into dot_tower(gtId,gtName,gtAddressText,gtAddressJd) values(7,'测试2','22222',0)";
        String sql3 = "insert into dot_tower(gtId,gtName,gtAddressText,gtAddressJd) values(8,'测试3','33333',0)";
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    public void getConnection() {
        db = this.getReadableDatabase();
    }


    public Cursor selectCursor(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    public int selectCount(String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor != null) {
            int count = cursor.getCount();
            cursor.close();
            return count;
        }
        return 0;
    }

    public void getDbData(String sql, String[] selectionArgs) {

        List<Map<String, Object>> list = cursorToList(selectCursor(sql, selectionArgs));

        List<CeshiItem> data = new ArrayList<>();
        for (Map<String, Object> map : list) {

            CeshiItem item = new CeshiItem();
            if (map.containsKey("gtId")) {
                item.setGtId((Integer) map.get("gtId"));
            }

            if (map.containsKey("gtAddressText")) {
                item.setGtAddressText((String) map.get("gtAddressText"));
            }

            if (map.containsKey("gtName")) {
                item.setGtName((String) map.get("gtName"));
            }

            if (map.containsKey("gtAddressJd")) {
                item.setGtAddressJd((float) map.get("gtAddressJd"));
            }

            data.add(item);

        }

        Log.e("data", data.toString());
    }

    public List<Map<String, Object>> cursorToList(Cursor cursor) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        if (cursor == null) {
            return list;
        }

        String[] arrCols = cursor.getColumnNames();
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < arrCols.length; i++) {
                int type = cursor.getType(i);
                Object value = null;
                switch (type) {
                    case 1:
                        value = cursor.getInt(i);
                        break;
                    case 2:
                        value = cursor.getFloat(i);
                        break;
                    case 3:
                        value = cursor.getString(i);
                        break;
                    case 4:
                        value = cursor.getBlob(i);
                        break;
                    default:
                        break;
                }
                map.put(arrCols[i], value);
            }
            list.add(map);
        }
        return list;
    }


    public void updateData(String sql, Object[] bindArgs) {
//        try {
//            if (bindArgs == null) {
//                db.execSQL(sql);
//            } else {
//                db.execSQL(sql, bindArgs);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Exception", "" + e.toString());
//            return false;
//        }

        if (bindArgs == null) {
            db.execSQL(sql);
        } else {
            db.execSQL(sql, bindArgs);
        }

    }

    public void destroy() {
        if (db != null) {
            db.close();
        }
    }


}
