package sukhoi.dev.com.tweetapp.network

import android.util.Log
import java.net.HttpURLConnection;
import java.net.URL;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders;
import org.json.JSONException
import sukhoi.dev.com.tweetapp.pojo.Tweet
import sukhoi.dev.com.tweetapp.pojo.User
import java.io.*


class HttpClient {
    lateinit var ins : InputStream
    lateinit var user: User
    val jsonParser: JsonParser
    val HEADER_AUTHORIZATION = "Authorization"
    val GET = "GET"
    val EXTENDED_MODE = "&tweet_mode=extended"
    init {
        jsonParser = JsonParser()
    }

    fun response(requestUrl: String): String {
        val url = URL(requestUrl)
        val connection = url.openConnection() as HttpURLConnection
        val authHeader = authHeader(requestUrl)

        connection.setRequestProperty(HEADER_AUTHORIZATION, authHeader)
        connection.connect()
        val status = connection.responseCode
        if (status != HttpURLConnection.HTTP_OK) {
            ins = connection.errorStream
        } else { ins = connection.inputStream }

        return convertStreamToString(ins)
    }

    fun readTweets(userId: Long): Collection<Tweet>? {
        val requestUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=$userId$EXTENDED_MODE"
        val response = response(requestUrl)
        return jsonParser.getTweets(response)
    }

    fun readUserInfo(userId: Long): User {
        val requestUrl = "https://api.twitter.com/1.1/users/show.json?user_id=$userId"
        val response =  response(requestUrl)
        user = jsonParser.getUser(response)
        return user

    }

    fun readUsers(query: String): Collection<User> {
        val requestUrl = "https://api.twitter.com/1.1/users/search.json?q=$query"
        val encodedUrl = requestUrl.replace(" ", "%20")
        val response = response(encodedUrl)
        return jsonParser.getUsers(response)
    }

    fun authHeader(url: String): String {
        val authConfig = TwitterCore.getInstance().authConfig
        val session = TwitterCore.getInstance().sessionManager.activeSession

        return OAuth1aHeaders().getAuthorizationHeader(authConfig, session.authToken, null, GET, url, null)
    }

    private fun convertStreamToString(stream: InputStream): String {

        val reader = BufferedReader(InputStreamReader(stream))
        val sb = StringBuilder()

        var line = reader.readLine()

        when(line != null) {
            true -> { sb.append(line).append("\n"); }
            else -> stream.close()
        }
        return sb.toString()
    }

}