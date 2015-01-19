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
- [x] Optional: User can specify completion due dates for items (a past date is not allowed; Dialog Fragment is used for date selection)
- [x] Optional: Persist the todo app in SQLite instead of using a text file
- [x] Optional: Display additional attributes, stylize the items in item ListView
- [x] Optional: Improved UI by using some simple Material design (need more work!)

**_Work-in-progress:_**
- [ ] Consolidate the code for adding new item and editing existing items, code reuse!
- [ ] Use fragment inside activities
- [ ] Optional: Use a DialogFragment instead of new Activity to support editing items. Making the editing screen a dialog fragment is doable, need to find a way to make the fragment communicate with the date picker fragment for smooth transition.
- [ ] Replace the ListView by RecyclerView

**_Further improvements:_**
- [ ] Sort. User shall be able to sort items by date or priority
- [ ] User preferences. User shall be able to hide lower priority items
- [ ] Notification. User shall be able to set (in perference) whether she should be notified, if so how and how much time ahead when due date is approaching.

**_Screencast:_**

![screenshot](https://github.com/fengsterooni/todoapp/blob/master/todoapp.gif)
