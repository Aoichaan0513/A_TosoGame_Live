package jp.aoichaan0513.A_TosoGame_Live.Utils

import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class HttpConnection {

    var result: String = ""

    constructor(url: String, headers: Headers = Headers.Builder().build()) {
        val client = OkHttpClient.Builder().build()
        val res = client.newCall(Request.Builder().url(url).headers(headers).get().build()).execute()

        this.result = res.body?.string() ?: ""
        res.close()
    }

    constructor(url: String, json: String, headers: Headers = Headers.Builder().build()) {
        val client = OkHttpClient.Builder().build()
        val res = client.newCall(Request.Builder().url(url).headers(headers).post(json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())).build()).execute()

        this.result = res.body?.string() ?: ""
        res.close()
    }

    constructor(url: String, jsonObject: JSONObject, headers: Headers = Headers.Builder().build()) : this(url, jsonObject.toString(), headers)
    constructor(url: String, jsonArray: JSONArray, headers: Headers = Headers.Builder().build()) : this(url, jsonArray.toString(), headers)
}