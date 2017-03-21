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

    private static final String TAG = "PersonDBHandler";

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "person.db";
    private static final String DB_TABLE_NAME = "persons";

    // Tabel en kolom namen ...
    private static final String COLUMN_ID = "_id";  // primary key, auto increment
    private static final String COLUMN_FIRSTNAME = "firstName";
    private static final String COLUMN_LASTNAME = "lastName";
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
     */
    public void addPerson(Person person) {
        Log.i(TAG, "addPerson " + person);

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRSTNAME, person.getFirst());
        values.put(COLUMN_LASTNAME, person.getLast());
        values.put(COLUMN_IMAGEURL, person.getImageUrl());
        values.put(COLUMN_IS_FAVORITE, person.isFavorite());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DB_TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Verwijder de gegeven persoon
     *
     * @param person
     * @return
     */
    public Integer deleteUser(Person person) {
        Log.i(TAG, "deletePerson " + person);

        SQLiteDatabase db = this.getWritableDatabase();

        // Hier moet je zeker weten dat je de juiste person verwijdert.
        return db.delete(DB_TABLE_NAME,
                COLUMN_FIRSTNAME + " = ? ",
                new String[] { person.getFirst() });
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
            person.setFirst(cursor.getString(cursor.getColumnIndex(COLUMN_FIRSTNAME)));
            person.setLast(cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME)));
            person.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGEURL)));

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
     * Zoeken op voornaam.
     *
     * @param firstName
     */
    public void getPersonByFirstName(String firstName) {
        Log.i(TAG, "getPersonByFirstName " + firstName);

        String query = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " +
                COLUMN_FIRSTNAME + "=" + "\"" + firstName + "\"";
        Log.i(TAG, "Query: " + query);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while(cursor.moveToNext() ) {
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(COLUMN_FIRSTNAME)));
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME)));
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGEURL)));
            Log.i(TAG, "--------------------------------------------");
        }

        db.close();
    }

}
