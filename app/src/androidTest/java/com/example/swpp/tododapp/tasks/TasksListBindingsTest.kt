package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    @Test
    fun setDueDateLabel_nullTask_clearsTextAndHidesView() {
        // Given a visible view showing D+4 and no task
        // When the null task is bound
        // Then the old label is removed and the view is hidden
        assertBinding(null, "D+4", View.VISIBLE, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_activeTaskWithoutDate_clearsTextAndHidesView() {
        // Given a visible view showing D+4 and an active task without a date
        val task = task(isCompleted = false, dueDate = null)
        // When the active task is bound
        // Then the old label is removed and the view is hidden
        assertBinding(task, "D+4", View.VISIBLE, "", View.GONE)
    }

    @Test
    fun setDueDateLabel_completedTaskWithoutDate_showsClear() {
        // Given a hidden view and a completed task without a date
        val task = task(isCompleted = true, dueDate = null)
        // When the completed task is bound
        // Then CLEAR is displayed
        assertBinding(task, "", View.GONE, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_completedTaskWithPastDate_showsClear() {
        // Given a visible view showing D+4 and a completed task due four days ago
        val task = task(isCompleted = true, dueDate = LocalDate.now().minusDays(4))
        // When the completed task is bound
        // Then CLEAR replaces the overdue label
        assertBinding(task, "D+4", View.VISIBLE, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeTaskFourDaysOverdue_showsDPlus4() {
        // Given a hidden view and an active task due four days ago
        val task = task(isCompleted = false, dueDate = LocalDate.now().minusDays(4))
        // When the active task is bound
        // Then D+4 is displayed
        assertBinding(task, "", View.GONE, "D+4", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeTaskDueToday_showsDDay() {
        // Given a hidden view and an active task due today
        val task = task(isCompleted = false, dueDate = LocalDate.now())
        // When the active task is bound
        // Then D-Day is displayed
        assertBinding(task, "", View.GONE, "D-Day", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeTaskSixDaysAhead_showsDMinus6() {
        // Given a hidden view and an active task due in six days
        val task = task(isCompleted = false, dueDate = LocalDate.now().plusDays(6))
        // When the active task is bound
        // Then D-6 is displayed
        assertBinding(task, "", View.GONE, "D-6", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_reusedViewWithUndatedTask_clearsPreviousLabel() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        // Given one hidden view, a task due tomorrow, and an undated active task
        val firstTask = task(isCompleted = false, dueDate = LocalDate.now().plusDays(1))
        val secondTask = task(isCompleted = false, dueDate = null).copy(title = "B", id = "task-2")

        instrumentation.runOnMainSync {
            val textView = TextView(instrumentation.targetContext).apply {
                text = ""
                visibility = View.GONE
            }
            // When both tasks are bound to the same view in sequence
            setDueDateLabel(textView, firstTask)
            assertEquals("D-1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
            setDueDateLabel(textView, secondTask)

            // Then the second task leaves no stale label
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_dayBeforeToday_showsDPlus1() {
        // Given a hidden view and an active task due yesterday
        val task = task(isCompleted = false, dueDate = LocalDate.now().minusDays(1))
        // When the active task is bound
        // Then D+1 is displayed
        assertBinding(task, "", View.GONE, "D+1", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_atTodayBoundary_showsDDay() {
        // Given a hidden view and an active task due today
        val task = task(isCompleted = false, dueDate = LocalDate.now())
        // When the active task is bound
        // Then the boundary label is D-Day
        assertBinding(task, "", View.GONE, "D-Day", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_dayAfterToday_showsDMinus1() {
        // Given a hidden view and an active task due tomorrow
        val task = task(isCompleted = false, dueDate = LocalDate.now().plusDays(1))
        // When the active task is bound
        // Then D-1 is displayed
        assertBinding(task, "", View.GONE, "D-1", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_thirtyOneDaysAhead_showsDMinus31() {
        // Given a hidden view and an active task due in 31 calendar days
        val task = task(isCompleted = false, dueDate = LocalDate.now().plusDays(31))
        // When the active task is bound
        // Then the full span is displayed, even though it crosses a month
        assertBinding(task, "", View.GONE, "D-31", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_completedThenActiveWithoutDate_clearsStaleClear() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        // Given the same task with no due date, first completed and then active
        val completedTask = task(isCompleted = true, dueDate = null)
        val activeTask = completedTask.copy(isCompleted = false)

        instrumentation.runOnMainSync {
            val textView = TextView(instrumentation.targetContext).apply {
                text = ""
                visibility = View.GONE
            }
            // When the completed and active states are bound in sequence
            setDueDateLabel(textView, completedTask)
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
            setDueDateLabel(textView, activeTask)

            // Then CLEAR is removed and the view is hidden
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    private fun task(isCompleted: Boolean, dueDate: LocalDate?): Task = Task(
        title = "A",
        description = "",
        isCompleted = isCompleted,
        id = "task-1",
        dueDate = dueDate
    )

    private fun assertBinding(
        task: Task?,
        initialText: String,
        initialVisibility: Int,
        expectedText: String,
        expectedVisibility: Int
    ) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.runOnMainSync {
            // Given the initial text and visibility
            val textView = TextView(instrumentation.targetContext).apply {
                text = initialText
                visibility = initialVisibility
            }
            // When the binding adapter is called
            setDueDateLabel(textView, task)
            // Then both observable properties match the spec
            assertEquals(expectedText, textView.text.toString())
            assertEquals(expectedVisibility, textView.visibility)
        }
    }
}
