package com.rose.greeneye.data.local

import android.net.Uri

data class PlantModel(
    val plant_name: String,
    val scientific_name: String,
    val imageUri: Uri,
)
