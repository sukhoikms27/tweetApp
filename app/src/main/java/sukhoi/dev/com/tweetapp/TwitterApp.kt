package sukhoi.dev.com.tweetapp

import android.app.Application
import com.twitter.sdk.android.core.Twitter

class TwitterApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Twitter.initialize(this)
    }
}