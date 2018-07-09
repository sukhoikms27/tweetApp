package sukhoi.dev.com.tweetapp.pojo

import com.google.gson.annotations.SerializedName


class Tweet(
        @SerializedName("user") val user: User,
        @SerializedName("id") val id: Long,
        @SerializedName("created_at") val creationDate: String,
        @SerializedName("full_text") val text: String,
        @SerializedName("retweet_count") val retweetCount: Long,
        @SerializedName("favorite_count") val favouriteCount: Long,
        var imageUrl: String?
)