package com.example.lucas.haushaltsmanager.ExpenseImporter.Parser.AtomicParser.CategoryParser.RequiredFields;

import com.example.lucas.haushaltsmanager.ExpenseImporter.Parser.IRequiredField;
import com.example.lucas.haushaltsmanager.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TitleTest {
    @Test
    public void returnsExpectedTranslationKey() {
        IRequiredField field = new Title();

        assertEquals(R.string.mapping_category_title, field.getTranslationKey());
    }
}
