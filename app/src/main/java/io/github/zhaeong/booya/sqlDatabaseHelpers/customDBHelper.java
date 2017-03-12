package io.github.zhaeong.booya.sqlDatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.github.zhaeong.booya.helperObjects.Post;
import io.github.zhaeong.booya.helperObjects.User;

/**
 * Created by Owen on 2017-03-02.
 */

public class customDBHelper extends SQLiteOpenHelper {

    private static customDBHelper sDeviceInternalDB;

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


    public static final String USER_TABLE_NAME = "USER";

    public static final String USER_COL_ID = "_id";
    public static final String USER_COL_USERID = "UserId";
    public static final String USER_COL_NAME = "UserName";
    public static final String USER_COL_EMAIL = "UserEmail";


    public static final String TABLE_CREATE_USER =
            "CREATE TABLE " +
                    USER_TABLE_NAME +
                    "( " + USER_COL_ID + " INTEGER PRIMARY KEY, " +
                    USER_COL_USERID +
                    " TEXT, " +
                    USER_COL_NAME +
                    " TEXT, " +
                    USER_COL_EMAIL +
                    " TEXT " +
                    " )";

    public customDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_POSTS);
        db.execSQL(TABLE_CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + POSTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CREATE_USER);
        onCreate(db);
    }

    public static synchronized customDBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sDeviceInternalDB == null) {
            sDeviceInternalDB = new customDBHelper(context.getApplicationContext());
        }
        return sDeviceInternalDB;
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
            result.close();
            return true;
        }
        return false;
    }

    public Post getPost(Long postId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from " + POSTS_TABLE_NAME + " where " + POSTS_COL_ID + " = " + postId;
        Cursor result = db.rawQuery( sqlQuery, null );
        if(result.getCount() == 1) {
            result.moveToFirst();
            String postDatabaseId = result.getString(result.getColumnIndex(POSTS_COL_POSTID));
            String postAuthor = result.getString(result.getColumnIndex(POSTS_COL_AUTHOR));
            String postName = result.getString(result.getColumnIndex(POSTS_COL_NAME));
            String postDesc = result.getString(result.getColumnIndex(POSTS_COL_DESC));
            result.close();
            return new Post(postDatabaseId, postAuthor, postName, postDesc);
        }
        else
        {
            return null;
        }
    }

    public void deleteAllPosts()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ POSTS_TABLE_NAME);
    }

    //
    //Users Functions
    //

    public long addUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        //only allow one user
        if(getUser() == null)
        {
            ContentValues contentValues = new ContentValues();

            contentValues.put(USER_COL_USERID, user.userId);
            contentValues.put(USER_COL_NAME, user.username);
            contentValues.put(USER_COL_EMAIL, user.email);

            return db.insert(USER_TABLE_NAME, null, contentValues);
        }
        else
        {
            return -1;
        }
    }

    public User getUser()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from " + USER_TABLE_NAME;
        Cursor result = db.rawQuery( sqlQuery, null );
        if(result.getCount() == 1) {
            result.moveToFirst();
            String userId = result.getString(result.getColumnIndex(USER_COL_USERID));
            String userName = result.getString(result.getColumnIndex(USER_COL_NAME));
            String userEmail = result.getString(result.getColumnIndex(USER_COL_EMAIL));
            result.close();
            return new User(userId, userName, userEmail);
        }
        else
        {
            return null;
        }
    }

    public void deleteUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ USER_TABLE_NAME);
    }

    public void printAllItemsInTable(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + tableName, null );
        try {
            res.moveToFirst();
            String loggerMSG = tableName + "\n";
            while (!res.isAfterLast()) {
                String loggerLine = "";
                for(int i = 0; i < res.getColumnCount(); i++)
                {
                    loggerLine += res.getString(i);
                    loggerLine += " ";
                }

                loggerLine += "\n";
                loggerMSG +=loggerLine;

                res.moveToNext();
            }
            Log.i("DatabaseHelper", loggerMSG);
        }
        finally {
            res.close();
        }

    }

}
