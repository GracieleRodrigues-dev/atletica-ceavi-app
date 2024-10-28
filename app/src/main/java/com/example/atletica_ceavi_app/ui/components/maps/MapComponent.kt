package com.example.atletica_ceavi_app.ui.components.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.util.Locale

@Composable
fun MapComponent(
    locationName: String,
    mapPosition: LatLng,
    onMapClick: (LatLng, String) -> Unit
) {
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    ) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasLocationPermission = isGranted }
    )

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val mapProperties = MapProperties(isMyLocationEnabled = true)
    val mapUiSettings = MapUiSettings(myLocationButtonEnabled = true)

    if (hasLocationPermission) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onMapClick = { latLng ->
                val address = getAddressFromLatLng(context, latLng)
                onMapClick(latLng, address)
            }
        ) {
            Marker(position = mapPosition, title = locationName)
        }
    } else {
        Text(text = "Permissão de localização necessária para exibir o mapa.")
    }
}


private fun getAddressFromLatLng(context: Context, latLng: LatLng): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        addresses?.firstOrNull()?.getAddressLine(0) ?: "Endereço desconhecido"
    } catch (e: Exception) {
        "Erro ao obter endereço"
    }
}