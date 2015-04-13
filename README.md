Todo App
=============

This is an Android application for creating, editing and saving a ToDo list.

**_Completed user stories:_**

- [x] Required: User can view a list of existing todo items
- [x] Required: User can add a new item to the todo list
- [x] Required: User can remove an item from the todo list 
- [x] Requried: User can edit the text of an item in the existing todo list and save the changes
- [x] Optional: User can add comments/notes to an item
- [x] Optional: User can specify priority for items
- [x] Optional: User can specify completion due dates for items 
- [x] Optional: Persist the todo app in SQLite instead of using a text file
- [x] Optional: Display additional attributes, stylize the items in item ListView
- [x] Optional: Improved UI by using some simple material design
- [x] Optional: Use a DialogFragment instead of new Activity to support editing items.
#####Note:
Both adding new and editing existing items are implemented by sharing the same code, in Fragment

**_New Enhancements:_**

- [x] Added Floating Action Button (FAB)
- [x] Added notification. When enabled, user shall get notification that due days are approaching for the items in the list.
- [x] Added user settings (preferenceFragment). User can enable/disable notification, and set the reminder time for notification via Settings
- [x] Added UndoBar
- [x] Used MaterialEditText Library to implement the floating label, empty title check, etc.


**_Work-in-progress:_**

- [ ] Further UI improvement


**_Library used:_**

- Floating Action Button (https://github.com/makovkastar/FloatingActionButton)
- ButterKnife (https://github.com/JakeWharton/butterknife)
- UndoBar (https://github.com/jenzz/Android-UndoBar)
- MaterialEditText (https://github.com/rengwuxian/MaterialEditText)



**_Screencast:_**

![screenshot](https://github.com/fengsterooni/todoapp/blob/master/todoapp.gif)
