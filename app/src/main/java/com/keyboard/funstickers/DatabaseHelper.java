package com.keyboard.funstickers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Shalu Dhochak on 5/8/2018.
 */

public class DatabaseHelper  extends SQLiteOpenHelper {

    public static final String DB_NAME = "Stickers_DB";
    public static final String TABLE_NAME = "Stickers";

    public static final String TABLE_NAME_1 = "PRI_TABLE";
    public static final String TABLE_NAME_2 = "REF_TABLE";

    public static final String ID = "id";
    public static final String STICKER_ID = "sticker_id";
    public static final String STICKER_NAME = "sticker_name";
    public static final String STICKER_PUBLISH_TAGS = "sticker_publish_tags";
    public static final String STICKER_USER_TAGS = "sticker_user_tags";


    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " + TABLE_NAME_1
                + "(" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + STICKER_ID + " VARCHAR, " + STICKER_NAME +
                " VARCHAR);";

        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE_NAME_2
                + "(" + STICKER_ID + " VARCHAR, " + STICKER_PUBLISH_TAGS + " VARCHAR, "+STICKER_USER_TAGS+ " VARCHAR);";
        db.execSQL(sql);


        sql = "CREATE TABLE " + TABLE_NAME
                + "(" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + STICKER_ID + " VARCHAR, " + STICKER_NAME +
                " VARCHAR, " + STICKER_PUBLISH_TAGS + " VARCHAR, "+STICKER_USER_TAGS+ " VARCHAR);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public boolean addSticker(String id, String name, String tags,String userTags) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STICKER_ID, id);
        contentValues.put(STICKER_NAME, name);
        contentValues.put(STICKER_PUBLISH_TAGS, tags);
        contentValues.put(STICKER_USER_TAGS, userTags);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }
    public Cursor getTableInfo()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+";";
        System.out.println("SQL: "+ sql);
        Cursor c = db.rawQuery(sql, null);
        Log.v("Cursor count", "" + c.getCount());
        return c;
    }
    public Cursor getSticker(String tag)
    {
        tag = tag.toUpperCase();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+ STICKER_PUBLISH_TAGS+" ='"+tag+"' OR "+ STICKER_USER_TAGS+"='"+tag+"';";
        System.out.println("SQL: "+ sql);
        Cursor c = db.rawQuery(sql, null);
        Log.v("Cursor count", "" + c.getCount());
        return c;
    }
    public void inserttTag(String userTag,String publishTag,String stickerName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STICKER_USER_TAGS, userTag);
        String whereCause = STICKER_PUBLISH_TAGS+"=? AND "+STICKER_NAME+"=?";
        System.out.println("updateQUREY: "+ userTag +", "+publishTag+", "+stickerName);
        int update =  db.update(TABLE_NAME, contentValues, whereCause, new String[]{publishTag,stickerName});
        System.out.println("PAGER updateQuery: "+ update);
    }
}