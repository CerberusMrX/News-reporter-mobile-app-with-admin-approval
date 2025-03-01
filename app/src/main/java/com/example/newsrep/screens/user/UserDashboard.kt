package com.example.newsrep.screens.user

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newsrep.ui.theme.*
import com.example.newsrep.data.NewsRepository
import com.example.newsrep.data.NewsArticle
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import com.example.newsrep.ui.theme.GradientBackground
import com.example.newsrep.ui.theme.AppColors
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboard(onBackClick: () -> Unit = {}) {
    GradientBackground {
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Latest News", "Trending")

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "User Dashboard",
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
                        0 -> LatestNewsContent()
                        1 -> TrendingContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun LatestNewsContent() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val allArticles = NewsRepository.getApprovedArticles()
    
    Column {
        SearchBar { query ->
            searchQuery = query
        }
        
        CategoryFilters(
            categories = listOf("Politics", "Technology", "Sports", "Entertainment", 
                              "Business", "Health", "Science", "Education"),
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )
        
        val filteredArticles = allArticles.filter { article ->
            val matchesSearch = article.title.contains(searchQuery, ignoreCase = true) ||
                              article.description.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "All" || article.category == selectedCategory
            matchesSearch && matchesCategory
        }
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredArticles) { article ->
                NewsCard(article)
            }
        }
    }
}

@Composable
private fun TrendingContent() {
    val articles = NewsRepository.getTrendingArticles()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(articles) { article ->
            NewsCard(article)
        }
    }
}

// Wilson score interval for trending calculation
private fun calculateTrendingScore(article: NewsArticle): Double {
    val age = (System.currentTimeMillis() - article.timestamp) / (1000.0 * 60 * 60) // Hours
    val gravity = 1.8
    
    // Calculate base score from likes and views
    val score = article.likes.toDouble() / maxOf(article.views, 1)
    
    // Decay score based on age
    return score * Math.pow(10.0, -gravity * ln(maxOf(age, 1.0)))
}

@Composable
private fun NewsCard(article: NewsArticle) {
    var isLiked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = article.category,
                        style = MaterialTheme.typography.labelMedium,
                        color = Primary
                    )
                    Text(
                        text = " • ${formatDate(article.timestamp)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { 
                            isLiked = !isLiked
                            if (isLiked) {
                                article.likes++
                            } else {
                                article.likes = maxOf(0, article.likes - 1)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else TextSecondary
                        )
                    }
                    Text(
                        text = "${article.likes}",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
                
                IconButton(
                    onClick = { 
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, article.title)
                            putExtra(Intent.EXTRA_TEXT, """
                                ${article.title}
                                
                                ${article.description}
                                
                                Category: ${article.category}
                                """.trimIndent()
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share News"))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = TextSecondary
                    )
                }
            }
        }
    }

    // Increment view count when article is displayed
    LaunchedEffect(article.id) {
        article.views++
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

@Composable
private fun SearchBar(onSearch: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { 
            searchQuery = it
            onSearch(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Search news...") },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = AppColors.DeepOrange,
            unfocusedBorderColor = Color.Gray
        ),
        singleLine = true
    )
}

// Add category filter chips
@Composable
private fun CategoryFilters(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categoryList = listOf("All") + categories
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categoryList) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(category) }
            )
        }
    }
} 