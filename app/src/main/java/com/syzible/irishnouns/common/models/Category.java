package com.syzible.irishnouns.common.models;

public class Category {
    private String displayName, fileName;
    private boolean isChosen;

    public Category(String displayName, String currentCategory) {
        this.displayName = displayName;
        this.fileName = domainToFile(displayName);
        this.isChosen = getFileName().equals(currentCategory);
    }

    public String getCategory() {
        return displayName.toLowerCase();
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public static String domainToFile(String domain) {
        return domain.toLowerCase()
                .replace(" ", "_")
                .replace("&", "and");
    }

    public static String fileToDomain(String fileName) {
        return fileName.toLowerCase()
                .replace("_", " ")
                .replace("and", "&");
    }
}
