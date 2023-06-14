package cafe.astolfo.auth

import java.util.Base64

/**
 * Validate credentials
 *
 * @return Boolean
 */
fun validateAndDecode(authorization: String): Boolean {
    val decoded = String(Base64.getUrlDecoder().decode(authorization.toByteArray()))
    println(decoded)
    val credentials = System.getenv("STARLIGHT_CREDENTIALS")
    println(credentials)

    return decoded == credentials
}