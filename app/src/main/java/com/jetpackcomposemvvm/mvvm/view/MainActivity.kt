package com.jetpackcomposemvvm.mvvm.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.jetpackcomposemvvm.mvvm.model.interfaces.ConnectivityUtils
import com.example.androidinterviewtest.view.theme.AndroidInterviewTestTheme
import com.jetpackcomposemvvm.mvvm.viewmodel.ImageDataViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ImageDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidInterviewTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isConnectedToInternet = ConnectivityUtils.isConnectedToInternet(LocalContext.current)
                    if(isConnectedToInternet){
                        ImageMainScreen(viewModel)
                    }else{
                        ConnectivityChecker()
                    }
                }
            }
        }
    }
}

