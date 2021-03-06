package com.hyeran.android.travely_manager.mypage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hyeran.android.travely_manager.MainActivity
import com.hyeran.android.travely_manager.R
import com.hyeran.android.travely_manager.R.id.*
import com.hyeran.android.travely_manager.network.ApplicationController
import com.hyeran.android.travely_manager.network.NetworkService
import kotlinx.android.synthetic.main.fragment_question.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.ctx
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream


class QuestionFragment : Fragment() {

    lateinit var questionAdapter: QuestionAdapter

    lateinit var networkService: NetworkService

    val REQUEST_CODE_SELECT_IMAGE: Int = 1004

    private var inquiryImgs: MultipartBody.Part? = null

    var inquiryImageSave: ArrayList<String> = ArrayList()

    val dataList: ArrayList<QuestionData> by lazy {
        ArrayList<QuestionData>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_question, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        networkService = ApplicationController.instance.networkService

        setRecyclerView()
        setClickListener()
        //postInquiryResponse()

    }

    private fun setRecyclerView() {
        questionAdapter = QuestionAdapter(activity!!, dataList)
        rv_file_question.adapter = questionAdapter
        rv_file_question.layoutManager = LinearLayoutManager(activity)

    }

    private fun setClickListener() {
        iv_back_question.setOnClickListener {
            (ctx as MainActivity).replaceFragment(SetFragment())
        }

        layout_add_question.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
            intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    var seletedPictureUri = it.data
                    val options = BitmapFactory.Options()
                    val inputStream: InputStream = ctx.contentResolver.openInputStream(seletedPictureUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                    val photoBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray())
                    //첫번째 매개변수 String을 꼭! 꼭! 서버 API에 명시된 이름으로 넣어주세요!!!
                    inquiryImgs = MultipartBody.Part.createFormData("inquiryImgs", File(seletedPictureUri.toString()).name, photoBody)

                    var filename = File(seletedPictureUri.toString()).name.toString()

                    try {
                        if (inquiryImgs != null) {
                            dataList.add(QuestionData(filename))   //리사이클러뷰 추가
                            inquiryImageSave.add(inquiryImgs.toString())   //서버에 넘기는 이미지 추가
                        }

                        questionAdapter = QuestionAdapter(activity!!, dataList)
                        rv_file_question.adapter = questionAdapter
                        rv_file_question.layoutManager = LinearLayoutManager(activity)

                    } catch (e: Exception) {
                        Log.e("test", e.message)
                    }
                }
            }
        }
    }


}