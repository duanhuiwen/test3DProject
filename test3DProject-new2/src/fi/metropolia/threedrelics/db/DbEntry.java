package fi.metropolia.threedrelics.db;

import android.provider.BaseColumns;

public class DbEntry implements BaseColumns{
	public static final String TABLE_NAME = "scenes";
    public static final String COLUMN_NAME_DOWNLOAD_ID = "download_id";
    public static final String COLUMN_NAME_SCENE_ID = "scene_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_MODEL_PATH = "path";
//  public static final String COLUMN_NAME_MODEL_URL = "url";
    public static final String COLUMN_NAME_DATE = "date";
//  public static final String COLUMN_NAME_DOWNLOAD_COMPLETE = "download_complete";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    
    public static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + DbEntry.TABLE_NAME + " (" +
        DbEntry._ID + " INTEGER PRIMARY KEY," +
        DbEntry.COLUMN_NAME_DOWNLOAD_ID + TEXT_TYPE + COMMA_SEP +
        DbEntry.COLUMN_NAME_SCENE_ID + INTEGER_TYPE + COMMA_SEP +
        DbEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
        DbEntry.COLUMN_NAME_MODEL_PATH + TEXT_TYPE + COMMA_SEP +
   //     DbEntry.COLUMN_NAME_MODEL_URL + TEXT_TYPE + COMMA_SEP +
   //     DbEntry.COLUMN_NAME_DOWNLOAD_COMPLETE + TEXT_TYPE + COMMA_SEP +
        DbEntry.COLUMN_NAME_DATE + TEXT_TYPE + 
        " )";

    public static final String SQL_DELETE_TABLE =
        "DROP TABLE IF EXISTS " + TABLE_NAME;
    
    
}
