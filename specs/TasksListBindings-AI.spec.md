# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, dueDate: LocalDate?, today: LocalDate)`

**Intent**: Display `dueDateLabel(dueDate, today)` in a task list row. Hide the due date view when no date exists, and restore it when the row is reused for a task with a due date.

**Pre-conditions**
- `textView` is a task-row due date `TextView`; `today` is the caller's current local calendar date. `dueDate` may be null.

**Post-conditions**
- Success cases
  - A missing due date clears the text and hides the view. `[Integration]`
    - Tip: Given a `TextView` showing `"Overdue"` with `View.VISIBLE`, `dueDate = null`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `""` and visibility is `View.GONE`.
  - A past due date shows the overdue label and makes the view visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and `View.GONE`, `dueDate = 2026-10-10`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `"Overdue"` and visibility is `View.VISIBLE`.
  - A due date today shows today's label and makes the view visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and `View.GONE`, `dueDate = 2026-10-14`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `"Due today"` and visibility is `View.VISIBLE`.
  - A due date tomorrow shows tomorrow's label and makes the view visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and `View.GONE`, `dueDate = 2026-10-15`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `"Due tomorrow"` and visibility is `View.VISIBLE`.
  - A later due date shows its ISO date and makes the view visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and `View.GONE`, `dueDate = 2026-10-20`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `"Due 2026-10-20"` and visibility is `View.VISIBLE`.
- Error cases
  - None. An existing `TextView`, null due date, and every `LocalDate` value are valid.
- Edge cases
  - The day before today shows overdue, immediately before the overdue/today boundary. `[Integration]`
    - Tip: Given a `TextView` with text `""` and `View.GONE`, `dueDate = 2026-10-13`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `"Overdue"` and visibility is `View.VISIBLE`.
  - Today is the overdue/today boundary and immediately before tomorrow. `[Integration]`
    - Tip: Given a `TextView` with text `""` and `View.GONE`, `dueDate = 2026-10-14`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `"Due today"` and visibility is `View.VISIBLE`.
  - Tomorrow is immediately after today and the tomorrow/later boundary. `[Integration]`
    - Tip: Given a `TextView` with text `""` and `View.GONE`, `dueDate = 2026-10-15`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `"Due tomorrow"` and visibility is `View.VISIBLE`.
  - The following day is immediately after the tomorrow/later boundary. `[Integration]`
    - Tip: Given a `TextView` with text `""` and `View.GONE`, `dueDate = 2026-10-16`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `"Due 2026-10-16"` and visibility is `View.VISIBLE`.
  - The next day remains in the later-date category, immediately beyond that boundary. `[Integration]`
    - Tip: Given a `TextView` with text `""` and `View.GONE`, `dueDate = 2026-10-17`, and `today = 2026-10-14` / When `setDueDateLabel` is called / Then its text is `"Due 2026-10-17"` and visibility is `View.VISIBLE`.

**Undefined behavior** (optional)
- None.
