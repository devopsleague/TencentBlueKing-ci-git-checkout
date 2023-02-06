package com.tencent.bk.devops.git.core.service.handler

import com.tencent.bk.devops.git.core.constant.ContextConstants
import com.tencent.bk.devops.git.core.pojo.GitSourceSettings
import com.tencent.bk.devops.git.core.service.GitCommandManager
import com.tencent.bk.devops.git.core.util.EnvHelper
import org.slf4j.LoggerFactory

class GitLfsHandler(
    private val settings: GitSourceSettings,
    private val git: GitCommandManager
) : IGitHandler {

    companion object {
        private val logger = LoggerFactory.getLogger(GitLfsHandler::class.java)
    }

    override fun doHandle() {
        val startEpoch = System.currentTimeMillis()
        try {
            with(settings) {
                if (!lfs) {
                    return
                }
                logger.groupStart("Fetching lfs")
                if (lfsConcurrentTransfers != null && lfsConcurrentTransfers > 0) {
                    git.config(configKey = "lfs.concurrenttransfers", configValue = lfsConcurrentTransfers.toString())
                }
                EnvHelper.addEnvVariable(
                    key = "BK_CI_GIT_REPO_STATR_LFS_PRUNE",
                    value = "0"
                )
                var canStart = false
                for (i in 1..4) {
                    Thread.sleep(5 * 1000)
                    canStart = EnvHelper.getEnvVariable(
                        key = "BK_CI_GIT_REPO_STATR_LFS_PRUNE"
                    ) == "1"
                    if (canStart) {
                        break
                    }
                }
                if (settings.enableGitLfsClean == true && canStart) {
                    git.tryCleanLfs()
                }
                git.lfsPull(
                    fetchInclude = includeSubPath,
                    fetchExclude = excludeSubPath
                )
                logger.groupEnd("")
            }
        } finally {
            EnvHelper.putContext(
                key = ContextConstants.CONTEXT_LFS_COST_TIME,
                value = (System.currentTimeMillis() - startEpoch).toString()
            )
        }
    }
}
