package com.codepath.todoapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.todoapp.R;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoItem> todoItems;
    private ItemClickListener itemClickListener;

    public TodoAdapter(List<TodoItem> objects, @NonNull ItemClickListener itemClickListener) {
        this.todoItems = objects;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.item_todo, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final TodoItem todoItem = todoItems.get(position);
        viewHolder.setIcon(todoItem.getPriority());
        viewHolder.setTitle(todoItem.getTitle());
        viewHolder.setNotes(todoItem.getNotes());
        Date date = todoItem.getDueDate();
        viewHolder.setDueMonth(DateUtils.getMonthString(date));
        viewHolder.setDueDay(DateUtils.getDayString(date));
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClicked(todoItem);
            }
        });
    }


    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final View parent;
        private final ImageView icon;
        private final TextView title;
        private final TextView notes;
        private final TextView dueMonth;
        private final TextView dueDay;

        public static ViewHolder newInstance(View parent) {
            ImageView icon = (ImageView) parent.findViewById(R.id.ivItemPriority);
            TextView title = (TextView) parent.findViewById(R.id.tvItemTitle);
            TextView notes = (TextView) parent.findViewById(R.id.tvItemNotes);
            TextView dueMonth = (TextView) parent.findViewById(R.id.tvItemDueMonth);
            TextView dueDay = (TextView) parent.findViewById(R.id.tvItemDueDay);

            return new ViewHolder(parent, icon, title, notes, dueMonth, dueDay);
        }

        private ViewHolder(View parent, ImageView icon,
                TextView title, TextView notes, TextView dueMonth, TextView dueDay) {
            super(parent);
            this.parent = parent;
            this.icon = icon;
            this.title = title;
            this.notes = notes;
            this.dueMonth = dueMonth;
            this.dueDay = dueDay;
        }

        public void setIcon(String text) {
            int imageId;
            switch (text) {
                case "High":
                    imageId = R.drawable.high32;
                    break;
                case "Default":
                    imageId = R.drawable.default32;
                    break;
                case "Low":
                    imageId = R.drawable.low32;
                    break;
                default:
                    imageId = R.drawable.unset32;
            }
            icon.setImageResource(imageId);
        }
        public void setTitle(CharSequence text) {
            title.setText(text);
        }
        public void setNotes(CharSequence text) {
            notes.setText(text);
        }
        public void setDueMonth(CharSequence text) {
            dueMonth.setText(text);
        }
        public void setDueDay(CharSequence text) {
            dueDay.setText(text);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }

    public interface ItemClickListener {
        public void itemClicked(TodoItem todoItem);
    }
}
