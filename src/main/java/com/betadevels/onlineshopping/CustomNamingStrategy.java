package com.betadevels.onlineshopping;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class CustomNamingStrategy extends ImprovedNamingStrategy
{
    private static final String PLURAL_SUFFIX = "s";
    @Override
    public String classToTableName(String className) {
        String singularTableName = super.classToTableName(className);
        return transformToPluralForm(singularTableName);
    }

    private String transformToPluralForm(String singularTableName)
    {
        return singularTableName + PLURAL_SUFFIX;
    }
}
