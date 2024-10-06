package com.example.courseworkcomp1786;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AddClassAdapter extends RecyclerView.Adapter<AddClassAdapter.AddClassViewHolder> {

    private List<AddClass> classList;
    private OnDeleteClickListener deleteClickListener;
    private OnItemClickListener itemClickListener;
    private Context context;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(AddClass addClass);
    }

    public AddClassAdapter(List<AddClass> classList, OnDeleteClickListener deleteListener, OnItemClickListener itemListener) {
        this.classList = classList;
        this.deleteClickListener = deleteListener;
        this.itemClickListener = itemListener;
    }

    @NonNull
    @Override
    public AddClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_class, parent, false);
        return new AddClassViewHolder(view);
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

    class AddClassViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTeacher;
        private final TextView textViewDate;
        private final TextView textViewComments;
        private final ImageButton buttonDeleteClass;

        public AddClassViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTeacher = itemView.findViewById(R.id.textViewTeacher);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewComments = itemView.findViewById(R.id.textViewComments);
            buttonDeleteClass = itemView.findViewById(R.id.buttonDeleteClass);

            buttonDeleteClass.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && deleteClickListener != null) {
                    showDeleteConfirmationDialog(v.getContext(), position);
                }
            });

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(classList.get(position));
                }
            });
        }

        private void showDeleteConfirmationDialog(Context context, int position) {
            new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa lớp học này?")
                .setPositiveButton("Có", (dialog, which) -> deleteClickListener.onDeleteClick(position))
                .setNegativeButton("Không", null)
                .show();
        }

        public void bind(AddClass addClass) {
            textViewTeacher.setText("Teacher: " + addClass.getTeacher());
            textViewDate.setText("Date: " + addClass.getDate());
            textViewComments.setText("Comments: " + addClass.getComments());
        }
    }

    public void updateList(List<AddClass> newList) {
        classList = newList;
        notifyDataSetChanged();
    }
}