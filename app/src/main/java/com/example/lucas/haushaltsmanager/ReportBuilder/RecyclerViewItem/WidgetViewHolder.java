package com.example.lucas.haushaltsmanager.ReportBuilder.RecyclerViewItem;

import android.content.ClipData;
import android.view.View;
import android.widget.ImageView;

import com.example.lucas.haushaltsmanager.R;
import com.example.lucas.haushaltsmanager.RecyclerView.Items.AbstractViewHolder;
import com.example.lucas.haushaltsmanager.RecyclerView.Items.IRecyclerItem;
import com.example.lucas.haushaltsmanager.ReportBuilder.RecyclerViewItem.WidgetViewItems.CardViewContent;
import com.example.lucas.haushaltsmanager.ReportBuilder.RecyclerViewItem.WidgetViewItems.CardViewItem;

public class WidgetViewHolder extends AbstractViewHolder implements View.OnLongClickListener {
    private CardViewContent cardViewContent;

    public WidgetViewHolder(View itemView) {
        super(itemView);

        itemView.setOnLongClickListener(this);
    }

    @Override
    public void bind(IRecyclerItem item) {
        CardViewItem cardViewItem = castToCardViewItem(item);
        cardViewContent = cardViewItem.getContent();

        setWidgetIcon(cardViewContent.getIcon());
    }

    @Override
    public boolean onLongClick(View v) {
        v.startDrag(
                ClipData.newPlainText("widget_tag", "widget_tag"),
                new View.DragShadowBuilder(v),
                cardViewContent.getWidget(),
                0
        );

        return true;
    }

    private void setWidgetIcon(int widgetIcon) {
        ImageView iconHolder = itemView.findViewById(R.id.imageView);
        iconHolder.setImageResource(widgetIcon);
    }

    private CardViewItem castToCardViewItem(IRecyclerItem item) {
        if (item instanceof CardViewItem) {
            return (CardViewItem) item;
        }

        throw new IllegalArgumentException(String.format(
                "Could not bind %s to ViewHolder of type %s!",
                item.getClass().toString(),
                WidgetViewHolder.class
        ));
    }
}