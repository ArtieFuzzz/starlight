package cafe.astolfo.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/**
 * Signs a token
 *
 * @return String
 */
fun signToken(): String {
    val jwtAudience = System.getenv("STARLIGHT_JWT_AUDIENCE")
    val jwtDomain = System.getenv("STARLIGHT_JWT_DOMAIN")
    // val jwtRealm = System.getenv("STARLIGHT_JWT_REALM")
    val jwtSecret = System.getenv("STARLIGHT_JWT_SECRET")

    return with(JWT.create()) {
        withAudience(jwtAudience)
        withIssuer(jwtDomain)
        withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
    }.sign(Algorithm.HMAC512(jwtSecret))
}