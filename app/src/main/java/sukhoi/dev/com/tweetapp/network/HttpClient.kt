package sukhoi.dev.com.tweetapp.network

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders;
import sukhoi.dev.com.tweetapp.pojo.User


class HttpClient() {
    lateinit var ins : InputStream
    lateinit var user: User
    lateinit var jsonParser: JsonParser
    val HEADER_AUTHORIZATION = "Authorization"
    val GET = "GET"
    init {
        jsonParser = JsonParser()
    }

    fun readUserInfo(userId: Long): User {
        val requestUrl = "https://api.twitter.com/1.1/users/show.json?user_id=$userId"
        val url = URL(requestUrl)
        val connection = url.openConnection() as HttpURLConnection

        val authHeader = authHeader(requestUrl)
        connection.setRequestProperty(HEADER_AUTHORIZATION, authHeader)

        connection.connect()

        val status = connection.responseCode
        if (status != HttpURLConnection.HTTP_OK) {
            ins = connection.errorStream
        } else { ins = connection.inputStream }

        val response = convertStreamToString(ins)
        user = jsonParser.getUser(response)
        return user
    }

    fun authHeader(url: String): String {
        val authConfig = TwitterCore.getInstance().authConfig
        val session = TwitterCore.getInstance().sessionManager.activeSession

        return OAuth1aHeaders().getAuthorizationHeader(authConfig, session.authToken, null, GET, url, null)
    }

    private fun convertStreamToString(stream: InputStream): String {

        val reader = BufferedReader(InputStreamReader(stream))
        val sb = StringBuilder()

        var line: String? = reader.readLine()
        while (line != null) {
            sb.append(line).append("\n")
            break
        }
        stream.close()

        return sb.toString()
    }
}