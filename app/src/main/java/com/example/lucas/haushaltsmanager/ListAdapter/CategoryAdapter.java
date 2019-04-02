package com.example.lucas.haushaltsmanager.ListAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.lucas.haushaltsmanager.Entities.Category;
import com.example.lucas.haushaltsmanager.R;
import com.example.lucas.haushaltsmanager.Utils.ViewUtils;
import com.example.lucas.haushaltsmanager.Views.RoundedTextView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseExpandableListAdapter {
    // TODO: Durch RecyclerView ersetzen

    private List<Category> mCategoryData;
    private Context mContext;
    private List<Category> mSelectedChildren;

    public CategoryAdapter(List<Category> categoryData, Context context) {

        mCategoryData = categoryData;
        mContext = context;
        mSelectedChildren = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return mCategoryData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mCategoryData.get(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategoryData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCategoryData.get(groupPosition).getChildren().get(childPosition);
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

    class ViewHolder {
        RoundedTextView roundedTextView;
        TextView txtCategoryName;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Category groupCategory = (Category) getGroup(groupPosition);
        ViewHolder groupViewHolder;

        if (convertView == null) {

            groupViewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.category_group_item, null);

            groupViewHolder.roundedTextView = convertView.findViewById(R.id.category_item_rounded_text_view);
            groupViewHolder.txtCategoryName = convertView.findViewById(R.id.category_item_name);

            convertView.setTag(groupViewHolder);
        } else {

            groupViewHolder = (ViewHolder) convertView.getTag();
        }

        String categoryName = groupCategory.getTitle();

        if (ViewUtils.getColorBrightness(groupCategory.getColorString()) > 0.5) {
            groupViewHolder.roundedTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text_color_dark));
        } else {
            groupViewHolder.roundedTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text_color_bright));
        }

        groupViewHolder.roundedTextView.setCenterText(categoryName.substring(0, 1).toUpperCase());
        groupViewHolder.roundedTextView.setCircleColor(groupCategory.getColorString());
        groupViewHolder.roundedTextView.setCircleDiameter(33);
        groupViewHolder.txtCategoryName.setText(categoryName);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Category childCategory = (Category) getChild(groupPosition, childPosition);
        ViewHolder childViewHolder;

        if (convertView == null) {

            childViewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.category_child_item, null);

            childViewHolder.roundedTextView = convertView.findViewById(R.id.category_item_rounded_text_view);
            childViewHolder.txtCategoryName = convertView.findViewById(R.id.category_item_name);

            convertView.setTag(childViewHolder);
        } else {

            childViewHolder = (ViewHolder) convertView.getTag();
        }

        String categoryName = childCategory.getTitle();
        if (mSelectedChildren.contains(childCategory)) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_highlighted));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        if (ViewUtils.getColorBrightness(childCategory.getColorString()) > 0.5) {// Hintergrund ist hell
            childViewHolder.roundedTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text_color_dark));
        } else {// Hintegrund ist dunkel
            childViewHolder.roundedTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text_color_bright));
        }
        childViewHolder.roundedTextView.setCenterText(categoryName.substring(0, 1).toUpperCase());
        childViewHolder.roundedTextView.setCircleColor(childCategory.getColorString());
        childViewHolder.roundedTextView.setCircleDiameter(33);
        childViewHolder.txtCategoryName.setText(categoryName);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean isGroup(long id) {
        return ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP;
    }

    public boolean isChildSelected(Category category) {
        return mSelectedChildren.contains(category);
    }

    public void deselectChild(Category category) {
        mSelectedChildren.remove(category);
    }

    public void deselectAll() {
        mSelectedChildren.clear();
    }

    public void selectChild(Category category) {
        // TODO: Überprüfe ob die Kategorie auch wirklich eine KindKategorie ist
        mSelectedChildren.add(category);
    }

    public List<Category> getSelectedChildData() {
        return mSelectedChildren;
    }

    public int getSelectedChildItemCount() {
        return mSelectedChildren.size();
    }
}