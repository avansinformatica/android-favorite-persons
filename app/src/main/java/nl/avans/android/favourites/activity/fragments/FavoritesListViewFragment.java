package nl.avans.android.favourites.activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import nl.avans.android.favourites.R;
import nl.avans.android.favourites.activity.FavoritesActivity;
import nl.avans.android.favourites.data.PersonDBHandler;
import nl.avans.android.favourites.domain.Person;
import nl.avans.android.favourites.domain.PersonAdapter;

public class FavoritesListViewFragment extends Fragment implements AdapterView.OnItemClickListener{

    // TAG for Log.i(...)
    private final String TAG = this.getClass().getSimpleName();

    private ListView favoritesListView;
    private GridView favoritesGridView;
    private ArrayList<Person> favoritePersons = new ArrayList<Person>();
    private PersonAdapter personListViewAdapter, personGridViewAdapter;
    private PersonDBHandler personDBHandler;

    // Verplichte empty constructor
    public FavoritesListViewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of this fragment.
     */
    public static FavoritesListViewFragment newInstance(String param1, String param2) {
        FavoritesListViewFragment fragment = new FavoritesListViewFragment();
        // Als je data mee wilt sturen naar het fragment kan dat hier
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Hier initialiseren we de data in het fragment.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        // Maak een koppeling naar de database
        personDBHandler = new PersonDBHandler(getContext());
        // Alle personen in de database zijn favorites, dus we hoeven niet
        // te zoeken naar specifieke personen.
        favoritePersons = (ArrayList<Person>) personDBHandler.getAllPersons();
        Log.i(TAG, "We hebben " + favoritePersons.size() + " favourites");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Inflate UI and set listeners and adapters and ...
        favoritesListView = (ListView) view.findViewById(R.id.favoritesListView);
        favoritesGridView = (GridView) view.findViewById(R.id.favoritesGridView);

        personListViewAdapter = new PersonAdapter(getContext(), favoritePersons, FavoritesActivity.VIEWMODE.LISTVIEW);
        personGridViewAdapter = new PersonAdapter(getContext(), favoritePersons, FavoritesActivity.VIEWMODE.GRIDVIEW);

        favoritesListView.setAdapter(personListViewAdapter);
        favoritesGridView.setAdapter(personGridViewAdapter);
        favoritesGridView.setNumColumns(2);

        favoritesListView.setOnItemClickListener(this);
        favoritesGridView.setOnItemClickListener(this);

        favoritesListView.setVisibility(View.VISIBLE);
        favoritesGridView.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent details = new Intent(getContext(), ScrollingActivity.class);
        startActivity(details);
    }
}
