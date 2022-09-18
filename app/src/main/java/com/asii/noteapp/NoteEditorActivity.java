package com.asii.noteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {
    EditText et_note;
    int noteId;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        ActionBar actionBar = getSupportActionBar();

        sharedPreferences = this.getSharedPreferences("com.asii.simplenoteapp", Context.MODE_PRIVATE);
        et_note = findViewById(R.id.et_note);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1){
            et_note.setText(MainActivity.notesList.get(noteId));
            actionBar.setTitle("Edit Note");
        }else{
            MainActivity.notesList.add("");
            noteId = MainActivity.notesList.size() - 1;
            actionBar.setTitle("Add Note");
        }

        et_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    MainActivity.notesList.set(noteId, String.valueOf(charSequence));
                    MainActivity.adapter.notifyDataSetChanged();

                HashSet<String> noteSet = new HashSet<>(MainActivity.notesList);
                sharedPreferences.edit().putStringSet("notes",noteSet).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if (item.getItemId() == R.id.save_note){
             startActivity(new Intent(getApplicationContext(), MainActivity.class));
             finish();
             return true;
         }
         return false;
    }
}