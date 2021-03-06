package com.codepath.todoapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.todoapp.R;
import com.codepath.todoapp.activities.TodoActivity;
import com.codepath.todoapp.activities.TodoItemActivity;
import com.codepath.todoapp.adapters.TodoAdapter;
import com.codepath.todoapp.database.TodoItemDatabase;
import com.codepath.todoapp.models.TodoItem;
import com.jensdriller.libs.undobar.UndoBar;
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
    private ArrayList<TodoItem> reminderItems = new ArrayList<>();
    private TodoAdapter todoAdapter;
    @InjectView(R.id.lvItem)
    RecyclerView recyclerView;
    @InjectView(R.id.fab)
    FloatingActionButton fab;

    @OnClick(R.id.fab)
    void click() {
        Intent intent = new Intent(context, TodoItemActivity.class);
        startActivityForResult(intent, ITEM_ADD_REQUEST);
    }

    private static final int ITEM_EDIT_REQUEST = 1;
    private static final int ITEM_ADD_REQUEST = 2;
    private TodoItemDatabase db;
    private Context context;
    private GestureDetectorCompat gDetector;
    private SharedPreferences preferences;
    private boolean enable;
    private NotificationManager manager;

    public TodoFragment() {
        // Required empty public constructor
    }

    public static TodoFragment newInstatnce() {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        gDetector = new GestureDetectorCompat(context, new RecyclerViewGestureListener());

        // Initialize the Database
        db = new TodoItemDatabase(context);

        // Read the TodoItem from the Database
        todoItems = db.getAllTodoItems();
        todoAdapter = new TodoAdapter(todoItems);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        enable = preferences.getBoolean("switch_preference", true);
        if (enable) {
            String value = preferences.getString("list_preference", "0");
            REMINDER = Integer.valueOf(value);
            checkForNotification();
        }

        // Inflate the ListView
        recyclerView.setAdapter(todoAdapter);

        return view;
    }

    private void checkForNotification() {
        if (todoItems.isEmpty())
            return;

        reminderItems.clear();

        Date dueDate;
        Calendar reminderDate, itemDueDate;

        for (TodoItem todoItem : todoItems) {
            dueDate = todoItem.getDueDate();
            itemDueDate = Calendar.getInstance();
            itemDueDate.setTime(dueDate);
            reminderDate = Calendar.getInstance();
            reminderDate.add(Calendar.DATE, REMINDER);
            if (itemDueDate.before(reminderDate)) {
                reminderItems.add(todoItem);
            }
        }

        if (!reminderItems.isEmpty()) {
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

            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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

            if (manager != null)
                manager.cancelAll();

            if (enable)
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
                    removeItem(position);
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
                removeItem(position);
            super.onLongPress(e);
        }
    }

    public void removeItem(final int position) {
        final TodoItem todoItem = todoItems.get(position);
        String title = todoItem.getTitle();
        todoItems.remove(position);
        todoAdapter.notifyDataSetChanged();

        new UndoBar.Builder(getActivity())//
                .setMessage("\"" + title + "\"" + " deleted.")//
                .setListener(new UndoBar.Listener() {
                    @Override
                    public void onHide() {
                        Toast.makeText(context, "ITEM DELETED", Toast.LENGTH_SHORT).show();

                        // Update the Database
                        db.deleteTodoItem(todoItem);
                    }

                    @Override
                    public void onUndo(Parcelable parcelable) {
                        Toast.makeText(context, "ITEM RECOVERED", Toast.LENGTH_SHORT).show();
                        todoItems.add(position, todoItem);
                        todoAdapter.notifyDataSetChanged();
                    }
                })//
                .setStyle(UndoBar.Style.LOLLIPOP)//
                .show();
    }

}
