package com.tencent.bk.devops.git.cache

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.http.server.GitServlet
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.io.IOException
import javax.servlet.http.HttpServletRequest


class CheckoutServer {

    fun start() {
        val repository = createNewRepository()

        populateRepository(repository)

        // Create the JGit Servlet which handles the Git protocol
        val gs = GitServlet()
        gs.setRepositoryResolver { _: HttpServletRequest?, _: String? ->
            repository.incrementOpen()
            repository
        }

        // start up the Servlet and start serving requests
        val server = configureAndStartHttpServer(gs)

        // finally wait for the Server being stopped
        server.join()
    }

    private fun configureAndStartHttpServer(gs: GitServlet): Server {
        val server = Server(8080)
        val handler = ServletHandler()
        server.handler = handler
        val holder = ServletHolder(gs)
        handler.addServletWithMapping(holder, "/*")
        server.start()
        return server
    }

    private fun populateRepository(repository: Repository) {
        // enable pushing to the sample repository via http
        repository.config.setString("http", null, "receivepack", "true")
        Git(repository).use { git ->
            val myfile = File(repository.directory.parent, "testfile")
            if (!myfile.createNewFile()) {
                throw IOException("Could not create file $myfile")
            }
            git.add().addFilepattern("testfile").call()
            println("Added file " + myfile + " to repository at " + repository.getDirectory())
            git.commit().setMessage("Test-Checkin").call()
        }
    }

    private fun createNewRepository(): Repository {
        // prepare a new folder
        val localPath = File.createTempFile("TestGitRepository", "")
        if (!localPath.delete()) {
            throw IOException("Could not delete temporary file $localPath")
        }
        if (!localPath.mkdirs()) {
            throw IOException("Could not create directory $localPath")
        }

        // create the directory
        val repository: Repository = FileRepositoryBuilder.create(File(localPath, ".git"))
        repository.create()
        return repository
    }
}