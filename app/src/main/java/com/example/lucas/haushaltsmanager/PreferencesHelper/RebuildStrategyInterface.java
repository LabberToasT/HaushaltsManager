package com.example.lucas.haushaltsmanager.PreferencesHelper;

import android.content.Context;

public interface RebuildStrategyInterface {
    void rebuild(Context context, SharedPreferences preferences);
}
