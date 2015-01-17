package com.codepath.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.todoapp.R;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

public class TodoAdapter extends ArrayAdapter<TodoItem> {
    public TodoAdapter(Context context, ArrayList<TodoItem> items) {
        super(context, R.layout.item_todo, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TodoItem todoItem = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_todo, parent, false);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.ivItemPriority);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvItemTitle);
            viewHolder.notes = (TextView) convertView.findViewById(R.id.tvItemNotes);
            viewHolder.dueMonth = (TextView) convertView.findViewById(R.id.tvItemDueMonth);
            viewHolder.dueDay = (TextView) convertView.findViewById(R.id.tvItemDueDay);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String priority = todoItem.getPriority();
        switch (priority) {
            case "High":
                viewHolder.icon.setImageResource(R.drawable.high32);
                break;
            case "Default":
                viewHolder.icon.setImageResource(R.drawable.default32);
                break;
            case "Low":
                viewHolder.icon.setImageResource(R.drawable.low32);
                break;
            default:
                viewHolder.icon.setImageResource(R.drawable.unset32);
        }

        viewHolder.title.setText(todoItem.getTitle());
        viewHolder.notes.setText(todoItem.getNotes());

        Date date = todoItem.getDueDate();
        viewHolder.dueMonth.setText(DateUtils.getMonthString(date));
        viewHolder.dueDay.setText(DateUtils.getDayString(date));

        return convertView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView notes;
        TextView dueMonth;
        TextView dueDay;
    }
}
