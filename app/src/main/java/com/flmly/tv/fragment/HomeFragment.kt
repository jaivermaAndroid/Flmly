 package com.flmly.tv.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.flmly.tv.activity.DetailsActivity
import com.flmly.tv.activity.HomeActivity
import com.flmly.tv.model.ContentDetailsModel
import com.flmly.tv.model.HomeModel
import com.flmly.tv.utility.Api
import com.flmly.tv.utility.CardPresenter
import org.json.JSONException
import org.json.JSONObject


 class HomeFragment : BrowseSupportFragment() {
    var  row:Int=0
     var samllbadge:String=""
     var smallCerti:String=""
     var temp:Int=0
     private var mCategoryRowAdapter = ArrayObjectAdapter(ListRowPresenter())
    private var cardPresenterHeader: HeaderItem? = null
    private var cardRowAdapter: ArrayObjectAdapter? = null
     private var cardPresenter = CardPresenter()
    val homeActivity=HomeActivity()
     var header:String=""
     var direct:String=""
     var rating:String=""
     var directThumb:String=""
     var duration:String=""
     var year:String=""
     var starRetting:Float=0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUIElements()


    }
    private fun setupUIElements() {
        headersState = BrowseSupportFragment.HEADERS_DISABLED
        isHeadersTransitionOnBackEnabled = false
        setUpEvents()
        loadData()
    }

    fun loadData() {
        try {
            val stringRequest = object : StringRequest(
                    Method.GET, Api.baseUrl + "viewers/get_viewers_series_by_category?from=tv",
                    Response.Listener { response ->
                        Log.e("FragmentResponse", "$response")
                        val listRowPresenter = ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE,false)
                        listRowPresenter.selectEffectEnabled = false
                        mCategoryRowAdapter = ArrayObjectAdapter(listRowPresenter)
                        val jsonObject = JSONObject(response)
                        HomeActivity.progressBar.visibility=View.VISIBLE
                        val js2=jsonObject.getJSONObject("data")
                       val jsonArraycategoryList=js2.getJSONArray("categoryList")
                        for (i in 0 until jsonArraycategoryList.length()) {
                            cardRowAdapter = ArrayObjectAdapter(cardPresenter)
                            row++
                            val jsonObjectCatList=jsonArraycategoryList.getJSONObject(i)
                            cardPresenterHeader = HeaderItem(0,jsonObjectCatList.getString("title"))
                            val jsonArrayseries=jsonObjectCatList.getJSONArray("series")
                            for (j in 0 until jsonArrayseries.length()) {
                                val homeModel= HomeModel()
                                val jsonObject=jsonArrayseries.getJSONObject(j)
                                homeModel.viewType=jsonObjectCatList.getString("view_type")
                                homeModel.priority=jsonObjectCatList.getString("priority")
                                homeModel.thumb=jsonObject.getString("thumb")
                                homeModel.is_featured=jsonObject.getString("is_featured")

                                homeModel.type=jsonObject.getString("type")
                                homeModel.row=row
                                homeModel.title=jsonObject.getString("title")
                                homeModel.synopsis=jsonObject.getString("synopsis")
                                homeModel.genre=jsonObject.getString("genre")
                                homeModel.user_id=jsonObject.getString("user_id")
                                if (jsonObject.has("portrait_thumb")) {
                                    homeModel.portrait_thumb =
                                        jsonObject.getString("portrait_thumb")
                                }
                                homeModel.numberOfEpisodes=jsonObject.getString("numberOfEpisodes")
                                homeModel.series_id=jsonObject.getString("series_id")
                                homeModel.tv_thumb=jsonObject.getString("tv_thumb")
                                homeModel.tv_title=jsonObject.getString("tv_title")
                                homeModel.published_at=jsonObject.getString("published_at")
                                cardRowAdapter!!.add(homeModel)

                                if (jsonObject.getString("is_featured")=="1" && temp==0)
                                {
                                    Log.d("zzz","method called")
                                    temp=1
                                    HomeActivity.tvPlay.visibility=View.VISIBLE
                                    HomeActivity.tvMore.visibility=View.VISIBLE
                                    HomeActivity.tvTitle.text = jsonObject.getString("title")
                                    HomeActivity.tvDesc.text = jsonObject.getString("synopsis")
                                    HomeActivity.tvGen.text = jsonObject.getString("genre")

                                    activity?.let {
                                        Glide.with(it).load("https://www.flmly.com/" + jsonObject.getString("tv_thumb"))
                                                .into(HomeActivity.imageBanner)
                                    }
                                    loadData(jsonObject.getString("series_id"))

                                }
                            }
                            mCategoryRowAdapter!!.add(ListRow(cardPresenterHeader, cardRowAdapter))
                        }
                        HomeActivity.imageShado.visibility=View.GONE
                        HomeActivity.progressBar.visibility=View.GONE
                        adapter=mCategoryRowAdapter
                        HomeActivity.tvPlay.requestFocus()
                    }, Response.ErrorListener {
                Toast.makeText(
                        activity,
                        "Something went wrong.",
                        Toast.LENGTH_SHORT
                ).show()
                    HomeActivity.progressBar.visibility=View.GONE
//                    HomeActivity.progressBar.visibility=View.GONE


            }) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["api-key"] = "6vQKnb_G5c6"
                    return headers
                }
            }

            val requestQueue = Volley.newRequestQueue(activity)
            requestQueue.add(stringRequest)
        }catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun setUpEvents() {
        onItemViewSelectedListener = OnItemViewSelectedListener { viewHolder, item, viewHolder1, row ->
            if (item is HomeModel) {
                if (temp==1) {
                    HomeActivity.tvPlay.requestFocus()
                    val content = item
                    HomeActivity.title1=content.title
                    HomeActivity.description = content.synopsis
                    HomeActivity.genere = content.genre
                    HomeActivity.bannerImageUrl="https://www.flmly.com/"+content.getTv_thumb()
                    HomeActivity.id=content.series_id
                    temp=0 }
            else {
                        val content = item
                        HomeActivity.tvPlay.visibility = View.INVISIBLE
                        HomeActivity.tvMore.visibility = View.INVISIBLE
                        HomeActivity.indexOfRow = content.row
                        HomeActivity.tvTitle.text = content.title
                        HomeActivity.tvDesc.text = content.synopsis
                        HomeActivity.tvGen.text = content.genre
                        activity?.let {
                            Glide.with(it).load("https://www.flmly.com/" + content.getTv_thumb())
                                    .into(HomeActivity.imageBanner)
                        }
                    val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                    if (activeNetwork?.isConnected!=null) {
                        loadData(content.series_id)
                    }
                    else{
                        Toast.makeText(
                                activity,
                                "Check your internet connection",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        onItemViewClickedListener =
            OnItemViewClickedListener { viewHolder, item, viewHolder1, row ->
                if (item is HomeModel) {
                    val content = item
                    Log.d("cc","data"+content)


                    val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                    if (activeNetwork?.isConnected!=null) {
                        // do your stuff. We have internet.
                        val intent= Intent(activity, DetailsActivity::class.java)
                        intent.putExtra("directerName",direct)
                        intent.putExtra("title",content.title)
                        intent.putExtra("banner","https://www.flmly.com/" + content.getTv_thumb())
                        intent.putExtra("header",header)
                        intent.putExtra("series_id",content.series_id)
                        Log.d("zz","series id"+content.series_id)
                        intent.putExtra("directerImage",directThumb)
                        intent.putExtra("duration",duration)
                        intent.putExtra("description",content.synopsis)
                        intent.putExtra("gen",content.genre)
                        intent.putExtra("year",content.published_at)
                        intent.putExtra("starRetting",starRetting)
                        intent.putExtra("rating",rating)
                        intent.putExtra("cast","")
                        HomeActivity.focus=1
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                                activity,
                                "Check your internet connection",
                                Toast.LENGTH_SHORT
                        ).show()
                        // We have no internet connection.
                    }

                }
            }
    }


    fun loadData(id:String) {
        try {
            val stringRequest = object : StringRequest(
                    Method.GET, Api.baseUrl + "film-details/get_film_user_and_ratings?series_id="+id,
                    Response.Listener { response ->
                        Log.e("Details", "$response")
                        HomeActivity.progressBar.visibility=View.VISIBLE
                        val jsonObject = JSONObject(response)
                        val js2=jsonObject.getJSONObject("data")
                        val jsonArrayseries=js2.getJSONArray("episodes")
                        for (i in 0 until jsonArrayseries.length()) {
                            val contentModel= ContentDetailsModel()
                            contentModel._id=js2.getString("_id")


                            Log.d("zzz","episode id"+js2.getString("_id"))

                            contentModel.thumb=js2.getString("thumb")
                            val jsonObjectEpisode=jsonArrayseries.getJSONObject(i)
                            val jsonArrayEpisode_credits=jsonObjectEpisode.getJSONArray("episode_credits")
                            for (j in 0 until jsonArrayEpisode_credits.length())
                            {
                                val jsonObjectepi=jsonArrayEpisode_credits.getJSONObject(j)
                                contentModel.episode_cre_id=jsonObjectepi.getString("_id")



                                Log.d("zzz","episode id"+jsonObjectepi.getString("_id"))

//                                HomeActivity.episode_id=jsonObjectepi.getString("_id")


                                val jsonObject=jsonObjectepi.getJSONObject("directed_by")
                                contentModel.value=jsonObject.getString("value")
                                contentModel.user_id=jsonObject.getString("user_id")
                            }
                            contentModel.series_id=js2.getString("series_id")
                            HomeActivity.series_id=js2.getString("series_id")
                            contentModel.user_fname=js2.getString("user_fname")
                            HomeActivity.tvheader.text="By the "+js2.getString("user_lname")+" family"
                            header="By the "+js2.getString("user_lname")+" family"
                            HomeActivity.header="By the "+js2.getString("user_lname")+" family"
                            contentModel.user_lname=js2.getString("user_lname")
                            contentModel.user_image=js2.getString("user_image")
                            contentModel.user_thumb=js2.getString("user_thumb")
                            if (js2.has("is_tutorial_badge_earned")) {
                                contentModel.is_tutorial_badge_earned = js2.getString("is_tutorial_badge_earned")
                                samllbadge=js2.getString("is_tutorial_badge_earned")

                            }
                            else{
                                contentModel.is_tutorial_badge_earned =""
                                HomeActivity.imgbadge.visibility=View.GONE
                                HomeActivity.smallcertificate.visibility=View.GONE

                            }
                            if (js2.has("is_tutorial_certificate_earned"))
                            {
                                    contentModel.is_tutorial_certificate_earned = js2.getString("is_tutorial_certificate_earned")
                                    smallCerti=js2.getString("is_tutorial_certificate_earned")

                            }
                            else{
                                HomeActivity.smallcertificate.visibility=View.GONE
                                HomeActivity.imgbadge.visibility=View.GONE
//                                HomeActivity.imgbadge.setImageResource(0)
                                contentModel.is_tutorial_certificate_earned=""
                            }

                            if (samllbadge=="true"&& smallCerti=="true")
                            {
                                HomeActivity.imgbadge.visibility=View.VISIBLE
                                HomeActivity.smallcertificate.visibility=View.GONE
//                                HomeActivity.imgbadge.setImageResource(R.drawable.smallbadge);
                            }
                            else if (smallCerti=="true")
                            {
                                HomeActivity.imgbadge.visibility=View.GONE
                                HomeActivity.smallcertificate.visibility=View.VISIBLE

                            }
                            else if (samllbadge=="true")
                            {
                                HomeActivity.smallcertificate.visibility=View.GONE
                                HomeActivity.imgbadge.visibility=View.VISIBLE
                            }
                            contentModel.user_age="Age "+js2.getString("user_age")
                            contentModel.director_name=js2.getString("director_name")
                            HomeActivity.tvDirect.text=js2.getString("director_name")
                            direct=js2.getString("director_name")
                            contentModel.director_thumb=js2.getString("director_thumb")
                            activity?.let { Glide.with(it).load("https://www.flmly.com"+"/uploads"+js2.getString("director_thumb")).into(HomeActivity.imageDir) }
                            directThumb="https://www.flmly.com"+"/uploads"+js2.getString("director_thumb")
//                            Log.d("zz","url"+ "https://www.flmly.com"+js2.getString("director_thumb"))
                            contentModel.director_age="Age "+js2.getString("director_age")
                            contentModel.duration=js2.getString("duration")
                            contentModel.rating=js2.getString("rating")
                            Log.d("xx","rating  "+js2.getString("rating")+"length "+js2.getString("rating").length)

                            if (js2.getString("rating").length==1)
                            {
                                HomeActivity.tvratting.text= js2.getString("rating")+".0"
                                rating=js2.getString("rating")+".0"

//                                HomeActivity.ratting.rating= (js2.getString("rating")+".0").toFloat()
                            }

                            else{
                                HomeActivity.tvratting.text= js2.getString("rating")
                                rating=js2.getString("rating")

                            }
                            HomeActivity.ratting.rating= js2.getDouble("rating").toFloat()
                            starRetting=js2.getDouble("rating").toFloat()
                            if (js2.getString("director_age")!="" ) {
                                HomeActivity.tvDirectAge.text =
                                    "Age " + js2.getString("director_age")
                            }
                            else{
                                HomeActivity.tvDirectAge.text =""
                            }

                            HomeActivity.tvDur.text= "\u2022"+" "+(js2.getInt("duration")/ 60).toInt()+"m"
                            duration="\u2022"+" "+(js2.getInt("duration")/ 60).toInt()+"m"

                            HomeActivity.progressBar.visibility=View.GONE
                        }

                    }, Response.ErrorListener {
                Toast.makeText(
                        activity,
                        "Something went wrong.",
                        Toast.LENGTH_SHORT
                ).show()
                    HomeActivity.progressBar.visibility=View.GONE


            }) {
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
        }catch (e: JSONException) {
            e.printStackTrace()
        }
    }


}