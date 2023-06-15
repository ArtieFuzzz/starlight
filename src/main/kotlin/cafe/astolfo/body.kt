package cafe.astolfo

import kotlinx.serialization.Serializable

@Serializable
data class MessagePayload(val message: String, val status: Int)

@Serializable
data class ImagePayload(val url: String)

@Serializable
data class AuthorizationPayload(val token: String)