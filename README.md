# ThreadInterruption

The following are the requirements of the use case:

1) Make a task that prints 0 through 9 on console.
2)After printing a number the task should wait 1 sec before printing the next number.
3)The task runs on a separate thread, other than main application thread.
4)After starting the task the main application should wait for 3 sec and then shutdown.
5)On shutdown the application should request the running task to finish.
6)Before shutting down completely the application should, at the max, wait for 1 sec for the task to finish.
7)The task should respond to the finish request by stopping immediately.

How to request a task, running on a separate thread, to finish early?

How to make a task responsive to such a finish request?


Summary
The answers to the two questions that I had set out to answer are:

How to request a task, running on a separate thread, to finish early? - Use interruption.
If using Thread directly in your code, you may call interrupt() on the instance of thread.
If using Executor framework, you may cancel each task by calling cancel() on Future
If using Executor framework, you may shutdown the ExecutorService by calling theshutdownNow() method.
How to make a task responsive to such a finish request? - Handle interruption request, which in most of the cases is done by handling InterruptedException. Also preserve the interruption status by calling Thread.currentThread().interrupt().
