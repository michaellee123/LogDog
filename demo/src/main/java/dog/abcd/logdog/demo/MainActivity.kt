package dog.abcd.logdog.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dog.abcd.logdog.demo.databinding.ActivityMainBinding
import dog.abcd.logdog.logDog
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val okHttpClient: OkHttpClient = OkHttpClient.Builder().logDog("LogDog").build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRequest.setOnClickListener {
            val request = Request.Builder()
                .url("http://www.worldtimeapi.org/api/timezone/Asia/Shanghai")
                .build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                }
            })
        }
    }
}