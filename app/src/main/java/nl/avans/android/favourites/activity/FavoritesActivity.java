package nl.avans.android.favourites.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import nl.avans.android.favourites.api.RandomUserTask;
import nl.avans.android.favourites.domain.PersonAdapter;
import nl.avans.android.favourites.data.PersonDBHandler;
import nl.avans.android.favourites.R;
import nl.avans.android.favourites.domain.Person;

public class FavoritesActivity extends AppCompatActivity {

    // TAG for Log.i(...)
    private final String TAG = this.getClass().getSimpleName();

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
    }

    /**
     * Deze methode toont het optionsmenu in de ActionBar.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    /**
     * Handelt een click op een item in het options menu af.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_toggle_favorites_view:
                //
                // Vervang hier de ene favorites view door de andere (ListView vs. GridView)
                //

                // En switch het icon
                
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
