package com.example.newsrep.data

import androidx.compose.runtime.mutableStateListOf
import android.net.Uri
import java.util.UUID

data class NewsArticle(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val category: String,
    val imageUri: Uri? = null,
    val reporterId: String,
    val reporterName: String,
    val timestamp: Long = System.currentTimeMillis(),
    var status: String = "Pending", // Pending, Approved, Rejected
    var likes: Int = 0,
    var views: Int = 0,
    var comments: MutableList<Comment> = mutableListOf()
)

data class Comment(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val userName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val name: String,
    val role: String, // admin, reporter, user
    var status: String = "Active" // Active, Suspended
)

object NewsRepository {
    val articles = mutableStateListOf<NewsArticle>()
    val users = mutableStateListOf<User>()
    
    // Sample data
    init {
        // Add sample users
        users.addAll(listOf(
            User("1", "admin@newsrep.com", "Admin User", "admin"),
            User("2", "reporter@newsrep.com", "John Reporter", "reporter"),
            User("3", "user@newsrep.com", "Regular User", "user")
        ))

        // Add sample articles
        articles.addAll(listOf(
            NewsArticle(
                title = "Breaking News: Technology Advancement",
                description = "Major breakthrough in quantum computing achieved...",
                category = "Technology",
                reporterId = "2",
                reporterName = "John Reporter",
                status = "Approved"
            ),
            NewsArticle(
                title = "Sports Update: World Cup Finals",
                description = "Exciting match results in unexpected victory...",
                category = "Sports",
                reporterId = "2",
                reporterName = "John Reporter",
                status = "Pending"
            )
        ))
    }

    fun addArticle(article: NewsArticle) {
        articles.add(article)
    }

    fun updateArticleStatus(articleId: String, status: String) {
        val index = articles.indexOfFirst { it.id == articleId }
        if (index != -1) {
            articles[index] = articles[index].copy(status = status)
        }
    }

    fun addComment(articleId: String, comment: Comment) {
        val index = articles.indexOfFirst { it.id == articleId }
        if (index != -1) {
            articles[index].comments.add(comment)
        }
    }

    fun getArticlesByReporter(reporterId: String): List<NewsArticle> {
        return articles.filter { it.reporterId == reporterId }
    }

    fun getApprovedArticles(): List<NewsArticle> {
        return articles.filter { it.status == "Approved" }
    }

    fun getTrendingArticles(): List<NewsArticle> {
        return getApprovedArticles()
            .sortedByDescending { article ->
                // Calculate trending score based on views, likes, and comments
                article.views + (article.likes * 2) + (article.comments.size * 3)
            }
    }

    fun getUsersByRole(role: String): List<User> {
        return users.filter { it.role == role }
    }

    fun updateUserStatus(userId: String, status: String) {
        val index = users.indexOfFirst { it.id == userId }
        if (index != -1) {
            users[index] = users[index].copy(status = status)
        }
    }

    fun signUp(email: String, name: String, role: String): User? {
        // Check if email already exists
        if (users.any { it.email == email }) {
            return null
        }
        
        val newUser = User(
            email = email,
            name = name,
            role = role,
            status = "Active"
        )
        users.add(newUser)
        return newUser
    }

    fun signIn(email: String): User? {
        return users.find { it.email == email }
    }
} 