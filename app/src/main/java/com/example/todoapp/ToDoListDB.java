package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class ToDoListDB extends SQLiteOpenHelper {

    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "ToDoListDBv3";
    private static String TABLE_NAME = "TaskItems";
    public static String KEY_ID = "id";
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static String CREATE_DATE = "createDate";
    public static String FINISH_DATE = "finishDate";
    public static String DEAD_LINE_DATE = "deadLineDate";
    public static String CREATE_TIME = "createTime";
    public static String FINISH_TIME = "finishTime";
    public static String DEAD_LINE_TIME = "deadLineTime";
    public static String IS_HIDDEN = "isHidden";

    public ToDoListDB(Context context)
    {
        super(context,DATABASE_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT," + DESCRIPTION + " TEXT," + CREATE_DATE + " TEXT," + CREATE_TIME + " TEXT," + FINISH_DATE + " TEXT,"  + FINISH_TIME + " TEXT," + DEAD_LINE_DATE  + " TEXT," + DEAD_LINE_TIME + " TEXT," + IS_HIDDEN + " TEXT)");
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
        cv.put(CREATE_TIME, taskItem.getCreateTime());
        cv.put(FINISH_TIME, taskItem.getFinishTime());
        cv.put(DEAD_LINE_TIME, taskItem.getDeadLineTime());
        cv.put(IS_HIDDEN,taskItem.getIsHidden());
        Log.d("check",taskItem.getIsHidden());
        if(taskItem.getKey_id()!=-1)
        {
            cv.put(KEY_ID,taskItem.getKey_id());
        }
        long id = db.insert(TABLE_NAME,null,cv);
        return (int)id;
    }

    public Cursor readAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME;
        return db.rawQuery(sql,null,null);
    }

    public Cursor readAllData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + KEY_ID + " = " + id + "";
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
        final int createTimeID = cursor.getColumnIndex(CREATE_TIME);
        final int finishTimeID = cursor.getColumnIndex(FINISH_TIME);
        final int deadLineTimeID = cursor.getColumnIndex(DEAD_LINE_TIME);
        final int isHiddenID = cursor.getColumnIndex(IS_HIDDEN);
        Log.d("check",isHiddenID+"");
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
            final String createTimeIDValue = cursor.getString(createTimeID);
            final String finishTimeIDValue = cursor.getString(finishTimeID);
            final String deadLineTimeIDValue = cursor.getString(deadLineTimeID);
            final String isHiddenIDValue = cursor.getString(isHiddenID);


            taskList.add(new TaskItem(keyIdIDValue,
                    titleIDValue,
                    descriptionIDValue,
                    createDateIDValue,
                    createTimeIDValue,
                    finishDateIDValue,
                    finishTimeIDValue,
                    deadLineDateIDValue,
                    deadLineTimeIDValue,
                    isHiddenIDValue));

        }while(cursor.moveToNext());
        return taskList;
    }

    public TaskItem getTaskItem(int id)
    {
        Cursor cursor = readAllData(id);

        final int keyIdID = cursor.getColumnIndex(KEY_ID);
        final int titleID = cursor.getColumnIndex(TITLE);
        final int descriptionID = cursor.getColumnIndex(DESCRIPTION);
        final int createDateID = cursor.getColumnIndex(CREATE_DATE);
        final int finishDateID = cursor.getColumnIndex(FINISH_DATE);
        final int deadLineDateID = cursor.getColumnIndex(DEAD_LINE_DATE);
        final int createTimeID = cursor.getColumnIndex(CREATE_TIME);
        final int finishTimeID = cursor.getColumnIndex(FINISH_TIME);
        final int deadLineTimeID = cursor.getColumnIndex(DEAD_LINE_TIME);
        final int isHiddenID = cursor.getColumnIndex(IS_HIDDEN);

        final TaskItem taskItem = new TaskItem();

        if(!cursor.moveToFirst())
        {
            return null;
        }

        final int keyIdIDValue = cursor.getInt(keyIdID);
        final String titleIDValue = cursor.getString(titleID);
        final String descriptionIDValue = cursor.getString(descriptionID);
        final String createDateIDValue = cursor.getString(createDateID);
        final String finishDateIDValue = cursor.getString(finishDateID);
        final String deadLineDateIDValue = cursor.getString(deadLineDateID);
        final String createTimeIDValue = cursor.getString(createTimeID);
        final String finishTimeIDValue = cursor.getString(finishTimeID);
        final String deadLineTimeIDValue = cursor.getString(deadLineTimeID);
        final String isHiddenIDValue = cursor.getString(isHiddenID);

        taskItem.setFinishDate(finishDateIDValue);
        taskItem.setKey_id(keyIdIDValue);
        taskItem.setDescription(descriptionIDValue);
        taskItem.setTitle(titleIDValue);
        taskItem.setDeadLineDate(deadLineDateIDValue);
        taskItem.setCreateDate(createDateIDValue);
        taskItem.setCreateTime(createTimeIDValue);
        taskItem.setFinishTime(finishTimeIDValue);
        taskItem.setDeadLineTime(deadLineTimeIDValue);
        taskItem.setIsHidden(isHiddenIDValue);
        return taskItem;

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
        cv.put(CREATE_TIME, taskItem.getCreateTime());
        cv.put(FINISH_TIME, taskItem.getFinishTime());
        cv.put(DEAD_LINE_TIME, taskItem.getDeadLineTime());
        cv.put(IS_HIDDEN,taskItem.getIsHidden());
        db.update(TABLE_NAME, cv, "id = ?", new String[]{taskItem.getKey_id()+""});
    }

    public void removeFromTheDatabase(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from " + TABLE_NAME + " where " + KEY_ID + " = " + id + "";
        db.execSQL(sql);
    }
}
