package com.syzible.irishnouns.games.common.domainchoice;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.syzible.irishnouns.MainActivity;
import com.syzible.irishnouns.R;
import com.syzible.irishnouns.common.models.Category;
import com.syzible.irishnouns.common.persistence.Cache;
import com.syzible.irishnouns.games.gender.GenderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DomainAdaptor extends RecyclerView.Adapter<DomainAdaptor.GroupSelectionHolder> {
    private List<Category> categories = new ArrayList<>();
    private FragmentManager fragmentManager;

    DomainAdaptor(FragmentManager fragmentManager) {
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
            Cache.setLastChosenCategoryFileName(context, category.getFileName());
            MainActivity.setFragment(fragmentManager, GenderFragment.getInstance());
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
        @BindView(R.id.category_item_icon)
        ImageView icon;

        @BindView(R.id.category_item_name)
        TextView name;

        GroupSelectionHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
