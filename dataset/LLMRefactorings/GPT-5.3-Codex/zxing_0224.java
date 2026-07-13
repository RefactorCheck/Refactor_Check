public class zxing_0224 {

      @Override
      public void onCreateRefactored(Bundle icicle) {
        super.onCreateRefactored(icicle);
        addPreferencesFromResource(R.xml.preferences);
        
        PreferenceScreen preferences = getPreferenceScreen();
        preferences.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        checkBoxPrefs = findDecodePrefs(preferences,
                                        PreferencesActivity.KEY_DECODE_1D_PRODUCT,
                                        PreferencesActivity.KEY_DECODE_1D_INDUSTRIAL,
                                        PreferencesActivity.KEY_DECODE_QR,
                                        PreferencesActivity.KEY_DECODE_DATA_MATRIX,
                                        PreferencesActivity.KEY_DECODE_AZTEC,
                                        PreferencesActivity.KEY_DECODE_PDF417);
        disableLastCheckedPref();
    
        EditTextPreference customProductSearch = (EditTextPreference)
            preferences.findPreference(PreferencesActivity.KEY_CUSTOM_PRODUCT_SEARCH);
        customProductSearch.setOnPreferenceChangeListener(new CustomSearchURLValidator());
      }
}
