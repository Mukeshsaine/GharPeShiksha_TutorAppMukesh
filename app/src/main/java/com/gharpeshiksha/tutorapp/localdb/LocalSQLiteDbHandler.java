package com.gharpeshiksha.tutorapp.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gharpeshiksha.tutorapp.data_model.ClassesForMe;
import com.gharpeshiksha.tutorapp.data_model.Classes_Model;
import com.gharpeshiksha.tutorapp.data_model.Model_Chats;
import com.gharpeshiksha.tutorapp.data_model.Model_Imonials;
import com.gharpeshiksha.tutorapp.data_model.Model_archived;
import com.gharpeshiksha.tutorapp.data_model.Written_Testimonial;
import com.google.android.exoplayer2.C;

import org.json.JSONObject;

import java.util.Calendar;

public class LocalSQLiteDbHandler extends SQLiteOpenHelper {
    //When adding row into ClassesForMe table make sure you add 0 if false 1 if true in ENQ_VIEWED and FEEDBACK columns value.
    //Create Query to create classesforme endpoint api of ClassesForMeFragment table in LocalSQLite database.
    public String createClassesForMeTable = "CREATE TABLE " + Contract.ClassesForMeAPI.TABLE_NAME + " (" +
            Contract.ClassesForMeAPI._ID + " INTEGER PRIMARY KEY," + Contract.ClassesForMeAPI.ENQ_ID + " INTEGER," +
            Contract.ClassesForMeAPI.SOFT_TTL + " TEXT," +
            Contract.ClassesForMeAPI.TEXT_MIN + " TEXT," + Contract.ClassesForMeAPI.TEXT_VIEWS + " TEXT," +
            Contract.ClassesForMeAPI.TEXT_NAME + " TEXT," + Contract.ClassesForMeAPI.TEXT_TUTOR_REQUIREMENT + " TEXT," +
            Contract.ClassesForMeAPI.TEXT_BUDGET + " TEXT," + Contract.ClassesForMeAPI.TEXT_LOC + " TEXT," +
            Contract.ClassesForMeAPI.FAVORITE + " TEXT," + Contract.ClassesForMeAPI.TEXT_DIS + " REAL," + Contract.ClassesForMeAPI.STATUS +
            " TEXT," + Contract.ClassesForMeAPI.PAYMENT_STATUS + " TEXT," + Contract.ClassesForMeAPI.ENQ_VIEWED + " TEXT," +
            Contract.ClassesForMeAPI.FEEDBACK + " TEXT," + Contract.ClassesForMeAPI.HIGH_COMP + " TEXT," + Contract.ClassesForMeAPI.FREE_CLASS +
            " TEXT," + Contract.ClassesForMeAPI.STUDENT_UUID + " TEXT," + Contract.ClassesForMeAPI.TUTOR_UUID + " TEXT," +
            Contract.ClassesForMeAPI.PIC_URL + " TEXT)";

    //Below is SQLite statement for classes subject recycler view list.
    public String createClassesTable = "CREATE TABLE " + Contract.ClassesTable.TABLE_NAME + " ( " + Contract.ClassesTable.CLASSES_NAME + " TEXT,"
    + Contract.ClassesTable.CLASS_PIC_URL + " TEXT," + Contract.ClassesTable.FILTER_CLS + " TEXT," + Contract.ClassesTable.SOFT_TTL + " TEXT)";

