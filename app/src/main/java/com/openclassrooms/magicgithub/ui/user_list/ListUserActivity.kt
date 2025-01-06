package com.openclassrooms.magicgithub.ui.user_list

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.magicgithub.databinding.ActivityListUserBinding
import com.openclassrooms.magicgithub.di.Injection.getRepository
import com.openclassrooms.magicgithub.model.User

class ListUserActivity : AppCompatActivity(), UserListAdapter.Listener {
    private lateinit var binding: ActivityListUserBinding
    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureFab()
        configureRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun configureRecyclerView() {
        adapter = UserListAdapter(this)
        binding.activityListUserRv.adapter = adapter

        // Configure swipe gestures
        val swipeCallback = SwipeCallback(adapter)
        val itemTouchSwipeHelper = ItemTouchHelper(swipeCallback)
        itemTouchSwipeHelper.attachToRecyclerView(binding.activityListUserRv)

        // Configure drag and drop
        val moveCallback = ItemMoveCallback(adapter)
        val itemTouchMoveHelper = ItemTouchHelper(moveCallback)
        itemTouchMoveHelper.attachToRecyclerView(binding.activityListUserRv)
    }

    private fun configureFab() {
        binding.activityListUserFab.setOnClickListener {
            getRepository().addRandomUser()
            loadData()
        }
    }

    private fun loadData() {
        adapter.updateList(getRepository().getUsers())
    }

    override fun onClickDelete(user: User) {
        Log.d(TAG, "User tries to delete an item.")
        getRepository().deleteUser(user)
        loadData()
    }

    override fun onUserStateChanged(user: User) {
        getRepository().updateUserState(user)
    }

    companion object {
        private const val TAG = "ListUserActivity"
    }
}