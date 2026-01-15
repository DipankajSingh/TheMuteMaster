package com.dipdev.themutemaster.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dipdev.themutemaster.R
import com.dipdev.themutemaster.ui.components.AppLogo

@Composable
fun Welcome(modifier: Modifier= Modifier){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(30.dp))
        AppLogo()
        Image(
            painter = painterResource(R.drawable.appicon),
            contentDescription = "hero image",
            modifier= Modifier.weight(3f)
        )
        Text(
            text = "Welcome to MuteMaster",
            style = typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))

        Text(
            text = "Say goodbye to managing audio profiles manually with a smart audio profile manager!",
            fontSize = typography.bodyLarge.fontSize,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = colorScheme.onBackground
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {},
            modifier= Modifier
                .fillMaxWidth(.7f)
        ) {
            Text("Continue")
        }
        Spacer(Modifier.height(30.dp))

    }

}