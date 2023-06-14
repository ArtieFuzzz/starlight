package cafe.astolfo.s3

import aws.sdk.kotlin.services.s3.*
import aws.sdk.kotlin.services.s3.model.*
import aws.smithy.kotlin.runtime.net.Url

class R2Client {
    private var client: S3Client? = null
    companion object var cache: ArrayList<String> = arrayListOf()

    // Always run this function before executing any other function such as `populateCache`
    // or `getRandom`
    suspend fun start() {
        this.client = S3Client.fromEnvironment {
            region = System.getenv("STARLIGHT_S3_REGION") ?: "auto"; endpointUrl =
            Url.parse(System.getenv("STARLIGHT_S3_URL"))
        }
    }

    suspend fun populateCache() {
        cache = arrayListOf()

        val req = ListObjectsRequest { bucket = System.getenv("STARLIGHT_S3_BUCKET") }
        val res = client?.listObjects(req)

        res?.contents?.forEach { img ->
            println(img.key)
            cache.add(img.key.toString())
        }
    }

    fun getRandom(alternative: Boolean = false): String? {
        return if (cache.isNotEmpty()) {
            var baseUrl = System.getenv("STARLIGHT_IMG_ENDPOINT")
            val image = cache.random()

            // Why not?
            return if (alternative && System.getenv("STARLIGHT_ALT_IMG_ENDPOINT").isNotEmpty()
            ) {
                baseUrl = System.getenv("STARLIGHT_ALT_IMG_ENDPOINT")
                "$baseUrl/$image"
            } else {
                "$baseUrl/$image"
            }
        } else {
            null
        }
    }
}