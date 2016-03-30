package br.com.crowncap.projecteuler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ProblemDatabaseHandler extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "problems.db";
	public static final String TABLE_PROBLEMS = "problems";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_INFO = "info";
	public static final String COLUMN_TEXT = "text";

	public ProblemDatabaseHandler(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PROBLEMS_TABLE = "CREATE TABLE " +
	             TABLE_PROBLEMS + "("
	             + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TITLE 
	             + " TEXT," + COLUMN_INFO + " TEXT," + COLUMN_TEXT 
	             + " TEXT" + ")";
		db.execSQL(CREATE_PROBLEMS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROBLEMS);
	    onCreate(db);
	}
	
	public void addProblem(Problem problem) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, problem.getId());
        values.put(COLUMN_TITLE, problem.getTitle());
        values.put(COLUMN_INFO, problem.getInfo());
        values.put(COLUMN_TEXT, problem.getText());
 
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.insert(TABLE_PROBLEMS, null, values);
        db.close();
	}
	
	public void updateProblem(Problem problem) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, problem.getId());
        values.put(COLUMN_TITLE, problem.getTitle());
        values.put(COLUMN_INFO, problem.getInfo());
        values.put(COLUMN_TEXT, problem.getText());
 
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.update(TABLE_PROBLEMS, values, COLUMN_ID + " = " + problem.getId(), null);
        db.close();
	}
	
	public Problem findProblem(int id) {
		String query = "Select * FROM " + TABLE_PROBLEMS + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		Problem problem = new Problem();
		
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			problem.setId(Integer.parseInt(cursor.getString(0)));
			problem.setTitle(cursor.getString(1));
			problem.setInfo(cursor.getString(2));
			problem.setText(cursor.getString(3));
			cursor.close();
		} else {
			problem = null;
		}
	    db.close();
		return problem;
	}
	
	public boolean deleteProblem(int id) {
		
		boolean result = false;
		
		String query = "Select * FROM " + TABLE_PROBLEMS + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		Problem problem = new Problem();
		
		if (cursor.moveToFirst()) {
			problem.setId(Integer.parseInt(cursor.getString(0)));
			db.delete(TABLE_PROBLEMS, COLUMN_ID + " = ?",
		            new String[] { String.valueOf(problem.getId()) });
			cursor.close();
			result = true;
		}
	    db.close();
		return result;
	}
	
}
