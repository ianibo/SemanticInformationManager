class BootStrap {

  def init = { servletContext ->
    setup();
  }

  def destroy = {
  }

  def setup() {
    // 1. See if we can locate an object definedby "http://json-schema.org/schema#" and with the id "http://json-schema.org/schema#" in the mongo repository
    // If not, load it.
  }
}
