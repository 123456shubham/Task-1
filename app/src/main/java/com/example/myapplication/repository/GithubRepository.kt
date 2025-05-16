package com.example.myapplication.repository


import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.api.ApiInterface
import com.example.myapplication.api.ApiResponse
import com.example.myapplication.model.CityRequest
import com.example.myapplication.model.CityResponse
import com.example.myapplication.model.MyError
import com.example.myapplication.model.github.GithubResponseItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GithubRepository(
    private val apiInterface: ApiInterface,val context: Context) {

    private val _repoLiveData = MutableLiveData<ApiResponse< List<GithubResponseItem>>>()
    val repoLiveData: LiveData<ApiResponse<List<GithubResponseItem>>>
        get() = _repoLiveData

    suspend fun getUserRepos(username: String) {
        _repoLiveData.postValue(ApiResponse.Loading())


        try {
            val response = apiInterface.getUserRepos(username)
            print("Response: ${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                _repoLiveData.postValue(ApiResponse.Success(response.body()))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse: MyError = Gson().fromJson(
                        errorBody, object : TypeToken<MyError>() {}.type
                    )
                    print("Response: ${errorResponse.message}")

                    _repoLiveData.postValue(ApiResponse.Error(errorResponse.message))
                } else {
                    _repoLiveData.postValue(
                        ApiResponse.Error(
                            response.message() ?: "Something went wrong"
                        )
                    )
                }

            }
        } catch (e: Exception) {
            _repoLiveData.postValue(ApiResponse.Error("Exception: ${e.message}"))
            print("Exception: ${e.message}")

        }
    }

    private  val _launchCityLiveData = MutableLiveData<ApiResponse<CityResponse>>()
    val launchCityLiveData: LiveData<ApiResponse<CityResponse>>
        get() = _launchCityLiveData

    suspend fun launchCity(cityRequest: CityRequest){

        _launchCityLiveData.postValue(ApiResponse.Loading())
        try {

            val response = apiInterface.launchCity(cityRequest)
            print("Response: ${response.body()}")


            if (response.isSuccessful && response.body() != null) {
                _launchCityLiveData.postValue(ApiResponse.Success(response.body()))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse: MyError = Gson().fromJson(
                        errorBody, object : TypeToken<MyError>() {}.type
                    )
                    print("Response: ${errorResponse.message}")

                    _launchCityLiveData.postValue(ApiResponse.Error(errorResponse.message))
                } else {
                    _launchCityLiveData.postValue(
                        ApiResponse.Error(
                            response.message() ?: "Something went wrong"
                        )
                    )
                }

            }

        }catch (e: Exception){
            _launchCityLiveData.postValue(ApiResponse.Error("Exception: ${e.message}"))
        }
    }
}
