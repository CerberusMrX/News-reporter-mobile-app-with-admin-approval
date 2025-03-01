package com.example.newsrep.screens.reporter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsrep.ui.theme.*
import com.example.newsrep.data.NewsRepository
import com.example.newsrep.data.NewsArticle
import com.example.newsrep.data.Comment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import android.net.Uri
import androidx.compose.foundation.background
import java.text.SimpleDateFormat
import java.util.*
import com.example.newsrep.ui.theme.GradientBackground
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.newsrep.ui.theme.AppColors

// Keep the predefined categories
val newsCategories = listOf(
    "Politics", "Technology", "Sports", "Entertainment", 
    "Business", "Health", "Science", "Education"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReporterDashboard(onBackClick: () -> Unit = {}) {
    GradientBackground {
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("My Articles", "Approved News", "Post News")

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Reporter Dashboard",
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
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = AppColors.TextFieldBackground,
                    contentColor = Color.White
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

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
                        0 -> MyArticlesContent()
                        1 -> ApprovedNewsContent()
                        2 -> PostNewsContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun MyArticlesContent() {
    val articles = NewsRepository.getArticlesByReporter("reporterId")
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(articles) { article ->
            ArticleCard(article)
        }
    }
}

@Composable
private fun ApprovedNewsContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(NewsRepository.getApprovedArticles()) { article ->
            ArticleCard(article)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostNewsContent() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image Upload Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 1.dp,
                    color = Primary.copy(alpha = 0.5f),
                    shape = MaterialTheme.shapes.medium
                )
                .clickable { imagePicker.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                Text("Image Selected", color = Primary)
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Image",
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Add Image", color = Primary)
                }
            }
        }

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("News Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("News Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5
        )

        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = showCategoryDropdown,
            onExpandedChange = { showCategoryDropdown = it }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = showCategoryDropdown,
                onDismissRequest = { showCategoryDropdown = false }
            ) {
                newsCategories.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            category = option
                            showCategoryDropdown = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                if (title.isNotBlank() && description.isNotBlank() && category.isNotBlank()) {
                    val article = NewsArticle(
                        title = title,
                        description = description,
                        category = category,
                        imageUri = imageUri,
                        reporterId = "reporterId",
                        reporterName = "reporterName"
                    )
                    NewsRepository.addArticle(article)
                    title = ""
                    description = ""
                    category = ""
                    imageUri = null
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit News")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Success") },
            text = { Text("Your news article has been submitted for review.") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun ArticleCard(article: NewsArticle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Show image placeholder if available
            article.imageUri?.let { uri ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Image Available")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = article.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = article.description,
                fontSize = 14.sp,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chip(
                    label = { Text(article.category) },
                    modifier = Modifier.padding(end = 8.dp)
                )
                
                StatusChip(article.status)
                
                Text(
                    text = formatDate(article.timestamp),
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun Chip(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = Primary.copy(alpha = 0.1f)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                label()
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

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
} 