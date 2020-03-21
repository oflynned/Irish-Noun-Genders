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
}