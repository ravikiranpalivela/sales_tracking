package com.tekskills.st_tekskills.data.model


import com.google.gson.annotations.SerializedName

class MomResponse : ArrayList<MomResponseItem>()

data class MomResponseItem(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("fileNames")
    val fileNames: Any,
    @SerializedName("files")
    val files: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("infoFromClient")
    val infoFromClient: String,
    @SerializedName("meetingDateTime")
    val meetingDateTime: Any,
    @SerializedName("meetingNotes")
    val meetingNotes: String,
    @SerializedName("purposeId")
    val purposeId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("targetDate")
    val targetDate: String,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int
)