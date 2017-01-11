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
public class ColorsFragment extends Fragment {

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

    public ColorsFragment() {
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

        words.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        // Create an adapter with the list of words
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_colors);

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
