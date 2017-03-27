package nl.avans.android.favourites.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import nl.avans.android.favourites.R;

public class FavoritesActivity extends AppCompatActivity {

    // TAG for Log.i(...)
    private final String TAG = this.getClass().getSimpleName();
    // Het switcherIcon dat LisView/GridView toont
    private ImageView switcherIcon;
    private ListView favoritesListView;
    private GridView favoritesGridView;

    // De mogelijke viewmodes
    public static enum VIEWMODE { GRIDVIEW, LISTVIEW };
    // Dit is de initiële viewmode.
    private VIEWMODE viewMode = VIEWMODE.LISTVIEW;

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
        // Log.i(TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_toggle_favorites_view:
                Log.i(TAG, "switcher clicked");

                // Inflate UI and set listeners and adapters and ...
                favoritesListView = (ListView) findViewById(R.id.favoritesListView);
                favoritesGridView = (GridView) findViewById(R.id.favoritesGridView);

                // Hier switchen we tussen ListView en GridView
                if(viewMode == VIEWMODE.LISTVIEW) {
                    viewMode = VIEWMODE.GRIDVIEW;
                    favoritesListView.setVisibility(View.GONE);
                    favoritesGridView.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.ic_view_list_black_24dp);
                } else {
                    viewMode = VIEWMODE.LISTVIEW;
                    favoritesListView.setVisibility(View.VISIBLE);
                    favoritesGridView.setVisibility(View.GONE);
                    item.setIcon(R.drawable.ic_view_module_black_24dp);
                }
                // Actie afgehandeld, wij zijn klaar.
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}
