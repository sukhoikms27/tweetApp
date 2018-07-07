package sukhoi.dev.com.tweetapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import sukhoi.dev.com.tweetapp.R
import sukhoi.dev.com.tweetapp.pojo.User




class UsersAdapter(onUserClickListener: OnUserClickListener) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private val userList = ArrayList<User>()
    private var onUserClickListener: OnUserClickListener = onUserClickListener

    init {
        this.onUserClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item_view, parent, false)
        return UserViewHolder(view)
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    fun setItems(users: Collection<User>) {
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun clearItems() {
        userList.clear()
        notifyDataSetChanged()
    }




    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val profileImageView: ImageView get() = itemView.findViewById(R.id.profile_image_view)
        private val userNameTextView: TextView get() = itemView.findViewById(R.id.user_name_text_view)
        private val userNickTextView: TextView get() = itemView.findViewById(R.id.user_nick_text_view)

        init {
            itemView.setOnClickListener {
                onUserClickListener.onUserClick(userList[layoutPosition])
            }
        }

        fun bind(user: User) {
            userNameTextView.text = user.name
            userNickTextView.text = user.nick
            Picasso.get().load(user.imageUrl).into(profileImageView)
        }
    }

    interface OnUserClickListener {
        fun onUserClick(user: User)
    }
}

