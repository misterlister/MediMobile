package com.example.medimobile.ui.components.inputfields

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import java.time.LocalDate

@Composable
fun DateSelector(
    context: Context,
    date: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val year = date?.year ?: LocalDate.now().year
    val month = (date?.monthValue ?: LocalDate.now().monthValue) - 1
    val day = date?.dayOfMonth ?: LocalDate.now().dayOfMonth

    DisposableEffect(Unit) {
        val datePicker = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                onDateSelected(LocalDate.of(selectedYear, selectedMonth + 1, selectedDay))
            },
            year, month, day
        )

        datePicker.show()

        onDispose { datePicker.dismiss() }
    }
}
