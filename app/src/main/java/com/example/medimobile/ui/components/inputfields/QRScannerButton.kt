package com.example.medimobile.ui.components.inputfields

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QRScannerButton(
    onResult: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            val scannedValue = result.contents // Extract the scanned QR code
            if (scannedValue != null) {
                onResult(scannedValue) // Pass it back
            }
        }
    )

    Button(onClick = {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scan a QR Code")
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
        }
        launcher.launch(options)
    },
        modifier = modifier
    ) {
        Text("Scan QR Code")
    }
}
