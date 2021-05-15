package com.example.lucas.haushaltsmanager.Activities;

import com.example.lucas.haushaltsmanager.Entities.Expense.IBooking;
import com.example.lucas.haushaltsmanager.Entities.Frequency;
import com.example.lucas.haushaltsmanager.Entities.RecurringBooking;

import java.util.Calendar;

public class RecurringBookingBuilder {
    private Calendar start;
    private Calendar end;
    private Frequency frequency;
    private IBooking booking;

    public void setStart(Calendar start) {
        this.start = start;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public void setBooking(IBooking booking) {
        this.booking = booking;
    }

    public RecurringBooking build() throws RuntimeException {
        if (!recurringBookingCanBeCreated()) {
            throw new RuntimeException("Cannot create RecurringBooking");
        }

        return new RecurringBooking(
                start,
                end,
                frequency,
                booking
        );
    }

    public boolean recurringBookingCanBeCreated() {
        if (null == start) {
            return false;
        }

        if (null == end) {
            return false;
        }

        if (null == frequency) {
            return false;
        }

        if (null == booking) {
            return false;
        }

        return true;
    }
}