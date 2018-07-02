package sukhoi.dev.com.tweetapp.pojo


class Tweet(
        val user: User,
        var id: Long,
        var creationDate: String,
        var text: String,
        var retweetCount: Long,
        var favouriteCount: Long,
        var imageUrl: String?
)