package com.app.githubtask.ui

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.githubtask.R
import com.app.githubtask.databinding.FragmentDetailBinding
import com.app.githubtask.db.DatabaseClient
import com.app.githubtask.model.DataItem
import com.app.githubtask.model.User
import com.app.githubtask.model.UserDao
import kotlin.Exception

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val bundle = arguments
            lastId = bundle!!.getInt("id")
        }
        if (lastId != -1) {
            val databaseClient = DatabaseClient.getInstance(requireContext())
            val userDao: UserDao = databaseClient.userDao()
            val dataItem: User = userDao.getItemById(lastId)
            binding.userName.text = dataItem.name
            binding.comments.isEnabled = true
            binding.userMail.text = dataItem.email
            binding.userId.text = "${dataItem.id}"
            binding.gender.text = dataItem.gender
            binding.status.text = dataItem.status
            binding.submitComments.visibility = View.VISIBLE
            binding.comments.setText(dataItem.comment)
            binding.submitComments.setOnClickListener(View.OnClickListener {
                if (TextUtils.isEmpty(binding.comments.text)) {
                    binding.comments.error = getString(R.string.comments_required)
                } else if (dataItem.comment == binding.comments.text.toString()) {
                    Toast.makeText(
                        activity,
                        getString(R.string.same_as_previous_cooments),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    dataItem.comment=binding.comments.text.toString()
                    userDao.update(dataItem)
                    binding.comments.setText(binding.comments.text.toString())
                    hideSoftKeyboard(requireActivity())
                    Toast.makeText(
                        activity,
                        getString(R.string.save_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    companion object {
        private var lastId = -1
        private var instance: DetailFragment? = null
        fun getInstance(): DetailFragment {
            if (instance == null)
                instance = DetailFragment()
            return instance!!
        }

        fun getInstance(bundle: Bundle): DetailFragment {
            instance = DetailFragment()
            instance!!.arguments = bundle
            return instance!!
        }

        fun hideSoftKeyboard(context: Activity) {
            try {
                val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    context.currentFocus!!.windowToken, 0
                )
            } catch (npe: Exception) {
                npe.printStackTrace()
            }
        }
    }
}