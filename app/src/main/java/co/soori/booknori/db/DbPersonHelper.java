package co.soori.personnori.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import co.soori.booknori.db.Person;

/**
 * Created by Soo-Young on 2018-03-11.
 */

public class DbPersonHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "personmanager";
    private static final String TABLE_NAME = "person";

    private static final String PERSON_ID = "person_id";
    private static final String PERSON_NAME = "person_name";
    private static final String PERSON_IMAGE = "person_image";
    private static final String PERSON_STORY = "person_story";

    public DbPersonHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_PERSONS_TABLE = "CREATE TABLE "+ TABLE_NAME + "("
                + PERSON_ID + " INTEGER PRIMARY KEY, "
                + PERSON_NAME + " TEXT, "
                + PERSON_IMAGE + " TEXT, "
                + PERSON_STORY + "TEXT)";
        db.execSQL(CREATE_PERSONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addPerson(Person person){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PERSON_NAME, person.getName());
        values.put(PERSON_STORY, person.getStory());
        values.put(PERSON_IMAGE, person.getImage());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Person getPerson(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {PERSON_ID,
                        PERSON_NAME, PERSON_STORY, PERSON_IMAGE},
                PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        Person person = new Person(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3));

        return person;
    }

    public List<Person> getAllPersons(){
        List<Person> personList = new ArrayList<Person>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Person person = new Person();
                person.setId(Integer.parseInt(cursor.getString(0)));
                person.setName(cursor.getString(1));
                person.setStory(cursor.getString(3));
                personList.add(person);
            }while(cursor.moveToNext());
        }
        return personList;
    }

    public int updatePerson(Person person){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PERSON_NAME, person.getName());
        values.put(PERSON_STORY, person.getStory());
        values.put(PERSON_IMAGE, person.getImage());

        return db.update(TABLE_NAME, values, PERSON_ID + " =? ",
                new String[] {String.valueOf(person.getId())});
    }

    public void deletePerson(Person person){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PERSON_ID + " =? ",
                new String[] {String.valueOf(person.getId())});
        db.close();
    }

    public int getPersonsCount(){
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

}
