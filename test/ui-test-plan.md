# Moon UI Test Plan

## Test environment

- Run the `Moon` main class with Java 25.
- Compare only text printed by the program. The terminal's echo of typed commands is not part of expected output.
- The test runner stops after the first failing test case.

## Test case: Create and list every task type

**Aim:** Verify that to-dos, deadlines, and events are stored and displayed with their type, details, and incomplete status.

### Input
```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

### Expected output
```text
____________________________________________________________
Hello! I'm Moon, your personal chatbot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Mark and unmark a task

**Aim:** Verify that marking a task done and then unmarking it changes the displayed status correctly.

### Input
```text
todo read book
mark 1
unmark 1
list
bye
```

### Expected output
```text
____________________________________________________________
Hello! I'm Moon, your personal chatbot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Reject invalid commands without ending the session

**Aim:** Verify that Moon reports missing todo descriptions and unknown commands, then continues to accept valid commands.

### Input
```text
todo
blah
todo borrow book
bye
```

### Expected output
```text
____________________________________________________________
Hello! I'm Moon, your personal chatbot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oof! your todo needs a description.
____________________________________________________________
____________________________________________________________
 Oof! I don't recognise that command. Try todo, deadline, event, list, mark, unmark, or bye.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
