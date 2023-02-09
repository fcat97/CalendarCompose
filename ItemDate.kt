package media.uqab.coreAndroid.presentation.calendarCompose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ItemDate(
    text: String,
    textColor: Color = Color.Unspecified,
    isSelected: Boolean = false,
    onClick: (d: String) -> Unit,
) {
    Surface(
        modifier = Modifier.requiredSize(32.dp),
        color = if (isSelected) MaterialTheme.colors.secondary else Color.Transparent,
        shape = CircleShape,
        elevation = if (isSelected) 2.dp else 0.dp,
        onClick = { onClick(text) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(text = text, color = if (isSelected) MaterialTheme.colors.onPrimary else textColor)
        }
    }
    

}