    //Below is SQLite Query to create Archived Table.
    private String createArchivedTable = "CREATE TABLE " + Contract.ArchivedTable.TABLE_NAME + " ( " + Contract.ArchivedTable._ID +
            " INTEGER PRIMARY KEY," + Contract.ArchivedTable.area + " TEXT," + Contract.ArchivedTable.studentUUID + " TEXT," +
            Contract.ArchivedTable.distance + " TEXT," + Contract.ArchivedTable.subjects + " TEXT," + Contract.ArchivedTable.tutorUUID +
            " TEXT," + Contract.ArchivedTable.enq_id + " TEXT," + Contract.ArchivedTable.op_count + " TEXT," + Contract.ArchivedTable.highComp1 +
            " TEXT," + Contract.ArchivedTable.freeCla + " TEXT," + Contract.ArchivedTable.name + " TEXT," + Contract.ArchivedTable.paymentStatus +
            " TEXT," + Contract.ArchivedTable.tutors_con + " TEXT," + Contract.ArchivedTable.time + " TEXT," + Contract.ArchivedTable.favorite + " TEXT," +
            Contract.ArchivedTable.classfor + " TEXT," + Contract.ArchivedTable.budget + " TEXT," + Contract.ArchivedTable.status1 + " TEXT," +
            Contract.ArchivedTable.enq_viewed + " TEXT," + Contract.ArchivedTable.cursor + " TEXT," + Contract.ArchivedTable.timestamp + " TEXT," +
            Contract.ArchivedTable.sofTTL + " TEXT)";

    //Below is SQLite query to create Favorite table
    private String createFavTable = "CREATE TABLE " + Contract.FavoriteTable.TABLE_NAME + " ( " + Contract.FavoriteTable._ID +
            " INTEGER PRIMARY KEY," + Contract.FavoriteTable.ENQ_ID + " TEXT," + Contract.FavoriteTable.TIME + " TEXT," +
        Contract.FavoriteTable.TUTOR_CON + " TEXT," + Contract.FavoriteTable.NAME + " TEXT, " + Contract.FavoriteTable.TITLE +
        " TEXT," + Contract.FavoriteTable.BUDGET + " TEXT," + Contract.FavoriteTable.UTF_8STR + " TEXT," + Contract.FavoriteTable.FAVORITE
        + " TEXT," + Contract.FavoriteTable.DISTANCE + " TEXT," + Contract.FavoriteTable.STATUS + " TEXT," + Contract.FavoriteTable.CHECK_STATUS
        + " TEXT," + Contract.FavoriteTable.ENQ_VIEWED + " TEXT," + Contract.FavoriteTable.FEEDBACK + " TEXT," + Contract.FavoriteTable.HIGH_COMP
        + " TEXT," + Contract.FavoriteTable.FREE_CLASS + " TEXT," + Contract.FavoriteTable.STUDENT_UUID + " TEXT," + Contract.FavoriteTable.TUTOR_UUID
        + " TEXT," + Contract.FavoriteTable.SOFT_TTL + " TEXT," + Contract.FavoriteTable.PIC_URL + " TEXT)";

    //Below is SQLite query to create written testimonials table
    private String createWrittenTable = "CREATE TABLE " + Contract.WrittenTestimonial.TABLE_NAME + " ( " + Contract.WrittenTestimonial._ID +
            " INTEGER PRIMARY KEY," + Contract.WrittenTestimonial.PIC_URL + " TEXT," + Contract.WrittenTestimonial.DESC + " TEXT," +
            Contract.WrittenTestimonial.TUTOR_ID + " TEXT," + Contract.WrittenTestimonial.SOFT_TTL + " TEXT," +
            Contract.WrittenTestimonial.TUTOR_NAME + " TEXT)";

    //Below is video testimonials
    private String createVideoTestimonialTable = "CREATE TABLE " + Contract.VideoTestimonial.TABLE_NAME + " ( " + Contract.VideoTestimonial._ID
            + " INTEGER PRIMARY KEY," + Contract.VideoTestimonial.YOUTUBE_URL + " TEXT," + Contract.VideoTestimonial.VIDEO_ID + " TEXT," +
            Contract.VideoTestimonial.DESC + " TEXT," + Contract.VideoTestimonial.IMG_URL + " TEXT," +
            Contract.VideoTestimonial.SOFT_TTL + " TEXT)";

    //Below is Basic Info table.
    private String createBasicInfo = "CREATE TABLE " + Contract.BasicInfo.TABLE_NAME + " ( " + Contract.BasicInfo._ID + " INTEGER PRIMARY KEY," +
            Contract.BasicInfo.ABOUT + " TEXT," + Contract.BasicInfo.PLAN_DETAIL + " TEXT," + Contract.BasicInfo.PIC_URL + " TEXT," +
            Contract.BasicInfo.TUT_ID + " TEXT," + Contract.BasicInfo.EMAIL + " TEXT," + Contract.BasicInfo.STATUS + " TEXT," +
            Contract.BasicInfo.REMAINING_VIEWS + " TEXT," + Contract.BasicInfo.NAME + " TEXT," + Contract.BasicInfo.SOFT_TL + " TEXT)";

