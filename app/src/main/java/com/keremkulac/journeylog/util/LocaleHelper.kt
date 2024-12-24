import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleHelper {
    fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity.createConfigurationContext(config)
        }
        saveLanguagePreference(activity, languageCode)
    }

    fun saveLanguagePreference(context: Context, languageCode: String) {
        val sharedPreferences = context.getSharedPreferences("LanguagePreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language", languageCode).apply()
    }

    fun loadLanguagePreference(context: Context): String {  // Null safety için
        val sharedPreferences = context.getSharedPreferences("LanguagePreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", Locale.getDefault().language) ?: Locale.getDefault().language
    }
}