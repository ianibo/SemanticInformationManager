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
      def admin_role = grails.plugins.nimble.core.Role.findByName('Administrator')

      println "admin user: ${admin_user}"

      println "Syncing context models with local GORM domain classes...."

      def gorm_local_repo = GormSIPRepository.findByUri('uri:gorm:localrepo') ?: new GormSIPRepository(uri:'uri:gorm:localrepo', name:'Local Database').save()

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
                               contextName:domainclass.shortName,
                               contextType:'GORM').save(flush:true);

          println "Create new default dynamic search context uri:gorm:${domainclass.fullName}/defaultsearch"
          DynamicSIPSearchTemplate dst = new DynamicSIPSearchTemplate(name:'Default Search',
                                                                      uri:"uri:gorm:${domainclass.fullName}/defaultsearch",
                                                                      owner:ctx).save(flush:true);
          if (dst.hasErrors()) {
            dst.errors.each {
              println(it)
            }
          }


          println "Create new default dynamic edit context uri:gorm:${domainclass.fullName}/defaultedit"
          DynamicSIPEditTemplate det = new DynamicSIPEditTemplate(name:'Default Editor',
                                                                  uri:"uri:gorm:${domainclass.fullName}/defaultedit",
                                                                  owner:ctx).save(flush:true);

          // Grant visibility of this role to the admin user, just to be on the safe side
          // admin_role.
          // grails.plugins.nimble.core.Permission p = new grails.plugins.nimble.core.Permission(target:'uri');
          // p.managed = true (== with grant option?)
          // permissionsService.createPermission(p,role)
          // Set up default search and layout
        }
      }

      // The following will get a list of the actual classes...
      // grailsApplication.getArtefacts("Domain")*.clazz
    }
}
