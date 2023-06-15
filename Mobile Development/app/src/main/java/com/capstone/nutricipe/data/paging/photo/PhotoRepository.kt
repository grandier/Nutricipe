package com.capstone.nutricipe.data.paging.photo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.nutricipe.data.database.HistoryDatabase
import com.capstone.nutricipe.data.remote.api.ApiService
import com.capstone.nutricipe.data.remote.model.ResultItem

class PhotoRepository(
    private val apiService: ApiService
) {
    fun getPhoto(token: String): LiveData<PagingData<ResultItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3
            ),
            pagingSourceFactory = {
                PhotoPagingSource(apiService, "Bearer $token")
            }
        ).liveData
    }
}
