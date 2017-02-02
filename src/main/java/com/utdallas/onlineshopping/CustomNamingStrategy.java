package com.utdallas.onlineshopping;

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
        StringBuilder pluralTableName = new StringBuilder();
        pluralTableName.append(singularTableName);
        pluralTableName.append(PLURAL_SUFFIX);
        return pluralTableName.toString();
    }
}
