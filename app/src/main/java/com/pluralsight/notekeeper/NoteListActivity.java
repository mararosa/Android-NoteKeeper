package com.pluralsight.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private ArrayAdapter<NoteInfo> mAdapterNotes;

    /**
     * Parte do processo de criar a activity. Only gets called when the activity is first created
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

    //Set our ListView, took an ArrayAdapter, loaded it up with the notes that we had in our DataManager and then display those in the ListView
    private void initializeDisplayContent() {
        //Dentro da listview add toda a lista de anotacoes)notes)
        //1 coisa: pegar a referencia da list view. Se marcamos o listView como uma variavel final podemos referenciar ela dentro da classe anonima
        final ListView listNotes = findViewById(R.id.list_notes);
        //***Get a reference from noteinfo. Datamanager is how we populate the activity. We got all the notes, and then loaded int an adapter(which then populate our ListView)
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        // Get the list notes, put into the listview with an adapter
        mAdapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        listNotes.setAdapter(mAdapterNotes);

        //***So when the user makes a selection we get the position passed into us, and ten using that position we get the actual NoteInfo. And that is we put in to the intent
        //Handle the user making a selection
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch the activity when the user makes that selection. To do it, we need an intent
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
                //get the NoteInfo that corresponds to their selection.
                // Declare a local variable (note). Assinar o resultado para obter a posicao de um item d listview
                //Pegamos a posicao que o usuario selecionou
//                NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
                //When the user makes a selection from the list, the note that they selected is now packaged up int that intent and then sent over to our NoteActivity.
                intent.putExtra(NoteActivity.NOTE_POSITION, position);

                startActivity(intent);
            }
        });
    }

}
