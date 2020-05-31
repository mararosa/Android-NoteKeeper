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

    //Constructor that accepts a Parcel as a parameter
    //Pribate to make sure it cannot be called from outside of our NoteInfo class
    private NoteInfo(Parcel parcel) {
        //First value we wanna be back
        mCourse = parcel.readParcelable(CourseInfo.class.getClassLoader());
        mTitle = parcel.readString();
        mText = parcel.readString();
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

    //Use when we have a special parcelling needs, otherwise return zero. Identifies any special handling needs.
    @Override
    public int describeContents() {
        return 0;
    }

    //Responsible to write the member information for the type instance into the Parcel and recevies a parcelable as a parameter
   //Handle the details of writing our content into the Parcel.
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        //Do: Write each member of NoteInfo into that Parcel
        //Course is a reference type, that means that course is going to have to be Parcelable
        parcel.writeParcelable(mCourse, 0); //zero is because we dont need a special handling
        parcel.writeString(mTitle);
        parcel.writeString(mText);
    }

    //Indicating CREATOR is able to creat instances of NoteInfo
    public static final Parcelable.Creator<NoteInfo> CREATOR =
            //Use to create new instaces of our NoteInfo class
            new Parcelable.Creator<NoteInfo>() {
                @Override
                public NoteInfo createFromParcel(Parcel parcel) {
                    return new NoteInfo(parcel); //passing a parcel as a constructor argument
                }

                @Override
                public NoteInfo[] newArray(int size) {
                    return new NoteInfo[size];
                }
            };

}
