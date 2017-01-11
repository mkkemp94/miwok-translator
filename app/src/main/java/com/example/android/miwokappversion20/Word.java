package com.example.android.miwokappversion20;

/**
 * Created by kempm on 12/15/2016.
 */

public class Word {

    // Default translation of the word
    private String mDefaultTranslation;

    // Miwok translation of the word
    private String mMiwokTranslation;

    // Image associated with this word (start off with none)
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    // Recording associated with this word
    private int mMediaResourceId;

    /**
     * Constructor if image exists
     * @param defaultTranslation
     * @param miwokTranslation
     * @param imageResourceId
     */
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId, int mediaResourceId) {
        this.mDefaultTranslation = defaultTranslation;
        this.mMiwokTranslation = miwokTranslation;
        this.mImageResourceId = imageResourceId;
        this.mMediaResourceId = mediaResourceId;
    }

    /**
     * Constructor if no image exists
     * @param defaultTranslation
     * @param miwokTranslation
     */
    public Word(String defaultTranslation, String miwokTranslation, int mediaResourceId) {
        this.mDefaultTranslation = defaultTranslation;
        this.mMiwokTranslation = miwokTranslation;
        this.mMediaResourceId = mediaResourceId;
    }

    /**
     * Get the default translation of the word.
     * @return a string holding the default translation
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Get the Miwok tranlation of the word.
     * @return a string holding the miwok translation
     */
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    /**
     * Get the image associated with this word object.
     * @return an int specifying the path of the word's image
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Return whether or not there is an image for this word
     * @return whether or not this word has an image
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    /**
     * Get the recorded pronunciation of the word.
     * @return an int specifying the path to the word's audio sample
     */
    public int getMediaResourceId() { return mMediaResourceId; }

    /**
     * With this I can print out the object as a string
     * @return all of the objects attributes and their values represented as a string
     */
    @Override
    public String toString() {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mMediaResourceId=" + mMediaResourceId +
                ", mImageResourceId=" + mImageResourceId +
                '}';
    }
}
