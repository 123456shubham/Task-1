package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemGithubRepoBinding
import com.example.myapplication.model.github.GithubResponseItem

class GithubAdapter (  private val repoList: List<GithubResponseItem>): RecyclerView.Adapter<GithubAdapter.GithubViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubViewHolder {
        val binding = ItemGithubRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GithubViewHolder, position: Int) {
        val repo = repoList[position]
        with(holder.binding) {
            tvRepoName.text = "Repository Name : "+repo.name
            tvRepoDescription.text = "Description : "+repo.description ?: "No description"
            tvProgrammingLanguage.text ="Programing Language : "+ repo.language ?: "Unknown"
            tvNumberOfStar.text = "Number Of Stars : "+repo.stargazers_count.toString()
            tvNumberOfForks.text = "Number Of Forks : "+repo.forks_count.toString()
        }
    }

    override fun getItemCount(): Int = repoList.size


    inner class GithubViewHolder(val binding: ItemGithubRepoBinding) :
        RecyclerView.ViewHolder(binding.root)

}