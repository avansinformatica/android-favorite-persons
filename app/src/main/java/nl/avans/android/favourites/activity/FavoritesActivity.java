package nl.avans.android.favourites.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import nl.avans.android.favourites.domain.PersonAdapter;
import nl.avans.android.favourites.data.PersonDBHandler;
import nl.avans.android.favourites.R;
import nl.avans.android.favourites.domain.Person;

public class FavoritesActivity extends AppCompatActivity {

    // TAG for Log.i(...)
    private final String TAG = this.getClass().getSimpleName();

    private ListView favoritesListView;
    private ArrayList<Person> favoritePersons = new ArrayList<Person>();
    private PersonAdapter personAdapter;
    private PersonDBHandler personDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Voeg de toolbar toe aan de bovenkant van de app
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle(getString(R.string.title_activity_favorites));
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Maak een koppeling naar de database
        personDBHandler = new PersonDBHandler(getApplicationContext());
        // Alle personen in de database zijn favorites, dus we hoeven niet
        // te zoeken naar specifieke personen.
        favoritePersons = (ArrayList<Person>) personDBHandler.getAllPersons();
        Log.i(TAG, "We hebben " + favoritePersons.size() + " favourites");

        // Inflate UI and set listeners and adapters and ...
        favoritesListView = (ListView) findViewById(R.id.favoritesListView);
        personAdapter = new PersonAdapter(getApplicationContext(),
                getLayoutInflater(),
                favoritePersons);
        favoritesListView.setAdapter(personAdapter);
    }

}
