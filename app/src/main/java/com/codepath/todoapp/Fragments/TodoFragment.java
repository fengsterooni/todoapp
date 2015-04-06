package com.codepath.todoapp.fragments;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.todoapp.R;
import com.codepath.todoapp.activities.TodoActivity;
import com.codepath.todoapp.activities.TodoItemActivity;
import com.codepath.todoapp.adapters.TodoAdapter;
import com.codepath.todoapp.database.TodoItemDatabase;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DividerItemDecoration;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TodoFragment extends Fragment implements RecyclerView.OnItemTouchListener {
    private int REMINDER = 1;
    private int NOTIFICATION_ID = 1;
    private ArrayList<TodoItem> todoItems;
    private TodoAdapter todoAdapter;
    @InjectView(R.id.lvItem) RecyclerView recyclerView;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @OnClick(R.id.fab) void click() {
        Intent intent = new Intent(context, TodoItemActivity.class);
        startActivityForResult(intent, ITEM_ADD_REQUEST);
    }
    private static final int ITEM_EDIT_REQUEST = 1;
    private static final int ITEM_ADD_REQUEST = 2;
    private TodoItemDatabase db;
    private Context context;
    private GestureDetectorCompat gDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        ButterKnife.inject(this, view);

        fab.attachToRecyclerView(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.addOnItemTouchListener(this);

        gDetector = new GestureDetectorCompat(context, new RecyclerViewGestureListener());

        // Initialize the Database
        db = new TodoItemDatabase(context);

        // Read the TodoItem from the Database
        todoItems = db.getAllTodoItems();
        todoAdapter = new TodoAdapter(todoItems);

        checkForNotification();

        // Inflate the ListView
        recyclerView.setAdapter(todoAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        return view;
    }

    private void checkForNotification() {
        if (todoItems.isEmpty())
            return;

        TodoItem todoItem = todoItems.get(0);
        Date date = todoItem.getDueDate();
        Calendar temp = Calendar.getInstance();
        temp.setTime(date);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, REMINDER);
        if (temp.before(calendar)) {

            Intent intent = new Intent(context, TodoActivity.class);
            int requestID = (int) System.currentTimeMillis();
            int flags = PendingIntent.FLAG_CANCEL_CURRENT;
            PendingIntent pIntent = PendingIntent.getActivity(context, requestID, intent, flags);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_stat_notify)
                    .setContentTitle("Task is due")
                    .setContentText("DO something about it!")
                    .setContentIntent(pIntent);

            builder.setAutoCancel(true);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {

    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        gDetector.onTouchEvent(event);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ITEM_EDIT_REQUEST) {
                int position = data.getIntExtra("position", 0);
                TodoItem todoItem = data.getParcelableExtra("item");
                todoItems.set(position, todoItem);
                db.updateTodoItem(todoItem);
                todoItems.clear();
                todoItems.addAll(db.getAllTodoItems());
                todoAdapter.notifyDataSetChanged();
            }

            if (requestCode == ITEM_ADD_REQUEST) {
                TodoItem todoItem = data.getParcelableExtra("item");
                db.addTodoItem(todoItem);
                todoItems.clear();
                todoItems.addAll(db.getAllTodoItems());
                todoAdapter.notifyDataSetChanged();
            }
            checkForNotification();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class RecyclerViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            int position = recyclerView.getChildPosition(view);
            if (position < 0) return false;
            TodoItem todoItem = todoItems.get(position);
            Intent intent = new Intent(context, TodoItemActivity.class);
            intent.putExtra("item", todoItem);
            intent.putExtra("position", position);
            startActivityForResult(intent, ITEM_EDIT_REQUEST);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            View view = recyclerView.findChildViewUnder(e1.getX(), e1.getY());
            int position = recyclerView.getChildPosition(view);
            if (position < 0) return false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    TodoItem todoItem = todoItems.get(position);
                    todoItems.remove(position);
                    todoAdapter.notifyDataSetChanged();

                    // Update the Database
                    db.deleteTodoItem(todoItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            int position = recyclerView.getChildPosition(view);
            if (position < 0) return;
            TodoItem todoItem = todoItems.get(position);
            todoItems.remove(position);
            todoAdapter.notifyDataSetChanged();

            // Update the Database
            db.deleteTodoItem(todoItem);
            super.onLongPress(e);
        }
    }
}
