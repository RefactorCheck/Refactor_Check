public class zxing_0224 {

      @Override
      public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
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
    
        setupCustomProductSearch(preferences);
      }
      
      private void setupCustomProductSearch(PreferenceScreen preferences) {
        EditTextPreference customProductSearch = (EditTextPreference)
            preferences.findPreference(PreferencesActivity.KEY_CUSTOM_PRODUCT_SEARCH);
        customProductSearch.setOnPreferenceChangeListener(new CustomSearchURLValidator());
      }
}
