package com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CommentEntity(
    @PrimaryKey @ColumnInfo(name = "comment_id") val commentId: Int,
    @ColumnInfo(name = "post_id") val postId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "body") val body: String,
)