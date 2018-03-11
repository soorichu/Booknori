package co.soori.booknori.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soo-Young on 2018-03-10.
 */

public class DbBookHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bookmanager";
    private static final String TABLE_NAME = "books";

    private static final String BOOK_ID = "book_id";
    private static final String BOOK_NAME = "book_name";
    private static final String BOOK_AUTHOR = "book_author";
    private static final String BOOK_IMAGE = "book_image";
    private static final String BOOK_STORY = "book_story";

    public DbBookHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_BOOKS_TABLE = "CREATE TABLE "+ TABLE_NAME + "("
                + BOOK_ID + " INTEGER PRIMARY KEY, "
                + BOOK_NAME + " TEXT, "
                + BOOK_AUTHOR + " TEXT, "
                + BOOK_IMAGE + " TEXT, "
                + BOOK_STORY + "TEXT)";
        db.execSQL(CREATE_BOOKS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BOOK_NAME, book.getName());
        values.put(BOOK_AUTHOR, book.getAuthor());
        values.put(BOOK_STORY, book.getStory());
        values.put(BOOK_IMAGE, book.getImage());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Book getBook(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {BOOK_ID,
                 BOOK_NAME, BOOK_AUTHOR, BOOK_STORY, BOOK_IMAGE},
                BOOK_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        Book book = new Book(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4));

        return book;
    }

    public List<Book> getAllBooks(){
        List<Book> bookList = new ArrayList<Book>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Book book = new Book();
                book.setId(Integer.parseInt(cursor.getString(0)));
                book.setName(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setStory(cursor.getString(3));
                bookList.add(book);
            }while(cursor.moveToNext());
        }
        return bookList;
    }

    public int updateBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOK_NAME, book.getName());
        values.put(BOOK_AUTHOR, book.getAuthor());
        values.put(BOOK_STORY, book.getStory());
        values.put(BOOK_IMAGE, book.getImage());

        return db.update(TABLE_NAME, values, BOOK_ID + " =? ",
                new String[] {String.valueOf(book.getId())});
    }

    public void deleteBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, BOOK_ID + " =? ",
                new String[] {String.valueOf(book.getId())});
        db.close();
    }

    public int getBooksCount(){
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }


}
