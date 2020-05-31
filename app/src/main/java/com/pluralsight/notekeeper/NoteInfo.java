package com.pluralsight.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Got members from the course, title e text from the note
 */
public final class NoteInfo implements Parcelable {
    private CourseInfo mCourse;
    private String mTitle;
    private String mText;

    public NoteInfo(CourseInfo course, String title, String text) {
        mCourse = course;
        mTitle = title;
        mText = text;
    }

    public CourseInfo getCourse() {
        return mCourse;
    }

    public void setCourse(CourseInfo course) {
        mCourse = course;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }


    private String getCompareKey() {
        return mCourse.getCourseId() + "|" + mTitle + "|" + mText;  //Just a way to find identifying information for it
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteInfo that = (NoteInfo) o;

        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    //Return a note, we will populate the list items
    @Override
    public String toString() {
        return getCompareKey();
    }

    //Use when we have a special parcelling needs, otherwise return zero
    @Override
    public int describeContents() {
        return 0;
    }

    //Responsible to write the member information for the type instance into the Parcel and recevies a parcelable as a parameter
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
