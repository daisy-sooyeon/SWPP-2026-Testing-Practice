# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Label a task with its completion state or the number of calendar days between `today` and its due date. Completed tasks show `CLEAR`; active tasks show an empty label without a due date, `D-Day` when due today, `D-N` when due in N days, or `D+N` when overdue by N days.

**Pre-conditions**
- `today` is the caller's current local calendar date. `dueDate` may be null; `isCompleted` is either `true` or `false`.

**Post-conditions**
- Success cases
  - A completed task returns `"CLEAR"` even without a due date. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-14`, and `isCompleted = true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - A completed task returns `"CLEAR"` regardless of whether its due date has passed. `[Unit]`
    - Tip: Given `dueDate = 2026-10-10`, `today = 2026-10-14`, and `isCompleted = true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - An active task without a due date returns an empty string. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `""`.
  - An active task overdue by N calendar days returns `"D+N"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-10`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+4"`.
  - An active task due today returns `"D-Day"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-14`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-Day"`.
  - An active task due in N calendar days returns `"D-N"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-20`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-6"`.
  - A full month between today and the due date counts every calendar day. `[Unit]`
    - Tip: Given `dueDate = 2026-11-14`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-31"`.
  - A full month overdue counts every calendar day. `[Unit]`
    - Tip: Given `dueDate = 2026-10-14`, `today = 2026-11-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+31"`.
- Error cases
  - None.
- Edge cases
  - The day before today is overdue by one day, immediately before the due-today boundary. `[Unit]`
    - Tip: Given `dueDate = 2026-10-13`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+1"`.
  - At the due-today boundary, the day difference is zero. `[Unit]`
    - Tip: Given `dueDate = 2026-10-14`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-Day"`.
  - The day after today is one day away, immediately after the due-today boundary. `[Unit]`
    - Tip: Given `dueDate = 2026-10-15`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
  - A one-day difference across a month boundary remains one calendar day. `[Unit]`
    - Tip: Given `dueDate = 2026-11-01`, `today = 2026-10-31`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
  - Leap day is counted when calculating a span across February. `[Unit]`
    - Tip: Given `dueDate = 2028-03-01`, `today = 2028-02-28`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-2"`.

**Undefined behavior** (optional)
- None.
