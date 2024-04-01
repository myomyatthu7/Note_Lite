package com.stone.mmt.app.notelite;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stone.mmt.app.notelite.library.CurrentDateTime;
import com.stone.mmt.app.notelite.library.ToastMsg;

public class Note_Update extends AppCompatActivity {
    private EditText etTitleUpdate,etDescUpdate;
    private String title,dateTime;
    private String newTitle,newDesc,newDateTime;
    private NoteDBAdapter noteDBAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_update);
        Toolbar toolbar = findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);
        etTitleUpdate = findViewById(R.id.etTitleUpdate);
        etDescUpdate = findViewById(R.id.etDescUpdate);
        noteDBAdapter = new NoteDBAdapter(Note_Update.this);
        getAndSetData();
    }
    private void getAndSetData() {
        title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");
        dateTime = getIntent().getStringExtra("dateTime");
        etTitleUpdate.setText(title);
        etDescUpdate.setText(desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int items = item.getItemId();
        if (items == R.id.deleteMenu) {
            showDialogDelete();
        } else if (items == R.id.updateMenu) {
             newTitle = etTitleUpdate.getText().toString();
             newDesc = etDescUpdate.getText().toString();
             newDateTime = CurrentDateTime.getCurrentDateTime();
            if (!newTitle.isEmpty() || !newDesc.isEmpty()){
                showDialogUpdate();
            } else {
                ToastMsg.toastMsg(Note_Update.this,"Enter text before updating");
            }
        }
        return true;
    }
    private void showDialogUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Note_Update.this);
        builder.setTitle("Update!");

        if (newTitle.isEmpty()) {
            builder.setMessage("Do you want to update this?");
        } else {
            builder.setMessage("Do you want to update "+newTitle+"?");
        }
        builder.setPositiveButton("Yes", (dialog, which) -> {
                noteDBAdapter.dbOpen();
                int result = noteDBAdapter.noteUpdate(dateTime,newTitle,newDesc,newDateTime);
                noteDBAdapter.dbClose();
                if (result <1) {
                    ToastMsg.toastMsg(Note_Update.this,"Update Fail");
                } else {
                    ToastMsg.toastMsg(Note_Update.this,"Update is successful");
                    finish();
                }
        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.show().create();
    }
    private void showDialogDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Note_Update.this);
        builder.setTitle("Delete!");
        if (title.isEmpty()) {
            builder.setMessage("Do you want to delete this?");
        } else {
            builder.setMessage("Do you want to delete "+title+"?");
        }
        builder.setPositiveButton("Yes", (dialog, which) -> {
            noteDBAdapter.dbOpen();
            noteDBAdapter.noteDelete(dateTime);
            noteDBAdapter.dbClose();
            ToastMsg.toastMsg(Note_Update.this,"Delete is successful");
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.show().create();
    }
}
