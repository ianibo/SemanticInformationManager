package com.k_int.sim

import com.k_int.sip.domain.*

class WorkspaceService {

    static transactional = true

    def serviceMethod() {
    }

    // List the workspace components available to the given user
    def listAccessibleComponents(user) {
      println "listing accessible workspace components for ${user}"

      def result = [:]

      result.availableRepositories = []

      // List each of the available repositories
      SIPRepository.findAll().each { repo ->
        println "Adding repo: ${repo.uri}"

        def repo_info = [name:repo.name, contexts:[]]
        
    
        // For each repository, list all the contexts available to the identified user in that repository
        SIPContext.findAllByOwner(repo).each{ c ->
          println "Adding context ${c.contextName} : ${c.contextUri}"
          def ctx_info = [name:c.contextName]
          repo_info.contexts.add(ctx_info)
        }

        result.availableRepositories.add(repo_info)
      }

      println 'listAccessibleComponents complete'

      result
    }
}
