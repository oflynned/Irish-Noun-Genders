package com.syzible.irishnoungenders.common.models;

import android.content.Context;

import com.syzible.irishnoungenders.R;

import java.util.Locale;

public class Category extends Domain {
    private boolean isChosen;

    public Category(String currentCategoryFileName, Domain domain) {
        this(currentCategoryFileName, domain.getFileName(), domain.getEn(), domain.getGa(), domain.getActiveLocale());
    }

    private Category(String currentCategoryFileName, String fileName, String en, String ga, String activeLocale) {
        super(fileName, en, ga, activeLocale);
        this.isChosen = getFileName().equals(currentCategoryFileName);
    }

    public String getDisplayName(String locale) {
        return locale.equals("en") ? getEn() : getGa();
    }

    public int getIcon(Context context) {
        int icon = context.getResources()
                .getIdentifier(getFileName(), "drawable", context.getPackageName());
        return icon == 0 ? R.drawable.accounting : icon;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public static String domainToFile(String domain) {
        return domain.toLowerCase(Locale.US)
                .replace(" ", "_")
                .replace("&", "and");
    }

    public static String fileToDomain(String fileName) {
        return fileName.toLowerCase(Locale.US)
                .replace("_", " ")
                .replace("and", "&");
    }
}
