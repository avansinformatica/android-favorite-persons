package nl.avans.android.favourites.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;
import nl.avans.android.favourites.api.RandomUsersTask;
import nl.avans.android.favourites.domain.PersonAdapter;
import nl.avans.android.favourites.R;
import nl.avans.android.favourites.domain.Person;

public class MainActivity extends AppCompatActivity
        implements RandomUsersTask.OnRandomUsersAvailable {

    // TAG for Log.i(...)
    private final String TAG = getClass().getSimpleName();

    // Label voor het overdragen van lijst van persons tussen schermen (Extras)
    private final String TAG_SAVED_PERSONS = "persons";

    // API URL waar we random personen ophalen
    // We moeten hier zelf nog het aantal users achteraan toevoegen - doen we
    // wanneer we de call uitvoeren.
    private final static String strApiURL = "https://randomuser.me/api/?results=";

    // Aantal personen dat we in één keer via de api ophalen.
    private final int AANTAL_PERSONEN = 15;

    private ListView personsListView = null;
    private ArrayList<Person> persons = new ArrayList<Person>();
    private PersonAdapter personAdapter = null;

    /**
     * onCreate wordt aangeroepen telkens wanneer deze activity opnieuw actief wordt.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Voeg de toolbar toe aan de bovenkant van de app
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        RandomUsersTask randomUsersTask = new RandomUsersTask(this);
        randomUsersTask.execute(strApiURL + AANTAL_PERSONEN);

        // Inflate UI and set listeners and adapters and ...
        personsListView = (ListView) findViewById(R.id.personslistView);
        personAdapter = new PersonAdapter(getApplicationContext(), persons);
        personsListView.setAdapter(personAdapter);
    }

    /**
     * Als je je device kantelt wordt de activity opnieuw opgebouwd en ben je je data kwijt.
     * Met deze methode kun je je data ondertussen bewaren.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        // construct a list of books you've favorited
        final ArrayList<Person> personList = new ArrayList<>();
        for (Person person: persons) {
                personList.add(person);
        }

        // save that list to outState for later
        outState.putSerializable(TAG_SAVED_PERSONS, personList);
    }

    /**
     * Als de activity opnieuw wordt opgebouwd en er is bewaarde data,
     * herlaad die dan hier.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);

        // get our previously saved list of persons
        final ArrayList<Person> personList = (ArrayList<Person>) savedInstanceState.getSerializable(TAG_SAVED_PERSONS);

        // look at all of your books and figure out which are the favorites
        for (Person person: personList) {
            persons.add(person);
        }
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

        // De parameters die we meesturen naar de RandomUsersTask.
        String[] params;

        // De Async task waarmee we users ophalen.
        // We geven een referentie naar onszelf mee(this) als callback
        RandomUsersTask randomUsersTask = new RandomUsersTask(this);

        switch (id){
            case R.id.action_favorites:
                intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_get_multiple_persons:
                // We halen in een keer AANTAL_PERSONEN op.
                params = new String[] { strApiURL + AANTAL_PERSONEN };
                randomUsersTask.execute(params);
                return true;
            case R.id.action_get_one_person:
                // We halen 1 persoon op en voegen die toe.
                params = new String[] { strApiURL + "1" };
                randomUsersTask.execute(params);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Dit is de callback methode die door de RandomUserTask wordt aangeroepen
     * zodra er een random user opgehaald is.
     *
     * @param
     */
    @Override
    public void onUsersAvailable(ArrayList<Person> list) {
        persons.clear();
        // persons = (ArrayList<Person>) list.clone();   // Raar, maar dit werkt niet...
        for(int i = 0; i < list.size(); i++) {
            persons.add(list.get(i));
        }
        Log.i(TAG, persons.size() + " persons available");
        personAdapter.notifyDataSetChanged();
    }
}
