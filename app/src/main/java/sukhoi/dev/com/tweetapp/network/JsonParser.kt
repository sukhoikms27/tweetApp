package sukhoi.dev.com.tweetapp.network

import org.json.JSONArray
import org.json.JSONObject
import sukhoi.dev.com.tweetapp.pojo.Tweet
import sukhoi.dev.com.tweetapp.pojo.User



class JsonParser {

    fun getUser(response: String): User {
        val userJson = JSONObject(response)
        return getUser(userJson)
    }

    private fun getUser(userJson: JSONObject): User {
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

    fun getUsers(response: String): Collection<User> {
        val jsonArray = JSONArray(response)
        val usersSearchResult = ArrayList<User>()

        for (i in 0 until jsonArray.length()) {
            val userJson = jsonArray.getJSONObject(i)
            val user = getUser(userJson)
            usersSearchResult.add(user)
        }

        return usersSearchResult
    }

    fun getTweets(response: String): Collection<Tweet> {
        val jsonArray = JSONArray(response)
        val tweetsResult = ArrayList<Tweet>()

        for (i in 0 until jsonArray.length()) {
            val tweetJson = jsonArray.getJSONObject(i)
            val userJson = tweetJson.getJSONObject("user")
            tweetsResult.add(
                    Tweet(
                            user = getUser(userJson),
                            id = tweetJson.getLong("id"),
                            creationDate = tweetJson.getString("created_at"),
                            text = tweetJson.getString("full_text"),
                            retweetCount = tweetJson.getLong("retweet_count"),
                            favouriteCount = tweetJson.getLong("favorite_count"),
                            imageUrl = getTweetImageUrl(tweetJson)
                    ))
        }
        return tweetsResult
    }

    private fun getTweetImageUrl(tweetJson: JSONObject): String? {
        val entities = tweetJson.getJSONObject("entities")
        val mediaArray = if (entities.has("media")) entities.getJSONArray("media") else null
        val firstMedia = mediaArray?.getJSONObject(0)
        return firstMedia?.getString("media_url")
    }
}