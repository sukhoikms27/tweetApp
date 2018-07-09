package sukhoi.dev.com.tweetapp.pojo

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("id") val id: Long,
        @SerializedName("profile_image_url") val imageUrl: String,
        @SerializedName("name") val name: String,
        @SerializedName("screen_name") val nick: String,
        @SerializedName("description") val description: String,
        @SerializedName("location") val location: String,
        @SerializedName("following_count") val followingCount: Int,
        @SerializedName("followers_count") val followersCount: Int
)