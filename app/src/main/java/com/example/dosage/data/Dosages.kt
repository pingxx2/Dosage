package com.example.dosage.data

import com.google.gson.annotations.SerializedName

data class Dosages(
    @SerializedName("#omit-xml-declaration")
    val omit_xml_declaration: String,
    val response: Response
)