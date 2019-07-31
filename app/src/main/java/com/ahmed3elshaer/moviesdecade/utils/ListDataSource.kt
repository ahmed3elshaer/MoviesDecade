package com.ahmed3elshaer.moviesdecade.utils

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource

// a util class that helps us convert any list into DataSource for paging and for test
class ListDataSource<T>(private val items: List<T>) : DataSource.Factory<Int, T>() {
    private val sourceLiveData = MutableLiveData<FakeDataSource<T>>()

    override fun create(): DataSource<Int, T> {
        val source = FakeDataSource(items)
        sourceLiveData.postValue(source)
        return source
    }

    class FakeDataSource<T>(var items: List<T>) : PositionalDataSource<T>() {
        override fun loadInitial(params: PositionalDataSource.LoadInitialParams,
                                 callback: PositionalDataSource.LoadInitialCallback<T>) {
            val totalCount = items.size

            val position = PositionalDataSource.computeInitialLoadPosition(params, totalCount)
            val loadSize = PositionalDataSource.computeInitialLoadSize(params, position, totalCount)

            val sublist = items.subList(position, position + loadSize)
            callback.onResult(sublist, position, totalCount)
        }

        override fun loadRange(params: PositionalDataSource.LoadRangeParams,
                               callback: PositionalDataSource.LoadRangeCallback<T>) {
            callback.onResult(items.subList(params.startPosition,
                params.startPosition + params.loadSize))
        }
    }
}