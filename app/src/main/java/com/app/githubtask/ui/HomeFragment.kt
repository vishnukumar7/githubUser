package com.app.githubtask.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.githubtask.ApiClient
import com.app.githubtask.MainActivity
import com.app.githubtask.R
import com.app.githubtask.databinding.FragmentHomeBinding
import com.app.githubtask.databinding.UserListItemBinding
import com.app.githubtask.db.DatabaseClient
import com.app.githubtask.model.DataItem
import com.app.githubtask.model.GitHubUser
import com.app.githubtask.model.User
import com.app.githubtask.model.UserDao
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    var itemArrayList = ArrayList<User>()
    lateinit var adapter: UserAdapter
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(false)
        itemArrayList.clear()
        adapter = UserAdapter(itemArrayList)
        binding.recyclerView.adapter = adapter

        (requireActivity() as MainActivity).userViewModel.userList.observe(viewLifecycleOwner){
            it?.let {
                itemArrayList.clear()
                itemArrayList.addAll(it)
                if (it.size==0){
                    (requireActivity() as MainActivity).userViewModel.getGithubUserData()
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    inner class UserAdapter(private val itemArrayList: ArrayList<User>) :
        RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val binding: UserListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.user_list_item,
                parent,
                false
            )
            return UserViewHolder(binding)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val (_, name, id, email, status) = itemArrayList[position]
            holder.itemBinding.userNameView.text = name
            holder.itemBinding.userMailView.text = email
            holder.itemBinding.userStatusView.text = status
            holder.itemBinding.mainLay.setOnClickListener(View.OnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", id.toInt())
                val fragment = DetailFragment.getInstance(bundle)
                (requireActivity() as MainActivity).binding.bottomNavigation.selectedItemId =
                    R.id.navigation_dashboard
                (requireActivity() as MainActivity).loadFragment(fragment)
            })
        }

        override fun getItemCount(): Int {
            return itemArrayList.size
        }

        inner class UserViewHolder(itemView: UserListItemBinding) :
            RecyclerView.ViewHolder(itemView.root) {
            var itemBinding: UserListItemBinding = itemView

        }
    }

    companion object {
        private var instance: HomeFragment? = null

        fun getInstance(): HomeFragment {
            if (instance == null)
                instance = HomeFragment()
            return instance!!
        }
    }
}