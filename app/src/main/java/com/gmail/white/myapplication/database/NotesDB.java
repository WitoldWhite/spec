package com.gmail.white.myapplication.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.gmail.white.myapplication.model.Note;

@Database(entities = Note.class, version = 1)
public abstract class NotesDB extends RoomDatabase {

    public abstract NotesDAO notesDAO();
    public static final String DATABSE_NAME ="notesDb" ;

    private static NotesDB instance;

    public static NotesDB getInstance(Context context){
        if (instance==null)
            instance = Room.databaseBuilder(context,NotesDB.class,DATABSE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;

    }

}
