package com.codepath.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.codepath.todoapp.R;
import com.codepath.todoapp.adapters.TodoAdapter;
import com.codepath.todoapp.database.TodoItemDatabase;
import com.codepath.todoapp.models.TodoItem;

import java.util.ArrayList;


public class TodoActivity extends ActionBarActivity
        implements RecyclerView.OnItemTouchListener {
    private static final String TAG = TodoActivity.class.getSimpleName();

    private ArrayList<TodoItem> todoItems;
    private TodoAdapter todoAdapter;
    private RecyclerView recyclerView;
    private static final int ITEM_EDIT_REQUEST = 1;
    private static final int ITEM_ADD_REQUEST = 2;
    private TodoItemDatabase db;

    private GestureDetectorCompat gDetector;

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        gDetector.onTouchEvent(event);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        recyclerView = (RecyclerView) findViewById(R.id.lvItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(this);

        gDetector = new GestureDetectorCompat(this, new RecyclerViewGestureListener());

        // Initialize the Database
        db = new TodoItemDatabase(this);

        // Read the TodoItem from the Database
        todoItems = db.getAllTodoItems();
        todoAdapter = new TodoAdapter(todoItems);

        // Inflate the ListView
        recyclerView.setAdapter(todoAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, TodoItemActivity.class);
                startActivityForResult(intent, ITEM_ADD_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == ITEM_EDIT_REQUEST) {
                int position = data.getIntExtra("position", 0);
                TodoItem todoItem = data.getParcelableExtra("item");
                todoItems.set(position, todoItem);
                todoAdapter.notifyDataSetChanged();
                db.updateTodoItem(todoItem);
            }

            if (requestCode == ITEM_ADD_REQUEST) {
                TodoItem todoItem = data.getParcelableExtra("item");
                db.addTodoItem(todoItem);
                todoItems.clear();
                todoItems.addAll(db.getAllTodoItems());
                todoAdapter.notifyDataSetChanged();
            }
        }
    }

    class RecyclerViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            int position = recyclerView.getChildPosition(view);
            if (position < 0) return false;
            TodoItem todoItem = todoItems.get(position);
            Intent intent = new Intent(TodoActivity.this, TodoItemActivity.class);
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
            TodoItem todoItem = todoItems.get(position);
            todoItems.remove(position);
            todoAdapter.notifyDataSetChanged();

            // Update the Database
            db.deleteTodoItem(todoItem);
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
