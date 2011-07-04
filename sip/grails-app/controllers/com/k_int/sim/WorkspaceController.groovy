package com.k_int.sim

import com.k_int.sip.domain.*;

class WorkspaceController {

  def workspaceService

  def getWorkspace() {
    // Return a workspace for authenticatedUser listing the activities available to this user only
    println "Creating workspace definition for ${authenticatedUser}"

    def result = workspaceService.listAccessibleComponents(authenticatedUser);

    result
  }
}
