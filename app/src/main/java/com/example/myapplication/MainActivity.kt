package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.GithubAdapter
import com.example.myapplication.api.ApiResponse
import com.example.myapplication.api.RetrofitBuilder
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.factory.GithubFactory
import com.example.myapplication.repository.GithubRepository
import com.example.myapplication.viewModel.GithubViewModel

class MainActivity : AppCompatActivity() {

    private val githubRepository by lazy {
        GithubRepository(RetrofitBuilder.getInstance(application)!!.api, application)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, GithubFactory(githubRepository))[GithubViewModel::class.java]
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    fun init(){

        binding.githubRecyclerView.setHasFixedSize(true)
        // vertical layout
        binding.githubRecyclerView.layoutManager = LinearLayoutManager(this)


        viewModel.userDataObserver("123456shubham")

        viewModel.repoLiveData.observe(this) {
            when (it) {


                is ApiResponse.Loading -> {
                    // show progress bar

                    binding.progressBar.visibility = android.view.View.VISIBLE
                    binding.layout.visibility = android.view.View.GONE
                }

                is ApiResponse.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    binding.layout.visibility = android.view.View.GONE
                    binding.errorMessage.visibility = android.view.View.VISIBLE
                    binding.errorMessage.text = it.errorMessage.toString()
                    Toast.makeText(this@MainActivity, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is ApiResponse.Success -> {
                    it.data?.let { response ->
                        binding.progressBar.visibility = android.view.View.GONE
                        binding.layout.visibility = android.view.View.VISIBLE
                        val adapter = GithubAdapter(response)
                        binding.githubRecyclerView.adapter = adapter
                    }


                }
            }
        }
    }
}