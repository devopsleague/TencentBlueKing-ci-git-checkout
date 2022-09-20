/*
 * Tencent is pleased to support the open source community by making BK-CI 蓝鲸持续集成平台 available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-CI 蓝鲸持续集成平台 is licensed under the MIT license.
 *
 * A copy of the MIT License is included in this file.
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.bk.devops.git.core.service.helper.auth

import com.tencent.bk.devops.git.core.constant.GitConstants
import com.tencent.bk.devops.git.core.pojo.GitSourceSettings
import com.tencent.bk.devops.git.core.service.GitCommandManager
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths

abstract class CredentialAuthHelper(
    private val git: GitCommandManager,
    private val settings: GitSourceSettings
) : HttpGitAuthHelper(git = git, settings = settings) {

    companion object {
        private val logger = LoggerFactory.getLogger(CredentialAuthHelper::class.java)
    }

    override fun configureSubmoduleAuth() {
        super.configureSubmoduleAuth()
        val commands = mutableListOf<String>()
        if (git.isAtLeastVersion(GitConstants.SUPPORT_EMPTY_CRED_HELPER_GIT_VERSION)) {
            combinableHost { protocol, host ->
                // 先卸载上一次执行可能没有清理的凭证
                commands.add("git config --unset credential.$protocol://$host/.helper")
                commands.add("git config credential.$protocol://$host/.helper '' ")
            }
        }
        commands.add("git config --unset credential.helper")
        addSubmoduleCommand(commands)
        git.submoduleForeach("${commands.joinToString(";")} || true", settings.nestedSubmodules)
    }

    /**
     * 子类对submodule操作命令
     */
    abstract fun addSubmoduleCommand(commands: MutableList<String>)

    override fun removeSubmoduleAuth() {
        super.removeSubmoduleAuth()
        val commands = mutableListOf<String>()
        if (git.isAtLeastVersion(GitConstants.SUPPORT_EMPTY_CRED_HELPER_GIT_VERSION)) {
            combinableHost { protocol, host ->
                commands.add("git config --unset credential.$protocol://$host/.helper '' ")
            }
            combinableHost { protocol, host ->
                commands.add("git config --remove-section credential.$protocol://$host/.helper '' ")
            }
        }
        commands.add("git config --unset credential.helper")
        commands.add("git config --remove-section credential.helper")
        git.submoduleForeach("${commands.joinToString(";")} || true", settings.nestedSubmodules)
    }

    /**
     * 凭证管理(如mac读取钥匙串,cache读取~/.cache)依赖HOME环境变量，不能覆盖HOME，所以覆盖XDG_CONFIG_HOME
     *
     * 先设置全局的凭证,然后将全局凭证的配置复制到xdg配置中
     */
    fun configureXDGConfig() {
        // 移除全局配置,然后把配置文件复制到xdg_config_home的git/config中，
        // git配置读取顺序是: home->xdg_config_home->~/.gitconfig->.git/config
        val tempHome = git.removeEnvironmentVariable(GitConstants.HOME)
        val gitConfigPath = Paths.get(tempHome!!, ".gitconfig")
        val gitXdgConfigHome = Paths.get(
            System.getProperty("user.home"),
            ".checkout",
            System.getenv(GitConstants.BK_CI_PIPELINE_ID) ?: "",
            System.getenv(GitConstants.BK_CI_BUILD_JOB_ID) ?: ""
        ).toString()
        val gitXdgConfigFile = Paths.get(gitXdgConfigHome, "git", "config")
        FileUtils.copyFile(gitConfigPath.toFile(), gitXdgConfigFile.toFile())
        logger.info(
            "Removing Temporarily HOME AND " +
                "Temporarily overriding XDG_CONFIG_HOME='$gitXdgConfigHome' for fetching submodules"
        )
        // 设置临时的xdg_config_home
        FileUtils.deleteDirectory(File(tempHome))
        git.setEnvironmentVariable(GitConstants.XDG_CONFIG_HOME, gitXdgConfigHome)
    }
}
