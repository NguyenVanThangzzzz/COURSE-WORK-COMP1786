package com.example.courseworkcomp1786;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AddClassAdapter extends RecyclerView.Adapter<AddClassAdapter.AddClassViewHolder> {

    private List<AddClass> classList;

    public AddClassAdapter(List<AddClass> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public AddClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_class, parent, false);
        return new AddClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddClassViewHolder holder, int position) {
        AddClass addClass = classList.get(position);
        holder.textViewTeacher.setText("Teacher: " + addClass.getTeacher());
        holder.textViewDate.setText("Date: " + addClass.getDate());
        holder.textViewComments.setText("Comments: " + addClass.getComments());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    static class AddClassViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTeacher;
        TextView textViewDate;
        TextView textViewComments;

        public AddClassViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTeacher = itemView.findViewById(R.id.textViewTeacher);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewComments = itemView.findViewById(R.id.textViewComments);
        }
    }
}