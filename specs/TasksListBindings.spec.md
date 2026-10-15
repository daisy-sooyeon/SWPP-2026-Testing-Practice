# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Display the task's `dueDateLabel(task.dueDate, currentLocalDate, task.isCompleted)` in a list-row `TextView`. Set both text and visibility on every call so a reused row never retains a label from the previous task.

**Pre-conditions**
- `textView` is an existing task-row due date `TextView`; `task` may be null. For date-sensitive Tips, the test environment's current local calendar date is the exact date stated in the Tip.

**Post-conditions**
- Success cases
  - A null task clears any old text and hides the view with `View.GONE`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `"D+4"` and `View.VISIBLE`, and `task = null` / When `setDueDateLabel` is called / Then its text is `""` and visibility is `View.GONE`.
  - An active task without a due date clears any old text and hides the view. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `"D+4"` and `View.VISIBLE`, and `task = Task(title = "A", description = "", isCompleted = false, id = "task-1", dueDate = null)` / When `setDueDateLabel` is called / Then its text is `""` and visibility is `View.GONE`.
  - A completed task without a due date shows `"CLEAR"` and makes the view visible. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `""` and `View.GONE`, and `task = Task(title = "A", description = "", isCompleted = true, id = "task-1", dueDate = null)` / When `setDueDateLabel` is called / Then its text is `"CLEAR"` and visibility is `View.VISIBLE`.
  - A completed task with an overdue date still shows `"CLEAR"`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `"D+4"` and `View.VISIBLE`, and `task = Task(title = "A", description = "", isCompleted = true, id = "task-1", dueDate = 2026-10-10)` / When `setDueDateLabel` is called / Then its text is `"CLEAR"` and visibility is `View.VISIBLE`.
  - An active overdue task shows its elapsed calendar-day count. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `""` and `View.GONE`, and `task = Task(title = "A", description = "", isCompleted = false, id = "task-1", dueDate = 2026-10-10)` / When `setDueDateLabel` is called / Then its text is `"D+4"` and visibility is `View.VISIBLE`.
  - An active task due today shows `"D-Day"`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `""` and `View.GONE`, and `task = Task(title = "A", description = "", isCompleted = false, id = "task-1", dueDate = 2026-10-14)` / When `setDueDateLabel` is called / Then its text is `"D-Day"` and visibility is `View.VISIBLE`.
  - An active future task shows its remaining calendar-day count. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `""` and `View.GONE`, and `task = Task(title = "A", description = "", isCompleted = false, id = "task-1", dueDate = 2026-10-20)` / When `setDueDateLabel` is called / Then its text is `"D-6"` and visibility is `View.VISIBLE`.
  - Reusing a visible view for an active task without a due date removes the previous task's label. `[Integration]`
    - Tip: Given local date `2026-10-14`, one `TextView` with text `""` and `View.GONE`, `firstTask = Task(title = "A", description = "", isCompleted = false, id = "task-1", dueDate = 2026-10-15)`, and `secondTask = Task(title = "B", description = "", isCompleted = false, id = "task-2", dueDate = null)` / When `setDueDateLabel` is called on that same view first with `firstTask` and then with `secondTask` / Then its text is `""` and visibility is `View.GONE`.
- Error cases
  - None.
- Edge cases
  - One day before the current date shows `"D+1"`, immediately before the due-today boundary. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `""` and `View.GONE`, and `task = Task(title = "A", description = "", isCompleted = false, id = "task-1", dueDate = 2026-10-13)` / When `setDueDateLabel` is called / Then its text is `"D+1"` and visibility is `View.VISIBLE`.
  - At the due-today boundary the view shows `"D-Day"`. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `""` and `View.GONE`, and `task = Task(title = "A", description = "", isCompleted = false, id = "task-1", dueDate = 2026-10-14)` / When `setDueDateLabel` is called / Then its text is `"D-Day"` and visibility is `View.VISIBLE`.
  - One day after the current date shows `"D-1"`, immediately after the due-today boundary. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `""` and `View.GONE`, and `task = Task(title = "A", description = "", isCompleted = false, id = "task-1", dueDate = 2026-10-15)` / When `setDueDateLabel` is called / Then its text is `"D-1"` and visibility is `View.VISIBLE`.
  - A future date in the next month displays the total calendar-day difference. `[Integration]`
    - Tip: Given local date `2026-10-14`, a `TextView` with text `""` and `View.GONE`, and `task = Task(title = "A", description = "", isCompleted = false, id = "task-1", dueDate = 2026-11-14)` / When `setDueDateLabel` is called / Then its text is `"D-31"` and visibility is `View.VISIBLE`.

**Undefined behavior** (optional)
- None.
