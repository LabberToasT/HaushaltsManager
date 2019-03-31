package com.example.lucas.haushaltsmanager.Utils;

import com.example.lucas.haushaltsmanager.Entities.Currency;
import com.example.lucas.haushaltsmanager.Entities.Price;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class MoneyUtilsTest {
    @Test
    public void testWithNullPrice() {
        String actualValue = MoneyUtils.formatHumanReadable(null, Locale.GERMANY);

        assertEquals("-,--", actualValue);
    }

    @Test
    public void testWithEmptyPrice() {
        String actualValue = MoneyUtils.formatHumanReadable(createPrice(0, true), Locale.GERMANY);

        assertEquals("-,--", actualValue);
    }

    @Test
    public void testWithPositiveOneDigitPrice() {
        String actualValue = MoneyUtils.formatHumanReadable(
                createPrice(1, false),
                Locale.GERMANY
        );

        assertEquals("1,00", actualValue);
    }

    @Test
    public void testWithPositiveFourDigitPrice() {
        String actualValue = MoneyUtils.formatHumanReadable(
                createPrice(1000, false),
                Locale.GERMANY
        );

        assertEquals("1.000,00", actualValue);
    }

    @Test
    public void testWithPositiveFourDigitAndCentPrice() {
        String actualValue = MoneyUtils.formatHumanReadable(
                createPrice(1000.77, false),
                Locale.GERMANY
        );

        assertEquals("1.000,77", actualValue);
    }

    @Test
    public void testWithNegativeOneDigitPrice() {
        String actualValue = MoneyUtils.formatHumanReadable(
                createPrice(1, true),
                Locale.GERMANY
        );

        assertEquals("-1,00", actualValue);
    }

    @Test
    public void testWithNegativeFourDigitPrice() {
        String actualValue = MoneyUtils.formatHumanReadable(
                createPrice(1000, true),
                Locale.GERMANY
        );

        assertEquals("-1.000,00", actualValue);
    }

    @Test
    public void testWithNegativeFourDigitAndCentPrice() {
        String actualValue = MoneyUtils.formatHumanReadable(
                createPrice(1000.98, true),
                Locale.GERMANY
        );

        assertEquals("-1.000,98", actualValue);
    }

    @Test
    public void testOmitCents() {
        String actualValue = MoneyUtils.formatHumanReadableOmitCents(
                createPrice(100.50, false),
                Locale.GERMANY
        );

        assertEquals("100", actualValue);
    }

    @Test
    public void testWithUSLocale() {
        String actualValue = MoneyUtils.formatHumanReadable(
                createPrice(1000.33, false),
                Locale.US
        );

        assertEquals("1,000.33", actualValue);
    }

    private Price createPrice(double withValue, boolean isNegative) {
        return new Price(withValue, isNegative, createCurrency());
    }

    private Currency createCurrency() {
        return new Currency("Euro", "EUR", "€");
    }
}
