package sukhoi.dev.com.tweetapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.json.JSONException
import sukhoi.dev.com.tweetapp.R
import sukhoi.dev.com.tweetapp.adapter.UsersAdapter
import sukhoi.dev.com.tweetapp.network.HttpClient
import sukhoi.dev.com.tweetapp.pojo.User
import java.io.IOException


class SearchUsersActivity : AppCompatActivity() {
    lateinit var usersRecyclerView: RecyclerView
    lateinit var usersAdapter: UsersAdapter
    lateinit var toolbar: Toolbar
    lateinit var queryEditText: EditText
    lateinit var searchButton: Button
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var httpClient: HttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_users)
        initRecyclerView()

        toolbar = findViewById(R.id.toolbar)
        queryEditText = toolbar.findViewById(R.id.query_edit_text)
        searchButton = toolbar.findViewById(R.id.search_button)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener { searchUsers() }

        searchButton.setOnClickListener { searchUsers() }

        queryEditText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchUsers()
                return@OnEditorActionListener true
            }
            false
        })

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        httpClient = HttpClient()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed(); return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        usersRecyclerView = findViewById(R.id.users_recycler_view)
        usersRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        val onUserClickListener = object : UsersAdapter.OnUserClickListener {
            override fun onUserClick(user: User) {
                val intent = Intent(this@SearchUsersActivity, UserInfoActivity::class.java)
                intent.putExtra(UserInfoActivity().USER_ID, user.id)
                startActivity(intent)
            }
        }

        usersAdapter = UsersAdapter(onUserClickListener)
        usersRecyclerView.adapter = usersAdapter
    }

    fun searchUsers() {
        val query = queryEditText.text.toString()
        if(query.isEmpty()) {
            Toast.makeText(this@SearchUsersActivity, R.string.not_enough_symbols_msg, Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
            return
        }
        SearchUsersAsyncTask().execute(query)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class SearchUsersAsyncTask : AsyncTask<String, Int, Collection<User>?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            swipeRefreshLayout.isRefreshing = true
        }

        override fun doInBackground(vararg params: String): Collection<User>? {
            val query = params[0]
            try {
                return httpClient.readUsers(query)
            } catch (e: IOException) {
                return null
            } catch (e: JSONException) {
                return null
            }

        }

        override fun onPostExecute(users: Collection<User>?) {
            swipeRefreshLayout.isRefreshing = false
            users?.let {
                usersAdapter.clearItems()
                usersAdapter.setItems(it)
            } ?: Toast.makeText(this@SearchUsersActivity, R.string.loading_error_msg, Toast.LENGTH_SHORT).show()
        }
    }
}