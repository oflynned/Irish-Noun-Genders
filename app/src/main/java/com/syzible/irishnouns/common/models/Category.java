package com.syzible.irishnouns.common.models;

import java.util.Locale;

public class Category {
    private String displayName, fileName;
    private boolean isChosen;

    public Category(String displayName, String currentCategory) {
        this.displayName = displayName;
        this.fileName = domainToFile(displayName);
        this.isChosen = getFileName().equals(currentCategory);
    }

    public String getCategory() {
        return displayName.toLowerCase(Locale.US);
    }

    public String getFileName() {
        return fileName;
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
