package com.k_int.sim

import org.jrdf.*;
import org.jrdf.graph.*;

class NewResourceController {

    def index = {
      JRDFFactory jrdfFactory = SortedMemoryJRDFFactory.getFactory();
      Graph graph = jrdfFactory.getGraph();
      GraphElementFactory elementFactory = graph.getElementFactory();
      Node node = elementFactory.createURIReference(URI.create("urn:node"));
      graph.add(node, node, node);
    }

}
