package cafe.astolfo.plugins

import cafe.astolfo.MessagePayload
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.concurrent.TimeUnit

fun Application.configureSecurity() {

    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = System.getenv("STARLIGHT_JWT_AUDIENCE")
    val jwtDomain = System.getenv("STARLIGHT_JWT_DOMAIN")
    val jwtRealm = System.getenv("STARLIGHT_JWT_REALM")
    val jwkProvider = JwkProviderBuilder(jwtDomain)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    authentication {
        jwt {
            realm = jwtRealm
            verifier(jwkProvider, jwtDomain) {
                acceptLeeway(3)
            }
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
            challenge { defaultScheme, realm ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    MessagePayload("Token is invalid or expired", HttpStatusCode.Unauthorized.value)
                )
            }
        }
    }
}
