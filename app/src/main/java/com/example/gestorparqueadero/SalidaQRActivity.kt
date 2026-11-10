
package com.example.gestorparqueadero

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.google.zxing.ResultPoint

class SalidaQRActivity : AppCompatActivity() {

    private lateinit var barcodeScanner: DecoratedBarcodeView
    private var codigoLeido = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salida_qr)

        barcodeScanner = findViewById(R.id.barcodeScannerSalida)

        barcodeScanner.decodeContinuous(object : BarcodeCallback {

            override fun barcodeResult(result: BarcodeResult?) {
                if (result != null && !codigoLeido) {
                    codigoLeido = true
                    barcodeScanner.pause()

                    Toast.makeText(
                        this@SalidaQRActivity,
                        "Código leído: ${result.text}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>?) {
            }
        })

        findViewById<Button>(R.id.btnEscanearSalida).setOnClickListener {
            codigoLeido = false
            barcodeScanner.resume()

            Toast.makeText(
                this,
                "Cámara activada. Enfoca el código QR.",
                Toast.LENGTH_SHORT
            ).show()
        }

        findViewById<Button>(R.id.btnCancelarSalida).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!codigoLeido) {
            barcodeScanner.resume()
        }
    }

    override fun onPause() {
        barcodeScanner.pause()
        super.onPause()
    }
}
