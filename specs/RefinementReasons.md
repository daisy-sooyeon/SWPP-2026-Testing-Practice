## DueDateUtils.spec.md Refinement

- Updated the function signature to include `isCompleted`.
- Replaced the earlier date-text labels with the intended `D-Day`, `D-N`, `D+N`, and `CLEAR` labels.
- Added cross-month and leap-day cases to verify that `N` counts total calendar days.
- Replaced the earlier tomorrow/later boundary cases with cases around the `D-Day` boundary.

## TasksListBindings.spec.md Refinement

- Updated the function signature to accept a `Task`.
- Updated the expected text and visibility for completed, overdue, due-today, future, and no-date tasks.
- Added a same-`TextView` reuse case to verify that an old label is cleared when the next active task has no due date.
- Added a reuse case to verify that `CLEAR` is cleared when a completed task without a due date becomes active.