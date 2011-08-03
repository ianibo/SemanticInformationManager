package com.k_int.sim

import com.k_int.sip.domain.*
import grails.converters.*

class GormRepositoryService {

    static transactional = true

    def grailsApplication

    // Generate a dynamic template edit for the identified context.
    String generateDynamicEditTemplate(SIPContext ctx) {

      def new_layout = [:]
      new_layout.element_id='tab1'
      new_layout.label=ctx.defaultType
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
                                         control:'assoc_combo',
                                         label:ctx.defaultType+'.'+pprop.name,
                                         property_uri:pprop.name,
                                         cardinality:'1',
                                         type:pprop.typePropertyName,
                                         mandatory:!(pprop.isOptional()),
                                         refTypeURI:"uri:gorm:${pprop.getReferencedDomainClass().fullName}",
                                         displayProps:generateDisplayProps(pprop.getReferencedDomainClass())])
              // log.debug("For fun, ref class, associationMap : ${pprop.getReferencedDomainClass().associationMap}")
            }
            else if ( pprop.manyToMany || pprop.oneToMany ) {
              // Add a new table control, which lists rows from an association and creates a form for adding new entries 
              def coldefs = []
              def refclassname = pprop.getReferencedDomainClass().fullName
              pprop.getReferencedDomainClass().getPersistentProperties().each { rp ->
                coldefs.add([
                             property_uri:rp.name, 
                             label:refclassname+'.'+rp.name])
              }
              new_layout.properties.add([
                                         control:'assoc_list',
                                         label:ctx.defaultType+'.'+pprop.name,
                                         property_uri:pprop.name,
                                         cardinality:'n',
                                         type:pprop.typePropertyName,
                                         mandatory:!(pprop.isOptional()),
                                         cols:coldefs])
            }
            else {
              log.error("Unhandled association type")
            }
          }
          else {
            new_layout.properties.add([control:'text',
                                       label:ctx.defaultType+'.'+pprop.name,
                                       property_uri:pprop.name,
                                       cardinality:'1',
                                       type:pprop.typePropertyName,
                                       mandatory:!(pprop.isOptional())])
          }
        }
      }
      else {
        log.error("Unable to locate domain class ${ctx.defaultType}")
      }

      def converter = new_layout as JSON;
      def json_string = converter.toString()

      return json_string
    }

    /**
     *  Sometimes we need to generate a property list for the target class of an association. This method returns that list.
     */
    def generateDisplayProps(domain_class) {
      def result = []
      domain_class.getPersistentProperties().each { pprop ->
        if ( !pprop.association ) {
          result.add(pprop.name)
        }
      }
      result
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

        new_template.search_columns.add([property:"id",selaction:'__edit'])
        new_template.search_columns.add([property:"class.name"])

        target_class_info.getPersistentProperties().each { pprop ->
          // println "${pprop}..."
          if ( pprop.association ) {
            // So far, only handle scalar types
          }
          else {
            new_template.access_points.add([propname:pprop.name,proptype:pprop.typePropertyName]);
            new_template.search_columns.add([property:pprop.name]);
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
            log.warn("Created new instance of ${new_resource_class_name}")
          }
          else {
            log.warn("Unable to resolve class for name ${new_resource_class_name}")
          }
        }
        else {
          log.warn("Updates not yet supported")
          if ( ! resource_uri.startsWith('_b') ) {  // Don't process blank nodes
            resource = resolveURI(resource_uri)
          }
          else {
            log.warn("Not processing blank node ${resource_uri}")
          }
        }

        // now process property references
        log.debug("Processing resource: ${res.key}")
        log.debug("__metamodel = ${res.value['__metamodel']}")
        res.value.each { prop ->
          log.debug("Processing property ${prop.key} = ${prop.value}")
          if ( prop.key == '__metamodel' ) {
          }
          else {
            // Scalar properties have a value component, which is an array of value components.
            if ( prop.value?.values?.size() > 0 ) {
              def value = prop.value.values[0]
              if ( value.__metamodel.status == 'new' || value.__metamodel.status == 'updated' ) {
                if ( value.value != null ) {
                  log.debug("Setting ${prop.key} to ${value.value}")
                  resource[prop.key] = value.value;
                  log.debug("After set, prop in object is : \"${resource[prop.key]}\"")
                }
                else if ( value.reference != null ) {
                  log.debug("Passed a reference to some other object.. need to process it! ${value.reference}")
                  if ( value.reference.startsWith("_b") ) {
                    log.warn("Reference is to a blank node, not yet implemented!");
                  }
                  else {
                    def linked_resource = resolveURI(value.reference)
                    // log.debug("Setting property ${prop.key} to ${linked_resource}")
                    resource[prop.key] = linked_resource
                  }
                }
              }
            }
          }
        }

        if ( resource != null ) {
          log.debug("Saving resource ${resource}")
          resource.save(flush:true)
          if ( resource.hasErrors() ) {
            resource.errors.each { err ->
              log.warn("err: ${err}")
            }
          }
 
        }
        else {
          log.debug("No resource to save")
        }
      }
    }

    /**
     * List the refdata values for the type referenced
     */
    def list(basetype, retprops=[]) {
      log.debug("GORM: list reference values for ${basetype}")
      def result = []
      if ( basetype.startsWith('uri:gorm:') ) {
        def classname = basetype.substring(9,basetype.length())
        log.debug("Process list for base class : ${classname}")
        def target_class_info = grailsApplication.getArtefact("Domain",classname);
        if ( target_class_info != null ) {
          if ( retprops.length == 0 ) {
            // Just add the identifier
            target_class_info.getClazz().list().each { e ->
              result.add([uri:"uri:gorm:${e.class.name}:${e.id}",display:"uri:gorm:${e.class.name}:${e.id}"]);
            }
          }
          else {
            // Add each of the named properties
            target_class_info.getClazz().list().each { e ->
              def disp_str = retprops.collect { e[it] }.join(' : ') 
              result.add([uri:"uri:gorm:${e.class.name}:${e.id}",display:disp_str]);
            }
          }
        }
      }
      else {
        log.error("Arrived at GORM list method, but uri ${basetype} is not for a gorm type");
      }
      result
    }
 
  def resolveURI(uri) {
    log.debug("resolveURI(${uri})")
    def resolved_object = null

    if ( ( uri == 'uri:sip:null' ) || ( uri == '' ) || ( uri == null ) ) {
      // Reference is to null!
      log.debug("reference to null: ${uri}")      
    }
    else {
      if ( uri.startsWith("uri:gorm:") ) {
        def oid = uri.substring(9,uri.length())
        int first_colon = oid.indexOf(':');
        def classname = oid.substring(0,first_colon)
        def keyvalue = oid.substring(first_colon+1,oid.length());

        def target_class_info = grailsApplication.getArtefact("Domain",classname);

        if ( target_class_info != null ) {
          resolved_object = target_class_info.getClazz().get(keyvalue);
          log.debug("resolve class:${classname} key:${keyvalue}")
        }
        else {
          log.error("Unable to locate refereced domain class ${classname}, key ${keyvalue}")
        }
      }
      else {
        log.warn("Attempting to reference a non-gorm URI object in a GORM datamodel")
      }
    }

    log.debug("resolveURI returning instance of ${resolved_object?.class.name} with ID ${resolved_object?.id}")

    resolved_object
  }

  def getDefaultTemplateFor(uri, user) {
    log.debug("getDefaultTemplateFor(${uri},${user})");
    def target_obj = resolveURI(uri);
    log.debug("Find context for type ${target_obj.class.name}")
    def context = SIPContext.findByDefaultType(target_obj.class.name);

    // Now find the first template for this context which is accessible to the user (Or one thats flagged as default in the template, or the user config)
    def temp = SIPEditTemplate.findByOwner(context);

    if ( temp != null )
      return temp.id

    return null
  }

  def addToGraph(result, uri) {
    log.debug("addToGraph... ${uri}");
    def obj = resolveURI(uri)
    def graph_object = [:]
    result[uri] = graph_object;

    if ( obj != null ) {
      // Get hold of the domain class definition
      def grails_domain_class_info = grailsApplication.getArtefact("Domain", obj.class.name);

      // For each property
      grails_domain_class_info.getPersistentProperties().each { pprop ->
       def ov = obj[pprop.name];
       log.debug("add proprty [${pprop.name}] value is ${ov}");

        if ( ov != null ) {
          // If it's an association property
          if ( pprop.association ) {
            log.debug("proprty ${pprop.name} is an assoc");
            if ( pprop.manyToOne || pprop.oneToOne ) {
              graph_object[pprop.name] = [values: [ [ reference: "uri:gorm:${ov.class.name}:${ov.id}", __metamodel:[status:'ok'] ] ] ];
            }
            else if (pprop.manyToMany || pprop.oneToMany ) {
              log.debug("manyToMany to oneToMany");
              // We are serialising a collection. A number of things need to happen. We treat everything as a list for now.
              def collection_items_list = []
              obj[pprop.name]?.each { collection_item ->
                // If the collection_item is a persistent class, then we simply add a reference to the URI of the resource.
                collection_items_list.add([reference:"uri:gorm:${collection_item.class.name}:${collection_item.id}", __metamodel:[status:'ok']])
                // Otherwise, we need to add a blank node to the graph and serialize the data.
              }
              graph_object[pprop.name] = [values: collection_items_list]
            } else {
              log.debug("Unhandled association type")
            }
          }
          else { // Else add the value
            // Each property is mapped to a map object containing an array of values. For GORM repositoris, these values must be scalar on non association properties
            log.debug("Set proprty [${pprop.name}] value is ${ov}");
            graph_object[pprop.name] = [values: [ [ value: obj[pprop.name], __metamodel:[status:'ok'] ] ] ];
            // else the column was null, don't bother setting.
          }
        }
      }
    }
    else {
      log.error("Problem looking up object with URI ${uri}");
    }
  }

}
