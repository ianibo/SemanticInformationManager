package com.k_int.sim

class DataController {

  def index = { }

  def list = {
    def result = []
    println "Listing for type: ${params.typeUri}"

    if ( params.repo != null ) {

      // Action needs to be driven by base repo - Each repo needs to understand how to handle references to it's own type.
      def repo = SIPRepository.get(params.repo)
      if ( repo != null ) {
        // Located repository, now ask the repository to deal with the list request
        result = repo.list(params.typeUri)
      }
    }

    result
  }
}
