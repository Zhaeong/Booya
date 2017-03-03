package io.github.zhaeong.booya.sqlDatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.zhaeong.booya.helperObjects.Post;

/**
 * Created by Owen on 2017-03-02.
 */

public class customDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TaskItems.db";
    public static final int DATABASE_VERSION = 1;

    public static final String POSTS_TABLE_NAME = "POSTS";

    public static final String POSTS_COL_ID = "_id";
    public static final String POSTS_COL_POSTID = "PostId";
    public static final String POSTS_COL_NAME = "PostName";
    public static final String POSTS_COL_DESC = "PostDesc";
    public static final String POSTS_COL_AUTHOR = "PostAuthor";

    public static final String TABLE_CREATE_POSTS =
            "CREATE TABLE " +
                    POSTS_TABLE_NAME +
                    "( " + POSTS_COL_ID + " INTEGER PRIMARY KEY, " +
                    POSTS_COL_POSTID +
                    " TEXT, " +
                    POSTS_COL_NAME +
                    " TEXT, " +
                    POSTS_COL_DESC +
                    " TEXT, " +
                    POSTS_COL_AUTHOR +
                    " TEXT " +
                    " )";

    public customDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_POSTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + POSTS_TABLE_NAME);
        onCreate(db);
    }

    //
    //General Functions
    //

    public Cursor getAllItemsInTable(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from " + tableName, null );
    }

    //
    //Posts Functions
    //

    public long addPost (Post post) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(POSTS_COL_POSTID, post.postID);
        contentValues.put(POSTS_COL_NAME, post.name);
        contentValues.put(POSTS_COL_DESC, post.description);
        contentValues.put(POSTS_COL_AUTHOR, post.postAuthor);


        return db.insert(POSTS_TABLE_NAME, null, contentValues);
    }

    public boolean doesPostExist(String postId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from " + POSTS_TABLE_NAME + " where " + POSTS_COL_POSTID + " = " + "'" + postId + "'";
        Cursor result = db.rawQuery( sqlQuery, null );
        if(result.getCount() > 0) {
            return true;
        }
        return false;
    }
}
