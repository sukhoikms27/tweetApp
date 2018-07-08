package sukhoi.dev.com.tweetapp.network

import org.json.JSONObject
import sukhoi.dev.com.tweetapp.pojo.User
import android.R.attr.description
import android.R.attr.name



class JsonParser {

    fun getUser(response: String): User {
        val userJson = JSONObject(response)
        return User(
                id = userJson.getLong("id"),
                name = userJson.getString("name"),
                nick = userJson.getString("screen_name"),
                location = userJson.getString("location"),
                description = userJson.getString("description"),
                imageUrl = userJson.getString("profile_image_url"),
                followersCount = userJson.getInt("followers_count"),
                followingCount = userJson.getInt("favourites_count")
        )
    }
}