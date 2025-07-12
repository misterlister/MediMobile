package com.example.medimobile.ui.components.inputfields

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.medimobile.ui.components.templates.MediButton
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QRScannerButton(
    onResult: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    emptyHighlight: Boolean = false
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

    MediButton(onClick = {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scan QR")
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
        }
        launcher.launch(options)
    },
        modifier = modifier,
        enabled = enabled,
        emptyHighlight = emptyHighlight
    ) {
        Text(
            text = "Scan QR",
            textAlign = TextAlign.Center
        )
    }
}
