package com.pluralsight.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final  String NOTE_INFO = "com.pluralsight.notekeeper.NOTE_INFO";
    private NoteInfo mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinnerCoursers = findViewById(R.id.spinner_courses);

        //Antes de criar o adapter precisamos pegar a lista de cursos
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        //Criar o adapter para popular o spinner (associar a lista ao spinner)
        //context this, recurso padrao do android: spinner item eh o recurso que usaremos para formatar
        // o item selecionado no spinner, e entao damos a informacao (courses) que sera coloca no spinner
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses); //build array adapter
        //Asssociar o resource que queremos usar para (drop down) uma lista
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Associar o adapter ao spinner
        spinnerCoursers.setAdapter((adapterCourses));

        readDisplayStateValues();

        EditText textNoteTitle = findViewById(R.id.text_note_title);
        EditText textNoteText = findViewById(R.id.text_note_text);

        displayNotes(spinnerCoursers, textNoteTitle, textNoteText);
    }

    private void displayNotes(Spinner spinnerCoursers, EditText textNoteTitle, EditText textNoteText) {
       List<CourseInfo> courses = DataManager.getInstance().getCourses();
       //We want to know the index of the course from our notes, so called getCourse.
       int courseIndex = courses.indexOf(mNote.getCourse());
       spinnerCoursers.setSelection(courseIndex);

        textNoteTitle.setText((mNote.getTitle()));
        textNoteText.setText((mNote.getTitle()));

    }

    private void readDisplayStateValues() {
        //Getting a reference to the intent that was used to start this activity
        Intent intent = getIntent();
        //Get the extra containing the note from it
        mNote = intent.getParcelableExtra(NOTE_INFO);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }
}
