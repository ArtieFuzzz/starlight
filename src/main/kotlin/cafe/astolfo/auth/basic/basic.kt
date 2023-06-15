package cafe.astolfo.auth.basic

import java.util.*

/**
 * Validate credentials
 *
 * @return Boolean
 */
fun verify(authorization: String): Boolean {
    val decoded = String(Base64.getUrlDecoder().decode(authorization.toByteArray()))
    val credentials = System.getenv("STARLIGHT_CREDENTIALS")

    return decoded == credentials
}