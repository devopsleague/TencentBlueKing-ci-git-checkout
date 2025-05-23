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

package com.tencent.bk.devops.git.core.enums

import com.tencent.bk.devops.git.core.constant.ContextConstants.CONTEXT_REPOSITORY_TYPE
import com.tencent.bk.devops.git.core.i18n.GitErrorsText
import com.tencent.bk.devops.git.core.pojo.api.RepositoryType
import com.tencent.bk.devops.git.core.util.EnvHelper
import com.tencent.bk.devops.plugin.pojo.ErrorType

@SuppressWarnings("LongParameterList")
enum class GitErrors(
    val regex: Regex,
    val title: String?,
    val cause: String?,
    val solution: String?,
    val errorType: ErrorType = ErrorType.USER,
    val errorCode: Int = 800001,
    val wiki: String?
) {
    // fetch命令错误
    AuthenticationFailed(
        regex = Regex(
            "(The requested URL returned error: 403)|" +
                "(fatal: could not read Username for '(.+)': terminal prompts disabled)|" +
                "(fatal: could not read Password for '(.+)': terminal prompts disabled)|" +
                "(fatal: Authentication failed for '(.+)')|" +
                "(fatal: Authentication failed)|" +
                "(fatal: '(.+)' 鉴权失败)|" +
                "(fatal: could not read Username for '(.+)': No such device or address)|" +
                "(fatal: could not read Password for '(.+)': No such device or address)|" +
                "(error: The requested URL returned error: 401 Unauthorized while accessing .*)|" +
                "(fatal: unable to access '(.+)': The requested URL returned error: 403)|" +
                "(error: The requested URL returned error: 401 while accessing (.+))|" +
                "(致命错误：could not read Username for '(.+)': terminal prompts disabled)"
        ),
        title = GitErrorsText.get().httpAuthenticationFailed,
        cause = GitErrorsText.get().httpAuthenticationFailedCause,
        solution = if (EnvHelper.getContext(CONTEXT_REPOSITORY_TYPE) == RepositoryType.URL.name) {
            GitErrorsText.get().httpAuthenticationFailedUrlSolution
        } else {
            GitErrorsText.get().httpAuthenticationFailedRepositorySolution
        },
        errorCode = 800002,
        wiki = GitErrorsText.get().httpAuthenticationFailedWiki
    ),

    SshAuthenticationFailed(
        regex = Regex(
            "(fatal: Could not read from remote repository.)|" +
                "(fatal: 无法读取远程仓库。)"
        ),
        title = GitErrorsText.get().sshAuthenticationFailed,
        cause = GitErrorsText.get().sshAuthenticationFailedCause,
        solution = GitErrorsText.get().sshAuthenticationFailedSolution,
        errorCode = 800003,
        wiki = GitErrorsText.get().sshAuthenticationFailedWiki
    ),

    RepositoryNotFoundFailed(
        regex = Regex(
            "(fatal: repository '(.+)' not found)|" +
                "(fatal: .* Git repository not found)|" +
                "(fatal: 远程错误：Git repository not found)|" +
                "(ERROR: Repository not found)|" +
                "(fatal: remote error: Git:Project not found.)|" +
                "(fatal: 远程错误：Git:Project not found.)"
        ),
        title = GitErrorsText.get().repositoryNotFoundFailed,
        cause = GitErrorsText.get().httpAuthenticationFailedCause,
        solution = if (EnvHelper.getContext(CONTEXT_REPOSITORY_TYPE) == RepositoryType.URL.name) {
            GitErrorsText.get().httpAuthenticationFailedUrlSolution
        } else {
            GitErrorsText.get().httpAuthenticationFailedRepositorySolution
        },
        errorCode = 800004,
        wiki = GitErrorsText.get().httpAuthenticationFailedWiki
    ),

    RemoteServerFailed(
        regex = Regex(
            "(fatal: (the|The) remote end hung up unexpectedly)|" +
                    "(fatal: unable to access '(.+)': The requested URL returned error: 502)|" +
                    "(fatal: 无法访问 '(.+)'：The requested URL returned error: 502)|" +
                    "(fatal: 远程错误：Internal server error)|" +
                    "(fatal: 远端意外挂断了)|" +
                    "(Git:Server is busy, please try again later)|" +
                    "(fatal: unable to access '(.+)': Failed to connect to (.+): Host is down)|" +
                    "(error: RPC failed; curl 56 Recv failure: Connection reset by peer)|" +
                    "(fatal: 过早的文件结束符（EOF）)|" +
                    "(fatal: index-pack 失败)|" +
                    "(fatal: early EOF)|" +
                    "(fatal: index-pack failed)|" +
                    "(fatal: unable to access '(.+)': The requested URL returned error: 429)|" +
                    "(error: RPC 失败。curl 18 transfer closed with outstanding read data remaining)|" +
                    "(error: RPC failed; curl 18 transfer closed with outstanding read data remaining)|" +
                    "(fatal: 协议错误：坏的包头)|" +
                    "(fatal: protocol error: bad pack header)|" +
                    "(fatal: 远程错误：(.+) unavailable)|" +
                    "(错误：RPC 失败。HTTP 500 curl 22 The requested URL returned error: 500)|" +
                    "(error: RPC failed; HTTP 500 curl 22 The requested URL returned error: 500)|" +
                    "(fatal: remote error: (.*)too many request, your request was forbidden(.*))|" +
                    "(fatal: 远程错误：(.*)too many request, your request was forbidden(.*))|" +
                    "(致命错误: remote error: (.*)too many request, your request was forbidden(.*))|" +
                    "(致命错误：远程错误：(.*)too many request, your request was forbidden(.*))"
        ),
        title = GitErrorsText.get().remoteServerFailed,
        cause = GitErrorsText.get().remoteServerFailedCause,
        solution = GitErrorsText.get().remoteServerFailedSolution,
        errorType = ErrorType.THIRD_PARTY,
        errorCode = 800005,
        wiki = GitErrorsText.get().remoteServerFailedWiki
    ),
    ConnectionTimeOut(
        regex = Regex(
            "ssh: connect to host (.+) port (\\d+): Connection timed out"
        ),
        title = GitErrorsText.get().connectionTimeOut,
        cause = GitErrorsText.get().connectionTimeOutCause,
        solution = GitErrorsText.get().connectionTimeOutSolution,
        errorCode = 800006,
        wiki = GitErrorsText.get().connectionTimeOutWiki
    ),

    // checkout命令错误
    NoMatchingBranch(
        regex = Regex(
            pattern = "(fatal: couldn't find remote ref .+)|" +
                "(fatal: 无法找到远程引用 .+)|" +
                "(Your configuration specifies to merge with the ref '(.+)')|" +
                "(您的配置中指定要合并远程的引用 '(.+)')|" +
                "(fatal: '(.+)' is not a commit and a branch '(.+)' cannot be created from it)|" +
                "(fatal: '.+' 不是一个提交，不能基于它创建分支 '.+')|" +
                "(error: pathspec '(.+)' did not match any file\\(s\\) known to git\\.)|" +
                "(error: 路径规格 '(.+)' 未匹配任何 git 已知文件)|" +
                "(fatal: path '(.+)' does not exist .+)|" +
                "(fatal: 路径 '(.+)' 不存在)|" +
                "(fatal: 引用不是一个树：(.+))|" +
                "(fatal: reference is not a tree: (.+))|" +
                "(error: 请求的上游分支 '(.+)' 不存在)|" +
                "(fatal: 不能同时更新路径并切换到分支'(.+)'。)|" +
                "(fatal: 不能同时更新路径并切换到分支'(.+)'。\\s+您是想要检出 '(.+)' 但其未能解析为提交么？)|" +
                "(fatal: Cannot update paths and switch to branch '(.+)' at the same time.)",
            options = setOf(RegexOption.IGNORE_CASE)
        ),
        title = GitErrorsText.get().noMatchingBranch,
        cause = GitErrorsText.get().noMatchingBranchCause,
        solution = GitErrorsText.get().noMatchingBranchSolution,
        errorCode = 800007,
        wiki = GitErrorsText.get().noMatchingBranchWiki
    ),
    EmptyBranch(
        regex = Regex(
            "(fatal: You are on a branch yet to be born)|" +
                "(fatal: 您位于一个尚未初始化的分支)"
        ),
        title = GitErrorsText.get().emptyBranch,
        cause = GitErrorsText.get().emptyBranchCause,
        solution = GitErrorsText.get().emptyBranchSolution,
        errorCode = 800008,
        wiki = GitErrorsText.get().emptyBranchWiki
    ),
    SparseCheckoutLeavesNoEntry(
        regex = Regex(
            "(error: Sparse checkout leaves no entry on working directory)"
        ),
        title = GitErrorsText.get().sparseCheckoutLeavesNoEntry,
        cause = GitErrorsText.get().sparseCheckoutLeavesNoEntryCause,
        solution = GitErrorsText.get().sparseCheckoutLeavesNoEntrySolution,
        errorCode = 800009,
        wiki = GitErrorsText.get().sparseCheckoutLeavesNoEntryWiki
    ),
    BranchOrPathNameConflicts(
        regex = Regex(
            "(fatal: '(.+)' 既可以是一个本地文件，也可以是一个跟踪分支。)"
        ),
        title = GitErrorsText.get().branchOrPathNameConflicts,
        cause = GitErrorsText.get().branchOrPathNameConflictsCause,
        solution = GitErrorsText.get().branchOrPathNameConflictsSolution,
        errorCode = 800010,
        wiki = GitErrorsText.get().branchOrPathNameConflictsWiki
    ),

    // merge命令错误
    MergeConflicts(
        regex = Regex(
            "(Automatic merge failed; fix conflicts and then commit the result.)|" +
                "(Automatischer Merge fehlgeschlagen; beheben Sie die Konflikte und committen Sie dann das Ergebnis)|" +
                "(Resolve all conflicts manually, mark them as resolved with)|" +
                "(自动合并失败，修正冲突然后提交修正的结果。)|" +
                "(error: add_cacheinfo 无法刷新路径 '.+'，合并终止。)|" +
                "(error: add_cacheinfo failed to refresh for path '.+'; merge aborting.)"
        ),
        title = GitErrorsText.get().mergeConflicts,
        cause = GitErrorsText.get().mergeConflictsCause,
        solution = GitErrorsText.get().mergeConflictsSolution,
        errorCode = 800011,
        wiki = GitErrorsText.get().mergeConflictsWiki
    ),
    InvalidMerge(
        regex = Regex(
            "(merge: (.+) - not something we can merge)|" +
                "(merge：.+ - 不能合并)|" +
                "(fatal: .+ - not something we can merge)"
        ),
        title = GitErrorsText.get().invalidMerge,
        cause = GitErrorsText.get().invalidMergeCause,
        solution = GitErrorsText.get().invalidMergeSolution,
        errorCode = 800012,
        wiki = GitErrorsText.get().invalidMergeWiki
    ),
    CannotMergeUnrelatedHistories(
        regex = Regex(
            "(fatal: refusing to merge unrelated histories)|" +
                "(fatal: 拒绝合并无关的历史)"
        ),
        title = GitErrorsText.get().cannotMergeUnrelatedHistories,
        cause = GitErrorsText.get().cannotMergeUnrelatedHistoriesCause,
        solution = GitErrorsText.get().cannotMergeUnrelatedHistoriesSolution,
        errorCode = 800013,
        wiki = GitErrorsText.get().cannotMergeUnrelatedHistoriesWiki
    ),
    LocalChangesOverwritten(
        regex = Regex(
            "(error: Your local changes to the following files would be overwritten by merge:)|" +
                "(error: Your local changes to the following files would be overwritten by checkout:)|" +
                "(error: 您对下列文件的本地修改将被检出操作覆盖：)"
        ),
        title = GitErrorsText.get().localChangesOverwritten,
        cause = GitErrorsText.get().localChangesOverwrittenCause,
        solution = GitErrorsText.get().localChangesOverwrittenSolution,
        errorCode = 800014,
        wiki = GitErrorsText.get().localChangesOverwrittenWiki
    ),

    // submodule命令错误
    NoSubmoduleMapping(
        regex = Regex(
            "(fatal: No submodule mapping found in .gitmodules for path '(.+)')|" +
                "(fatal: 在 .gitmodules 中未找到子模组路径 '(.+)' 的 url)|" +
                "(fatal: 在 .gitmodules 中没有发现路径 '(.+)' 的子模组映射)|" +
                "(fatal: 在 .gitmodules 中未找到子模组 '(.+)' 的 url)|" +
                "(fatal: No url found for submodule path '(.+)' in .gitmodules)|" +
                "(No url found for submodule path '(.+)' in .gitmodules)"
        ),
        title = GitErrorsText.get().noSubmoduleMapping,
        cause = GitErrorsText.get().noSubmoduleMappingCause,
        solution = GitErrorsText.get().noSubmoduleMappingSolution,
        errorCode = 800015,
        wiki = GitErrorsText.get().noSubmoduleMappingWiki
    ),
    SubmoduleRepositoryDoesNotExist(
        regex = Regex(
            pattern = "(clone of '.+' into submodule path '(.+)' failed)|" +
                "(fatal：无法克隆 '(.+)' 到子模组路径 '(.+)')",
            options = setOf(RegexOption.IGNORE_CASE)
        ),
        title = GitErrorsText.get().submoduleRepositoryDoesNotExist,
        cause = GitErrorsText.get().submoduleRepositoryDoesNotExistCause,
        solution = GitErrorsText.get().submoduleRepositoryDoesNotExistSolution,
        errorCode = 800016,
        wiki = GitErrorsText.get().submoduleRepositoryDoesNotExistWiki
    ),
    InvalidSubmoduleSHA(
        regex = Regex(
            "(Fetched in submodule path '(.+)', but it did not contain (.+). " +
                "Direct fetching of that commit failed.)|" +
                "(获取了子模组路径 '(.+)'，但是它没有包含 (.+)。直接获取该提交失败。)|" +
                "(无法在子模组路径 '(.+)' 中找到当前版本)|" +
                "(fatal: Needed a single revision)|" +
                "(fatal: Unable to find current revision in submodule path '.+')"
        ),
        title = GitErrorsText.get().invalidSubmoduleSHA,
        cause = GitErrorsText.get().invalidSubmoduleSHACause,
        solution = GitErrorsText.get().invalidSubmoduleSHASolution,
        errorCode = 800017,
        wiki = GitErrorsText.get().invalidSubmoduleSHAWiki
    ),

    // lfs命令错误
    LfsAttributeDoesNotMatch(
        regex = Regex(
            "The .+ attribute should be .+ but is .+"
        ),
        title = GitErrorsText.get().lfsAttributeDoesNotMatch,
        cause = GitErrorsText.get().lfsAttributeDoesNotMatchCause,
        solution = GitErrorsText.get().lfsAttributeDoesNotMatchSolution,
        errorCode = 800018,
        wiki = GitErrorsText.get().lfsAttributeDoesNotMatchWiki
    ),
    ErrorDownloadingObject(
        regex = Regex(
            "(Error downloading object: .*)|" +
                "(LFS: Repository or object not found: .+)"
        ),
        title = GitErrorsText.get().errorDownloadingObject,
        cause = GitErrorsText.get().errorDownloadingObjectCause,
        solution = GitErrorsText.get().errorDownloadingObjectSolution,
        errorCode = 800019,
        wiki = GitErrorsText.get().errorDownloadingObjectWiki
    ),
    LfsNotInstall(
        regex = Regex(
            "(git: 'lfs' is not a git command. See 'git --help'.)|" +
                "(git：'lfs' 不是一个 git 命令。参见 'git --help'。)"
        ),
        title = GitErrorsText.get().lfsNotInstall,
        cause = GitErrorsText.get().lfsNotInstallCause,
        solution = GitErrorsText.get().lfsNotInstallSolution,
        errorCode = 800020,
        wiki = GitErrorsText.get().lfsNotInstallWiki
    ),

    // 其他命令错误
    LockFileAlreadyExists(
        regex = Regex(
            "(Another git process seems to be running in this repository, e.g.)|" +
                "(error: could not lock config file (.+): File exists)|" +
                "(error: could not lock config file .+: 文件已存在)|" +
                "(fatal: 无法创建 '(.+)'：文件已存在。)|" +
                "(error: 无法创建 '(.+)'：文件已存在。)|" +
                "(错误：无法创建 '(.+)'：文件已存在。)"
        ),
        title = GitErrorsText.get().lockFileAlreadyExists,
        cause = GitErrorsText.get().lockFileAlreadyExistsCause,
        solution = GitErrorsText.get().lockFileAlreadyExistsSolution,
        errorCode = 800021,
        wiki = GitErrorsText.get().lockFileAlreadyExistsWiki
    ),
    BadRevision(
        regex = Regex(
            "fatal: bad revision '(.*)'"
        ),
        title = GitErrorsText.get().badRevision,
        cause = GitErrorsText.get().badRevisionCause,
        solution = GitErrorsText.get().badRevisionSolution,
        errorCode = 800022,
        wiki = GitErrorsText.get().badRevisionWiki
    ),
    NotAGitRepository(
        regex = Regex(
            "fatal: [Nn]ot a git repository \\(or any of the parent directories\\): .git"
        ),
        title = GitErrorsText.get().notAGitRepository,
        cause = GitErrorsText.get().notAGitRepositoryCause,
        solution = GitErrorsText.get().notAGitRepositorySolution,
        errorCode = 800023,
        wiki = GitErrorsText.get().notAGitRepositoryWiki
    ),
    LibcurlNotSupportHttps(
        regex = Regex(
            "fatal: unable to access '(.*)': Protocol \"https\" not supported or disabled in libcurl"
        ),
        title = GitErrorsText.get().libcurlNotSupportHttps,
        cause = GitErrorsText.get().libcurlNotSupportHttpsCause,
        solution = GitErrorsText.get().libcurlNotSupportHttpsSolution,
        errorCode = 800024,
        wiki = GitErrorsText.get().libcurlNotSupportHttpsWiki
    ),
    GitNotInstall(
        regex = Regex(
            "(Cannot run program \"git\" \\(in directory (.*): CreateProcess error=2, " +
                    "The system cannot find the file specified\\))|" +
                    "('git' 不是内部或外部命令，也不是可运行的程序)|" +
                    "(Cannot run program \"git\" \\(in directory (.*)\\): CreateProcess error=2, 系统找不到指定的文件。)|" +
                    "(Cannot run program \"git\" \\(in directory (.*)\\): error=2, 没有那个文件或目录)"
        ),
        title = GitErrorsText.get().gitNotInstall,
        cause = GitErrorsText.get().gitNotInstallCause,
        solution = GitErrorsText.get().gitNotInstallSolution,
        errorCode = 800025,
        wiki = GitErrorsText.get().gitNotInstallWiki
    ),
    NotFoundGitRemoteHttps(
        regex = Regex(
            "(fatal: 无法为 'http' 找到远程助手)|" +
                "(git: 'remote-https' is not a git command. See 'git --help'.)"
        ),
        title = GitErrorsText.get().notFoundGitRemoteHttps,
        cause = GitErrorsText.get().notFoundGitRemoteHttpsCause,
        solution = GitErrorsText.get().notFoundGitRemoteHttpsSolution,
        errorCode = 800026,
        wiki = GitErrorsText.get().notFoundGitRemoteHttpsWiki
    ),
    InvalidRefSpec(
        regex = Regex(
            "(fatal: 无效的引用规格：'(.*)')|" +
                    "(fatal: invalid refspec '(.*)')"
        ),
        title = GitErrorsText.get().invalidRefSpec,
        cause = GitErrorsText.get().invalidRefSpecCause,
        solution = GitErrorsText.get().invalidRefSpecSolution,
        errorCode = 800027,
        wiki = GitErrorsText.get().invalidRefSpecWiki
    ),
    MergeStashFail(
        regex = Regex(
            "(fatal: stash failed)|" +
                    "(fatal: 贮藏失败)"
        ),
        title = null,
        cause = null,
        solution = null,
        errorCode = 800028,
        wiki = null
    )
    ;

    companion object {
        fun matchError(message: String): GitErrors? {
            for (gitError in values()) {
                if (gitError.regex.matches(message)) {
                    return gitError
                }
            }
            return null
        }
    }
}
