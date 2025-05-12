package com.example.myapplication.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.GithubRepository
import com.example.myapplication.viewModel.GithubViewModel

class GithubFactory(private val githubRepository: GithubRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GithubViewModel::class.java)){
            GithubViewModel(this.githubRepository) as T
        }else{
            throw IllegalArgumentException("View Model Not Found")
        }
    }

}