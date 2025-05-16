package com.example.myapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.CityRequest
import com.example.myapplication.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GithubViewModel (private val githubRepository: GithubRepository) : ViewModel() {

    val repoLiveData get() = githubRepository.repoLiveData

    fun userDataObserver(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            githubRepository.getUserRepos(userName)
        }
    }

    val launchCityLiveData get() = githubRepository.launchCityLiveData
    fun launchCity(cityRequest: CityRequest){
        viewModelScope.launch(Dispatchers.IO) {
            githubRepository.launchCity(cityRequest)
        }

    }
}