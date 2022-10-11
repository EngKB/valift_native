package com.example.myapplication

import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.valify.valify_ekyc.sdk.Valify
import com.valify.valify_ekyc.sdk.ValifyConfig
import com.valify.valify_ekyc.sdk.ValifyException
import com.valify.valify_ekyc.sdk.ValifyFactory
import com.valify.valify_ekyc.viewmodel.ValifyData


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var valify: Valify? = null
    private var valifyConfig: ValifyConfig.Builder? = null
    private val CHANNEL = "valify.com/valify"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            var token = "MnxNoJzvoqbXB69XmL2T8FdrLQ1BjP";
            var lang = "en";
            valify = ValifyFactory(applicationContext).client // Pass context
            valifyConfig = ValifyConfig.Builder()
            valifyConfig!!.setBaseUrl("https://valifystage.com")
                .setAccessToken(token)
                .setBundleKey("45f03d3cdfe44f9897b54f3f0c14aebf")
                ///
                .setLanguage(lang)
                .withTerms(false)
                .withTutorial(true)
                .withOCR(true)
                .withDocumentVerification(true)
                .withWatermark(false)
                .withDataUpdating(false)
                .withLiveness(true)
                .withFaceMatch(true)
                .withRatingDialog(false)
                .withoutPreviewData()
                .build()
            try {
                this.valify!!.startActivityForResult(
                    this@MainActivity, 44,
                    valifyConfig!!.build()
                )

            } catch (e: ValifyException) {
                e.printStackTrace()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 44) {
            valify!!.handleActivityResult(resultCode, data, object : Valify.ValifyResultListener {
                override fun onSuccess(
                    valifyToken: String, valifyData: ValifyData
                ) {
                    Log.println(Log.DEBUG,valifyData.authToken,valifyData.authToken)
                }

                override fun onExit(
                    valifyToken: String, step: String,
                    valifyData: ValifyData
                ) {
                    Log.println(Log.DEBUG,"exit","exit'")

                }

                override fun onError(
                    valifyToken: String, errorCode: Int,
                    step: String, valifyData: ValifyData
                ) {
                    Log.println(Log.DEBUG,"error","error'")

                }
            })
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}