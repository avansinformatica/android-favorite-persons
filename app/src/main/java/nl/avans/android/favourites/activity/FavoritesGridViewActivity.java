package nl.avans.android.favourites.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import nl.avans.android.favourites.R;
import nl.avans.android.favourites.data.PersonDBHandler;
import nl.avans.android.favourites.domain.Person;
import nl.avans.android.favourites.domain.PersonAdapter;

public class FavoritesGridViewActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private PersonDBHandler personDBHandler;
    private ArrayList<Person> favoritePersons;
    private GridView favoritesGridView;
    private PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_grid_view);

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
        favoritesGridView = (GridView) findViewById(R.id.gridviewFavorites);
        personAdapter = new PersonAdapter(getApplicationContext(),
                getLayoutInflater(),
                favoritePersons);
        favoritesGridView.setAdapter(personAdapter);
    }
}
