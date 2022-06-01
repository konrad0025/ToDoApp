package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ToDoListDB extends SQLiteOpenHelper {

    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "ToDoListDB";
    private static String TABLE_NAME = "TaskItems";
    public static String KEY_ID = "id";
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static String CREATE_DATE = "createDate";
    public static String FINISH_DATE = "finishDate";
    public static String DEAD_LINE_DATE = "deadLineDate";

    public ToDoListDB(Context context)
    {
        super(context,DATABASE_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT," + DESCRIPTION + " TEXT," + CREATE_DATE + " TEXT,"  + FINISH_DATE + " TEXT,"  + DEAD_LINE_DATE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int insertIntoTheDatabase(TaskItem taskItem){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, taskItem.getTitle());
        cv.put(DESCRIPTION, taskItem.getDescription());
        cv.put(CREATE_DATE, taskItem.getCreateDate());
        cv.put(FINISH_DATE, taskItem.getFinishDate());
        cv.put(DEAD_LINE_DATE, taskItem.getDeadLineDate());

        long id = db.insert(TABLE_NAME,null,cv);
        return (int)id;
    }

    public Cursor readAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME;
        return db.rawQuery(sql,null,null);
    }

    public ArrayList<TaskItem> getTaskList() {

        Cursor cursor = readAllData();

        final int keyIdID = cursor.getColumnIndex(KEY_ID);
        final int titleID = cursor.getColumnIndex(TITLE);
        final int descriptionID = cursor.getColumnIndex(DESCRIPTION);
        final int createDateID = cursor.getColumnIndex(CREATE_DATE);
        final int finishDateID = cursor.getColumnIndex(FINISH_DATE);
        final int deadLineDateID = cursor.getColumnIndex(DEAD_LINE_DATE);

        final ArrayList<TaskItem> taskList = new ArrayList<>();

        if(!cursor.moveToFirst())
        {
            return taskList;
        }
        do{

            final int keyIdIDValue = cursor.getInt(keyIdID);
            final String titleIDValue = cursor.getString(titleID);
            final String descriptionIDValue = cursor.getString(descriptionID);
            final String createDateIDValue = cursor.getString(createDateID);
            final String finishDateIDValue = cursor.getString(finishDateID);
            final String deadLineDateIDValue = cursor.getString(deadLineDateID);

            taskList.add(new TaskItem(keyIdIDValue,
                    titleIDValue,
                    descriptionIDValue,
                    createDateIDValue,
                    finishDateIDValue,
                    deadLineDateIDValue));

        }while(cursor.moveToNext());
        return taskList;
    }

    public void updateTask(TaskItem taskItem)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, taskItem.getTitle());
        cv.put(DESCRIPTION, taskItem.getDescription());
        cv.put(CREATE_DATE, taskItem.getCreateDate());
        cv.put(FINISH_DATE, taskItem.getFinishDate());
        cv.put(DEAD_LINE_DATE, taskItem.getDeadLineDate());

        db.update(TABLE_NAME, cv, "id = ?", new String[]{taskItem.getKey_id()+""});
    }

    public void removeFromTheDatabase(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from " + TABLE_NAME + " where " + KEY_ID + " = " + id + "";
        db.execSQL(sql);
    }
}
