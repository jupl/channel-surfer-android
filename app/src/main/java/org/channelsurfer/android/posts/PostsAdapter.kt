package org.channelsurfer.android.posts

import android.content.Context
import android.view.ViewGroup
import org.channelsurfer.android.base.RecyclerAdapter
import org.channelsurfer.android.base.network
import org.channelsurfer.android.boards.Board
import org.channelsurfer.android.database.all
import org.channelsurfer.android.database.database
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread

public class PostsAdapter(
        private val context: Context,
        private val onClick: (Post) -> Unit = {}) : RecyclerAdapter<Post, PostsItemView.Holder>() {
    private val network = context.network
    private val database = context.database

    override fun initialize() { data = database.posts.all }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = PostsItemView.Holder(context) { onClick(it) }

    override fun update(callback: (Exception?) -> Unit) {
        async {
            network.getPosts(Board("tech", "Technology")) { posts, error ->
                if(posts != null) database.posts.all = posts
                uiThread {
                    if(posts != null) data = posts
                    callback(error)
                }
            }
        }
    }
}
