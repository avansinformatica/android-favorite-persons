package nl.avans.android.favourites;

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

/**
 * Created by dkroeske on 9/15/15.
 */
public class RandomUserTask extends AsyncTask<String, Void, String>  {

    // Call back
    private OnRandomUserAvailable listener = null;

    // Statics
    private static final String TAG = RandomUserTask.class.getSimpleName();
    private static final String urlString = "https://randomuser.me/api/";

    // Constructor, set listener
    public RandomUserTask(OnRandomUserAvailable listener) {
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
        int responsCode = -1;

        // Het resultaat dat we gaan retourneren
        String response = "";

        for(String url : params) {
            Log.i(TAG, "doInBackground " + url);
        }

        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                // Url
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            responsCode = httpConnection.getResponseCode();

            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                // Log.i(TAG, "doInBackground response = " + response);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        return response;
    }

    /**
     * onPostExecute verwerkt het resultaat uit de doInBackground methode.
     *
     * @param response
     */
    protected void onPostExecute(String response) {

        Log.i(TAG, "onPostExecute " + response);

        // parse JSON and inform caller
        JSONObject jsonObject;

        try {
            // Top level json object
            jsonObject = new JSONObject(response);

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
                Log.i(TAG, "Got user " + title + " " + firstName + " " + lastName);

                // Get image url
                JSONObject picture = user.getJSONObject("picture");
                String imageurl = picture.getString("large");
                Log.i(TAG, imageurl);

                // Create new Person object
                Person p = new Person();
                p.setFirst(firstName);
                p.setLast(lastName);
                p.setTitle(title);
                p.setImageUrl(imageurl);
                p.setEmailAddress(email);

                // call back with new person data
                listener.onRandomUserAvailable(p);

            }
        } catch( JSONException ex) {
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
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

    // Call back interface
    public interface OnRandomUserAvailable {
        void onRandomUserAvailable(Person person);
    }
}


