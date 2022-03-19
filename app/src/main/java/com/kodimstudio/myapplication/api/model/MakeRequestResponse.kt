package com.kodimstudio.myapplication.api.model

data class MakeRequestResponse(
    val job: JobResponse
) {
    data class JobResponse(
        val jobId: Long
    )
}
