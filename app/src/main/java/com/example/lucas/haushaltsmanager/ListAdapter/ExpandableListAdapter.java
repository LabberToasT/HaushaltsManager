package com.example.lucas.haushaltsmanager.ListAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.lucas.androidcharts.DataSet;
import com.example.lucas.androidcharts.PieChart;
import com.example.lucas.haushaltsmanager.Entities.Category;
import com.example.lucas.haushaltsmanager.Entities.ExpenseObject;
import com.example.lucas.haushaltsmanager.ExpListViewSelectedItem;
import com.example.lucas.haushaltsmanager.ExpandableListItemSelector;
import com.example.lucas.haushaltsmanager.PreferencesHelper.UserSettingsPreferences;
import com.example.lucas.haushaltsmanager.R;
import com.example.lucas.haushaltsmanager.Utils.ViewUtils;
import com.example.lucas.haushaltsmanager.Views.RoundedTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ExpenseObject> mGroupData;
    private HashMap<ExpenseObject, List<ExpenseObject>> mChildData;
    private int mRed, mGreen;
    private ExpandableListItemSelector mItemSelector;

    public ExpandableListAdapter(
            Context context,
            List<ExpenseObject> groupData,
            HashMap<ExpenseObject, List<ExpenseObject>> childData
    ) {

        mContext = context;
        mGroupData = groupData;
        mChildData = childData;
        mItemSelector = new ExpandableListItemSelector(this);

        mRed = context.getResources().getColor(R.color.booking_expense);
        mGreen = context.getResources().getColor(R.color.booking_income);
    }

    @Override
    public int getGroupCount() {

        return mGroupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return mChildData.get(mGroupData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mGroupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return mChildData.get(mGroupData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpenseObject groupExpense = (ExpenseObject) getGroup(groupPosition);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        boolean isSelected = isItemSelected(groupPosition, -1);

        switch (groupExpense.getExpenseType()) {

            case PARENT_EXPENSE:

                convertView = inflater.inflate(R.layout.activity_exp_listview_parent, parent, false);
                if (isExpanded)
                    convertView.findViewById(R.id.exp_listview_paren_divider).setVisibility(View.GONE);

                if (isSelected)
                    convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_highlighted));

                PieChart pieChart = convertView.findViewById(R.id.exp_listview_parent_pie_chart);
                TextView txtTitle22 = convertView.findViewById(R.id.exp_listview_parent_title);
                TextView txtPrice = convertView.findViewById(R.id.exp_listview_parent_price);
                TextView txtCurrencySymbol = convertView.findViewById(R.id.exp_listview_parent_currency_symbol);
                TextView txtPerson2 = convertView.findViewById(R.id.exp_listview_parent_person);

                pieChart.setPieData(preparePieData(mChildData.get(groupExpense)));
                txtTitle22.setText(groupExpense.getTitle());
                txtPrice.setText(String.format(mContext.getResources().getConfiguration().locale, "%.2f", groupExpense.getSignedPrice()));
                txtPrice.setTextColor(groupExpense.getSignedPrice() < 0 ? mRed : mGreen);
                txtCurrencySymbol.setText(getMainCurrencySymbol());
                txtCurrencySymbol.setTextColor(groupExpense.getSignedPrice() < 0 ? mRed : mGreen);
                txtPerson2.setText("");// sollen User bei einer Multiuser funktionalität auch bei ParentBuchungen angezeigt werden || falls nicht, einen leeren string übergeben sodass das Format gleich bleibt!
                break;
            case DATE_PLACEHOLDER:

                convertView = inflater.inflate(R.layout.activity_exp_listview_date, parent, false);

                TextView date = convertView.findViewById(R.id.exp_listview_sep_header_date);
                date.setText(groupExpense.getDate());
                break;
            case NORMAL_EXPENSE:

                convertView = inflater.inflate(R.layout.activity_exp_listview_group, parent, false);
                if (isSelected)
                    convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_highlighted));

                RoundedTextView roundedTextView = convertView.findViewById(R.id.exp_listview_group_rounded_textview);
                TextView txtTitle2 = convertView.findViewById(R.id.exp_listview_group_title);
                TextView txtPerson = convertView.findViewById(R.id.exp_listview_group_person);
                TextView txtPaidPrice = convertView.findViewById(R.id.exp_listview_group_price);
                TextView txtPaidCurrency = convertView.findViewById(R.id.exp_listview_group_currency_symbol);

                if (ViewUtils.getColorBrightness(groupExpense.getCategory().getColorString()) > 0.5) {
                    roundedTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text_color_dark));
                } else {
                    roundedTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text_color_bright));
                }

                String category = groupExpense.getCategory().getTitle();
                roundedTextView.setCenterText(category.substring(0, 1).toUpperCase());
                roundedTextView.setCircleColor(groupExpense.getCategory().getColorString());
                txtTitle2.setText(groupExpense.getTitle());
                txtPerson.setText("");
                txtPaidPrice.setText(String.format(mContext.getResources().getConfiguration().locale, "%.2f", groupExpense.getUnsignedPrice()));
                txtPaidPrice.setTextColor(groupExpense.isExpenditure() ? mRed : mGreen);
                txtPaidCurrency.setText(getMainCurrencySymbol());
                txtPaidCurrency.setTextColor(groupExpense.isExpenditure() ? mRed : mGreen);
                break;

            case TRANSFER_EXPENSE:

                // TODO: Eigenes Layout für transfer mExpenses definieren
                convertView = inflater.inflate(R.layout.activity_exp_listview_group, parent, false);
                if (isSelected)
                    convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_highlighted));

                RoundedTextView roundedTextView3 = convertView.findViewById(R.id.exp_listview_group_rounded_textview);
                TextView txtTitle3 = convertView.findViewById(R.id.exp_listview_group_title);
                TextView txtPerson3 = convertView.findViewById(R.id.exp_listview_group_person);
                TextView txtPaidPrice3 = convertView.findViewById(R.id.exp_listview_group_price);
                TextView txtPaidCurrency3 = convertView.findViewById(R.id.exp_listview_group_currency_symbol);

                String category2 = groupExpense.getCategory().getTitle();
                roundedTextView3.setTextColor(Color.WHITE);
                roundedTextView3.setCenterText(category2.substring(0, 1).toUpperCase());
                roundedTextView3.setCircleColor(groupExpense.getCategory().getColorString());
                txtTitle3.setText(groupExpense.getTitle());
                txtPerson3.setText("");
                txtPaidPrice3.setText(String.format(mContext.getResources().getConfiguration().locale, "%.2f", groupExpense.getUnsignedPrice()));
                txtPaidPrice3.setTextColor(groupExpense.isExpenditure() ? mRed : mGreen);
                txtPaidCurrency3.setText(getMainCurrencySymbol());
                txtPaidCurrency3.setTextColor(groupExpense.isExpenditure() ? mRed : mGreen);
                break;
            default:
                throw new UnsupportedOperationException("Für den Buchungstyp: " + groupExpense.getExpenseType().name() + " gibt es keine View methode!");
        }

        return convertView;
    }

    /**
     * Methode um die Datensätze eines PieCharts erstellen soll.
     *
     * @param expenses Buchungen
     * @return DataSets
     */
    private List<DataSet> preparePieData(List<ExpenseObject> expenses) {
        List<DataSet> dataSets = new ArrayList<>();
        Map<Category, Integer> summedCategories = new HashMap<>();

        for (ExpenseObject expense : expenses) {
            Category category = expense.getCategory();
            Integer count = summedCategories.get(category);

            if (count != null)
                summedCategories.put(category, count + 1);
            else
                summedCategories.put(category, 1);
        }

        for (Map.Entry<Category, Integer> category : summedCategories.entrySet()) {
            dataSets.add(new DataSet(category.getValue(), category.getKey().getColorInt(), category.getKey().getTitle()));
        }

        return dataSets;
    }

    /**
     * Methode um das Symbol der Hauptwährung aus den SharedPreferences auszulesen.
     *
     * @return MainCurrencySymbol
     */
    private String getMainCurrencySymbol() {
        return new UserSettingsPreferences(mContext).getMainCurrency().getSymbol();
    }

    /**
     * Klasse um nicht bei jedem Child die Objekte neu erstellen zu müssen
     */
    private class ChildViewHolder {

        RoundedTextView roundedTextView;
        TextView txtTitle;
        TextView txtPerson;
        TextView txtPaidPrice;
        TextView txtBaseCurrency;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ExpenseObject childExpense = (ExpenseObject) getChild(groupPosition, childPosition);
        ChildViewHolder childViewHolder;

        if (convertView == null) {

            childViewHolder = new ChildViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_exp_listview_child, parent, false);

            childViewHolder.roundedTextView = convertView.findViewById(R.id.exp_list_view_item_circle);
            childViewHolder.txtTitle = convertView.findViewById(R.id.exp_list_view_item_title);
            childViewHolder.txtPerson = convertView.findViewById(R.id.exp_list_view_item_person);
            childViewHolder.txtPaidPrice = convertView.findViewById(R.id.exp_list_view_item_paid_price);
            childViewHolder.txtBaseCurrency = convertView.findViewById(R.id.exp_list_view_item_paid_currency);

            convertView.setTag(childViewHolder);
        } else {

            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        if (isItemSelected(groupPosition, childPosition))
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_highlighted));
        else
            convertView.setBackgroundColor(Color.WHITE);


        String category = childExpense.getCategory().getTitle();

        childViewHolder.roundedTextView.setTextColor(Color.WHITE);
        childViewHolder.roundedTextView.setCenterText(category.substring(0, 1).toUpperCase());
        childViewHolder.roundedTextView.setCircleColor(childExpense.getCategory().getColorString());
        childViewHolder.roundedTextView.setCircleDiameter(33);
        childViewHolder.txtTitle.setText(childExpense.getTitle());
        childViewHolder.txtPerson.setText("");
        childViewHolder.txtPaidPrice.setText(String.format(mContext.getResources().getConfiguration().locale, "%.2f", childExpense.getUnsignedPrice()));
        childViewHolder.txtPaidPrice.setTextColor(childExpense.isExpenditure() ? mRed : mGreen);
        childViewHolder.txtBaseCurrency.setText(getMainCurrencySymbol());
        childViewHolder.txtBaseCurrency.setTextColor(childExpense.isExpenditure() ? mRed : mGreen);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void selectItem(int groupPosition, int childPosition) {
        mItemSelector.selectItem(groupPosition, childPosition);

        notifyDataSetChanged();
    }

    public void unselectItem(int groupPosition, int childPosition) {
        mItemSelector.unselectItem(groupPosition, childPosition);

        notifyDataSetChanged();
    }

    public boolean isItemSelected(int groupPosition, int childPosition) {
        return mItemSelector.isItemSelected(groupPosition, childPosition);
    }

    public int getSelectedGroupsCount() {
        return mItemSelector.getSelectedGroupsCount();
    }

    public int getSelectedChildrenCount() {
        return mItemSelector.getSelectedChildrenCount();
    }

    public int getSelectedItemCount() {
        return mItemSelector.getSelectedItemCount();
    }

    public List<ExpListViewSelectedItem> getSelectedItems() {
        return mItemSelector.getSelectedItems();
    }

    public void unselectAll() {
        mItemSelector.unselectAll();
    }
}
