package com.openclassrooms.magicgithub.ui.user_list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.magicgithub.databinding.ItemListUserBinding
import com.openclassrooms.magicgithub.model.User
import com.openclassrooms.magicgithub.utils.UserDiffCallback
import java.util.Collections

class UserListAdapter(private val callback: Listener) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private var users: MutableList<User> = mutableListOf()

    interface Listener {
        fun onClickDelete(user: User)
        fun onUserStateChanged(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateList(newList: List<User>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffCallback(newList, users))
        users = newList.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    fun toggleUserState(position: Int) {
        users[position].isActive = !users[position].isActive
        callback.onUserStateChanged(users[position])
        notifyItemChanged(position)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        Collections.swap(users, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(private val binding: ItemListUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.itemListUserUsername.text = user.login

            Glide.with(binding.root)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.itemListUserAvatar)

            binding.itemListUserDeleteButton.setOnClickListener {
                callback.onClickDelete(user)
            }

            // Update background based on user state
            binding.root.setBackgroundColor(
                if (user.isActive) Color.WHITE else Color.rgb(255, 200, 200)
            )
        }
    }
}