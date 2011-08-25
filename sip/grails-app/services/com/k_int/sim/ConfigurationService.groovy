package com.k_int.sim

import grails.converters.deep.JSON
import grails.plugins.nimble.core.Role

class ConfigurationService implements org.springframework.context.ApplicationContextAware {

  def applicationContext

  def sync() {
    log.debug("ConfigurationService::sync");
      
    def sip_config_folder = applicationContext.getResource("classpath:/sip");
    log.debug("sip_config_folder: ${sip_config_folder}");

	log.debug("sip_config_folder exists : " + sip_config_folder.exists());
	
    if ( sip_config_folder != null && sip_config_folder.exists()) {
      def sip_file = sip_config_folder.getFile();

      if ( sip_file != null ) {
        log.debug("Loading config files from sip config area");
        loadConfigFiles(sip_file);
      }
      else {
        log.warn("Unable to find sip directory");
      }
    }
  }

  def loadConfigFiles(basedir) {
    log.debug("loadConfigFiles ${basedir}");
    if ( basedir.isDirectory() ) {
      log.debug("Processing directory...");
      basedir.listFiles().each { entry ->
        log.debug("Processing ${entry}");
        if ( entry.isFile() ) {
          process(entry);
        }
      }
    }
    else {
      log.debug("... Not a directory: ${basedir.class.name}");
    }
  }

  def process(configfile) {
    log.debug("processing configfile ${configfile}");
    def conf_json = JSON.parse(configfile.text)
    conf_json.roles.each { e ->
      log.debug("Process role entry ${e}");
      log.debug("Add role id: ${e.id} name:${e.name}");
      def r = Role.findByName(e.id)
      if ( r == null ) {
        log.debug("Saving new role");
        r = new Role(name:e.id, description: e.name, protect:true, external:false)
        r.save(flush:true);
        if (r.hasErrors()) {
          r.errors.each {
            log.warn(it)
          }
        }

      }
    }
    conf_json.contexts.each { e ->
      log.debug("Process context entry id: ${e.id}");
    }
  }

  public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
