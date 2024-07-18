package com.tencent.bk.devops.git.core.pojo

data class GitMetricsInfo(
    val atomCode: String,
    val projectId: String,
    val pipelineId: String,
    val buildId: String,
    val taskId: String,
    val scmType: String,
    val url: String,
    val projectName: String,
    val startTime: String,
    val endTime: String,
    val costTime: Long,
    val prepareCostTime: Long,
    val initCostTime: Long,
    val submoduleCostTime: Long,
    val lfsCostTime: Long,
    val fetchCostTime: Long,
    val checkoutCostTime: Long,
    val logCostTime: Long,
    val authCostTime: Long,
    val fetchStrategy: String,
    val errorType: Int? = 0,
    val errorCode: Int? = null,
    val errorMessage: String? = null,
    val status: String? = null,
    val cacheDownloadCostTime: Long,
    val cacheDownloadResult: String,
    val errorInfo: String,
    val transferRate: Double,
    val totalSize: Double,
    val authHelper: String,
    val gitVersion: String,
    val osType: String,
    val jobType: String?,
    val channel: String,
    val invalidRef: Int = 0,
    val vmExistRepo: String,
    val devcloudDataCached: String
)
