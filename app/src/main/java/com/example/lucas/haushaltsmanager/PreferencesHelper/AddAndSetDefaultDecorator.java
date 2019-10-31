package com.example.lucas.haushaltsmanager.PreferencesHelper;

import android.content.Context;

import com.example.lucas.haushaltsmanager.Entities.Account;

import java.util.List;

public class AddAndSetDefaultDecorator implements ActiveAccountsPreferencesInterface {
    private ActiveAccountsPreferencesInterface preferences;
    private UserSettingsPreferences userPreferences;

    public AddAndSetDefaultDecorator(ActiveAccountsPreferencesInterface preferences, Context context) {
        this.preferences = preferences;
        userPreferences = new UserSettingsPreferences(context);
    }

    @Override
    public void addAccount(Account account) {
        preferences.addAccount(account);

        userPreferences.setActiveAccount(account);
    }

    @Override
    public void removeAccount(Account account) {
        preferences.removeAccount(account);
    }

    @Override
    public void changeVisibility(Account account, boolean visibility) {
        preferences.changeVisibility(account, visibility);
    }

    @Override
    public boolean isActive(Account account) {
        return preferences.isActive(account);
    }

    @Override
    public List<Long> getActiveAccounts() {
        return preferences.getActiveAccounts();
    }
}
