package com.pluralsight.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    //Intent extras are name-value pairs and those names of course are string. So when use strings we generally want to be constants.
    //Colocamos a constante da activity que sera o destino do extra.
    public static final  String NOTE_POSITION = "com.pluralsight.notekeeper.NOTE_INFO";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCoursers;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;
    private boolean mIsCancelling;
    private NoteActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //When the activity initially created will get a brand-new ViewModel instance, but if our activity is destroyed due to  a configuration change, in that case, will get back the existing instance that we had before the activity was destroyed.
        //Create instance of ViewModelProvider. This method is boilerplate code, meaning that is code just type the same every time
        //getViewModelStore >>> where we wanna store ViewModels
        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        //Get our ViewMoldel. Pass the class information for our NoteActivityModel
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        mSpinnerCoursers = findViewById(R.id.spinner_courses);

        //Antes de criar o adapter precisamos pegar a lista de cursos
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        //Criar o adapter para popular o spinner (associar a lista ao spinner)
        //context this, recurso padrao do android: spinner item eh o recurso que usaremos para formatar
        // o item selecionado no spinner, e entao damos a informacao (courses) que sera coloca no spinner
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses); //build array adapter
        //Asssociar o resource que queremos usar para (drop down) uma lista
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Associar o adapter ao spinner
        mSpinnerCoursers.setAdapter((adapterCourses));

        readDisplayStateValues();
        //preserve the original value from of the note
        saveOriginalNoteValues();

        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        //we only want to display the note if there is actually a note
        if(!mIsNewNote)
            displayNotes(mSpinnerCoursers, mTextNoteTitle, mTextNoteText);
    }

    private void saveOriginalNoteValues() {
        //check if the note is null
        if (mIsNewNote)
            return;
        //Each course has a unique ID. Now a have the course ID of original note stored inside of our activity
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mOriginalNoteTitle = mNote.getTitle();
        mOriginalNoteText = mNote.getText();
    }

    //Activity will save any changes to the note when the user leaves a note.
    //When the user moves away from the activity, onPause will automatically be called
    //onPause calls saveNote, which takes all the values from the fiels on screen and puts them into our note
    @Override
    protected void onPause() {
        super.onPause();
        //check if we are cancelling
        if (mIsCancelling) {
            if (mIsNewNote) {
                //We only remove the note if it will canceling and a new note
                DataManager.getInstance().removeNote(mNotePosition);
            } else {
                storePreviousNoteValues();
            }
        } else {
            saveNote();
        }

    }

    //set each of the original values back
    private void storePreviousNoteValues() {
        //this give me a reference to the original course
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mOriginalNoteTitle);
        mNote.setText(mOriginalNoteText);
    }

    private void saveNote() {
        //set the values in a note I have a reference to.
        mNote.setCourse((CourseInfo) mSpinnerCoursers.getSelectedItem());
        //get the values of each of the text fields and set the title and the text on the note
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    private void displayNotes(Spinner spinnerCoursers, EditText textNoteTitle, EditText textNoteText) {
        //Pego a lista de cursos que esta no spinner
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        //We want to know the index of the course from our notes, so called getCourse.
        int courseIndex = courses.indexOf(mNote.getCourse());
        spinnerCoursers.setSelection(courseIndex);

        textNoteTitle.setText((mNote.getTitle()));
        textNoteText.setText((mNote.getText()));

    }

    //NoteActivity is where we actually get the note information out of the intent that was passed to us and display within a screen
    private void readDisplayStateValues() {
        //Getting a reference to the intent that was used to start this activity
        Intent intent = getIntent();
        //Get the extra containing the note from it. Note_info is the name of the extra
        //-1 indicate that the extra was not found in the intent
        //Get the position from intent extra
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        //make sure that the position is actually set, reach out the data manager and get the note at that position and put it into my field
        mIsNewNote = position == POSITION_NOT_SET;
        if(mIsNewNote) {
            createNewNote();

        } else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        //got the position of the note
        int mNotePosition = dm.createNewNote();
        //got the note at that position and assign it to my field mNote
        mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    //This method happens when user selects menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {
            sendEmail();
            return true;
        } else if (id == R.id.action_cancel) {
            mIsCancelling = true;
            //when user selects that menu option(cancel option) the activity will exit and return back the previous activity
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        //Going to use an implicit intent
        //We need the course information. Select the item for our spinner and store it in a local variable called course
        CourseInfo course = (CourseInfo) mSpinnerCoursers.getSelectedItem();
        //Create a subject for email. Assign that back value of the editText for our note title. It will return something called an editable, so use tostring to return string
        String subject = mTextNoteTitle.getText().toString();
        //Body of the email
        String text = "Hello!!! Checkout what I learned in the Pluralsight course: \"" + course.getTitle() + "\"\n" + mTextNoteText.getText();
        //creating the intent to do the send and an ACTION associate with email
        Intent intent = new Intent(Intent.ACTION_SEND);
        //This is a standard Internet mime type for sending email. Will identify a target
        intent.setType("message/rfc2822");
        //Component to handle the action. Provide the subject and the body
        intent.putExtra(Intent.EXTRA_SUBJECT, subject); //email title
        intent.putExtra(Intent.EXTRA_TEXT, text); //email body
        startActivity(intent);
    }
}
