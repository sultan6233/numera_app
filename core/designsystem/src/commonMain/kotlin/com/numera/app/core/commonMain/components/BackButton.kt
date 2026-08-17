package com.numera.app.core.commonMain.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.numera.app.core.commonMain.Primary
import org.jetbrains.compose.resources.stringResource
import numera.core.designsystem.generated.resources.Res
import numera.core.designsystem.generated.resources.back

/** Every non-tab screen is reached by pushing a route onto the back stack, so it needs an
 *  explicit way back rather than relying on the system/gesture back button alone. */
@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = "←", fontSize = 18.sp, color = Primary)
        Text(text = stringResource(Res.string.back), fontSize = 14.sp, color = Primary)
    }
}
