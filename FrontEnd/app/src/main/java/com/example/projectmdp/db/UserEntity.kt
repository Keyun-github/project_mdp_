package com.example.projectmdp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serverId: String,
    val customId: String,
    val namaLengkap: String,
    val email: String,
    val role: String
)