package com.example.lucas.haushaltsmanager.FABToolbar.Actions.MenuItems;

import android.content.Context;
import android.util.Log;

import com.example.lucas.haushaltsmanager.Database.Repositories.ChildExpenses.ChildExpenseRepository;
import com.example.lucas.haushaltsmanager.Database.Repositories.ChildExpenses.Exceptions.ChildExpenseNotFoundException;
import com.example.lucas.haushaltsmanager.Entities.Expense.ExpenseObject;
import com.example.lucas.haushaltsmanager.FABToolbar.Actions.ActionPayload;
import com.example.lucas.haushaltsmanager.FABToolbar.Actions.MenuItems.ActionKey.ActionKey;
import com.example.lucas.haushaltsmanager.FABToolbar.Actions.MenuItems.ActionKey.IActionKey;
import com.example.lucas.haushaltsmanager.R;
import com.example.lucas.haushaltsmanager.RecyclerView.AdditionalFunctionality.SelectedRecyclerItem;
import com.example.lucas.haushaltsmanager.RecyclerView.RecyclerViewItems.ChildItem;

public class ExtractMenuItem implements IMenuItem {
    public static final String ACTION_KEY = "extractAction";
    private static final String TAG = ExtractMenuItem.class.getSimpleName();

    private IActionKey mActionKey;

    private OnSuccessCallback mCallback;
    private ChildExpenseRepository mChildExpenseRepo;

    public ExtractMenuItem(OnSuccessCallback callback) {
        mCallback = callback;
        mActionKey = new ActionKey(ACTION_KEY);
    }

    @Override
    public int getIconRes() {
        return R.drawable.ic_extract_child_white;
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public int getHintRes() {
        return R.string.fab_menu_item_extract_hint;
    }

    @Override
    public IActionKey getActionKey() {
        return mActionKey;
    }

    @Override
    public void handleClick(ActionPayload actionPayload, Context context) {
        initRepo(context);

        for (SelectedRecyclerItem selectedItem : actionPayload.getItems()) {
            extractChild((ChildItem) selectedItem.getItem(), selectedItem.getPosition());
        }
    }

    private void initRepo(Context context) {
        mChildExpenseRepo = new ChildExpenseRepository(context);
    }

    private void extractChild(ChildItem child, int position) {
        try {
            ExpenseObject newExpense = mChildExpenseRepo.extractChildFromBooking(child.getContent());

            if (null != mCallback) {
                mCallback.onSuccess(position, newExpense);
            }
        } catch (ChildExpenseNotFoundException e) {

            Log.e(TAG, "Could not extract ChildExpense " + (child.getContent()).getTitle(), e);
        }
    }

    public interface OnSuccessCallback {
        void onSuccess(int oldPosition, ExpenseObject newExpense);
    }
}