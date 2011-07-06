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

        def repo_info = [id:repo.id, name:repo.name, contexts:[]]
        
    
        // For each repository, list all the contexts available to the identified user in that repository
        SIPContext.findAllByOwner(repo).each{ c ->
          println "Adding context ${c.contextName} : ${c.contextUri}"
          def ctx_info = [name:c.contextName, searchtemplates:[], edittemplates:[]]
          repo_info.contexts.add(ctx_info)

          println "Adding search templates"
          // Add all the search templates this user can see for this context
          SIPSearchTemplate.findAllByOwner(c).each { sst ->
            println "${sst}"
            ctx_info.searchtemplates.add([id:sst.id,name:sst.name,uri:sst.uri,type:sst.class.name,id:sst.id])
          }

          println "Adding edit templates ${SIPEditTemplate.list().size()}"
          // Add each record edit template this user can see for this context
          SIPEditTemplate.findAllByOwner(c).each { set ->
            println "${set}"
            ctx_info.edittemplates.add([id:set.id,name:set.name,uri:set.uri,type:set.class.name,id:set.id])
          }
        }

        result.availableRepositories.add(repo_info)
      }

      println 'listAccessibleComponents complete'

      result
    }
}
