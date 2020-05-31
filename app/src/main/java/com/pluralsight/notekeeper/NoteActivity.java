package com.pluralsight.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        //we only want to display the note if there is actually a note
        if(!mIsNewNote)
        displayNotes(mSpinnerCoursers, mTextNoteTitle, mTextNoteText);
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

    //NoteActivity is where we actually get the note out of the intent that was passed to us and display within a screen
    private void readDisplayStateValues() {
        //Getting a reference to the intent that was used to start this activity
        Intent intent = getIntent();
        //Get the extra containing the note from it. Note_info is the name of the extra
        //-1 indicate that the extra was not found in the intent
        //Get the position from intent extra
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        //make sure that the position is actually set, reach out the data manager and get the note at that position and put it into my field
        mIsNewNote = position == POSITION_NOT_SET;
        if(!mIsNewNote)
            mNote = DataManager.getInstance().getNotes().get(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

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
        String text = "Checkout what I learned in the Pluralsight course \"" + course.getTitle() + "\"\n" + mTextNoteText.getText();
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
