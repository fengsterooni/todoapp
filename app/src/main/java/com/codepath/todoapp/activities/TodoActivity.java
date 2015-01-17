package com.codepath.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.todoapp.R;
import com.codepath.todoapp.database.TodoItemDatabase;
import com.codepath.todoapp.adapters.TodoAdapter;
import com.codepath.todoapp.models.TodoItem;

import java.util.ArrayList;


public class TodoActivity extends ActionBarActivity {
    private static final String TAG = TodoActivity.class.getSimpleName();

    private ArrayList<TodoItem> todoItems;
    private ArrayAdapter<TodoItem> todoAdapter;
    private ListView lvItems;
    private static final int ITEM_EDIT_REQUEST = 1;
    private static final int ITEM_ADD_REQUEST = 2;
    private TodoItemDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        // Initialize the Database
        db = new TodoItemDatabase(this);

        // Read the TodoItem from the Database
        todoItems = db.getAllTodoItems();
        todoAdapter = new TodoAdapter(this, todoItems);

        // Inflate the ListView
        lvItems = (ListView) findViewById(R.id.lvItem);
        lvItems.setAdapter(todoAdapter);

        setupListViewListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddNewActivity.class);
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

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todoItem = todoItems.get(position);
                todoItems.remove(position);
                todoAdapter.notifyDataSetChanged();

                // Update the Database
                db.deleteTodoItem(todoItem);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todoItem = todoItems.get(position);
                Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
                intent.putExtra("item", todoItem);
                intent.putExtra("position", position);
                startActivityForResult(intent, ITEM_EDIT_REQUEST);
            }
        });
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
}
