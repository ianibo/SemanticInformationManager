@Grapes([
    @Grab(group='org.jrdf',module='jrdf',version='0.5.6.3')
])

import org.jrdf.*;
import org.jrdf.graph.*;
import org.jrdf.writer.*;

println "Starting jrdf create graph tests"
JRDFFactory jrdfFactory = SortedMemoryJRDFFactory.getFactory();
Graph graph = jrdfFactory.getGraph();
GraphElementFactory elementFactory = graph.getElementFactory();

def dc_title_property = URI.create("http://purl.org/dc/elements/1.1/title")

Resource res = elementFactory.createResource(URI.create("urn:uuid:${java.util.UUID.randomUUID().toString()}"));
res.addValue(dc_title_property,"Fishy-1")
res.addValue(dc_title_property,"Fishy-2")
res.addValue(dc_title_property,"Fishy-3")
res.addValue(dc_title_property,"Fishy-4")
// Node node = elementFactory.createURIReference(URI.create("urn:node"));
// Node dc_title_property = elementFactory.createURIReference(URI.create(""));
// graph.add(node, node, node);

println "\n\nWriting RDF/XML"
new org.jrdf.writer.rdfxml.MemRdfXmlWriter().write(graph,System.out)

println "\n\nWriting ntriples"
try {
  new org.jrdf.writer.ntriples.NTriplesWriterImpl().write(graph, System.out)
}
catch ( Exception e ) {
  e.printStackTrace()
}
finally {
  println "done"
}

println "\n\nCompleted jrdf create graph tests"




// Reading RDF using JRDF
//
// in = new FileInputStream("MYRDF.rdf");
//  final Graph jrdfMem = JRDF_FACTORY.getGraph();
//  Parser parser = new GraphRdfXmlParser(jrdfMem, new MemMapFactory());
//  parser.parse(in, toEscapedString(new URL("http://foo.bar.org/foo#")));
//  ClosableIterable<Triple> triples = jrdfMem.find(
//    ANY_SUBJECT_NODE, ANY_PREDICATE_NODE, ANY_OBJECT_NODE);
//  for(Triple t: triples) {
//    .....
//  }
//  triples.close();
