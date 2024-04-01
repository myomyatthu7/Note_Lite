package com.stone.mmt.app.notelite;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stone.mmt.app.notelite.library.CurrentDateTime;
import com.stone.mmt.app.notelite.library.ToastMsg;

public class Note_Add extends AppCompatActivity {
    private EditText etTitle,etDesc;
    private NoteDBAdapter noteDBAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_add);
        Toolbar toolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        noteDBAdapter = new NoteDBAdapter(Note_Add.this);
    }

//    public String getCurrentDateTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy \t hh:mm:ss");
//        return sdf.format(Calendar.getInstance().getTime());
//    }

    @Override
    protected void onPause() {
        super.onPause();
        String title,desc;
        title = etTitle.getText().toString();
        desc = etDesc.getText().toString();
        String datetime = CurrentDateTime.getCurrentDateTime();
        if (!title.isEmpty() || !desc.isEmpty()) {
            noteDBAdapter.dbOpen();
            long result = noteDBAdapter.noteInsert(title,desc,datetime);
            noteDBAdapter.dbClose();
            if (result < 0) {
                ToastMsg.toastMsg(Note_Add.this,"There was an error, try again.");
            } else {
                finish();
            }
        }
    }
}
