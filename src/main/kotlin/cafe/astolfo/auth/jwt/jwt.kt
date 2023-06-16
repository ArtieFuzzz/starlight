package cafe.astolfo.auth.jwt

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Signs a token
 *
 * @return String
 */
fun signToken(username: String): String {
    val jwtDomain = System.getenv("STARLIGHT_JWT_DOMAIN")
    val jwtAudience = System.getenv("STARLIGHT_JWT_AUDIENCE")
    val privateKeyString = System.getenv("STARLIGHT_JWT_PRIVATE_KEY").toString()
    val publicKeyString = System.getenv("STARLIGHT_JWT_PUBLIC_KEYID").toString()
    val jwkProvider = JwkProviderBuilder(jwtDomain)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
    val publicKey = jwkProvider.get(publicKeyString).publicKey
    val privateKey = KeyFactory.getInstance("EC").generatePrivate(keySpecPKCS8)


    return with(JWT.create()) {
        withAudience(jwtAudience)
        withIssuer(jwtDomain)
        withClaim("user", username)
        withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 1 week expiration
    }.sign(Algorithm.ECDSA384(publicKey as ECPublicKey, privateKey as ECPrivateKey))
}