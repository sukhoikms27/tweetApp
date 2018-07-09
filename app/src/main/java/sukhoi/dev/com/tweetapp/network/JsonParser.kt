package sukhoi.dev.com.tweetapp.network
import com.google.gson.Gson
import sukhoi.dev.com.tweetapp.pojo.Tweet
import sukhoi.dev.com.tweetapp.pojo.User
import com.google.gson.reflect.TypeToken
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializer
import java.lang.reflect.Type
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

class JsonParser {
    val GSON = GsonBuilder()
            .registerTypeAdapter(Tweet::class.java, TweetDeserializer())
            .create()

    fun getUser(response: String): User {
        return Gson().fromJson(response, User::class.java)
    }

    fun getUsers(response: String): Collection<User> {
        val usersType = object : TypeToken<Collection<User>>() {}.type
        return Gson().fromJson(response, usersType)
    }

    fun getTweets(response: String): Collection<Tweet> {
        val tweetsType = object : TypeToken<Collection<Tweet>>() {}.type
        return GSON.fromJson(response, tweetsType)
    }
}

class TweetDeserializer : JsonDeserializer<Tweet> {
    private val GSON = Gson()

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Tweet {
        val tweetJson = json.asJsonObject
        return GSON.fromJson(tweetJson, Tweet::class.java)
                .apply { this.imageUrl = getTweetImageUrl(tweetJson) }
    }

    private fun getTweetImageUrl(tweetJson: JsonObject): String? {
        val entities = tweetJson.get("entities").asJsonObject
        val mediaArray = if (entities.has("media")) entities.get("media").asJsonArray else null
        val firstMedia = mediaArray?.get(0)?.asJsonObject
        return firstMedia?.get("media_url")?.asString
    }
}