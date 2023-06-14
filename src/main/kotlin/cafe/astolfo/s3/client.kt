package cafe.astolfo.s3

import aws.sdk.kotlin.services.s3.*
import aws.sdk.kotlin.services.s3.model.*
import aws.smithy.kotlin.runtime.net.Url

class R2Client {
    private var client: S3Client? = null
    private var cache: ArrayList<String> = arrayListOf()

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

    fun getRandom(): String? {
        return if (cache.isNotEmpty()) {
            val baseUrl = System.getenv("STARLIGHT_IMG_ENDPOINT")
            val image = cache.random()

            return "$baseUrl/$image"
        } else {
            null
        }
    }
}