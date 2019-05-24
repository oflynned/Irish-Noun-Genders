package com.syzible.irishnoungenders.common.models;

import org.junit.Assert;
import org.junit.Test;

public class CategoryTest {
    @Test
    public void shouldConvertDomainToFile() {
        Assert.assertEquals("this_and_that", Category.domainToFile("this & that"));
    }

    @Test
    public void shouldConvertFileToDomain() {
        Assert.assertEquals("this & that", Category.fileToDomain("this_and_that"));
    }

    @Test
    public void shouldMarkChosen() {
        Category category = new Category("this", "this");
        Assert.assertTrue(category.isChosen());
    }

    @Test
    public void shouldMarkUnchosen() {
        Category category = new Category("this", "that");
        Assert.assertFalse(category.isChosen());
    }

    @Test
    public void shouldMarkCategoryAsLowerCase() {
        Category category = new Category("THIS", "THAT");
        Assert.assertEquals("this", category.getCategory());
    }

    @Test
    public void shouldMarkFilenameAsLowerCase() {
        Category category = new Category("THIS", "THAT");
        Assert.assertEquals("this", category.getFileName());
    }
}