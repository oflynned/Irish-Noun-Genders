package com.syzible.irishnoungenders.common.models;

import org.jetbrains.annotations.NotNull;

public class Domain implements Comparable<Domain> {
    private String fileName, en, ga, activeLocale;

    public Domain(String fileName, String en, String ga, String activeLocale) {
        this.fileName = fileName;
        this.en = en;
        this.ga = ga;
        this.activeLocale = activeLocale;
    }

    public String getFileName() {
        return fileName;
    }

    public String getEn() {
        return en;
    }

    public String getGa() {
        return ga;
    }

    public String getActiveLocale() {
        return activeLocale;
    }

    public String getLocalisedName() {
        return this.activeLocale.equals("en") ? en : ga;
    }

    @Override
    public int compareTo(@NotNull Domain domain) {
        if (activeLocale.equals("ga")) {
            return this.ga.compareTo(domain.ga);
        }

        return this.en.compareTo(domain.en);
    }

    @Override
    public String toString() {
        return "Domain{" +
                "fileName='" + fileName + '\'' +
                ", en='" + en + '\'' +
                ", ga='" + ga + '\'' +
                ", activeLocale='" + activeLocale + '\'' +
                '}';
    }
}
