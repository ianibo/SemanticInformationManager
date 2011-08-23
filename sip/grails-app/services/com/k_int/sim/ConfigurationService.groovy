package com.k_int.sim

class ConfigurationService implements org.springframework.context.ApplicationContextAware {

  def applicationContext

  def sync() {
    log.debug("ConfigurationService::sync");
      
    def sip_config_folder = applicationContext.getResource("classpath:/sip");
    log.debug("sip_config_folder: ${sip_config_folder}");

    if ( sip_config_folder != null ) {
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
      }
    }
    else {
      log.debug("... Not a directory: ${basedir.class.name}");
    }
  }

  public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
