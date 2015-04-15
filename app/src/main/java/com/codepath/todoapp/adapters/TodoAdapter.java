package com.codepath.todoapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
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
    boolean enable;
    int REMINDER;
    Resources resources;

    public TodoAdapter(List<TodoItem> objects) {
        this.todoItems = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        SharedPreferences preferences;
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.item_todo, viewGroup, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        enable = preferences.getBoolean("switch_preference", true);
        if (enable) {
            String value = preferences.getString("list_preference", "0");
            REMINDER = Integer.valueOf(value);
        }
        resources = context.getResources();
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final TodoItem todoItem = todoItems.get(position);
        Date dueDate;
        Calendar reminderDate, itemDueDate;

        viewHolder.setIcon(todoItem.getPriority().toString());
        viewHolder.setNotes(todoItem.getNotes());

        dueDate = todoItem.getDueDate();
        viewHolder.setDueMonth(DateUtils.getShortMonthString(dueDate));
        viewHolder.setDueDay(DateUtils.getDayString(dueDate));

        reminderDate = Calendar.getInstance();
        itemDueDate = Calendar.getInstance();
        itemDueDate.setTime(dueDate);
        reminderDate.add(Calendar.DATE, REMINDER);
        if (itemDueDate.before(reminderDate)) {
            if (enable) {
                // viewHolder.setTitleColor(getResource().getColor(R.color.red));
                viewHolder.setTitleColor(resources.getColor(R.color.red));
            }
        }

        viewHolder.setTitle(todoItem.getTitle());
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

        public void setTitleColor(int color) { title.setTextColor(color);}
    }
}
