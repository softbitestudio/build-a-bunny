package com.softbite.buildabunny

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.softbite.buildabunny.data.repository.CharacterRepository
import com.softbite.buildabunny.navigation.AppNavGraph
import com.softbite.buildabunny.receipts.data.repository.ReceiptRepository
import com.softbite.buildabunny.ui.theme.BuildABunnyTheme

class MainActivity : ComponentActivity() {

    private val characterRepository = CharacterRepository()
    private val receiptRepository = ReceiptRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuildABunnyTheme {
                AppNavGraph(
                    characterRepository = characterRepository,
                    receiptRepository = receiptRepository,
                )
            }
        }
    }
}
