package com.example.android.miwokappversion20;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_GAIN;
import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    // Plays pronunciations
    private MediaPlayer mediaPlayer;

    // Release media player on completion
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    // Handles audio focus
    private AudioManager audioManager;

    // On focus loss/gain, act accordingly
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback and seek to beginning
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AUDIOFOCUS_GAIN) {
                // Resume playback
                mediaPlayer.start();
            } else if (focusChange == AUDIOFOCUS_LOSS) {
                // Stop playback
                releaseMediaPlayer();
            }
        }
    };

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Request audio service
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // List of words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));

        // Create an adapter with the list of words
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        // Get the list view from xml
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Set it's data adapter to our custom adapter
        listView.setAdapter(adapter);

        // When a word is tapped
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Get word at this position
                Word thisWord = words.get(position);

                // Request audio focus for playback
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        // Use the music stream
                        AudioManager.STREAM_MUSIC,
                        // Request focus for a short time
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                // We now have audio focus!
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    // Free the player if it's already playing
                    releaseMediaPlayer();

                    // Play word
                    mediaPlayer = MediaPlayer.create(getActivity(), thisWord.getMediaResourceId());
                    mediaPlayer.start();

                    // Relese media player when finished
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });

        return rootView;
    }

    /**
     * Release resources taken up by media player.
     */
    private void releaseMediaPlayer() {

        // If the media player is not null, then it may be currently playing a sound
        if (mediaPlayer != null) {

            // Regardless of the current state of the media player, release its resources
            mediaPlayer.release();

            // Set the media player back to null
            mediaPlayer = null;

            // Audio doesn't need focus any more
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // Stop playing sound!
        releaseMediaPlayer();
    }
}
