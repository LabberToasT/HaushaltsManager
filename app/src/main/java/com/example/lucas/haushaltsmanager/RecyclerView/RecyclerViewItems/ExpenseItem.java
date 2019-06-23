package com.example.lucas.haushaltsmanager.RecyclerView.RecyclerViewItems;

import com.example.lucas.haushaltsmanager.Entities.Expense.ExpenseObject;

import java.util.ArrayList;
import java.util.List;

public class ExpenseItem implements IRecyclerItem {
    public static final int VIEW_TYPE = 1;
    private static final String TAG = ExpenseItem.class.getSimpleName();

    private ExpenseObject mExpense;
    private DateItem mParent;

    public ExpenseItem(ExpenseObject expense, DateItem parent) {
        mExpense = expense;
        mParent = parent;
    }

    @Deprecated
    public ExpenseItem(ExpenseObject expense) {
        mExpense = expense;
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE;
    }

    @Override
    public ExpenseObject getContent() {
        return mExpense;
    }

    @Override
    public boolean canExpand() {
        return false;
    }

    @Override
    public boolean isExpanded() {
        return false;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        throw new IllegalStateException(String.format("setExpanded method called on a Object that cannot expand: %s", TAG));
    }

    @Override
    public List<IRecyclerItem> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExpenseItem)) {
            return false;
        }

        ExpenseItem other = (ExpenseItem) obj;

        return other.getContent().equals(getContent());
    }

    @Override
    public void addChild(IRecyclerItem item) {
        // Do nothing
    }

    @Override
    public DateItem getParent() {
        return mParent;
    }
}
