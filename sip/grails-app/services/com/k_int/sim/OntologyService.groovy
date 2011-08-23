package com.k_int.sim

class OntologyService {

    static transactional = true
    def domainModelInfoService
    def configurationService

    def sync() {
      println "OntologyService::sync"
      domainModelInfoService.sync()
      configurationService.sync()
    }
}
