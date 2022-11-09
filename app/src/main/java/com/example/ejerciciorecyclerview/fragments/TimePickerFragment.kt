package com.example.ejerciciorecycvalerview.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(val listener : (hours: Int, minutes: Int) -> Unit): DialogFragment(),
    TimePickerDialog.OnTimeSetListener
{
    override fun onTimeSet(p0: TimePicker?, hour: Int, mins: Int) {
        listener(hour,mins)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()

        val hora = c.get(Calendar.HOUR_OF_DAY)
        val min = c.get(Calendar.MINUTE)

        val picker = TimePickerDialog(activity as Context, this, hora,min, true)

        return picker
    }
}