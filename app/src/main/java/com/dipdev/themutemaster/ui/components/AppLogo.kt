package com.dipdev.themutemaster.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dipdev.themutemaster.R
import com.dipdev.themutemaster.ui.theme.displayFontFamily


@Composable
fun AppLogo(modifier: Modifier= Modifier){
    Row(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.appicon),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )
        Text(
            text = "MuteMaster",
            fontFamily = FontFamily(Font(R.font.sekuya_regular)),
            fontSize = 18.sp,
            lineHeight = 15.sp
        )
        Spacer(Modifier
            .padding(top = 10.dp)
            .clip(CircleShape)
            .background(colorScheme.primary)
            .size(5.dp)
        )
    }
}