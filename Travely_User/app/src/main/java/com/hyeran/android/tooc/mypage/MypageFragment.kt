package com.hyeran.android.tooc.mypage

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hyeran.android.tooc.R
import com.hyeran.android.tooc.adapter.MypageRecentStoreAdapter
import com.hyeran.android.tooc.model.ProfileResponseData
import com.hyeran.android.tooc.model.StoreInfoResponseData
import com.hyeran.android.tooc.network.ApplicationController
import com.hyeran.android.tooc.network.NetworkService
import kotlinx.android.synthetic.main.fragment_mypage.*
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment : Fragment() {

    lateinit var networkService: NetworkService

    lateinit var mypageRecentStoreAdapter: MypageRecentStoreAdapter

    lateinit var v: View

    companion object {
        var mInstance: MypageFragment? = null
        @Synchronized
        fun getInstance(): MypageFragment {
            if (mInstance == null) {
                mInstance = MypageFragment()
            }
            return mInstance!!
        }
    }

    var dataList= ArrayList<StoreInfoResponseData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_mypage, container, false)
        init()
        getProfileResponse()
        getRecentStoreResponse()

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClickListener()


    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
    }

    private fun setRecyclerView() {
        mypageRecentStoreAdapter = MypageRecentStoreAdapter(activity!!, dataList)
        rv_recentstore_mypage.adapter = mypageRecentStoreAdapter
        //rv_recentstore_mypage.layoutManager = LinearLayoutManager(activity)
        //val mLayoutManager = LinearLayoutManager(this.activity)

        val mLayoutManager = LinearLayoutManager(this.activity)

        rv_recentstore_mypage.setLayoutManager(mLayoutManager)

    }

    private fun setClickListener() {

        iv_profile_mypage.setOnClickListener {
            replaceFragment(ProfileFragment())
        }
        layout_like_mypage.setOnClickListener {
            replaceFragment(LikeFragment())
        }
        layout_myreview_mypage.setOnClickListener {
            replaceFragment(MyreviewFragment())
        }
        iv_set_mypage.setOnClickListener {
            replaceFragment(SetFragment())
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.frame_main, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getProfileResponse() {

        var jwt: String? = SharedPreferencesController.instance!!.getPrefStringData("jwt")

        val getProfileResponse = networkService.getProfileResponse(jwt)

        getProfileResponse!!.enqueue(object : Callback<ProfileResponseData> {
            override fun onFailure(call: Call<ProfileResponseData>, t: Throwable) {
            }

            override fun onResponse(call: Call<ProfileResponseData>, response: Response<ProfileResponseData>) {
                response?.let {
                    when (it.code()) {
                        200 -> {
                            tv_name_mypage.text = response.body()?.name
                            tv_mybag_cnt_mypage.text = response.body()!!.myBagCount.toString()
                            tv_favorite_cnt_mypage.text = response.body()!!.favoriteCount.toString()
                            tv_review_cnt_mypage.text = response.body()!!.reviewCount.toString()

                            Glide.with(this@MypageFragment)
                                    .load(response.body()!!.profileImg)
                                    .into(iv_profile_mypage)
                        }
                        500 -> {
                            toast("서버 에러")
                        }
                        else -> {
                            toast("error")
                        }
                    }
                }
            }

        })
    }

    private fun getRecentStoreResponse() {
        var jwt: String? = SharedPreferencesController.instance!!.getPrefStringData("jwt")
        val getProfileResponse = networkService.getProfileResponse(jwt)
        getProfileResponse!!.enqueue(object : Callback<ProfileResponseData> {
            override fun onFailure(call: Call<ProfileResponseData>, t: Throwable) {
            }
            override fun onResponse(call: Call<ProfileResponseData>, response: Response<ProfileResponseData>) {
                response?.let {
                    when (it.code()) {
                        200 -> {
                            //Log.d("@@@@@@@@@@@@", dataList_recent.toString())
                            if (response.body()!!.storeInfoResponseDtoList.size > 0) {
                                dataList = response.body()!!.storeInfoResponseDtoList
                                Log.d("TAGG",dataList.toString())
//                                setRecyclerView()
//                                var mypageRecentStoreAdapter = MypageRecentStoreAdapter()
                                var mypageRecentStoreAdapter = MypageRecentStoreAdapter(activity!!, dataList)
                                rv_recentstore_mypage.adapter = mypageRecentStoreAdapter
                                rv_recentstore_mypage.setNestedScrollingEnabled(false)
                                //rv_recentstore_mypage.layoutManager = LinearLayoutManager(activity)
                                //val mLayoutManager = LinearLayoutManager(this.activity)

                                val mLayoutManager = LinearLayoutManager(activity)

                                rv_recentstore_mypage.setLayoutManager(mLayoutManager)
//                                var mypageRecent = mypageRecentStoreAdapter.dataList
//                                var position = mypageRecentStoreAdapter.itemCount
//                                mypageRecent = dataList_recent
//                                mypageRecentStoreAdapter.notifyItemInserted(position)
                                //mypageRecentStoreAdapter.dataList.clear()

//                                var position = mypageRecentStoreAdapter.itemCount
//                                //mypageRecentStoreAdapter.dataList.clear()
//                                mypageRecentStoreAdapter.dataList.addAll(dataList_recent)
//                                mypageRecentStoreAdapter.notifyItemInserted(position)

                            } else {
                            }
                        }
                        500 -> {
                            toast("서버 에러")
                        }
                        else -> {
                            toast("error")
                        }
                    }
                }
            }

        })
    }

}
