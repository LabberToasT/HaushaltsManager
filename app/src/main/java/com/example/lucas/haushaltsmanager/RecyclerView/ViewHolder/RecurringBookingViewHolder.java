package com.example.lucas.haushaltsmanager.RecyclerView.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;

import com.example.lucas.haushaltsmanager.App.app;
import com.example.lucas.haushaltsmanager.Entities.Category;
import com.example.lucas.haushaltsmanager.Entities.Expense.ExpenseObject;
import com.example.lucas.haushaltsmanager.Entities.Price;
import com.example.lucas.haushaltsmanager.Entities.RecurringBooking;
import com.example.lucas.haushaltsmanager.R;
import com.example.lucas.haushaltsmanager.RecyclerView.RecyclerViewItems.IRecyclerItem;
import com.example.lucas.haushaltsmanager.RecyclerView.RecyclerViewItems.RecurringBookingItem;
import com.example.lucas.haushaltsmanager.Views.MoneyTextView;
import com.example.lucas.haushaltsmanager.Views.RoundedTextView;

public class RecurringBookingViewHolder extends AbstractViewHolder {
    // TODO: Dieser ViewHolder hat exakt die gleiche Struktur die auch der ExpenseViewHolder hat
    // --> kann man diese beiden ViewHolder irgendwie zusammen legen?
    private static final String TAG = RecurringBookingViewHolder.class.getSimpleName();

    private RoundedTextView mRoundedTextView;
    private TextView mTitle;
    private MoneyTextView mPrice;
    private TextView mPerson;

    public RecurringBookingViewHolder(View itemView) {
        super(itemView);

        mRoundedTextView = itemView.findViewById(R.id.recycler_view_expense_rounded_text_view);
        mTitle = itemView.findViewById(R.id.recycler_view_expense_title);
        mPrice = itemView.findViewById(R.id.recycler_view_expense_price);
        mPerson = itemView.findViewById(R.id.recycler_view_expense_person);
    }

    @Override
    public void bind(IRecyclerItem item) {
        if (!(item instanceof RecurringBookingItem)) {
            throw new IllegalArgumentException(String.format("Could not attach %s to %s", item.getClass().getSimpleName(), TAG));
        }

        ExpenseObject expense = ((RecurringBooking) item.getContent()).getBooking();

        setRoundedTextViewText(expense.getCategory());
        setTitle(expense.getTitle());
        setPrice(expense.getPrice());
        setPerson("");

        setBackgroundColor();
    }

    private void setBackgroundColor() {
        if (itemView.isSelected())
            itemView.setBackgroundColor(getColor(R.color.list_item_highlighted));
        else
            itemView.setBackgroundColor(getColor(R.color.list_item_background));
    }

    private void setRoundedTextViewText(Category category) {
        mRoundedTextView.setCircleColorConsiderBrightness(category.getColor().getColorInt());
        mRoundedTextView.setCenterText(category.getTitle().charAt(0) + "");
    }

    private void setTitle(String title) {
        mTitle.setText(title);
    }

    private void setPrice(Price price) {
        mPrice.bind(price);
    }

    private void setPerson(String person) {
        mPerson.setText(person);
    }

    private int getColor(@ColorRes int colorRes) {
        return app.getContext().getResources().getColor(colorRes);
    }
}