    //Below is display chats table
    private String createDisplayChatsTable = "CREATE TABLE " + Contract.DisplayChats.TABLE_NAME + " ( " + Contract.DisplayChats.PIC_URL + " TEXT," +
            Contract.DisplayChats.STUDENT_UUID + " TEXT," + Contract.DisplayChats.STUDENT_NAME + " TEXT," + Contract.DisplayChats.TUTOR_UUId +
            " TEXT," + Contract.DisplayChats.MESSAGE + " TEXT," + Contract.DisplayChats.TIMESTAMP + " TEXT,"
            + Contract.DisplayChats.IS_APPROVED + " TEXT," + Contract.DisplayChats.TIMESTAMP_IN_MILLIS + " TEXT," +
            Contract.DisplayChats.ENQ_ID + " TEXT," + Contract.DisplayChats.SEND_BY + " TEXT," + Contract.DisplayChats.SOFT_TL
            + " TEXT)";

    //Delete Query to delete classesforme endpoint api of ClassesForMeFragment table from LocalSQLite DB.
    public String deleteClassesForMeTable = "DROP TABLE IF EXISTS " + Contract.ClassesForMeAPI.TABLE_NAME;
    //statement to delete classesTable of HomeFragment
    public String deleteClassesTable = "DROP TABLE IF EXISTS " + Contract.ClassesTable.TABLE_NAME;
    //delete archivedTable
    public String deleteArchivedTable = "DROP TABLE IF EXISTS " + Contract.ArchivedTable.TABLE_NAME;
    //Delete query for favorite table
    public String deleteFavTable = "DROP TABLE IF EXISTS " + Contract.FavoriteTable.TABLE_NAME;
    //Delete query for Classes table
    public String deleteWrittenTable = "DROP TABLE IF EXISTS " + Contract.WrittenTestimonial.TABLE_NAME;
    //delete query for video testimonials
    public String deleteVideoTestimonialsTable = "DROP TABLE IF EXISTS " + Contract.VideoTestimonial.TABLE_NAME;
    //delete query for basic info
    public String deleteBasicInfoTable = "DROP TABLE IF EXISTS " + Contract.BasicInfo.TABLE_NAME;
    //delete query for display chats
    public String deleteDisplayChatsTable = "DROP TABLE IF EXISTS " + Contract.DisplayChats.TABLE_NAME;

    private String TAG = "DbHandler.java";

