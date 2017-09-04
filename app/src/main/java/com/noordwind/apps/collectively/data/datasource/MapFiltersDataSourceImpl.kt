package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import io.reactivex.Observable
import net.grandcentrix.tray.TrayPreferences
import java.util.*

class MapFiltersDataSourceImpl(context: Context) : MapFiltersDataSource, TrayPreferences(context, "MAP_FILTERS", 1), Constants {

    private val gson = Gson()
    private val listType = object : TypeToken<LinkedList<String>>() {}.type
    private val allCategoryFilters = listOf("defect", "issue", "suggestion", "praise").sortedBy { it }
    private val allStatusFilters = listOf("new", "processing", "resolved", "renewed").sortedBy { it }

    override fun addCategoryFilter(filter: String) : Observable<Boolean> {
        return Observable.fromCallable {
            var filters = LinkedList<String>(selectedCategoryFilters())
            if (!filters.contains(filter)) filters.add(filter)
            saveCategoryFilters(filters)
        }.flatMap { Observable.just(true) }
    }

    override fun removeCategoryFilter(filter: String) : Observable<Boolean>  {
        return Observable.fromCallable {
            saveCategoryFilters(selectedCategoryFilters().filterNot { it.equals(filter) })
        }.flatMap { Observable.just(true) }
    }

    override fun allCategoryFilters(): List<String> = allCategoryFilters

    override fun selectedCategoryFilters(): List<String> {
        var filtersJson = getString(Constants.PreferencesKey.REMARK_CATEGORY_FILTERS, gson.toJson(allCategoryFilters))
        var filters = if(filtersJson.isNullOrBlank()) LinkedList<String>() else gson.fromJson(filtersJson, listType)
        return filters
    }

    private fun saveCategoryFilters(filters: List<String>) {
        put(Constants.PreferencesKey.REMARK_CATEGORY_FILTERS, gson.toJson(filters))
    }

    override fun addStatusFilter(filter: String): Observable<Boolean> {
        return Observable.fromCallable {
            var filters = LinkedList<String>(selectedStatusFilters())
            if (!filters.contains(filter)) filters.add(filter)
            saveStatusFilters(filters)
        }.flatMap { Observable.just(true) }
    }

    override fun removeStatusFilter(filter: String): Observable<Boolean> {
        return Observable.fromCallable {
            saveStatusFilters(selectedStatusFilters().filterNot { it.equals(filter) })
        }.flatMap { Observable.just(true) }
    }

    override fun allStatusFilters(): List<String> = allStatusFilters

    override fun selectedStatusFilters(): List<String>  {
        var filtersJson = getString(Constants.PreferencesKey.REMARK_STATUS_FILTERS, gson.toJson(allStatusFilters))
        var filters = if(filtersJson.isNullOrBlank()) LinkedList<String>() else gson.fromJson(filtersJson, listType)
        return filters
    }

    private fun saveStatusFilters(filters: List<String>) {
        put(Constants.PreferencesKey.REMARK_STATUS_FILTERS, gson.toJson(filters))
    }

    override fun reset() {
        saveStatusFilters(allStatusFilters)
        saveCategoryFilters(allCategoryFilters)
    }

    override fun selectGroup(group: String): Observable<Boolean> {
        return Observable.fromCallable {
            put(Constants.PreferencesKey.REMARK_GROUP, group)
        }.flatMap { Observable.just(true) }
    }

    override fun getSelectedGroup(): Observable<String> = Observable.just(getString(Constants.PreferencesKey.REMARK_GROUP, context.getString(R.string.add_remark_all_groups_target)))

    override fun selectShowOnlyMine(shouldShow: Boolean): Observable<Boolean> {
        return Observable.fromCallable {
            put(Constants.PreferencesKey.SHOW_ONLY_MINE, shouldShow)
        }.flatMap { Observable.just(true) }
    }

    override fun getShowOnlyMineStatus(): Observable<Boolean> = Observable.just(getBoolean(Constants.PreferencesKey.SHOW_ONLY_MINE, false))

    override fun onCreate(i: Int) {
    }

    override fun onUpgrade(i: Int, i1: Int) {

    }
}
