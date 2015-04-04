package com.codepath.todoapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.todoapp.R;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoItem> todoItems;

    public TodoAdapter(List<TodoItem> objects) {
        this.todoItems = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.item_todo, viewGroup, false);
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final TodoItem todoItem = todoItems.get(position);
        viewHolder.setIcon(todoItem.getPriority().toString());
        viewHolder.setTitle(todoItem.getTitle());
        viewHolder.setNotes(todoItem.getNotes());
        Date date = todoItem.getDueDate();
        viewHolder.setDueMonth(DateUtils.getShortMonthString(date));
        viewHolder.setDueDay(DateUtils.getDayString(date));
        Calendar calendar = Calendar.getInstance();
        if (date.before(calendar.getTime())) {
            viewHolder.setBackground(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        @InjectView(R.id.ivItemPriority) ImageView icon;
        @InjectView(R.id.tvItemTitle) TextView title;
        @InjectView(R.id.tvItemNotes) TextView notes;
        @InjectView(R.id.tvItemDueMonth) TextView dueMonth;
        @InjectView(R.id.tvItemDueDay) TextView dueDay;

        public ViewHolder(View parent) {
            super(parent);
            root = parent;
            ButterKnife.inject(this, parent);
        }

        public void setIcon(String text) {
            int imageId;
            switch (text) {
                case "High":
                    imageId = R.drawable.high;
                    break;
                case "Default":
                    imageId = R.drawable.normal;
                    break;
                case "Low":
                    imageId = R.drawable.low;
                    break;
                default:
                    imageId = R.drawable.unset;
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

        public void setBackground(int color) { root.setBackgroundResource(color);}
    }
}
