package com.pluralsight.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    /**
     * Parte do processo de criar a activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
            }
        });

        //Popular a listView
        initializeDisplayContent();
    }

    private void initializeDisplayContent() {
        //Dentro da listview add toda a lista de anotacoes)notes)
        //1 coisa: pegar a referencia da list view
        final ListView listNotes = findViewById(R.id.list_notes);
        //Get a reference from noteinfo
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        // Get the list notes, put into the listview with an adapter
        ArrayAdapter<NoteInfo> adapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        listNotes.setAdapter(adapterNotes);

        //Handle the user making a selection
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch the activity when the user makes that selection. To do it, we need an intent
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
                //get the NoteInfo that corresponds to their selection.
                // Declare a local variable (note)
                NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
                //When the user makes a selection from the list, the note that they selected is now packaged up int that intent and then sent over to our NoteActivity.
                intent.putExtra(NoteActivity.NOTE_INFO, note);

                startActivity(intent);
            }
        });
    }

}
