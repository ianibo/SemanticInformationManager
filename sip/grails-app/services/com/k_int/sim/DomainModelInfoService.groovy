package com.k_int.sim

class DomainModelInfoService {

    static transactional = true

    def serviceMethod() {
      // Neat code from http://stackoverflow.com/questions/2707796/list-of-all-domain-classes-in-grails to list all domain classes
      // grailsApplication.getArtefacts("Domain")

      // The following will get a list of the actual classes...
      // grailsApplication.getArtefacts("Domain")*.clazz
    }
}
