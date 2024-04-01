package com.stone.mmt.app.notelite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomNoteAdapter extends RecyclerView.Adapter<CustomNoteAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<NoteData> noteData;

    public CustomNoteAdapter(Context context,ArrayList<NoteData>noteData) {
        this.context = context;
        this.noteData = noteData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_note,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(noteData.get(position).title());
        holder.tvDesc.setText(noteData.get(position).desc());
        holder.tvDateTime.setText(noteData.get(position).dateTime());
        holder.noteCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context,Note_Update.class);
            intent.putExtra("title",noteData.get(holder.getAdapterPosition()).title());
            intent.putExtra("desc",noteData.get(holder.getAdapterPosition()).desc());
            intent.putExtra("dateTime",noteData.get(holder.getAdapterPosition()).dateTime());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return noteData.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle,tvDesc,tvDateTime;
        private final CardView noteCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            noteCardView = itemView.findViewById(R.id.noteCardView);
            Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.note_list);
            noteCardView.setAnimation(animation);
        }
    }
}
