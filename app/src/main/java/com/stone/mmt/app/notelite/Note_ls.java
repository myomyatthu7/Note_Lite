package com.stone.mmt.app.notelite;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stone.mmt.app.notelite.library.ToastMsg;
import com.stone.mmt.app.notelite.library.password.PwSetting;

import java.util.ArrayList;
import java.util.Collections;


public class Note_ls extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView note_ls;
    private NoteDBAdapter noteDBAdapter;
    private ArrayList<NoteData> noteData ;
    private CustomNoteAdapter customNoteAdapter;
    private TextView tvWelcome_Msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_ls);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        note_ls = findViewById(R.id.note_ls);
        tvWelcome_Msg = findViewById(R.id.tvWelcome_Msg);
        Button btnAdd = findViewById(R.id.btnAdd);
        noteDBAdapter = new NoteDBAdapter(Note_ls.this);
        btnAdd.setOnClickListener(v -> startActivity(new Intent(Note_ls.this, Note_Add.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu,menu);
        createMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.deleteAll);
        menuItem.setTitle("Delete All ["+note_ls.getChildCount()+"]");
        return true;
    }

    private void createMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int items = item.getItemId();
        if (items == R.id.deleteAll) {
            showDialogDeleteAll();
        } else if (items == R.id.setting) {
            startActivity(new Intent(Note_ls.this, PwSetting.class));
        } else if (items == R.id.about) {
            showDialogAbout();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteData = new ArrayList<>();
        storeDataInArray();
        customNoteAdapter = new CustomNoteAdapter(Note_ls.this, noteData);
        note_ls.setLayoutManager(new LinearLayoutManager(Note_ls.this));
        note_ls.setAdapter(customNoteAdapter);
    }

    private void storeDataInArray() {
        noteDBAdapter.dbOpen();
        Cursor c = noteDBAdapter.readAllData();
        if (c.getCount() == 0) {
            ToastMsg.toastMsg(Note_ls.this,"No data");
            tvWelcome_Msg.setVisibility(View.VISIBLE);
        } else {
            tvWelcome_Msg.setVisibility(View.GONE);
            for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
                String title = c.getString(noteDBAdapter.indexTitle);
                String desc = c.getString(noteDBAdapter.indexDesc);
                String dateTime = c.getString(noteDBAdapter.indexDateTime);
                noteData.add(new NoteData(title,desc,dateTime));
            }
            Collections.reverse(noteData);
        }
        noteDBAdapter.dbClose();
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchData(newText);
        return true;
    }

    private void searchData(String newText) {
        ArrayList<NoteData> searchData = new ArrayList<>();
        for (NoteData data : noteData) {
            if (data.title().toLowerCase().contains(newText.toLowerCase())) {
                searchData.add(data);
            }
        }
        customNoteAdapter = new CustomNoteAdapter(Note_ls.this,searchData);
        note_ls.setLayoutManager(new LinearLayoutManager(Note_ls.this));
        note_ls.setAdapter(customNoteAdapter);
    }
    private void showDialogDeleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Note_ls.this);
        builder.setTitle("Delete!");
        builder.setMessage("Do you want to delete all?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            noteDBAdapter.dbOpen();
            noteDBAdapter.deleteAllData();
            noteDBAdapter.dbClose();
            onResume();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.show().create();
    }

    private void showDialogAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Note_ls.this);
        builder.setTitle("Note Lite");
        builder.setMessage("Version : 1.0.0 [minimum android version 6.0]"+"\n"+"Your feedback is invaluable in shaping the application. " +
                "Let me know what you love and where i can make improvements. Thank you for being part of my journey!");
        builder.setPositiveButton("Email", (dialog, which) -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Note Lite App Feedback.");
            emailIntent.putExtra(Intent.EXTRA_EMAIL,"myatphone1799@gmail.com"); // new String [] {}
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
            }
         });
        builder.setNegativeButton("OK", (dialog, which) -> {
        });
        builder.show().create();
    }
}
