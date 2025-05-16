package com.example.myapplication.api

import com.example.myapplication.model.CityRequest
import com.example.myapplication.model.CityResponse
import com.example.myapplication.model.github.GithubResponse
import com.example.myapplication.model.github.GithubResponseItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {


    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String
    ): Response<List<GithubResponseItem>>



    @POST("v1/app-launching-city")
    suspend fun  launchCity(@Body launchCityRequest: CityRequest): Response<CityResponse>
}