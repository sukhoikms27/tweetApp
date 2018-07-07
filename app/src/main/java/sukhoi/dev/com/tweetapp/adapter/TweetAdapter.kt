package sukhoi.dev.com.tweetapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import sukhoi.dev.com.tweetapp.R
import sukhoi.dev.com.tweetapp.pojo.Tweet
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.ArrayList


class TweetAdapter : RecyclerView.Adapter<TweetAdapter.TweetViewHolder>() {

    private val tweetList = ArrayList<Tweet>()
    private val TWITTER_RESPONSE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
    private val MONTH_DAY_FORMAT = "MMM d"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tweet_item_view, parent, false)
        return TweetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tweetList.size
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(tweetList[position])
    }


    fun setItems(tweets: Collection<Tweet>) {
        tweetList.addAll(tweets)
        notifyDataSetChanged()
    }

    fun clearItems() {
        tweetList.clear()
        notifyDataSetChanged()
    }

    inner class TweetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImageView: ImageView get() = itemView.findViewById(R.id.profile_image_view)
        private val nameTextView: TextView get() = itemView.findViewById(R.id.author_name_text_view)
        private val nickTextView: TextView get() = itemView.findViewById(R.id.author_nick_text_view)
        private val creationDateTextView: TextView get() = itemView.findViewById(R.id.creation_date_text_view)
        private val contentTextView: TextView get() = itemView.findViewById(R.id.tweet_content_text_view)
        private val tweetImageView: ImageView get() = itemView.findViewById(R.id.tweet_image_view)
        private val retweetsTextView: TextView get() = itemView.findViewById(R.id.retweets_text_view)
        private val likesTextView: TextView get() = itemView.findViewById(R.id.likes_text_view)

        fun bind(tweet: Tweet) {
            nameTextView.text = tweet.user.name
            nickTextView.text = tweet.user.nick
            contentTextView.text = tweet.text
            retweetsTextView.text = tweet.retweetCount.toString()
            likesTextView.text = tweet.favouriteCount.toString()
            creationDateTextView.text = formattedDate(tweet.creationDate)

            Picasso.get().load(tweet.user.imageUrl).into(userImageView)

            val tweetPhotoUrl = tweet.imageUrl
            Picasso.get().load(tweet.imageUrl).into(tweetImageView)
            tweetImageView.visibility = if (tweetPhotoUrl != null) View.VISIBLE else View.GONE
        }

        fun formattedDate(rawDate: String): String {
            val utcFormat = SimpleDateFormat(TWITTER_RESPONSE_FORMAT, Locale.ROOT)
            val displayedFormat = SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault())
            try {
                val date = utcFormat.parse(rawDate)
                return displayedFormat.format(date)
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
        }
    }
}