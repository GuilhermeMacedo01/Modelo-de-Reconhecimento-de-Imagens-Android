package com.br.alura.galeria.navigation.graphs

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.br.alura.galeria.R
import com.br.alura.galeria.navigation.Destinations
import com.br.alura.galeria.ui.imageDetail.ImageDetailScreen
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions


fun NavGraphBuilder.imageDetailGraph() {
    composable(
        route = Destinations.ImageDetail.route
    ) {
        val context = LocalContext.current

        var description by remember {
            mutableStateOf("#descrição #da #imagem")
        }

        val imageBitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.image_test
        )

        var currentImage: Any by remember {
            mutableStateOf(imageBitmap)
        }


        ImageDetailScreen(
            defaultImage = currentImage,
            description = description,
            onImageChange = {
                currentImage = it
                val image = InputImage.fromFilePath(context, it)

                val options = ImageLabelerOptions.Builder()
                    .setConfidenceThreshold(0.5f)
                    .build()
                val labeler = ImageLabeling.getClient(options)

                labeler.process(image)
                    .addOnSuccessListener { labels ->
                        labels.forEach{
                            val labelConfidence = "${it.text} - ${it.confidence}"
                            Log.d("Log confiança no label:", labelConfidence);
                        }

                        description = labels.map{it.text}.toString()
                    }
            }
        )
    }
}