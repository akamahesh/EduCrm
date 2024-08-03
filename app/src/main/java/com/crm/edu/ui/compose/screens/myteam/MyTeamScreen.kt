package com.crm.edu.ui.compose.screens.myteam


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R

data class TeamMember(
    val name: String,
    val role: String,
    val checkedIn: Boolean,
    val time: String
)


@Composable
fun MyTeamScreen(
    navController: NavHostController,
    viewModel: MyTeamScreenViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
) {
    MyTeamScreenInternal(navController, viewModel, onUpClick)
}

@Composable
private fun MyTeamScreenInternal(
    navController: NavHostController,
    viewModel: MyTeamScreenViewModel,
    onUpClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(onUpClick = onUpClick)
        },
    ) { padding ->
        TeamList(padding, members = getTeamList())
    }
}

@Composable
fun TeamList(padding: PaddingValues = PaddingValues(), members: List<TeamMember>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        items(members.size) { index ->
            TeamMemberItem(member = members[index])
        }
    }
}


@Composable
fun TeamMemberItem(member: TeamMember) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 4.dp)) {
            Text(text = member.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = member.role, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .background(
                    if (member.checkedIn) Color(0xFFD0EFD7) else Color(0xFFD3D3D3),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            val text = if (member.checkedIn) "Checked in" else "Not Checked in"
            Text(
                text = text,
                color = Color.Black,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = member.time,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .background(Color(0xFFF2F2F2))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}


private fun getTeamList(): List<TeamMember> {
    val members = listOf(
        TeamMember("AmarJeet Kumar", "Digital Marketing Manager", false, "00.00 AM"),
        TeamMember("Anita Shende", "Graphics Designer", true, "10.30 AM"),
        TeamMember("Sahil Choudhary", "Developer", true, "00.00 AM"),
        TeamMember("Dipti Jadhav", "Content writer", true, "00.00 AM"),
        TeamMember("Kabbyik Mitra", "Content writer", false, "00.00 AM"),
        TeamMember("Prashant", "Content writer", true, "00.00 AM"),
        TeamMember("Ritika Manocha", "SEO", true, "00.00 AM"),
        TeamMember("Ruby", "SEO", false, "00.00 AM"),
        TeamMember("AmarJeet Kumar", "Digital Marketing Manager", false, "00.00 AM"),
        TeamMember("Anita Shende", "Graphics Designer", true, "10.30 AM"),
        TeamMember("Sahil Choudhary", "Developer", true, "00.00 AM"),
        TeamMember("Dipti Jadhav", "Content writer", true, "00.00 AM"),
        TeamMember("Kabbyik Mitra", "Content writer", false, "00.00 AM"),
        TeamMember("Prashant", "Content writer", true, "00.00 AM"),
        TeamMember("Ritika Manocha", "SEO", true, "00.00 AM"),
        TeamMember("Ruby", "SEO", false, "00.00 AM")
    )
    return members
}

@Preview
@Composable
fun PreviewTeamList() {
    Surface(color = Color(0xFFF2F2F2), modifier = Modifier.fillMaxSize()) {
        TeamList(members = getTeamList())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.my_team_title))
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