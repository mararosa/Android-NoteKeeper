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
        //Save the values into the bundle. So when the activity's onSaveInstance method gets called, we will call our
        //SaveState method, we receive that Bundle, and then we write the value of our ORIGINALNOTECOURSEID, TITLE and TEXT into that Bundle
        //And the system will take care persisting that information even if our activity and viewmodel are destroyed
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }
}
