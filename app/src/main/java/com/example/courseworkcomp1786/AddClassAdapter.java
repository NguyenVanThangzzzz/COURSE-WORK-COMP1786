package com.example.courseworkcomp1786;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AddClassAdapter extends RecyclerView.Adapter<AddClassAdapter.AddClassViewHolder> {

    private List<AddClass> classList;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public AddClassAdapter(List<AddClass> classList, OnDeleteClickListener listener) {
        this.classList = classList;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public AddClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_class, parent, false);
        return new AddClassViewHolder(view, deleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddClassViewHolder holder, int position) {
        AddClass addClass = classList.get(position);
        holder.bind(addClass);
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    static class AddClassViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTeacher;
        private final TextView textViewDate;
        private final TextView textViewComments;
        private final Button buttonDeleteClass;

        public AddClassViewHolder(@NonNull View itemView, final OnDeleteClickListener listener) {
            super(itemView);
            textViewTeacher = itemView.findViewById(R.id.textViewTeacher);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewComments = itemView.findViewById(R.id.textViewComments);
            buttonDeleteClass = itemView.findViewById(R.id.buttonDeleteClass);

            buttonDeleteClass.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(position);
                }
            });
        }

        public void bind(AddClass addClass) {
            textViewTeacher.setText("Teacher: " + addClass.getTeacher());
            textViewDate.setText("Date: " + addClass.getDate());
            textViewComments.setText("Comments: " + addClass.getComments());
        }
    }
}