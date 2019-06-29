package com.example.lucas.haushaltsmanager.RecyclerView.RecyclerViewItems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lucas.haushaltsmanager.Entities.Template;

import java.util.ArrayList;
import java.util.List;

public class TemplateItem implements IRecyclerItem {
    public static final int VIEW_TYPE = 8;
    private static final String TAG = TemplateItem.class.getSimpleName();

    private Template template;

    public TemplateItem(Template template) {
        this.template = template;
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE;
    }

    @Override
    public Object getContent() {
        return template;
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
    public IRecyclerItem getParent() {
        return null;
    }

    @Override
    public List<IRecyclerItem> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public void addChild(IRecyclerItem item) {
        // Do nothing
    }

    @NonNull
    @Override
    public String toString() {
        return template.toString();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof TemplateItem)) {
            return false;
        }

        TemplateItem other = (TemplateItem) obj;

        return other.getContent().equals(getContent());
    }
}
