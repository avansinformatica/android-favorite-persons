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

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity
        implements RandomUserTask.OnRandomUserAvailable {

    // TAG for Log.i(...)
    private final String TAG = getClass().getSimpleName();
    private static final String API_URL = "https://randomuser.me/api/";
    // Label voor het saven van lijst van persons tussen schermen (Extras)
    private final String EXTRAS_PERSONS = "persons";

    private Button addOnePersonBtn = null;
    private ListView personsListView = null;
    private ArrayList<Person> persons = new ArrayList<Person>();
    private PersonAdapter personAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Voeg de toolbar toe aan de bovenkant van de app
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Inflate UI and set listeners and adapters and ...
        personsListView = (ListView) findViewById(R.id.personslistView);
        personAdapter = new PersonAdapter(getApplicationContext(),
                // getLayoutInflater(),
                persons);
        personsListView.setAdapter(personAdapter);
    }

    /**
     * onResume wordt aangeroepen als je vanuit een andere activity hier terugkeert.
     */
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.i(TAG, "onResume");

//        Bundle savedInstance = getIntent().getExtras();
//        if (savedInstance != null) {
//            Log.i(TAG, "savedInstance is niet null");
//            persons = (ArrayList<Person>) savedInstance.getSerializable(EXTRAS_PERSONS);
//            if(persons != null) {
//                Log.i(TAG, "persons.size = " + persons.size());
//                personsListView = (ListView) findViewById(R.id.personslistView);
//                personAdapter = new PersonAdapter(getApplicationContext(),
//                        getLayoutInflater(),
//                        persons);
//                personsListView.setAdapter(personAdapter);
//            }
//        }
    }

    /**
     * onStop wordt aangeroepen wanneer je vanuit de huidige activitiy naar een andere
     * activity gaat.
     */
    @Override
    protected void onStop() {
        // call the superclass method first
        Log.i(TAG, "onStop");
        super.onStop();
//        Bundle savedInstance = new Bundle();
//        savedInstance.putSerializable(EXTRAS_PERSONS, persons);
//        getIntent().putExtras(savedInstance);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        Intent intent;

        switch (id){
            case R.id.action_favorites:
                intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                startActivity(intent);
                break;
            case R.id.actionAddPerson:
                RandomUserTask getRandomUser = new RandomUserTask(this);
                String[] urls = new String[] { API_URL };
                getRandomUser.execute(urls);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Dit is de callback methode die door de RandomUserTask wordt aangeroepen
     * zodra er een random user opgehaald is.
     *
     * @param person
     */
    @Override
    public void onRandomUserAvailable(Person person) {
        persons.add(person);
        Log.i(TAG, "Person added (" + person.toString() + ")");
        personAdapter.notifyDataSetChanged();
    }
}
