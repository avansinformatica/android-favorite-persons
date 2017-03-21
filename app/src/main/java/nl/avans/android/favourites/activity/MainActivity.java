package nl.avans.android.favourites.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import nl.avans.android.favourites.domain.PersonAdapter;
import nl.avans.android.favourites.R;
import nl.avans.android.favourites.api.RandomUserTask;
import nl.avans.android.favourites.domain.Person;

public class MainActivity extends AppCompatActivity implements RandomUserTask.OnRandomUserAvailable,
        View.OnClickListener {

    // TAG for Log.i(...)
    private static final String TAG = "MainActivity";
    private static final String API_URL = "https://randomuser.me/api/";

    private Button addOnePersonBtn = null;
    private ListView personsListView = null;
    private ArrayList<Person> persons = new ArrayList<Person>();
    private PersonAdapter personAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Voeg de toolbar toe aan de bovenkant van de app
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Inflate UI and set listeners and adapters and ...
        personsListView = (ListView) findViewById(R.id.personslistView);
        personAdapter = new PersonAdapter(getApplicationContext(),
                getLayoutInflater(),
                persons);
        personsListView.setAdapter(personAdapter);

//        addOnePersonBtn = (Button) findViewById(R.id.addPersonButton);
//        addOnePersonBtn.setOnClickListener(this);
    }

    /**
     * onResume wordt aangeroepen als je vanuit een andere activity hier terugkeert.
     */
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.i(TAG, "onResume - we zijn terug in de " + TAG);
        personAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_favorites) {
            Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_favorites_gridview) {
            Intent intent = new Intent(getApplicationContext(), FavoritesGridViewActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.actionAddPerson) {
            RandomUserTask getRandomUser = new RandomUserTask(this);
            String[] urls = new String[] { API_URL };
            getRandomUser.execute(urls);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick(...)");
        // Connect and pass self for callback
        RandomUserTask getRandomUser = new RandomUserTask(this);
        String[] urls = new String[] { API_URL };
        getRandomUser.execute(urls);
    }

    /**
     * Dit is de callback methode die door de RandomUserTask wordt aangeroepen
     * zodra er een random user opgehaald is.
     *
     * @param person
     */
    @Override
    public void onRandomUserAvailable(Person person) {
        // Opslaag in array of mss wel in db?
        persons.add(person);
        Log.i(TAG, "Person added (" + person.toString() + ")");
        personAdapter.notifyDataSetChanged();
    }
}
