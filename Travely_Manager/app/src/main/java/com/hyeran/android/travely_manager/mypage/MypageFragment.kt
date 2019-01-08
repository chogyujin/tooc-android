package com.hyeran.android.travely_manager.mypage

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hyeran.android.travely_manager.R
import com.hyeran.android.travely_manager.ReviewFragment
import com.hyeran.android.travely_manager.db.SharedPreferencesController
import com.hyeran.android.travely_manager.login.LoginActivity
import com.hyeran.android.travely_manager.model.MypageResponseData
import com.hyeran.android.travely_manager.network.ApplicationController
import com.hyeran.android.travely_manager.network.NetworkService
import kotlinx.android.synthetic.main.fragment_mypage.*
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment : Fragment() {

    lateinit var networkService: NetworkService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_mypage, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClickListener()

        networkService = ApplicationController.instance.networkService

        getProfileResponse()
    }

    private fun setClickListener() {
        iv_set_mypage.setOnClickListener {
            replaceFragment(SetFragment())
        }

        switch_available_mypage.setOnCheckedChangeListener { buttonView, isChecked ->
            if (switch_available_mypage.isChecked) tv_resevable_mypage.text = "ON"
            else tv_resevable_mypage.text = "OFF"
            putMypageOnOffResponse()
//            toast(switch_available_mypage.isChecked.toString())
        }


//        btn_whole_statistics_mypage.setOnClickListener {
//            replaceFragment(WholeStatisticsFragment())
//        }

        btn_review_mypage.setOnClickListener {
            replaceFragment(ReviewFragment())
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_main, fragment)
        transaction.commit()
    }

    private fun getProfileResponse() {

        var jwt: String? = SharedPreferencesController.instance!!.getPrefStringData("jwt")

        val getMypageResponse = networkService.getMypageResponse(jwt)

        getMypageResponse!!.enqueue(object : Callback<MypageResponseData> {
            override fun onFailure(call: Call<MypageResponseData>, t: Throwable) {
            }

            override fun onResponse(call: Call<MypageResponseData>, response: Response<MypageResponseData>) {
                response?.let {
                    when (it.code()) {
                        200 -> {
                            tv_name_mypage.text = response.body()!!.ownerName
                            tv_current_bag_mypage.text = response.body()!!.serviceCount.toString()
//                            tv_favorite_mypage.text = response.body()!!.
                            tv_review_mypage.text = response.body()!!.reviewCount.toString()
                            tv_store_name_mypage.text = response.body()!!.storeName
                            tv_address_mypage.text = response.body()!!.address
                            tv_address_number_mypage.text = response.body()!!.addressNumber
                            if (response.body()!!.onOff == 1) {
                                tv_resevable_mypage.text = "ON"
                                switch_available_mypage.isChecked = true
                            }
                            else {
                                tv_resevable_mypage.text = "OFF"
                                switch_available_mypage.isChecked = false
                            }
//                            tv_mybag_cnt_mypage.text = response.body()!!.myBagCount.toString()
//                            tv_favorite_cnt_mypage.text = response.body()!!.favoriteCount.toString()
//                            tv_review_cnt_mypage.text = response.body()!!.reviewCount.toString()
//
                            Glide.with(this@MypageFragment)
                                    .load(response.body()!!.ownerImgUrl)
                                    .into(iv_profile_mypage)
//
                            toast("프로필 조회 성공")
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

    private fun putMypageOnOffResponse() {

        var jwt: String? = SharedPreferencesController.instance!!.getPrefStringData("jwt")

        val putMypageOnOffResponse = networkService.putMypageOnOffResponse(jwt)

        putMypageOnOffResponse!!.enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                response?.let {
                    when (it.code()) {
                        200 -> {
                            toast("관리자 마이페이지")
                        }
                        400 -> {
                            toast("잘못된 접근")
                        }
                        403 -> {
                            toast("인증 에러")
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