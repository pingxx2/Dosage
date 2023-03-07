package com.example.dosage.data

data class Item(
    val CPNT_CD: String,
    val DAY_MAX_DOSG_QY: String,
    val DAY_MAX_DOSG_QY_UNIT: DAYMAXDOSGQYUNIT,
    val DOSAGE_ROUTE_CODE: String,
    val DRUG_CPNT_ENG_NM: String,
    val DRUG_CPNT_KOR_NM: String,
    val FOML_CD: String,
    val FOML_NM: String
)