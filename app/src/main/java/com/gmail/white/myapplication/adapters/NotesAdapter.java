package com.gmail.white.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gmail.white.myapplication.R;
import com.gmail.white.myapplication.callbacks.NoteEventListener;
import com.gmail.white.myapplication.model.Note;
import com.gmail.white.myapplication.utils.NoteUtils;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private Context context;
    private ArrayList<Note> notes;
    private NoteEventListener listener;
    private boolean checkMode=false;
    private List<Note> checkedNotes;

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout,parent,false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder( NoteHolder holder, int position) {
        final Note note = getNote(position);
        if (note != null){
            holder.noteText.setText(note.getNoteText());
            holder.noteData.setText(NoteUtils.dateFromLong(note.getNoteDate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNoteClick(note);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onNoteLongClick(note);
                    return false;
                }
            });
        }

        if (checkMode){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(note.isChecked());
        } else holder.checkBox.setVisibility(View.GONE);

    }

    private Note getNote(int position){
        return notes.get(position);

    }

    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public List<Note> getCheckedNotes() {
        List<Note> checkedNotes = new ArrayList<>();
        for (Note n:this.notes) {
            if(n.isChecked())
                checkedNotes.add(n);
        }
        return checkedNotes;
    }

    public void setCheckMode(boolean checkMode) {
        this.checkMode = checkMode;
        notifyDataSetChanged();
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        TextView noteText;
        TextView noteData;
        CheckBox checkBox;

        public NoteHolder(View itemView) {
            super(itemView);
            noteData=itemView.findViewById(R.id.note_date);
            noteText=itemView.findViewById(R.id.note_text);
            checkBox=itemView.findViewById(R.id.checkBox);


        }
    }
}
