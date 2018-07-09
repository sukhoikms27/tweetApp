package sukhoi.dev.com.tweetapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import sukhoi.dev.com.tweetapp.R
import sukhoi.dev.com.tweetapp.adapter.TweetAdapter
import sukhoi.dev.com.tweetapp.network.HttpClient
import sukhoi.dev.com.tweetapp.pojo.Tweet
import sukhoi.dev.com.tweetapp.pojo.User
import java.io.IOException
import android.os.AsyncTask
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.widget.Toast
import org.json.JSONException

class UserInfoActivity : AppCompatActivity() {

    lateinit var userImageView: ImageView
    lateinit var nameTextView: TextView
    lateinit var nickTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var locationTextView: TextView
    lateinit var followingTextView: TextView
    lateinit var followersTextView: TextView
    lateinit var tweetsRecyclerView: RecyclerView
    lateinit var tweetAdapter: TweetAdapter
    lateinit var toolbar: Toolbar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var httpClient: HttpClient
    var taskInProgressCount = 0
    val USER_ID = "userId"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        val userId = intent.getLongExtra(USER_ID, -1)

        userImageView = findViewById(R.id.user_image_view)
        nameTextView = findViewById(R.id.user_name_text_view)
        nickTextView = findViewById(R.id.user_nick_text_view)
        descriptionTextView = findViewById(R.id.user_description_text_view)
        locationTextView = findViewById(R.id.user_location_text_view)
        followingTextView = findViewById(R.id.following_count_text_view)
        followersTextView = findViewById(R.id.followers_count_text_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        httpClient = HttpClient()

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            tweetAdapter.clearItems()
            loadUserInfo(userId)
            loadTweets(userId)
        }
        initRecyclerView()

        loadUserInfo(userId)
        loadTweets(userId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            startActivity(Intent(this, SearchUsersActivity::class.java))
        }
        return true
    }

    fun loadTweets(userId: Long) {
        TweetsAsyncTask().execute(userId)
    }

    private fun loadUserInfo(userId: Long) {
        UserInfoAsyncTask().execute(userId)
    }

    fun displayUserInfo(user: User) {
        Picasso.get().load(user.imageUrl).into(userImageView)
        nameTextView.text = user.name
        nickTextView.text = user.nick
        descriptionTextView.text = user.description
        locationTextView.text = user.location
        followingTextView.text = user.followingCount.toString()
        followersTextView.text = user.followersCount.toString()

        supportActionBar?.title = user.name

    }

    fun initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view)

        ViewCompat.setNestedScrollingEnabled(tweetsRecyclerView, false)
        tweetsRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        tweetsRecyclerView.layoutManager = LinearLayoutManager(this)
        tweetAdapter = TweetAdapter()
        tweetsRecyclerView.adapter = tweetAdapter
    }

    private fun refreshLayoutVisible(visible: Boolean) {
        if (visible) {
            taskInProgressCount++
            if (taskInProgressCount == 1) swipeRefreshLayout.isRefreshing = true
        } else {
            taskInProgressCount--
            if (taskInProgressCount == 0) swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class UserInfoAsyncTask : AsyncTask<Long, Int, User>() {
        override fun onPreExecute() {
            super.onPreExecute()
            refreshLayoutVisible(true)
        }

        override fun doInBackground(vararg p0: Long?): User? {
            try {
                val userId = p0[0]
                return httpClient.readUserInfo(userId!!)

            } catch (e: IOException) {
                e.printStackTrace()
                return null
            } catch (e: JSONException) {
                e.printStackTrace()
                return null
            }

        }

        override fun onPostExecute(user: User?) {
            refreshLayoutVisible(false)
            user?.let { displayUserInfo(it) } ?: Toast.makeText(this@UserInfoActivity, R.string.loading_error_msg, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class TweetsAsyncTask : AsyncTask<Long, Int, Collection<Tweet>?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            refreshLayoutVisible(true)
        }

        override fun doInBackground(vararg p0: Long?): Collection<Tweet>? {
            try {
                val userId = p0[0]!!
                return httpClient.readTweets(userId)

            } catch (e: IOException) {
                e.printStackTrace()
                return null
            } catch (e: JSONException) {
                e.printStackTrace()
                return null
            }

        }

        override fun onPostExecute(tweets: Collection<Tweet>?) {
            refreshLayoutVisible(false)
            tweets?.let { tweetAdapter.setItems(it) } ?: Toast.makeText(this@UserInfoActivity, R.string.loading_error_msg, Toast.LENGTH_SHORT).show()
        }
    }
}
