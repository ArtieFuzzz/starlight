package cafe.astolfo.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*

fun Application.configureSecurity() {
    
    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = System.getenv("STARLIGHT_JWT_AUDIENCE")
    val jwtDomain = System.getenv("STARLIGHT_JWT_DOMAIN")
    val jwtRealm = System.getenv("STARLIGHT_JWT_REALM")
    val jwtSecret = System.getenv("STARLIGHT_JWT_SECRET")
    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
