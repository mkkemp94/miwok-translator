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
public class PhrasesFragment extends Fragment {

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

    public PhrasesFragment() {
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

        words.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));

        // Create an adapter with the list of words
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

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
