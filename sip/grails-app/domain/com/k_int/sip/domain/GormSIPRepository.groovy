package com.k_int.sip.domain

import grails.converters.*

class GormSIPRepository extends SIPRepository {

    def grailsApplication

    static constraints = {
    }

    // Generate a dynamic template edit for the identified context.
    String generateDynamicEditTemplate(SIPContext ctx) {

      def new_layout = [:]
      new_layout.element_id='tab1'
      new_layout.tab_name='TabNameForClass'
      new_layout.properties = []

      def target_class_info = grailsApplication.getArtefact("Domain",ctx.defaultType);
      if ( target_class_info != null ) {

        target_class_info.getPersistentProperties().each { pprop ->
          log.debug("${pprop}...")
          if ( pprop.association ) {
            // So far, only handle scalar types
            if ( pprop.manyToOne || pprop.oneToOne ) {
              log.debug("Single reference")
              // In the default dynamic template, we make a choice depending on the cardinality of the target set. If it's less than 20,
              // then send a standard combobox with the necessary values and URI's to related records. If it's greater, send a more complex control
              new_layout.properties.add([
                                         control:'combo',
                                         label:pprop.name,
                                         property_uri:pprop.name,
                                         cardinality:'1',
                                         type:pprop.typePropertyName,
                                         mandatory:!(pprop.isOptional()),
                                         refTypeURI:"uri:gorm:${pprop.getReferencedDomainClass().fullName}"])
              // log.debug("For fun, ref class, associationMap : ${pprop.getReferencedDomainClass().associationMap}")
            }
            else {
              log.debug("m:n or 1:m association")
            }
          }
          else {
            new_layout.properties.add([control:'text',label:pprop.name,property_uri:pprop.name,cardinality:'1',type:pprop.typePropertyName,mandatory:!(pprop.isOptional())])
          }
        }
      }
      else {
        println "Unable to locate domain class"
      }

      def converter = new_layout as JSON;
      def json_string = converter.toString()

      return json_string
    }

    // Generate a dynamic search template for the identified context
    String generateDynamicSearchTemplate(SIPContext ctx) {
      def new_template = [:]
      new_template.element_id='tab1'
      new_template.access_points = []
      new_template.search_columns = []

      def target_class_info = grailsApplication.getArtefact("Domain",ctx.defaultType);
      if ( target_class_info != null ) {

        new_template.access_points.add([propname:"id",proptype:"long"])
        new_template.search_columns.add("id")
        new_template.search_columns.add("class.name")

        target_class_info.getPersistentProperties().each { pprop ->
          // println "${pprop}..."
          if ( pprop.association ) {
            // So far, only handle scalar types
          }
          else {
            new_template.access_points.add([propname:pprop.name,proptype:pprop.typePropertyName]);
            new_template.search_columns.add(pprop.name);
          }
        }
      }

      def converter = new_template as JSON;
      def json_string = converter.toString()

      return json_string
    }

    def processUpdate(model) {

      model.graph.each { res ->

        def resource = null;

        def resource_uri = res.key
        def properties = res.value

        // For each resource in the graph, firstly find out if we are creating a new one, or editing
        // an existing resource.
        if ( properties.__metamodel.status == 'new' ) {
          // Resources in the GORM repository can only have 1 type, they aren't like traditional RDF descriptions in that way.
          def new_resource_class_name = properties.__metamodel.types[0]
          def new_resource_class = grailsApplication.getArtefact("Domain",new_resource_class_name)
          if ( new_resource_class != null ) {
            resource = new_resource_class.newInstance();
            println "Created new instance of ${new_resource_class_name}"
          }
          else {
            println "Unable to resolve class for name ${new_resource_class_name}"
          }
        }
        else {
          println "Updates not yet supported"
        }
        println "Processing resource: ${res.key}"
        println "__metamodel = ${res.value['__metamodel']}"
        res.value.each { prop ->
          println "Processing property ${prop.key} = ${prop.value}"
          if ( prop.key == '__metamodel' ) {
          }
          else {
            if ( prop.value?.values?.size() > 0 ) {
              def value = prop.value.values[0]
              if ( value.__metamodel.status == 'new' || value.__metamodel.status == 'updated' ) {
                println "Setting ${prop.key} to ${value.value}"
                resource[prop.key] = value.value;
                println "After set, prop in object is : \"${resource[prop.key]}\""
              }
            }
          }
        }

        if ( resource != null ) {
          println "Saving resource ${resource}"
          resource.save(flush:true)
          if ( resource.hasErrors() ) {
            resource.errors.each { err ->
              println "err: ${err}"
            }
          }
 
        }
        else {
          println "No resource to save"
        }
      }
    }

    /**
     * List the refdata values for the type referenced
     */
    def list(basetype) {
      log.debug("GORM: list reference values for ${basetype}")
      def result = []
      if ( basetype.startsWith('uri:gorm:') ) {
        def classname = basetype.subString(9,basetype.length)
        println "Process list for base class : ${classname}"
      }
      else {
        log.error("Arrived at GORM list method, but uri ${basetype} is not for a gorm type");
      }
      result
    }
}
