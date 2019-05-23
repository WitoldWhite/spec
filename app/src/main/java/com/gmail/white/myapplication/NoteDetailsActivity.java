package com.gmail.white.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.gmail.white.myapplication.database.NotesDAO;
import com.gmail.white.myapplication.database.NotesDB;
import com.gmail.white.myapplication.model.Note;

import java.util.Date;

public class EditeNoteActivity extends AppCompatActivity {
    private EditText inputNote;
    private NotesDAO dao;
    private Note temp;
    public static final String NOTE_EXTRA_KEY = "note_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_note);
        inputNote = findViewById(R.id.input_note);
        dao = NotesDB.getInstance(this).notesDAO();
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            temp = dao.getNoteById(id);
            inputNote.setText(temp.getNoteText());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edite_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        String text = inputNote.getText().toString();
        if (!text.isEmpty()) {
            long date = new Date().getTime();
            if (temp==null){
                temp = new Note(text, date);
                dao.insertNote(temp);
            } else{
                temp.setNoteDate(date);
                temp.setNoteText(text);
                dao.updateNote(temp);
            }

            finish();
        }
    }

}
