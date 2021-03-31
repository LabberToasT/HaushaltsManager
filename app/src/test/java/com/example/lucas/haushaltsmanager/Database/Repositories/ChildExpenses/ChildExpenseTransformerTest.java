package com.example.lucas.haushaltsmanager.Database.Repositories.ChildExpenses;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.MatrixCursor;

import com.example.lucas.haushaltsmanager.Database.Repositories.Categories.CategoryTransformer;
import com.example.lucas.haushaltsmanager.Entities.Category;
import com.example.lucas.haushaltsmanager.Entities.Color;
import com.example.lucas.haushaltsmanager.Entities.Expense.ExpenseObject;
import com.example.lucas.haushaltsmanager.Entities.Expense.ExpenseType;
import com.example.lucas.haushaltsmanager.Entities.Price;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;

public class ChildExpenseTransformerTest {
    private ChildExpenseTransformer transformer;

    public void setUp() {
        transformer = new ChildExpenseTransformer(
                new CategoryTransformer()
        );
    }

    @Test
    public void testCursorToChildBookingWithValidCursorShouldSucceed() {
        // Arrange
        final ExpenseObject expectedChildExpense = getSimpleExpense();
        expectedChildExpense.setExpenseType(ExpenseObject.EXPENSE_TYPES.CHILD_EXPENSE);

        Cursor cursor = createCursor(new HashMap<String, Object>() {{
            put("BOOKINGS.id", expectedChildExpense.getId().toString());
            put("BOOKINGS.date", expectedChildExpense.getDate().getTimeInMillis());
            put("BOOKINGS.title", expectedChildExpense.getTitle());
            put("BOOKINGS.price", expectedChildExpense.getUnsignedPrice());
            put("BOOKINGS.expenditure", expectedChildExpense.isExpenditure() ? 1 : 0);
            put("BOOKINGS.notice", expectedChildExpense.getNotice());
            put("BOOKINGS.account_id", expectedChildExpense.getAccountId());
            put("CATEGORIES.id", expectedChildExpense.getCategory().getId().toString());
            put("CATEGORIES.name", expectedChildExpense.getCategory().getTitle());
            put("CATEGORIES.color", expectedChildExpense.getCategory().getColor().getColorString());
            put("CATEGORIES.default_expense_type", expectedChildExpense.getCategory().getDefaultExpenseType().value() ? 1 : 0);
        }});

        // Act
        ExpenseObject transformedChildExpense = transformer.transform(cursor);

        // Assert
        assertEquals(expectedChildExpense, transformedChildExpense);
    }

    @Test(expected = CursorIndexOutOfBoundsException.class)
    public void testCursorToChildBookingWithInvalidCursorShouldThrowCursorIndexOutOfBoundsException() {
        final ExpenseObject expectedChildExpense = getSimpleExpense();
        expectedChildExpense.setExpenseType(ExpenseObject.EXPENSE_TYPES.CHILD_EXPENSE);

        Cursor cursor = createCursor(new HashMap<String, Object>() {{
            put("BOOKINGS.id", expectedChildExpense.getId().toString());
            put("BOOKINGS.date", expectedChildExpense.getDate().getTimeInMillis());
            put("BOOKINGS.title", expectedChildExpense.getTitle());
            // Price is not present in Cursor
            put("BOOKINGS.expenditure", expectedChildExpense.isExpenditure() ? 1 : 0);
            put("BOOKINGS.notice", expectedChildExpense.getNotice());
            put("BOOKINGS.account_id", expectedChildExpense.getAccountId());
            put("CATEGORIES.id", expectedChildExpense.getCategory().getId().toString());
            put("CATEGORIES.name", expectedChildExpense.getCategory().getTitle());
            put("CATEGORIES.color", expectedChildExpense.getCategory().getColor().getColorString());
            put("CATEGORIES.default_expense_type", expectedChildExpense.getCategory().getDefaultExpenseType().value() ? 1 : 0);
        }});

        // Act
        transformer.transform(cursor);
    }

    private Cursor createCursor(Map<String, Object> values) {
        String[] columns = values.keySet().toArray(new String[0]);
        MatrixCursor cursor = new MatrixCursor(columns);

        MatrixCursor.RowBuilder builder = cursor.newRow();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        cursor.moveToFirst();
        return cursor;
    }

    private ExpenseObject getSimpleExpense() {
        Category category = new Category("Kategorie", new Color("#121212"), ExpenseType.expense());

        return new ExpenseObject(
                "Ausgabe",
                new Price(3135, false),
                category,
                UUID.randomUUID()
        );
    }
}
