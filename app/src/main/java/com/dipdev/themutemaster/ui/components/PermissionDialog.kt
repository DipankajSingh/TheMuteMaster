package com.dipdev.themutemaster.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun PermissionDialog(
    title: String,
    message: String,
    onDismissRequest:()->Unit,
    onActionRequest:()->Unit,
    actionName: String
){
    Dialog(onDismissRequest = {}) {
        Card (
            modifier = Modifier

        ){
            Column (
                modifier = Modifier
                    .padding(18.dp)
            ){
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(20.dp))

                Button(onClick = {onActionRequest()},
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = actionName,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))

                Button(onClick = {onDismissRequest()},
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.inversePrimary,
                        contentColor = colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Cancel",
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    )
                }
            }
        }
    }
}