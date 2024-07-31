@file:JvmName("AttendanceViewModelKt")

package com.crm.edu.ui.compose.screens.attendance


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R
import com.crm.edu.ui.compose.Screen

@Composable
fun AttendanceScreen(
    navController: NavHostController,
    viewModel: AttendanceViewModel = hiltViewModel(),
    onOptionClick: (String) -> Unit = {},
    onUpClick: () -> Unit = {},
) {
    val optionItems = getAttendanceOptionItems()

    AttendanceScreenInternal(
        optionItems = optionItems,
        onOptionClick = onOptionClick,
        onUpClick = onUpClick
    )
}


@Composable
private fun AttendanceScreenInternal(
    optionItems: List<AttendanceOptionItem>,
    onOptionClick: (String) -> Unit = {},
    onUpClick: () -> Unit = {},
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            AttendanceTopBar(onUpClick = onUpClick)
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(optionItems) { item ->
                    MainItem(
                        text = item.name,
                        drawableRes = item.drawableRes,
                        textColor = item.color,
                        onClick = {
                            onOptionClick.invoke(item.route)
                        })
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttendanceTopBar(
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.attendance_title))
        },
        modifier = modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = onUpClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
    )
}

private fun getAttendanceOptionItems(): List<AttendanceOptionItem> {
    val optionItems = mutableListOf(
        AttendanceOptionItem(
            name = "My Team",
            route = Screen.MyTeam.route,
            drawableRes = R.drawable.ic_my_team_color,
            color = Color.Magenta
        ),
        AttendanceOptionItem(
            name = "My Attendance",
            route = Screen.Calendar.route,
            drawableRes = R.drawable.ic_attendance_multi_color,
            color = Color.Blue
        ),
        AttendanceOptionItem(
            name = "Mark Attendance",
            route = Screen.MarkAttendance.route,
            drawableRes = R.drawable.ic_mark_attendance,
            color = Color.DarkGray
        ),
    )
    return optionItems
}


@Composable
private fun MainItem(text: String, drawableRes: Int, textColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = text,
                color = textColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            val painter = painterResource(id = drawableRes)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

        }
    }
}

@Preview
@Composable
private fun AttendanceScreenPreview(
    @PreviewParameter(AttendanceScreenPreviewParamProvider::class) optionItems: List<AttendanceOptionItem>
) {
    AttendanceScreenInternal(optionItems = optionItems)
}

private class AttendanceScreenPreviewParamProvider :
    PreviewParameterProvider<List<AttendanceOptionItem>> {

    override val values: Sequence<List<AttendanceOptionItem>> =
        sequenceOf(
            getAttendanceOptionItems()
        )
}

private data class AttendanceOptionItem(
    val name: String,
    val route: String,
    val drawableRes: Int,
    val color: Color = Color.Black
)

enum class AttendanceStatus {
    PRESENT, LEAVE, ABSENT, HOLIDAY, HALF_DAY, COMPULSORY_OFF, OUTDOOR_DUTY, CHECK_IN
}