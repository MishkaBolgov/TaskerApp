package mbolg.tasker.recognizer
import mbolg.tasker.utils.LangUtils
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.*
import java.io.File
import javax.inject.Inject

class YandexSpeechRecognizer @Inject constructor(): SpeechRecognizer {
    override fun recognize(file: File, listener: SpeechRecognizer.RecognitionListener) {
        val retrofit = getRetrofit()

        val call = retrofit.create(RecognizeApi::class.java)

        val uuid = SpeechKitConfig.generateUUId()
        val key = SpeechKitConfig.SPEECHKIT_API_KEY
        val topic = "queries"

        val body = RequestBody.create(MediaType.parse("audio"), file)

        call.sendAudio(uuid, key, topic, body).enqueue(object : Callback<RecResponse> {
            override fun onResponse(call: Call<RecResponse>, response: Response<RecResponse>) {
                println("mylog: onResponse ${response.body()}")
                listener.onSuccess("${response.body()?.recognizedText}")
            }

            override fun onFailure(call: Call<RecResponse>, t: Throwable) {
                println("mylog: onFailure ${t.message}")
            }
        })
    }


    private fun getRetrofit(): Retrofit {

        val bodyInterceptor = HttpLoggingInterceptor()
        bodyInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(bodyInterceptor).build()

        val BASE_REC_URL = "https://asr.yandex.net/"

        return Retrofit.Builder()
                .baseUrl(BASE_REC_URL)
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
                .client(client)
                .build()
    }
}

internal interface RecognizeApi {
    @Multipart
    @Headers("Content-Type: audio/x-pcm;bit=16;rate=8000", "Transfer-Encoding: chunked")
    @POST("/asr_xml")
    fun sendAudio(
            @Query("uuid") uuid: String,
            @Query("key") key: String,
            @Query("topic") topic: String,
//            @Query("lang") lang: String,
            @Part("audio") body: RequestBody): Call<RecResponse>
}

