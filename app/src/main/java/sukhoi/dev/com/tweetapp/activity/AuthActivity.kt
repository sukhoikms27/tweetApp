package sukhoi.dev.com.tweetapp.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import sukhoi.dev.com.tweetapp.R
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import io.github.tonnyl.whatsnew.WhatsNew
import io.github.tonnyl.whatsnew.item.WhatsNewItem
import io.github.tonnyl.whatsnew.util.PresentationOption


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

        val whatsnew = WhatsNew.newInstance(
                WhatsNewItem("Find your friend!", "Now you can find your friends and see what they do!", R.drawable.baseline_search_24),
                WhatsNewItem("Faster!", "Increase speed of loaders", R.drawable.round_fast_forward_24),
                WhatsNewItem("Better!", "Fix more crashes", R.drawable.baseline_thumb_up_24),
                WhatsNewItem("Stronger!", "Protect your data", R.drawable.baseline_verified_user_24))

        with(whatsnew) {
            presentationOption = PresentationOption.DEBUG

            titleColor = ContextCompat.getColor(this@AuthActivity, R.color.colorPrimary)
            titleText = "What's Up"

            buttonText = "Got it!"
            buttonBackground = ContextCompat.getColor(this@AuthActivity, R.color.colorPrimary)
            buttonTextColor = ContextCompat.getColor(this@AuthActivity, R.color.white)

            itemContentColor = Color.parseColor("#808080")
            itemTitleColor = ContextCompat.getColor(this@AuthActivity, R.color.colorPrimaryDark)
        }
        whatsnew.presentAutomatically(this@AuthActivity)
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