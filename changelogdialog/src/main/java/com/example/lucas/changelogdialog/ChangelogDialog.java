package com.example.lucas.changelogdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.StringRes;
import android.view.View;
import android.widget.ListView;

public class ChangelogDialog extends DialogFragment {
    private AlertDialog.Builder mBuilder;
    private ReleaseHistory mReleaseHistory;
    @StringRes
    private int mCloseBtnTxt = R.string.close;

    public void createBuilder(Context context) {

        mBuilder = new AlertDialog.Builder(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.changelog_dialog, null);
        ListView releaseListView = view.findViewById(R.id.changelog_list);
        releaseListView.setAdapter(getReleaseAdapter());

        mBuilder.setView(view);

        mBuilder.setPositiveButton(mCloseBtnTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dismiss();
            }
        });

        return mBuilder.create();
    }

    public void setCloseBtnText(@StringRes int btnText) {

        mCloseBtnTxt = btnText;
    }

    /**
     * Methode um den Titel des ChangelogDialogs anzupassen.
     *
     * @param title ChangelogDialog Title
     */
    public void setTitle(String title) {

        mBuilder.setTitle(title);
    }

    public void setReleaseHistory(ReleaseHistory releaseHistory) {
        mReleaseHistory = releaseHistory;
    }

    private ChangelogAdapter getReleaseAdapter() {

        return new ChangelogAdapter(mReleaseHistory.getHistory(), getActivity());
    }
}
