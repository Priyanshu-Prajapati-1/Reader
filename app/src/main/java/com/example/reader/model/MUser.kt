package com.example.reader.model

import androidx.compose.runtime.Immutable

@Immutable
data class MUser(
    val id: String?,
    val userId: String,
    val displayName: String,
    val avatarUrl: String,
    val quote: String,
    val profession: String
){
    fun toMap():MutableMap<String, Any>{
        return mutableMapOf(
            "userId" to this.userId,
            "displayName" to this.displayName,
            "avatarUrl" to this.avatarUrl,
            "quote" to this.quote,
            "profession" to this.profession
        )
    }
}
