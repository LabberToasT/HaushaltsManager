package com.example.lucas.haushaltsmanager.Entities.Report;

import com.example.lucas.haushaltsmanager.Entities.Currency;
import com.example.lucas.haushaltsmanager.Entities.Expense.ExpenseObject;
import com.example.lucas.haushaltsmanager.Utils.ExpenseUtils.ExpenseGrouper;

import java.util.List;

public class Month extends AbstractReport {
    private int month;
    private int year;

    private Month(int month, List<ExpenseObject> expenses, Currency currency) {
        super(
                String.valueOf(month),
                expenses,
                currency
        );
    }

    public static Month create(int month, int year, List<ExpenseObject> expenses, Currency currency) {
        Month self = new Month(month, expenses, currency);
        self.month = month;
        self.year = year;

        return self;
    }

    @Override
    protected List<ExpenseObject> filterExpenses(List<ExpenseObject> expenses) {

        return new ExpenseGrouper().byMonth(
                expenses,
                this.month,
                this.year
        );
    }
}
