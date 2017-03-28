package nl.avans.android.favourites.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nl.avans.android.favourites.domain.Person;

/**
 * Created by dkroeske on 9/16/15.
 */
public class PersonDBHandler extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "person.db";
    private static final String DB_TABLE_NAME = "persons";

    // Tabel en kolom namen ...
    private static final String COLUMN_ID = "_id";  // primary key, auto increment
    private static final String COLUMN_FIRSTNAME = "firstName";
    private static final String COLUMN_LASTNAME = "lastName";
    private static final String COLUMN_EMAIL_ADDRESS = "emailadress";
    private static final String COLUMN_IMAGEURL = "imageUrl";
    private static final String COLUMN_IS_FAVORITE = "isFavorite";

    // Default constructor
    public PersonDBHandler(Context context, String name,
                           SQLiteDatabase.CursorFactory factory,
                           int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    // Alternatieve constructor
    public PersonDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Als de db niet bestaat wordt de db gemaakt. In de onCreate() de query
    // voor de aanmaak van de database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PERSON_TABLE = "CREATE TABLE " + DB_TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_FIRSTNAME + " TEXT," +
                COLUMN_LASTNAME + " TEXT," +
                COLUMN_IMAGEURL + " TEXT," +
                COLUMN_EMAIL_ADDRESS + " TEXT," +
                COLUMN_IS_FAVORITE + " INTEGER" +
                ")";
        db.execSQL(CREATE_PERSON_TABLE);
    }

    // Bij verandering van de db (verhoging van version) wordt onUpgrade aangeroepen.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        onCreate(db);
    }

    // Er bestaat ook een onDowngrade - aangeroepen bij verlaging van version.
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        onCreate(db);
    }

    //
    // CRUD functies op Person object hier
    //

    /**
     * Voeg een pesoon toe aan de database.
     *
     * @param person
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public Long addPerson(Person person) {
        Log.i(TAG, "addPerson " + person);

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRSTNAME, person.getFirstName());
        values.put(COLUMN_LASTNAME, person.getLastName());
        values.put(COLUMN_IMAGEURL, person.getImageUrl());
        values.put(COLUMN_EMAIL_ADDRESS, person.getEmailAddress());
        values.put(COLUMN_IS_FAVORITE, person.isFavorite());

        SQLiteDatabase db = this.getWritableDatabase();
        Long _id = db.insert(DB_TABLE_NAME, null, values);
        // Bewaar ID bij persoon; nodig bij zoeken of verwijderen.
        person.setId(_id);

        // check
        getPersonByFirstName(person.getFirstName());

        return _id;
    }

    /**
     * Verwijder de gegeven persoon
     *
     * @param person
     * @return Het aantal verwijderde rows, of 0 als niets verwijderd is.
     */
    public Integer deletePerson(Person person) {
        Log.i(TAG, "deletePerson " + person);

        SQLiteDatabase db = this.getWritableDatabase();

        // Hier moet je zeker weten dat je de juiste person verwijdert.
        return db.delete(DB_TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[] { person.getId().toString() });
    }

    /**
     * Geef alle personen uit de database terug.
     *
     * @return
     */
    public List<Person> getAllPersons() {
        Log.i(TAG, "getAllPersons");

        // Als dit er heel veel zouden zijn - 1000 of meer -
        // kun je de records beter in stukjes ophalen. Dan moet je
        // dus een aantal en startpunt meegeven m.b.v. LIMIT (aantal en startpunt).
        String query = "SELECT * FROM " + DB_TABLE_NAME + " LIMIT 100";
        Log.i(TAG, "Query: " + query);

        ArrayList<Person> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Person person = new Person();
            person.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRSTNAME)));
            person.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME)));
            person.setEmailAddress(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_ADDRESS)));
            person.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGEURL)));
            // person.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            person.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));

            int isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE));
            if(0 == isFavorite){
                person.setFavorite(false);
            } else {
                person.setFavorite(true);
            }

            Log.i(TAG, "Found " + person + ", adding to list");
            result.add(person);
        }

        db.close();
        Log.i(TAG, "Returning " + result.size() + " items");
        return result;
    }

    /**
     * Zoeken op voornaam. Deze methode retourneert alleen de eerst gevonden persoon.
     * We vinden dus niet alle personen met dezelfde voornaam... (mag je zelf maken)
     *
     * @param firstName Voornaam van persoon die we zoeken
     * @return 1 gevonden persoon.
     */
    public Person getPersonByFirstName(String firstName) {
        Log.i(TAG, "getPersonByFirstName " + firstName);

        String query = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " +
                COLUMN_FIRSTNAME + "=" + "'" + firstName + "'";
        Log.i(TAG, "Query: " + query);

        Person person = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            person = new Person();
            person.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRSTNAME)));
            person.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME)));
            person.setEmailAddress(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_ADDRESS)));
            person.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGEURL)));
            // person.setEmailAddress(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            int isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE));
            if(0 == isFavorite){
                person.setFavorite(false);
            } else {
                person.setFavorite(true);
            }
            Log.i(TAG, "Found " + person);
        } else {
            Log.e(TAG, "Error: person " + firstName + " not found");
        }
        db.close();
        return person;
    }
}
