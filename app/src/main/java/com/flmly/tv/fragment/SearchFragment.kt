package com.flmly.tv.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flmly.tv.activity.DetailsActivity
import com.flmly.tv.activity.HomeActivity
import com.flmly.tv.model.SearchModel
import com.flmly.tv.utility.Api
import com.flmly.tv.utility.CategoryPresenter
import org.json.JSONException
import org.json.JSONObject

class SearchFragment : VerticalGridSupportFragment() {
    var row: Int = 0
    private val NUM_COLUMNS = 8
    var mCategoryRowAdapter = ArrayObjectAdapter(CategoryPresenter())
    private var cardPresenter_Header: HeaderItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUIElements()

    }

    private fun setupUIElements() {
        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false)
        gridPresenter.numberOfColumns = NUM_COLUMNS
        setGridPresenter(gridPresenter)
        setUpEvents()
//        loadData()
    }

    fun loadData(searchText: String) {
        try {
            mCategoryRowAdapter.clear()
            HomeActivity.progressBar.visibility = View.VISIBLE
            val stringRequest = object : StringRequest(
                Method.GET, "https://www.flmly.com/api/film-details/search_film?tv&search_text="+searchText.trim(),
                Response.Listener { response ->
                    Log.e("TAG Search", "$response")
                    row++
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray("data")
                    if (jsonArray.length() > 0) {
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            cardPresenter_Header = HeaderItem("")
                            val searchModel = SearchModel()
                            searchModel.row=i
                            searchModel.thumb = jsonObject1.getString("thumb")
                            searchModel.title = jsonObject1.getString("title")
                            searchModel.series_id = jsonObject1.getString("series_id")
                            searchModel.episode_id = jsonObject1.getString("episode_id")
                            mCategoryRowAdapter!!.add(searchModel)
                        }
                        setAdapter(mCategoryRowAdapter)
                        HomeActivity.searchLayout.visibility = View.VISIBLE
                        HomeActivity.searchFragment!!.view!!.visibility=View.VISIBLE
//                        HomeActivity.searchFragment!!.view!!.requestFocus()
                        HomeActivity.progressBar.visibility = View.GONE
                        HomeActivity.movieNotFoundText.visibility=View.GONE
                        HomeActivity.searchData="yes"
                    } else {
//                        Toast.makeText(
//                            activity,
//                            "Nothing to show",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        HomeActivity.progressBar.visibility = View.GONE
                        HomeActivity.searchEditText.requestFocus()
                        HomeActivity.searchFragment?.view?.visibility =View.GONE
                        HomeActivity.searchData="no"
                        HomeActivity.movieNotFoundText.visibility=View.VISIBLE
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        activity,
                        "Something went wrong.",
                        Toast.LENGTH_SHORT
                    ).show()
                    HomeActivity.progressBar.visibility = View.GONE
                })
            {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["api-key"] = "6vQKnb_G5c6"
                    headers["x-auth-token"] = HomeActivity.auth
                    return headers
                }
            }

            val requestQueue = Volley.newRequestQueue(activity)
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    fun setUpEvents() {
        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is SearchModel) {
                //                val content = item
                HomeActivity.searchRow  = item.row

                Log.d("zz","row"+item.row)
            }
        }
        onItemViewClickedListener =
            OnItemViewClickedListener { viewHolder, item, viewHolder1, row ->
                if (item is SearchModel) {
                    val intent = Intent(activity, DetailsActivity::class.java)
                    intent.putExtra("series_id", item.series_id)
                    startActivity(intent)
                }
            }

    }
}