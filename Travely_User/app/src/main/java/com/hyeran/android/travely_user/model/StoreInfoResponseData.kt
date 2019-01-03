package com.hyeran.android.travely_user.model

data class StoreInfoResponseData (
        var address : String,
        var closeTime : Long,
        var limit : Int,
        var latitude : Double,
        var openTime : Long,
        var ownerIdx : Int,
        var storeCall : String,
        var storeIdx : Int,
        var storeName : String,
        var storeUrl : String
)