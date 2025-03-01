package com.example.newsrep.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsrep.ui.theme.*
import com.example.newsrep.data.NewsRepository
import com.example.newsrep.data.NewsArticle
import java.text.SimpleDateFormat
import java.util.*
import com.example.newsrep.data.User
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import com.example.newsrep.ui.theme.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    onBackClick: () -> Unit
) {
    GradientBackground {
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Dashboard", "Reporters", "News", "Users")

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Admin Dashboard",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = AppColors.TextFieldBackground,
                    contentColor = Color.White
                ) {
                    tabs.forEach { tab ->
                        Tab(
                            selected = tab == tabs[selectedTabIndex],
                            onClick = { selectedTabIndex = tabs.indexOf(tab) },
                            text = { Text(tab) },
                            icon = { 
                                if (tab == "Dashboard") {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null
                                    ) 
                                } else if (tab == "Reporters") {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null
                                    ) 
                                } else if (tab == "News") {
                                    Icon(
                                        imageVector = Icons.Default.List,
                                        contentDescription = null
                                    ) 
                                } else if (tab == "Users") {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null
                                    ) 
                                }
                            }
                        )
                    }
                }

                // Content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(
                            Color.White.copy(alpha = 0.9f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    when (selectedTabIndex) {
                        0 -> ArticlesManagementContent()
                        1 -> ReportersManagementContent()
                        2 -> NewsManagementContent()
                        3 -> UsersManagementContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticlesManagementContent() {
    val articles = NewsRepository.articles
    val reporters = NewsRepository.getUsersByRole("reporter")
    val users = NewsRepository.getUsersByRole("user")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Statistics Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.List,
                    title = "Total Articles",
                    value = articles.size.toString(),
                    color = Primary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Person,
                    title = "Reporters",
                    value = reporters.size.toString(),
                    color = Secondary
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Person,
                    title = "Users",
                    value = users.size.toString(),
                    color = Primary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Warning,
                    title = "Pending",
                    value = articles.count { it.status == "Pending" }.toString(),
                    color = Color(0xFFFFA000)
                )
            }
        }

        // Recent Activities
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Recent Activities",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    articles.sortedByDescending { it.timestamp }
                        .take(5)
                        .forEach { article ->
                            RecentActivityItem(
                                "New article: ${article.title} (${article.status})"
                            )
                        }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Column {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun RecentActivityItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Primary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = TextSecondary)
    }
}

@Composable
private fun ReportersManagementContent() {
    val reporters = NewsRepository.getUsersByRole("reporter")
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(reporters) { reporter ->
            UserCard(
                user = reporter,
                onStatusChange = { newStatus ->
                    NewsRepository.updateUserStatus(reporter.id, newStatus)
                }
            )
        }
    }
}

@Composable
private fun NewsManagementContent() {
    val articles = remember { NewsRepository.articles }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(articles) { article ->
            NewsApprovalCard(article)
        }
    }
}

@Composable
private fun NewsApprovalCard(article: NewsArticle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = article.category,
                        color = Primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                StatusChip(article.status)
                
                Text(
                    text = "•",
                    color = TextSecondary
                )
                
                Text(
                    text = formatDate(article.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (article.status == "Pending") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { NewsRepository.updateArticleStatus(article.id, "Rejected") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reject")
                    }
                    
                    Button(
                        onClick = { NewsRepository.updateArticleStatus(article.id, "Approved") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Approve")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val color = when (status) {
        "Approved" -> Color(0xFF4CAF50)
        "Rejected" -> Color(0xFFE53935)
        else -> Color(0xFFFFA000)
    }
    
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = status,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun UsersManagementContent() {
    val users = NewsRepository.getUsersByRole("user")
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(users) { user ->
            UserCard(
                user = user,
                onStatusChange = { newStatus ->
                    NewsRepository.updateUserStatus(user.id, newStatus)
                }
            )
        }
    }
}

@Composable
private fun UserCard(
    user: User,
    onStatusChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
                
                UserStatusChip(
                    status = user.status,
                    onClick = {
                        val newStatus = if (user.status == "Active") "Suspended" else "Active"
                        onStatusChange(newStatus)
                    }
                )
            }
        }
    }
}

@Composable
private fun UserStatusChip(
    status: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small,
        color = when (status) {
            "Active" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
            else -> Color(0xFFE53935).copy(alpha = 0.1f)
        }
    ) {
        Text(
            text = status,
            color = when (status) {
                "Active" -> Color(0xFF4CAF50)
                else -> Color(0xFFE53935)
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
} 