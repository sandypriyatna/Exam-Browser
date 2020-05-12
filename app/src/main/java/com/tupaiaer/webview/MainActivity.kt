package com.tupaiaer.webview

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
        setupAds()
        greetings()

    }

    private fun setupAds() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.interstital_ad_id)
        mInterstitialAd.loadAd(AdRequest.Builder().build())

    }

    private fun setupView() {
        btn_smkn1_crb.setOnClickListener {
            val intent = Intent(this, WebviewActivity::class.java)
            intent.putExtra("url", "cbt.smkn1-cirebon.sch.id:88")
            startActivity(intent)
        }

        btn_go.setOnClickListener {
            val intent = Intent(this, WebviewActivity::class.java)
            intent.putExtra("url", et_url.text.toString())

            if (et_url.text.isNullOrBlank()) {
                Toast.makeText(this, "Inputkan url atau IP", Toast.LENGTH_LONG)
            } else {
                startActivity(intent)
            }
        }
    }

    private fun greetings() {
        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)

        if (timeOfDay >= 0 && timeOfDay < 12) {
            tv_greetings.text = "Selamat pagi,"
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            tv_greetings.text = "Selamat siang,"
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            tv_greetings.text = "Selamat sore,"
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            tv_greetings.text = "Selamat malam,"
        }
    }

    override fun onBackPressed() {
        if (mInterstitialAd.isLoaded)
            mInterstitialAd.show()
        else {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setTitle("Keluar Aplikasi")
            builder.setMessage("Yakin ingin keluar aplikasi?")

            builder.setPositiveButton("Ya") { dialog, which ->
                moveTaskToBack(true)
                exitProcess(-1)
            }

            builder.setNegativeButton("Tidak") { dialog, which ->
                null
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setTitle("Keluar Aplikasi")
            builder.setMessage("Yakin ingin keluar aplikasi?")

            builder.setPositiveButton("Ya") { dialog, which ->
                moveTaskToBack(true)
                exitProcess(-1)
            }

            builder.setNegativeButton("Tidak") { dialog, which ->
                null
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
            return true
        }
        return super.onKeyDown(keyCode, event);
    }
}
