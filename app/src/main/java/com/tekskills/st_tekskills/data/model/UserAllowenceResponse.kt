package com.tekskills.st_tekskills.data.model


import com.google.gson.annotations.SerializedName

class UserAllowanceResponse : ArrayList<UserAllowanceData>()

data class UserAllowanceData(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("foodLimit")
    val foodLimit: Int,
    @SerializedName("hotelLimit")
    val hotelLimit: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("roleId")
    val roleId: Int,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("travelLimit")
    val travelLimit: Int,
    @SerializedName("travelType")
    val travelType: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int
)