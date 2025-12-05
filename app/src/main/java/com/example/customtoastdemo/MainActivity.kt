package com.example.customtoastdemo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage

// Main entry point: hosts Compose UI and connects to ImageViewModel
class MainActivity : ComponentActivity() {

    // ViewModel is created using our custom factory (API + Room)
    private val imageVm: ImageViewModel by viewModels {
        ImageViewModel.factory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface {
                    DemoScreen(imageVm)
                }
            }
        }
    }
}

// Screen that shows Picsum image + Room "saved image" support
@Composable
fun DemoScreen(imageVm: ImageViewModel) {

    // Observe the ViewModel state inside Compose
    val ui by imageVm.uiState.collectAsState()

    val context = LocalContext.current

    // Simple placeholder / error colors for images
    val placeholder = ColorPainter(Color(0xFFEEEEEE))
    val errorPainter = ColorPainter(Color(0xFFFFCDD2))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title text describing what this demo shows
        Text(
            text = "Picsum Image Browser + Room Cache",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        // Subtitle to remind Demo 2 behaviour
        Text(
            text = "Demo 3: API image fetch + save & load last image locally",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        // Text field for optional author filtering
        OutlinedTextField(
            value = ui.authorFilter,
            onValueChange = imageVm::onAuthorChange,
            label = { Text("Filter by author (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Button: call API to fetch and save an image
        Button(
            onClick = { imageVm.fetchOne() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fetch Image from API")
        }

        Spacer(Modifier.height(8.dp))

        // Button: load last saved image from Room
        Button(
            onClick = { imageVm.loadLastSaved() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Last Saved Image (Room)")
        }

        Spacer(Modifier.height(16.dp))

        // Show loading spinner or error message if needed
        when {
            ui.loading -> CircularProgressIndicator()
            ui.error != null -> Text(
                text = ui.error ?: "Something went wrong",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(12.dp))

        // If a photo is available, display it and show action button
        ui.photo?.let { p ->
            AsyncImage(
                model = p.downloadUrl,
                contentDescription = "Downloaded image",
                placeholder = placeholder,
                error = errorPainter,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                onSuccess = {
                    Toast.makeText(context, "Image loaded", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            )

            Spacer(Modifier.height(8.dp))
            Text("Author: ${p.author}")

            Spacer(Modifier.height(8.dp))

            // Opens the image's URL in a browser
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, p.url.toUri())
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Image Source in Browser")
            }
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
    }
}
