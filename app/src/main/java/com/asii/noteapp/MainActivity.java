package com.asii.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView notes_listview;
    TextView tv_empty;

    static List<String> notesList;
    static ArrayAdapter adapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.asii.simplenoteapp", Context.MODE_PRIVATE);
        notes_listview = findViewById(R.id.notes_listview);
        tv_empty = findViewById(R.id.tv_empty);

        notesList = new ArrayList<>();

        HashSet<String> noteSet = (HashSet<String>) sharedPreferences.getStringSet("notes",null);

        if (noteSet != null){
            tv_empty.setVisibility(View.GONE);
            notesList = new ArrayList<>(noteSet);
        }else{
            tv_empty.setVisibility(View.VISIBLE);
        }

        adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_notes_row, R.id.textview, notesList);
        notes_listview.setAdapter(adapter);

        notes_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId",i);
                startActivity(intent);
            }
        });

        notes_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemToDelete = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you Sure?")
                        .setMessage("Do you want to delete this?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notesList.remove(itemToDelete);
                                adapter.notifyDataSetChanged();

                                HashSet<String> noteSet = new HashSet<>(notesList);
                                sharedPreferences.edit().putStringSet("notes",noteSet).apply();

                                if (noteSet.isEmpty() || noteSet == null){
                                    tv_empty.setVisibility(View.VISIBLE);
                                }
                            }
                        }).setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_note){
            startActivity(new Intent(getApplicationContext(), NoteEditorActivity.class));
            return true;
        }
        return false;
    }
}