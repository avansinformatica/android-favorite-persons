package nl.avans.android.favourites.domain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import nl.avans.android.favourites.activity.FavoritesActivity;
import nl.avans.android.favourites.api.ImageLoader;
import nl.avans.android.favourites.data.PersonDBHandler;
import nl.avans.android.favourites.R;

/**
 * Created by dkroeske on 9/16/15.
 */
public class PersonAdapter extends ArrayAdapter<Person> {

    // TAG for Log.i(...)
    private static final String TAG = PersonAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList mPersonArrayList;
    private PersonDBHandler personDBHandler;

    private FavoritesActivity.VIEWMODE VIEWMODE = FavoritesActivity.VIEWMODE.LISTVIEW;

    /**
     * Constructor gebruikt vanuit Fragments.
     *
     * @param context
     * @param personArrayList
     */
    public PersonAdapter(Context context, ArrayList<Person> personArrayList)
    {
        super(context, 0, personArrayList);
        mContext = context;
        mPersonArrayList = personArrayList;
        personDBHandler = new PersonDBHandler(context);
    }

    /**
     * Constructor gebruikt vanuit Fragments.
     *
     * @param context
     * @param personArrayList
     */
    public PersonAdapter(Context context, ArrayList<Person> personArrayList, FavoritesActivity.VIEWMODE viewmode)
    {
        super(context, 0, personArrayList);
        mContext = context;
        mPersonArrayList = personArrayList;
        personDBHandler = new PersonDBHandler(context);
        VIEWMODE = viewmode;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        Log.i(TAG, "getView " + position);

        final ViewHolder viewHolder;

        // Create new of gebruik een al bestaande (recycled by Android)
        // The old view to reuse, if possible. Note: You should check
        // that this view is non-null and of an appropriate type before using. If
        // it is not possible to convert this view to display the correct data,
        // this method can create a new view.
        if(convertView == null) {

            viewHolder = new ViewHolder();

            // Als convertView nog niet bestaat maken we een nieuwe aan.
            // convertView = mInflator.inflate(R.layout.person_listview_row, null);
            // Let op: bij Fragments gebruik je deze variant, omdat je het scherm vanuit de context van
            // het Fragment wilt opbouwen.
            if(VIEWMODE == FavoritesActivity.VIEWMODE.LISTVIEW) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_listview_row, null);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_gridview_row, null);
            }
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.personRowImageView);
            // Zorg dat de afbeelding in GridView uitgerekt kan worden (moet groter plaatje zijn)
            viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.fullName = (TextView) convertView.findViewById(R.id.personRowFullName);
            viewHolder.emailAddress = (TextView) convertView.findViewById(R.id.personRowEmailAddress);
            viewHolder.imageCheckbox = (ImageView) convertView.findViewById(R.id.imageCheckbox);

            // Koppel de view aan de viewHolder
            convertView.setTag(viewHolder);
        } else {
            // Als de convertView wel bestaat gebruiken we die.
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // En nu de viewHolder vullen met de juiste person
        final Person person = (Person) mPersonArrayList.get(position);

        viewHolder.fullName.setText(person.getFirstName() + " " + person.getLastName());
        viewHolder.fullName.setAllCaps(true);

        // EmailAddress, met elipses aan het einde als tekst te lang is
        viewHolder.emailAddress.setText(person.getEmailAddress());
//        viewHolder.emailAddress.setEllipsize(TextUtils.TruncateAt.END);

        new ImageLoader(viewHolder.imageView).execute(person.getImageUrl());

        if (person.isFavorite()){
            viewHolder.imageCheckbox.setImageResource(R.drawable.ic_check_box_black_24dp);
       } else {
            viewHolder.imageCheckbox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }

        // Handel het selecteren/deselecteren af
        // Deze PersonAdapter wordt in meerdere ListViews gebruikt: in de lijst met nieuw
        // toegevoegde personen, en in de lijst met Favorites.
        viewHolder.imageCheckbox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Verander isFavorite: true wordt false, false wordt true
                person.setFavorite(!person.isFavorite());
                if(person.isFavorite()) {
                    // addPerson geeft de rowID van de person in de database terug, of -1 bij error.
                    if(personDBHandler.addPerson(person) > -1) {
                        // Zet de juiste checkbox image
                        viewHolder.imageCheckbox.setImageResource(R.drawable.ic_check_box_black_24dp);
                        // Laat een bericht zien dat het toevoegen gelukt is
                        Toast.makeText(mContext, R.string.toast_person_added, Toast.LENGTH_SHORT).show();
                    } else {
                        // Het toevoegen is niet gelukt!
                        Toast.makeText(mContext, R.string.toast_person_added_error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(personDBHandler.deletePerson(person) == 1) {
                        // Zet de juiste checkbox image
                        viewHolder.imageCheckbox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                        // Als we in de Favorites lijst zitten verwijderen we deze persoon
                        // ook uit de lijst.
                        if (parent.getId() == R.id.favoritesListView)
                            mPersonArrayList.remove(position);
                            notifyDataSetChanged();
                        // Laat een bericht zien dat het verwijderen gelukt is
                        Toast.makeText(mContext, R.string.toast_person_deleted, Toast.LENGTH_SHORT).show();
                    } else {
                        // Het verwijderen is niet gelukt!
                        Toast.makeText(mContext, R.string.toast_person_deleted_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return convertView;
    }

    // Holds all data to the view. Wordt evt. gerecycled door Android
    private static class ViewHolder {
        public ImageView imageView;
        public ImageView imageCheckbox;
        public TextView fullName;
        public TextView emailAddress;
        public Button addToFavoritesBtn;
    }
}
