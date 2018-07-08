package sukhoi.dev.com.tweetapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import sukhoi.dev.com.tweetapp.R
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import android.widget.Toast





class AuthActivity: AppCompatActivity() {
    lateinit var twitterLoginButton: TwitterLoginButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        twitterLoginButton = findViewById(R.id.login_button)

        twitterLoginButton.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                navigateToUserInfo(result.data.userId)
            }

            override fun failure(exception: TwitterException) {
            }
        }
    }

    fun navigateToUserInfo(userId: Long) {
        val intent = Intent(this@AuthActivity, UserInfoActivity::class.java)
        intent.putExtra(UserInfoActivity().USER_ID, userId)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitterLoginButton.onActivityResult(requestCode, resultCode, data)
    }
}