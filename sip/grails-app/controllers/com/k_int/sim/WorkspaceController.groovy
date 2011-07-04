package com.k_int.sim

import com.k_int.sip.domain.*;

class WorkspaceController {

    def index = { }

  def getWorkspace() {
    // Return a workspace for authenticatedUser listing the activities available to this user only
    println "Creating workspace definition for ${authenticatedUser}

    def result = [:]

    result.availableRepositories = []

    // List each of the available repositories
    SIPReposiory.findAll().each { repo ->
      println "Adding repo: ${repo.uri}
    }
    
    result
  }
}
