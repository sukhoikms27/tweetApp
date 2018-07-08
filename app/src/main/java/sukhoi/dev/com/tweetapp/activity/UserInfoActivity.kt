package sukhoi.dev.com.tweetapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
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
    lateinit var httpClient: HttpClient
    val user: User = User(
            id = 1L,
            imageUrl = "http://i.imgur.com/DvpvklR.png",
            name = "DevAuthor",
            nick = "@sukhoi",
            description = "Some description",
            location = "Saint-Petersburg",
            followingCount = 1993,
            followersCount = 89)
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

        initRecyclerView()
        displayUserInfo(user)
        loadTweets()

        supportActionBar?.title = user.name

        httpClient = HttpClient()
        loadUserInfo(userId)

    }

    private fun loadUserInfo(userId: Long) {

        UserInfoAsyncTask().execute(userId)

//        val readUserRunnable = Runnable {
//            try {
//                val userInfo = httpClient.readUserInfo(userId)
//
//                val showResultRunnable = Runnable {
//                    Toast.makeText(this@UserInfoActivity, userInfo,
//                            Toast.LENGTH_SHORT).show()
//                }
//
//                // выполняем в UI потоке
//                runOnUiThread(showResultRunnable)
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }


        // создаём объект Runnable
//        val readUserRunnable = Runnable {
//            try {
//                val userInfo = httpClient.readUserInfo(userId)
//                Log.d("HttpTest", userInfo)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }

        // Запускаем runnable в новом потоке
//        Thread(readUserRunnable).start()
    }

    fun displayUserInfo(user: User) {
        Picasso.get().load(user.imageUrl).into(userImageView)
        nameTextView.text = user.name
        nickTextView.text = user.nick
        descriptionTextView.text = user.description
        locationTextView.text = user.location
        followingTextView.text = user.followingCount.toString()
        followersTextView.text = user.followersCount.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_info_menu, menu)
//        return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            startActivity(Intent(this, SearchUsersActivity::class.java))
        }
//        return super.onOptionsItemSelected(item)
        return true
    }


    fun initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view)
        tweetsRecyclerView.layoutManager = LinearLayoutManager(this)
        tweetAdapter = TweetAdapter()
        tweetsRecyclerView.adapter = tweetAdapter
    }

    fun loadTweets() {
        val tweets = tweets()
        tweetAdapter.setItems(tweets)
    }

    fun tweets(): Collection<Tweet> {
        return listOf(
                Tweet(this.user, 1L, "Thu Dec 13 07:31:08 +0000 2017", "Очень длинное описание твита 1",
                        4L, 4L, "https://www.w3schools.com/w3css/img_fjords.jpg"),

                Tweet(user, 2L, "Thu Dec 12 07:31:08 +0000 2017", "Очень длинное описание твита 2",
                        5L, 5L, "https://www.w3schools.com/w3images/lights.jpg"),

                Tweet(user, 3L, "Thu Dec 11 07:31:08 +0000 2017", "Очень длинное описание твита 3",
                        6L, 6L, "https://www.w3schools.com/css/img_mountains.jpg")
        )
    }

    @SuppressLint("StaticFieldLeak")
    private inner class UserInfoAsyncTask : AsyncTask<Long, Int, User>() {

        override fun onPostExecute(user: User?) {
            super.onPostExecute(user)
            displayUserInfo(user!!)
        }

        override fun doInBackground(vararg p0: Long?): User? {
            try {
                // достаём userId, который передали в метод execute
                val userId = p0[0]!!
                return httpClient.readUserInfo(userId)
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }

    }
}
