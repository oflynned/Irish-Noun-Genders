package com.syzible.irishnoungenders.screens.modes.common.domainchoice;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.firebase.Event;
import com.syzible.irishnoungenders.common.firebase.FirebaseLogger;
import com.syzible.irishnoungenders.common.models.Category;
import com.syzible.irishnoungenders.common.persistence.Cache;

import java.util.ArrayList;
import java.util.List;

public class DomainAdapter extends RecyclerView.Adapter<DomainAdapter.GroupSelectionHolder> {
    private List<Category> categories = new ArrayList<>();
    private FragmentManager fragmentManager;

    DomainAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public GroupSelectionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.component_category_item, viewGroup, false);
        return new GroupSelectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupSelectionHolder groupSelectionHolder, int i) {
        Context context = groupSelectionHolder.itemView.getContext();
        Category category = categories.get(i);
        groupSelectionHolder.setIsRecyclable(false);
        groupSelectionHolder.icon.setImageResource(category.getIcon(context));
        groupSelectionHolder.name.setText(category.getCategory());
        if (category.isChosen()) {
            groupSelectionHolder.name.setTypeface(groupSelectionHolder.name.getTypeface(), Typeface.BOLD);
            groupSelectionHolder.name.setTextSize(18);
        }

        groupSelectionHolder.itemView.setOnClickListener(v -> {
            FirebaseLogger.logEvent(groupSelectionHolder.itemView.getContext(), Event.CHANGE_DOMAIN, "new_domain", category.getCategory());
            Cache.setLastChosenCategoryFileName(context, category.getFileName());
            MainActivity.popFragment(fragmentManager);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    void setCategoryList(List<Category> categories) {
        this.categories = categories;
    }

    static class GroupSelectionHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        GroupSelectionHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.category_item_icon);
            name = itemView.findViewById(R.id.category_item_name);
        }
    }
}
