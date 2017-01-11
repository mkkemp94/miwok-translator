package com.example.android.miwokappversion20;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kempm on 12/15/2016.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    // Holds the color of this list item
    private int mListItemColor;

    /**
     * Constructor
     *
     * @param context
     * @param words
     */
    public WordAdapter(Activity context, ArrayList<Word> words, int backgroundColor) {
        super(context, 0, words);

        mListItemColor = backgroundColor;
    }

    /**
     * Get and raw the list item view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Set the word at this position to a variable
        Word currentWord = getItem(position);

        // Set the miwok text view to show translation
        TextView miwokTextView = (TextView) listViewItem.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(currentWord.getMiwokTranslation());

        // Set the default text view to show translation
        TextView defaultTextView = (TextView) listViewItem.findViewById(R.id.default_text_view);
        defaultTextView.setText(currentWord.getDefaultTranslation());

        // Set the image view to show image (if it exists)
        ImageView imageView = (ImageView) listViewItem.findViewById(R.id.image_view);

        if (currentWord.hasImage()) {
            // If exists, set the image
            imageView.setImageResource(currentWord.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Otherwise hide the image view
            imageView.setVisibility(View.GONE);
        }

        // Get the view container that holds the text views
        View view = listViewItem.findViewById(R.id.text_container);

        // Translate color id into color
        int color = ContextCompat.getColor(getContext(), mListItemColor);

        // Set color to view
        view.setBackgroundColor(color);

        // Return updated list item view
        return listViewItem;
    }
}
