package com.k_int.sim

import com.k_int.sip.domain.*

import grails.plugins.nimble.core.LevelPermission
import grails.plugins.nimble.core.Role
import grails.plugins.nimble.core.Group
import grails.plugins.nimble.core.AdminsService
import grails.plugins.nimble.core.UserService


class DomainModelInfoService {

    static transactional = true

    def grailsApplication 

    def sync() {

      // When called, this method needs to iterate the gorm domain classes in the application, and make sure there is a
      // system defined context for that class. The context provides an access point for search and record creation.
      // Only the system administrator should be granted permission to these contexts.
      // Each system level context should have the URI uri:gorm:--fully-qualified-domain-class/context

      println "Looking up admin user..."
      def admin_user = grails.plugins.nimble.core.UserBase.findByUsername('admin')
      println "admin user: ${admin_user}"

      println "Syncing context models with local GORM domain classes...."

      def gorm_local_repo = GormSIPRepository.findByUri('uri:gorm:localrepo') ?: new GormSIPRepository(uri:'uri:gorm:localrepo').save()

      // Neat code from http://stackoverflow.com/questions/2707796/list-of-all-domain-classes-in-grails to list all domain classes
      println "DomainModelInfoService::syn()"
      grailsApplication.getArtefacts("Domain").each { domainclass ->
        println "Process domain class ${domainclass.fullName}"
        def ctxname = "uri:gorm:${domainclass.fullName}/context"
        def ctx = SIPContext.findByContextUri(ctxname)
        if ( ctx == null ) {
          println "Create new sip context for ${ctxname}"
          ctx = new SIPContext(owner:gorm_local_repo,
                               contextUri:ctxname,
                               contextName:ctxname,
                               contextType:'GORM').save();
          // Set up default search and layout
        }
      }

      // The following will get a list of the actual classes...
      // grailsApplication.getArtefacts("Domain")*.clazz
    }
}
