package com.tekskills.st_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class ClientsDetails(
    @SerializedName("clientId")
    val clientId: Int,
    @SerializedName("clientName")
    val clientName: String
)