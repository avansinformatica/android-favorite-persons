package nl.avans.android.favourites.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import nl.avans.android.favourites.domain.Person;

/**
 * Deze class haalt asynchroon een of meer users op via de gegeven API.
 * Het aantal personen dat opgehaald wordt kun je aangeven in de URL.
 * Zie daarvoor de Activity die deze class gebruikt (MainActivity).
 */
public class RandomUsersTask extends AsyncTask<String, Void, String>  {

    // Call back, ontvangt het resultaat.
    private OnRandomUsersAvailable listener = null;

    // De log string.
    private static final String TAG = RandomUsersTask.class.getSimpleName();

    // De api string wordt gezet vanuit de parameters in doInBackground.
    private String urlApiString = "";

    /**
     * Constructor, set listener
     */
    public RandomUsersTask(OnRandomUsersAvailable listener) {
        this.listener = listener;
    }

    /**
     * doInBackground is de methode waarin de aanroep naar een service op het Internet gedaan wordt.
     *
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responseCode = -1;

        // Het resultaat dat we gaan retourneren
        String response = "";

        // We zouden meerder parameters in een array kunnen krijgen.
        // We gebruiken in ons geval echter alleen de eerste parameter.
        urlApiString = params[0];
        Log.i(TAG, "doInBackground '" + urlApiString + "'");

        // Maak verbinding met de urlApiString en haal het response op
        try {
            URL url = new URL(urlApiString);
            URLConnection urlConnection = url.openConnection();

            // Als het niet gelukt is om een URL connection op te zetten moeten we stoppen
            if (!(urlConnection instanceof HttpURLConnection)) {
                Log.e(TAG, "Niet gelukt om een URL connectie te maken!");
                return null;
            }

            // Initiëer de connectie en maak verbinding
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // De verbinding is gelukt. Lees hier de data.
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                // Log.i(TAG, "doInBackground response = " + response);
            }
        } catch (MalformedURLException e) {
            // De URL was niet correct geformuleerd.
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            // Het lukte niet om verbinding te maken.
            Log.e("TAG", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }
        // Hier hebben we een resultaat - geef dat door aan onPostExecute.
        return response;
    }

    /**
     * onPostExecute verwerkt het resultaat uit de doInBackground methode.
     *
     * @param response Het response uit doInBackground.
     */
    protected void onPostExecute(String response) {

        Log.i(TAG, "onPostExecute " + response);

        // We gaan een lijst met personen opstellen.
        ArrayList<Person> results = new ArrayList<>();

        // Parse de JSON uit het resultaat, zodat we de velden
        // kunnen vinden die wij willen gebruiken.
        try {
            // Top level json object
            JSONObject jsonObject = new JSONObject(response);

            // Get all users and start looping
            JSONArray users = jsonObject.getJSONArray("results");
            for(int idx = 0; idx < users.length(); idx++) {
                // array level objects and get user
                JSONObject user = users.getJSONObject(idx);

                // Get title, first and last name
                JSONObject name = user.getJSONObject("name");
                String title = name.getString("title");
                String firstName = name.getString("first");
                String lastName = name.getString("last");

                String email = user.getString("email");
                Log.i(TAG, "Found " + title + " " + firstName + " " + lastName);

                // Get image url
                JSONObject picture = user.getJSONObject("picture");
                String imageurl = picture.getString("large");
                Log.i(TAG, imageurl);

                // Create new Person object
                Person person = new Person();
                person.setFirstName(firstName);
                person.setLastName(lastName);
                person.setTitle(title);
                person.setImageUrl(imageurl);
                person.setEmailAddress(email);

                results.add(person);
            }
        } catch( JSONException ex) {
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
        // call back with new person data
        Log.i(TAG, "Returning " + results.size() + " persons");
        listener.onUsersAvailable(results);
    }


    //
    // convert InputStream to String
    //
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
    
    /**
     * Call back interface
     * Deze moet worden geïmplementeerd door de activity die de RandomUsersTask gebruikt.
     */
    public interface OnRandomUsersAvailable {
        void onUsersAvailable(ArrayList<Person> persons);
    }
}


