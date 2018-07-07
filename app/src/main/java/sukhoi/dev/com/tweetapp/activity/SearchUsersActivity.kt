package sukhoi.dev.com.tweetapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import sukhoi.dev.com.tweetapp.R
import sukhoi.dev.com.tweetapp.pojo.User
import sukhoi.dev.com.tweetapp.adapter.UsersAdapter
import android.widget.TextView




class SearchUsersActivity: AppCompatActivity() {
    lateinit var usersRecyclerView: RecyclerView
    lateinit var usersAdapter: UsersAdapter
    lateinit var toolbar: Toolbar
    lateinit var queryEditText: EditText
    lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_users)
        initRecyclerView()

        toolbar = findViewById(R.id.toolbar)
        queryEditText = toolbar.findViewById(R.id.query_edit_text)
        searchButton = toolbar.findViewById(R.id.search_button)

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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { onBackPressed(); return true }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        usersRecyclerView = findViewById(R.id.users_recycler_view)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        val onUserClickListener = object: UsersAdapter.OnUserClickListener {
            override fun onUserClick(user: User) {
                val intent = Intent(this@SearchUsersActivity, UserInfoActivity::class.java)
                intent.putExtra(UserInfoActivity().USER_ID, user.id)
                startActivity(intent)
            }
        }
        usersAdapter = UsersAdapter(onUserClickListener)
        usersRecyclerView.adapter = usersAdapter
    }

    fun loadUsers() {
        val users = getUsers()
        usersAdapter.setItems(users)
    }

    fun getUsers(): Collection<User> {
        return listOf(
                User(929257819349700608L,
                        "http://i.imgur.com/DvpvklR.png",
                        "DevAuthor",
                        "@sukhoi",
                        "Some description",
                        "Saint-Petersburg",
                        1993,
                        89
                ),
                User(44196397L,
                        "https://pbs.twimg.com/profile_images/782474226020200448/zDo-gAo0_400x400.jpg",
                        "Elon Musk",
                        "@elonmusk",
                        "Hat Salesman",
                        "Boring",
                        14,
                        13)
        )
    }

    fun searchUsers() {
        val users = getUsers()
        usersAdapter.clearItems()
        usersAdapter.setItems(users)
    }
}