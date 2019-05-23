package com.gmail.white.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.white.myapplication.adapters.NotesAdapter;
import com.gmail.white.myapplication.callbacks.MainActionModeCallback;
import com.gmail.white.myapplication.callbacks.NoteEventListener;
import com.gmail.white.myapplication.database.NotesDAO;
import com.gmail.white.myapplication.database.NotesDB;
import com.gmail.white.myapplication.model.Note;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.gmail.white.myapplication.EditeNoteActivity.NOTE_EXTRA_KEY;

public class MainActivity extends AppCompatActivity implements NoteEventListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private NotesDAO dao;
    private MainActionModeCallback mainActionModeCallback;
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNewNote();
            }
        });

        dao = NotesDB.getInstance(this).notesDAO();


    }

    private void loadNotes() {
        this.notes = new ArrayList<>();
        List<Note> list = dao.getNotes();
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this, this.notes);
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyView();
    }

    private void onAddNewNote() {
        startActivity(new Intent(this, EditeNoteActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteClick(Note note) {

        Intent edit = new Intent(this, EditeNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_KEY, note.getId());
        startActivity(edit);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onNoteLongClick(Note note) {
        note.setChecked(true);
        count = 1;
        adapter.setCheckMode(true);
        adapter.setListener(new NoteEventListener() {
            @Override
            public void onNoteClick(Note note) {
                note.setChecked(!note.isChecked());
                if (note.isChecked())
                    count++;
                else count--;

                mainActionModeCallback.setCountItem(count + "/" + notes.size());
                adapter.notifyDataSetChanged();
                if (count > 1) mainActionModeCallback.shareItemVisibility(false);
                else mainActionModeCallback.shareItemVisibility(true);

            }

            @Override
            public void onNoteLongClick(Note note) {

            }
        });
        mainActionModeCallback = new MainActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_delete_notes)
                    deleteMultiNotes();
                else if (item.getItemId() == R.id.action_share_notes)
                    shareNote();
                mode.finish();
                return false;
            }
        };
        startActionMode(mainActionModeCallback);
        fab.setVisibility(GONE);
        mainActionModeCallback.setCountItem(count + "/" + notes.size());


    }

    private void shareNote() {
        Note note = adapter.getCheckedNotes().get(0);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, note.getNoteText()+"\n\n " );
        startActivity(share);
    }

    private void deleteMultiNotes() {

        List<Note> checkedNotes = adapter.getCheckedNotes();
        if (checkedNotes.size() != 0) {
            for (Note note : checkedNotes) {
                dao.deleteNote(note);
            }
            // refresh Notes
            loadNotes();
            Toast.makeText(this, checkedNotes.size() + " SUCCESSFULLY!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "No Note(s) SELECTED", Toast.LENGTH_SHORT).show();


    }

    private void showEmptyView() {
        if (notes.size() == 0) {
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        adapter.setCheckMode(false);
        adapter.setListener(this);
        fab.setVisibility(View.VISIBLE);
    }
}
