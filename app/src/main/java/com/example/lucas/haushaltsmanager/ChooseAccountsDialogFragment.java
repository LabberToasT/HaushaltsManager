package com.example.lucas.haushaltsmanager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ChooseAccountsDialogFragment extends DialogFragment {

    ExpensesDataSource expensesDataSource;
    boolean checkedItems[];
    SharedPreferences settings;
    OnSelectedAccount mCallback;

    /**
     * Standart Fragment Methode die genutzt wird, um zu checken ob die aufrufende Activity auch das interface inplementiert.
     * @param context Kontext
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

            mCallback = (OnSelectedAccount) context;
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + " must implement OnSelectedAccountListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        settings = getActivity().getSharedPreferences("ActiveAccounts", Context.MODE_PRIVATE);
        expensesDataSource = new ExpensesDataSource(getActivity());

        expensesDataSource.open();
        final ArrayList<Account> accounts = expensesDataSource.getAllAccounts();
        expensesDataSource.close();

        final SharedPreferences.Editor editor = settings.edit();


        checkedItems = new boolean[accounts.size()];
        for (int i = 0; i < accounts.size(); i++) {

            checkedItems[i] = settings.getBoolean(accounts.get(i).getAccountName(), false);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.choose_account);

        builder.setMultiChoiceItems(accountNames(accounts), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                editor.putBoolean(accounts.get(which).getAccountName(), isChecked);
                //TODO die buchungen der MainActivityTab live neuladen beim click auf ein Konto
                //informaiert die activity darüber, dass ein Konto ausgewählt wurde
                mCallback.onAccountSelected(accounts.get(which).getIndex(), isChecked);
            }
        });

        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editor.apply();
                dismiss();
            }
        });

        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dismiss();
            }
        });

        builder.setNeutralButton(R.string.btn_new_acc, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent newAccountIntent = new Intent(getActivity(), CreateNewAccount.class);
                getActivity().startActivity(newAccountIntent);
            }
        });

        return builder.create();
    }

    String[] accountNames(ArrayList<Account> accounts) {

        String accountNames[] = new String[accounts.size()];

        for (int i = 0; i < accounts.size(); i++) {

            accountNames[i] = accounts.get(i).getAccountName();
        }

        return accountNames;
    }

    public interface OnSelectedAccount {
        void onAccountSelected(long accountId, boolean isChecked);
    }
}