    public LocalSQLiteDbHandler( Context context) {
        super(context, Contract.DB_NAME, null, Contract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("DbHandler.java", createClassesForMeTable);
        //Create table by executing create query
        db.execSQL(createClassesForMeTable);
        db.execSQL(createClassesTable);
        db.execSQL(createArchivedTable);
        db.execSQL(createFavTable);
        db.execSQL(createWrittenTable);
        db.execSQL(createVideoTestimonialTable);
        db.execSQL(createBasicInfo);
        db.execSQL(createDisplayChatsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(deleteClassesForMeTable);
        db.execSQL(deleteClassesTable);
        db.execSQL(deleteArchivedTable);
        db.execSQL(deleteFavTable);
        db.execSQL(deleteWrittenTable);
        db.execSQL(deleteVideoTestimonialsTable);
        db.execSQL(deleteBasicInfoTable);
        db.execSQL(deleteDisplayChatsTable);
        onCreate(db);
    }

    //Testimonials video table methods
    public void addTestimonials(SQLiteDatabase db, Model_Imonials model_imonials) {
        ContentValues val = new ContentValues();
        val.put(Contract.VideoTestimonial.YOUTUBE_URL, model_imonials.getYoutube_url());
        val.put(Contract.VideoTestimonial.DESC, model_imonials.getDesc());
        val.put(Contract.VideoTestimonial.VIDEO_ID, model_imonials.getVideoId());
        val.put(Contract.VideoTestimonial.SOFT_TTL, System.currentTimeMillis() + "");
        val.put(Contract.VideoTestimonial.IMG_URL, model_imonials.getImgUrl());

        db.insert(Contract.VideoTestimonial.TABLE_NAME, null, val);
    }

    public Cursor getTestimonials(SQLiteDatabase readDb) {
        return readDb.query(Contract.VideoTestimonial.TABLE_NAME, null, null, null, null, null, null);
    }

    public void deleteTestimonials(SQLiteDatabase writeDb) {
        writeDb.delete(Contract.VideoTestimonial.TABLE_NAME, null, null);
    }

    //display chats methods
    public void addDisplayChats(SQLiteDatabase db, Model_Chats chats) {
        try {
            ContentValues val = new ContentValues();
            val.put(Contract.DisplayChats.PIC_URL, chats.getPicUrl());
            val.put(Contract.DisplayChats.STUDENT_UUID, chats.getStudentUUID());
            val.put(Contract.DisplayChats.STUDENT_NAME, chats.getStudentname());
            val.put(Contract.DisplayChats.TUTOR_UUId, chats.getTutorUUID());
            val.put(Contract.DisplayChats.MESSAGE, chats.getMessage());
            val.put(Contract.DisplayChats.TIMESTAMP, chats.getDay());
            val.put(Contract.DisplayChats.TIMESTAMP_IN_MILLIS, chats.getTimestampInMilliSec());
            val.put(Contract.DisplayChats.SOFT_TL, System.currentTimeMillis());
            val.put(Contract.DisplayChats.ENQ_ID, chats.getEnqId());
            val.put(Contract.DisplayChats.IS_APPROVED, chats.getIsApproved());
            val.put(Contract.DisplayChats.SEND_BY, chats.getSendBy());

            db.insert(Contract.DisplayChats.TABLE_NAME, null, val);
        } catch (Exception e) {
            Log.d(TAG, "addBasicInfo: " + e);
        }
    }

    public Cursor getDisplayChats(SQLiteDatabase readDb) {
        return readDb.query(Contract.DisplayChats.TABLE_NAME, null, null, null, null, null, null);
    }

    public void deleteDisplayChats(SQLiteDatabase writeDb) {
        writeDb.delete(Contract.DisplayChats.TABLE_NAME, null, null);
    }

    public boolean hasChat(SQLiteDatabase readableDatabase, String studentUUID, SQLiteDatabase writeDb) {
        String selectionCol = Contract.DisplayChats.STUDENT_UUID + " LIKE ?";
        String[] selectionColValue = new String[] { studentUUID };
        Cursor cursor = readableDatabase.query(Contract.DisplayChats.TABLE_NAME, null, selectionCol, selectionColValue, null,
                null, null);
        if(cursor.getCount() > 0) {
            //has
            long deletedRow = writeDb.delete(Contract.DisplayChats.TABLE_NAME, selectionCol, selectionColValue);
            Log.d(TAG, "hasChat: true " + deletedRow);
            return true;
        } else {
            //not contains
            Log.d(TAG, "hasChat: false ");
            return false;
        }
    }

    //basic info table methods
    public void addBasicInfo(SQLiteDatabase db, JSONObject json) {
        try {
            ContentValues val = new ContentValues();
            val.put(Contract.BasicInfo.ABOUT, json.getString("about"));
            val.put(Contract.BasicInfo.PLAN_DETAIL, json.getString("planDetail"));
            val.put(Contract.BasicInfo.REMAINING_VIEWS, json.getString("remainingViews"));
            val.put(Contract.BasicInfo.NAME, json.getString("name"));
            val.put(Contract.BasicInfo.PIC_URL, json.getString("profile_pic_url"));
            val.put(Contract.BasicInfo.TUT_ID, json.getString("tut_id"));
            val.put(Contract.BasicInfo.EMAIL, json.getString("email"));
            val.put(Contract.BasicInfo.STATUS, json.getString("status"));
            val.put(Contract.BasicInfo.SOFT_TL, System.currentTimeMillis() + "");

            db.insert(Contract.BasicInfo.TABLE_NAME, null, val);
        } catch (Exception e) {
            Log.d(TAG, "addBasicInfo: " + e);
        }
    }

    public Cursor getBasicInfo(SQLiteDatabase readDb) {
        return readDb.query(Contract.BasicInfo.TABLE_NAME, null, null, null, null, null, null);
    }

    public void deleteBasicInfo(SQLiteDatabase writeDb) {
        writeDb.delete(Contract.BasicInfo.TABLE_NAME, null, null);
    }

    //written table methods
    public void addWritten(SQLiteDatabase db, Written_Testimonial writtenTestimonial) {
        ContentValues val = new ContentValues();
        val.put(Contract.WrittenTestimonial.PIC_URL, writtenTestimonial.getPicurl());
        val.put(Contract.WrittenTestimonial.DESC, writtenTestimonial.getDesc());
        val.put(Contract.WrittenTestimonial.TUTOR_ID, writtenTestimonial.getTutorID());
        val.put(Contract.WrittenTestimonial.TUTOR_NAME, writtenTestimonial.getTutorName());
        val.put(Contract.WrittenTestimonial.SOFT_TTL, System.currentTimeMillis() + "");

        db.insert(Contract.WrittenTestimonial.TABLE_NAME, null, val);
    }

    public Cursor getWritten(SQLiteDatabase readDb) {
        return readDb.query(Contract.WrittenTestimonial.TABLE_NAME, null, null, null, null, null, null);
    }

    public void deleteWritten(SQLiteDatabase writeDb) {
        writeDb.delete(Contract.WrittenTestimonial.TABLE_NAME, null, null);
    }

    //favorite table methods
    public void addFav(SQLiteDatabase db, ClassesForMe clsForMe, String sub, String cls) {
        ContentValues val = new ContentValues();
        val.put(Contract.FavoriteTable.ENQ_ID, clsForMe.getTextEnq_id());
        val.put(Contract.FavoriteTable.TIME, clsForMe.getTextMins());
        val.put(Contract.FavoriteTable.TUTOR_CON, clsForMe.getTextViews());
        val.put(Contract.FavoriteTable.NAME, clsForMe.getTextName());
        val.put(Contract.FavoriteTable.TITLE, "Tutor Requirement for " + sub + " for " + cls);
        val.put(Contract.FavoriteTable.BUDGET, clsForMe.getTextBudget());
        val.put(Contract.FavoriteTable.UTF_8STR, clsForMe.getTextLoc());
        val.put(Contract.FavoriteTable.FAVORITE, clsForMe.getFavorite());
        val.put(Contract.FavoriteTable.DISTANCE, clsForMe.getTextDis());
        val.put(Contract.FavoriteTable.STATUS, clsForMe.getStatus());
        val.put(Contract.FavoriteTable.CHECK_STATUS, clsForMe.getPaymentstatus());
        val.put(Contract.FavoriteTable.ENQ_VIEWED, clsForMe.getEnq_viewed());
        val.put(Contract.FavoriteTable.FEEDBACK, clsForMe.getFeedback());
        val.put(Contract.FavoriteTable.HIGH_COMP, clsForMe.getHighComp());
        val.put(Contract.FavoriteTable.FREE_CLASS, clsForMe.getFreeClass());
        val.put(Contract.FavoriteTable.STUDENT_UUID, clsForMe.getStudentUUId());
        val.put(Contract.FavoriteTable.TUTOR_UUID, clsForMe.getTutorUUId());
        val.put(Contract.FavoriteTable.PIC_URL, clsForMe.getPicUrl());
        val.put(Contract.FavoriteTable.SOFT_TTL, System.currentTimeMillis() + "");

        db.insert(Contract.FavoriteTable.TABLE_NAME, null, val);
    }

    public Cursor getFav(SQLiteDatabase readDb) {
        return readDb.query(Contract.FavoriteTable.TABLE_NAME, null, null, null, null, null, null);
    }

    public void deleteFav(SQLiteDatabase writeDb) {
        writeDb.delete(Contract.FavoriteTable.TABLE_NAME, null, null);
    }

    //Below archived table
    public void addArchived(SQLiteDatabase db, Model_archived archived) {
        ContentValues val = new ContentValues();
        val.put(Contract.ArchivedTable.area, archived.getArea());
        val.put(Contract.ArchivedTable.studentUUID, archived.getStudentUUID());
        val.put(Contract.ArchivedTable.distance, archived.getDistance());
        val.put(Contract.ArchivedTable.subjects, archived.getSubjects());
        val.put(Contract.ArchivedTable.tutorUUID, archived.getTutorUUID());
        val.put(Contract.ArchivedTable.enq_id, archived.getEnq_id());
        val.put(Contract.ArchivedTable.op_count, archived.getOp_count());
        val.put(Contract.ArchivedTable.highComp1, archived.getHighComp1());
        val.put(Contract.ArchivedTable.freeCla, archived.getFreeClass1());
        val.put(Contract.ArchivedTable.name, archived.getName());
        val.put(Contract.ArchivedTable.paymentStatus, archived.getPaymentstatus());
        val.put(Contract.ArchivedTable.tutors_con, archived.getTutors_contacted());
        val.put(Contract.ArchivedTable.time, archived.getTime());
        val.put(Contract.ArchivedTable.favorite, archived.getFavorite());
        val.put(Contract.ArchivedTable.classfor, archived.getClassfor());
        val.put(Contract.ArchivedTable.budget, archived.getBudget());
        val.put(Contract.ArchivedTable.status1, archived.getStatus1());
        val.put(Contract.ArchivedTable.enq_viewed, archived.getEnq_viewed());
        val.put(Contract.ArchivedTable.cursor, archived.getCursor());
        val.put(Contract.ArchivedTable.timestamp, archived.getTimestamp());
        val.put(Contract.ArchivedTable.sofTTL, System.currentTimeMillis() + "");

        db.insert(Contract.ArchivedTable.TABLE_NAME, null, val);
    }

    public Cursor getArchived(SQLiteDatabase readDb, long itemId) {
        String selection = Contract.ArchivedTable._ID + " > ?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = itemId + "";
        return readDb.query(Contract.ArchivedTable.TABLE_NAME, null, selection, selectionArgs, null,
                null, null, "15");
    }

    public void deleteArchived(SQLiteDatabase writeDb) {
        writeDb.delete(Contract.ArchivedTable.TABLE_NAME, null, null);
    }

    //classes table methods
    public void addClasses(SQLiteDatabase writeDb, Classes_Model cls) {
        ContentValues values = new ContentValues();
        values.put(Contract.ClassesTable.CLASSES_NAME, cls.getStudentscla());
        values.put(Contract.ClassesTable.CLASS_PIC_URL, cls.getImage());
        values.put(Contract.ClassesTable.FILTER_CLS, cls.getFilterCls());
        values.put(Contract.ClassesTable.SOFT_TTL, System.currentTimeMillis() + "");

        long rows = writeDb.insert(Contract.ClassesTable.TABLE_NAME, null, values);
        Log.d(TAG, "addClasses: " + rows);
    }

    public Cursor getClasses(SQLiteDatabase readDb) {
        return readDb.query(Contract.ClassesTable.TABLE_NAME, null, null, null, null, null,
                null);
    }

    public void deleteClasses(SQLiteDatabase writeDb) {
        long rowsDeleted = writeDb.delete(Contract.ClassesTable.TABLE_NAME, null, null);
    }

    //classes for me response methods
    public void addClassesForMeResponse(ClassesForMe classesForMe) {
        //get access from SQLiteDatabase to write into LocalSQLite database of our app
        SQLiteDatabase writeDb = this.getWritableDatabase();
        //current timestamp which is use for time limit of classesForMe table row data. timeLimit = SOFT_TTL(Time to live) + 10 minutes(in milli)
        Calendar cal = Calendar.getInstance();
        String softTTL = String.valueOf(cal.getTimeInMillis() / 1000);
        String enqViewed = "0", feedback = "0";
        if(classesForMe.getEnq_viewed()) {
            enqViewed = "1";//1 represent true because SQLite not contain any boolean type keyword for storage type.
        } else if(classesForMe.getFeedback()) {
            feedback = "1";
        }

        ContentValues tableDataRowData = new ContentValues();
        tableDataRowData.put(Contract.ClassesForMeAPI.ENQ_ID, classesForMe.getTextEnq_id());
        tableDataRowData.put(Contract.ClassesForMeAPI.SOFT_TTL, softTTL);
        tableDataRowData.put(Contract.ClassesForMeAPI.TEXT_MIN, classesForMe.getTextMins());
        tableDataRowData.put(Contract.ClassesForMeAPI.TEXT_VIEWS, classesForMe.getTextViews());
        tableDataRowData.put(Contract.ClassesForMeAPI.TEXT_NAME, classesForMe.getTextName());
        tableDataRowData.put(Contract.ClassesForMeAPI.TEXT_TUTOR_REQUIREMENT, classesForMe.getTextTutorReq());
        tableDataRowData.put(Contract.ClassesForMeAPI.TEXT_BUDGET, classesForMe.getTextBudget());
        tableDataRowData.put(Contract.ClassesForMeAPI.TEXT_LOC, classesForMe.getTextLoc());
        tableDataRowData.put(Contract.ClassesForMeAPI.FAVORITE, classesForMe.getFavorite());
        tableDataRowData.put(Contract.ClassesForMeAPI.TEXT_DIS, classesForMe.getTextDis());
        tableDataRowData.put(Contract.ClassesForMeAPI.STATUS, classesForMe.getStatus());
        tableDataRowData.put(Contract.ClassesForMeAPI.PAYMENT_STATUS, classesForMe.getPaymentstatus());
        tableDataRowData.put(Contract.ClassesForMeAPI.ENQ_VIEWED, enqViewed);
        tableDataRowData.put(Contract.ClassesForMeAPI.FEEDBACK, feedback);
        tableDataRowData.put(Contract.ClassesForMeAPI.HIGH_COMP, classesForMe.getHighComp());
        tableDataRowData.put(Contract.ClassesForMeAPI.FREE_CLASS, classesForMe.getFreeClass());
        tableDataRowData.put(Contract.ClassesForMeAPI.STUDENT_UUID, classesForMe.getStudentUUId());
        tableDataRowData.put(Contract.ClassesForMeAPI.TUTOR_UUID, classesForMe.getTutorUUId());
        tableDataRowData.put(Contract.ClassesForMeAPI.PIC_URL, classesForMe.getPicUrl());
//        Log.v("dbHandler.java", tableDataRowData.toString());

        long newRowId = writeDb.insert(Contract.ClassesForMeAPI.TABLE_NAME, null, tableDataRowData);
        Log.v("dbHandler.java", newRowId + "soft: " + softTTL);
        writeDb.close();
    }

    public Cursor getClassesForMeResponse() {
        //Get readable access to LocalSQLite database
        SQLiteDatabase readDb = this.getReadableDatabase();
        Cursor cursor = readDb.query(Contract.ClassesForMeAPI.TABLE_NAME, null, null,
                null, null, null, null);
//        Cursor cursor = readDb.rawQuery("SELECT * FROM " + Contract.ClassesForMeAPI.TABLE_NAME, null);
//        cursor.close();
        return cursor;
    }

    public void deleteClassesForMeResponse() {
        //getWritable database access of LocalSQLite
        SQLiteDatabase writeDb = this.getWritableDatabase();
        //First delete old classesForMe api response rows from table using delete() and it returns how much row are deleted
        //and you can use selection String variable to add filter to delete by column and selectionArgs String type array to delete row
        //according to column values.
        long totalRowDeleted = writeDb.delete(Contract.ClassesForMeAPI.TABLE_NAME, null, null);
        Log.v("dbHandler.java", totalRowDeleted + " deleted");
        writeDb.close();
    }
}
