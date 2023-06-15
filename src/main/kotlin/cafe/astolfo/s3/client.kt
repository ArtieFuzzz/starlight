package cafe.astolfo.s3

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.ListObjectsRequest
import aws.smithy.kotlin.runtime.net.Url

class R2Client {
    private var client: S3Client? = null
    var cache: ArrayList<String> = arrayListOf()

    /**
     * This function is required to be executed, if not, populateCache, etc will not work.
     */
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
            cache.add(img.key.toString())
        }
    }

    fun getRandom(alternative: Boolean = false): String? {
        return if (cache.isNotEmpty()) {
            val baseUrl = System.getenv("STARLIGHT_IMG_ENDPOINT")
            val altUrl = System.getenv("STARLIGHT_ALT_IMG_ENDPOINT") ?: ""
            val image = cache.random()

            // Why not?
            return if (alternative && altUrl.isNotEmpty()
            ) {
                "$altUrl/$image"
            } else {
                "$baseUrl/$image"
            }
        } else {
            null
        }
    }
}