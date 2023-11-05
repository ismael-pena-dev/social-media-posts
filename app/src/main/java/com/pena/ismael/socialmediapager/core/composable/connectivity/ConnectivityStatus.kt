package com.pena.ismael.socialmediapager.core.composable.connectivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pena.ismael.socialmediapager.R
import com.pena.ismael.socialmediapager.core.composable.preview.DarkLightPreview
import com.pena.ismael.socialmediapager.core.services.connectivityobserver.ConnectivityObserver
import com.pena.ismael.socialmediapager.ui.theme.SocialMediaPagerTheme

@Composable
fun ConnectivityStatus(
    status: ConnectivityObserver.Status,
    modifier: Modifier = Modifier
) {
    if (status != ConnectivityObserver.Status.Available) {
        val message = when (status) {
            ConnectivityObserver.Status.Losing -> "Weak internet connection"
            ConnectivityObserver.Status.Lost -> "Network connection lost "
            ConnectivityObserver.Status.Unavailable -> "Network connection unavailable"
            ConnectivityObserver.Status.Available -> "Network connection restored"
        }
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Icon(painter = painterResource(R.drawable.baseline_signal_wifi_connected_no_internet_4_24), contentDescription = "connectivity status")
                Text(text = message)
            }
        }
    }
}

@DarkLightPreview
@Composable fun PreviewConnectivityStatus() {
    SocialMediaPagerTheme {
        Surface {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ConnectivityStatus(ConnectivityObserver.Status.Available)
                ConnectivityStatus(ConnectivityObserver.Status.Losing)
                ConnectivityStatus(ConnectivityObserver.Status.Lost)
                ConnectivityStatus(ConnectivityObserver.Status.Unavailable)
            }
        }
    }
}