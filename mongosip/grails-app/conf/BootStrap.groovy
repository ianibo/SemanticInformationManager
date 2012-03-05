import com.gmongo.GMongo
import org.springframework.core.io.Resource
import org.codehaus.groovy.grails.commons.ApplicationAttributes
import grails.util.GrailsUtil
import grails.converters.*

class BootStrap {


  def init = { servletContext ->
    def ctx = servletContext.getAttribute(ApplicationAttributes.APPLICATION_CONTEXT)
    setup(ctx);
  }

  def destroy = {
  }

  def setup(ctx) {
    def mongo = new com.gmongo.GMongo();
    def db = mongo.getDB("mongosip")

    // Maybe we should cycle through all schemas in schemas dir and add any missing ones?

    Resource r = ctx.getResource("/WEB-INF/schemas");
    def f = r.getFile();
    if ( f.exists() && f.isDirectory() ) {
      log.debug("got schemas dir: ${f}");
      f.listFiles().each { schema_file ->
        log.debug("Checking ${schema_file}");
        // Parse JSON file
        def j = JSON.parse(new java.io.FileReader(schema_file));
        log.debug(j);
      }
    }

    // 1. See if we can locate an object definedby "http://json-schema.org/schema#" and with the id "http://json-schema.org/schema#" in the mongo repository
    // If not, load it.
    // def core_schema = db.schemas.findOne(id:'http://json-schema.org/schema#',definedby:'http://json-schema.org/schema#');
  }
}
