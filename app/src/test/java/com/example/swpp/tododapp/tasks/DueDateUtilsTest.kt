package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {

    private val today = LocalDate.of(2026, 10, 14)

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        // Given a completed task without a due date
        val dueDate: LocalDate? = null
        // When its label is calculated
        val result = dueDateLabel(dueDate, today, isCompleted = true)
        // Then completion takes precedence over the missing date
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_completedWithPastDueDate_returnsClear() {
        // Given a completed task due on 2026-10-10
        val dueDate = LocalDate.of(2026, 10, 10)
        // When its label is calculated
        val result = dueDateLabel(dueDate, today, isCompleted = true)
        // Then completion takes precedence over the overdue date
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmptyString() {
        // Given an active task without a due date
        val dueDate: LocalDate? = null
        // When its label is calculated
        val result = dueDateLabel(dueDate, today, isCompleted = false)
        // Then no label is shown
        assertEquals("", result)
    }

    @Test
    fun dueDateLabel_fourDaysOverdue_returnsDPlus4() {
        // Given an active task due on 2026-10-10
        val dueDate = LocalDate.of(2026, 10, 10)
        // When its label is calculated on 2026-10-14
        val result = dueDateLabel(dueDate, today, isCompleted = false)
        // Then it is four calendar days overdue
        assertEquals("D+4", result)
    }

    @Test
    fun dueDateLabel_dueToday_returnsDDay() {
        // Given an active task due on 2026-10-14
        val dueDate = LocalDate.of(2026, 10, 14)
        // When its label is calculated
        val result = dueDateLabel(dueDate, today, isCompleted = false)
        // Then it is due today
        assertEquals("D-Day", result)
    }

    @Test
    fun dueDateLabel_sixDaysAhead_returnsDMinus6() {
        // Given an active task due on 2026-10-20
        val dueDate = LocalDate.of(2026, 10, 20)
        // When its label is calculated on 2026-10-14
        val result = dueDateLabel(dueDate, today, isCompleted = false)
        // Then six calendar days remain
        assertEquals("D-6", result)
    }

    @Test
    fun dueDateLabel_fullMonthAhead_returnsDMinus31() {
        // Given an active task due on 2026-11-14
        val dueDate = LocalDate.of(2026, 11, 14)
        // When its label is calculated on 2026-10-14
        val result = dueDateLabel(dueDate, today, isCompleted = false)
        // Then all 31 calendar days are counted
        assertEquals("D-31", result)
    }

    @Test
    fun dueDateLabel_fullMonthOverdue_returnsDPlus31() {
        // Given an active task due on 2026-10-14
        val dueDate = LocalDate.of(2026, 10, 14)
        val laterToday = LocalDate.of(2026, 11, 14)
        // When its label is calculated on 2026-11-14
        val result = dueDateLabel(dueDate, laterToday, isCompleted = false)
        // Then all 31 overdue calendar days are counted
        assertEquals("D+31", result)
    }

    @Test
    fun dueDateLabel_dayBeforeToday_returnsDPlus1() {
        // Given an active task due on 2026-10-13
        val dueDate = LocalDate.of(2026, 10, 13)
        // When its label is calculated on 2026-10-14
        val result = dueDateLabel(dueDate, today, isCompleted = false)
        // Then it is one calendar day overdue
        assertEquals("D+1", result)
    }

    @Test
    fun dueDateLabel_atTodayBoundary_returnsDDay() {
        // Given an active task due on 2026-10-14
        val dueDate = LocalDate.of(2026, 10, 14)
        // When its label is calculated on that date
        val result = dueDateLabel(dueDate, today, isCompleted = false)
        // Then the boundary label is D-Day
        assertEquals("D-Day", result)
    }

    @Test
    fun dueDateLabel_dayAfterToday_returnsDMinus1() {
        // Given an active task due on 2026-10-15
        val dueDate = LocalDate.of(2026, 10, 15)
        // When its label is calculated on 2026-10-14
        val result = dueDateLabel(dueDate, today, isCompleted = false)
        // Then one calendar day remains
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_oneDayAcrossMonth_returnsDMinus1() {
        // Given an active task due on 2026-11-01
        val dueDate = LocalDate.of(2026, 11, 1)
        val monthEnd = LocalDate.of(2026, 10, 31)
        // When its label is calculated on 2026-10-31
        val result = dueDateLabel(dueDate, monthEnd, isCompleted = false)
        // Then the month boundary is one calendar day
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_acrossLeapDay_returnsDMinus2() {
        // Given an active task due on 2028-03-01
        val dueDate = LocalDate.of(2028, 3, 1)
        val leapMonth = LocalDate.of(2028, 2, 28)
        // When its label is calculated on 2028-02-28
        val result = dueDateLabel(dueDate, leapMonth, isCompleted = false)
        // Then February 29 is included in the two calendar days
        assertEquals("D-2", result)
    }
}
