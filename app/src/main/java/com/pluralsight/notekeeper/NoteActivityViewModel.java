package com.pluralsight.notekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {
    //we create string constant when we have to pass values into an intent
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.pluralsight.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE= "com.pluralsight.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.pluralsight.notekeeper.ORIGINAL_NOTE_TEXT";

    //Make the fields public so we can use them from other classes
    public String mOriginalNoteCourseId;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;

    public void saveState(Bundle outState) {
        //Save the values into the bundle

    }
}
