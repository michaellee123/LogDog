package dog.abcd.logdog

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun OkHttpClient.Builder.logDog(tag: String, enable: Boolean = true): OkHttpClient.Builder {
    val logInterceptor = HttpLoggingInterceptor(LogDog(tag, enable))
    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
    addNetworkInterceptor(logInterceptor)
    return this
}

class LogDog(
    val tag: String,
    val enable: Boolean = BuildConfig.DEBUG,
    val print: (String) -> Unit = { Log.d(tag, it) },
    val gson: Gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
) : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        if (!enable || message.trim().isEmpty()) {
            return
        }
        if ((message.startsWith("{") && message.endsWith("}")) || (message.startsWith("[") && message.endsWith(
                "]"
            ))
        ) {
            print("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄")
            val json = gson.toJson(JsonParser.parseString(message))
            val prints = json.toString().split("\n")
            for (p in prints) {
                print("│ $p")
            }
        } else {
            when {
                message.startsWith("<-- HTTP FAILED") -> {
                    print("┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────")
                    print("│ $message ⚠️")
                    print("└────────────────────────────────────────────────────────────────────────────────────────────────────────────────")
                }
                message.startsWith("--> END") || message.startsWith("<-- END") -> {
                    print("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄")
                    print("│ $message")
                    print("└────────────────────────────────────────────────────────────────────────────────────────────────────────────────")
                }
                message.startsWith("-->") || message.startsWith("<--") -> {
                    print("┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────")
                    print("│ $message 🐶")
                    print("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄")
                }
                else -> {
                    print("│ $message")
                }
            }
        }
    }
}