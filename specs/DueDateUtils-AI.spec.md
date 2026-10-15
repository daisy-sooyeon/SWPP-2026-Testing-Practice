# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun dueDateLabel(dueDate: LocalDate?, today: LocalDate): String`

**Intent**: Produce a short label for an optional task due date. Compare local calendar dates, using ISO date text after tomorrow.

**Pre-conditions**
- `today` is the caller's current local calendar date; `dueDate` may be null.

**Post-conditions**
- Success cases
  - A missing due date produces an empty label. `[Unit]`
    - Tip: Given `dueDate = null` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `""`.
  - A past due date produces `"Overdue"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-10` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `"Overdue"`.
  - A due date today produces `"Due today"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-14` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `"Due today"`.
  - A due date tomorrow produces `"Due tomorrow"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-15` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `"Due tomorrow"`.
  - A later due date produces `"Due yyyy-MM-dd"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-20` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `"Due 2026-10-20"`.
- Error cases
  - None. Null and every `LocalDate` value are valid.
- Edge cases
  - One day before today remains overdue (immediately before the overdue/today boundary). `[Unit]`
    - Tip: Given `dueDate = 2026-10-13` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `"Overdue"`.
  - Today is the overdue/today boundary and immediately before tomorrow. `[Unit]`
    - Tip: Given `dueDate = 2026-10-14` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `"Due today"`.
  - Tomorrow is immediately after today and the tomorrow/later boundary. `[Unit]`
    - Tip: Given `dueDate = 2026-10-15` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `"Due tomorrow"`.
  - The day after tomorrow is immediately after the tomorrow/later boundary. `[Unit]`
    - Tip: Given `dueDate = 2026-10-16` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `"Due 2026-10-16"`.
  - The next day remains in the later-date category, immediately beyond that boundary. `[Unit]`
    - Tip: Given `dueDate = 2026-10-17` and `today = 2026-10-14` / When `dueDateLabel` is called / Then it returns `"Due 2026-10-17"`.

**Undefined behavior** (optional)
- None.
