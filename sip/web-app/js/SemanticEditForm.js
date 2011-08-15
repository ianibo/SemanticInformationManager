

/* -> A model is an object instance with some links to view data
 *   model :
 *      __metamodel
 *      property:1 {
 *        __metamodel: {
 *          property_value_containers:[]
 *          num_value_controls :
 *        }
 *        values: [
 *          [ value: thevalue, __metamodel: 'ok'|'updated' ]
 *                ]
 *      }
 *      property:2
 *      property:3
 *      property:n
 *
 *  A property can be scalar or a collection
 *  Scalar properties can be values (int,str,...) or reference
 *  Reference properties can be expanded as needed, with their related data hanging off the graph tree
 *  all properties have the ability to store a graph URI - Not used for relational backends
 */
var the_model = {

  // Information about the model
  __metamodel : {},

  // Target repository
  __target_repository_id : 0,

  // A counter for producing blank nodes
  __blank_node_counter : 0,

  // The URI of the root node in the graph.. Essentially, the node we are editing
  __root_graph_uri : "",

  // Contains all the graphs related to this model
  __graphmap : {},

  // Pointer to the json definition of the template as loaded from the server
  __template : {},

  // Counter to easily create new unique control identifiers
  __form_control_counter : 0,

  // Handy quick access to the base URL for the server side
  __base_url : "",

  __popup_form_counter : 0,

  __popup_form_info : {}
}

//var general_type_layout = {
//  element_id : 'general_info_panel',
//  tab_name : 'General',
//  properties : [
//    { label : 'Title', property_uri : 'dc.title', cardinality:'m' },
//    { label : 'Description', property_uri : 'dc.description' , cardinality:'1'}
//  ]
//}


function newBlankNode() {
  return "_b"+(the_model.__blank_node_counter++);
}

// Turn the div identified by editor_id into a semantic editing form
// This function is to be used for the creation of new descriptions rather than editing existing ones
function makeSIMEditor(editor_id, 
                       base_template_uri, 
                       base_url, 
                       target_repository_id, 
                       target_uri, 
                       root_types) {

  var template = loadTemplateFrom(base_template_uri);
  the_model.__template = template;

  // Get hold of the element
  root_element = $( editor_id )

  // Make the root element into a tabs control
  var tab_control = root_element.tabs();

  // For now, we are going to assume we are creating a new record
  the_model.__base_url = base_url;

  the_model.__target_repository_id = target_repository_id;

  if ( target_uri ) {
    console.log("target_uri is present, load object");
    the_model.__root_graph_uri = target_uri;
    importGraph(target_repository_id, the_model.__graphmap, target_uri);
    the_model.__graphmap[the_model.__root_graph_uri].__metamodel = {status:"ok", types:root_types};
  }
  else {
    console.log("no target_uri, go directly to create new");
    the_model.__root_graph_uri = newBlankNode();
    the_model.__graphmap[the_model.__root_graph_uri] = {};
    the_model.__graphmap[the_model.__root_graph_uri].__metamodel = {status:"new", types:root_types};
  }

  var general_info_panel = buildFormPanel(template, 
                                          root_element, 
                                          the_model.__root_graph_uri,
                                          target_repository_id)

  // Add the generic details tab
  // Working from http://jqueryui.com/demos/tabs/#...immediately_select_a_just_added_tab
  // and http://blog.favrik.com/2009/08/11/dynamically-adding-jquery-tabs-round2/
  // tab_control.tabs('add','#'+general_type_layout.element_id,general_type_layout.tab_name);
  tab_control.tabs('add','#'+template.element_id,template.label);

  //tab_control.tabs('add','#default',"Default Properties");
  //tab_control.tabs('add','#t1',"Default Properties1");
  //tab_control.tabs('add','#t2',"Default Properties2");
  //tab_control.tabs('add','#t3',"Default Properties3");
}


// Paint the template for the General Tab
function buildFormPanel(layout_definition, 
                        parent_element, 
                        root_object_uri,
                        target_repository_id) {

  // the class here is copied from the page generated by a static tab. It may change with versions of JQuery I guess, so beware!
  var p = parent_element.append("<div id=\""+layout_definition.element_id+"\" class=\"ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide\"></div>");
  var new_table = $(document.createElement('table'));

  p.append(new_table);

  var parent_context = "";

  var i=0;

  for ( p in layout_definition.properties ) {

    // The definition of this property within the layout for this panel
    var propdef = layout_definition.properties[p];

    // The path to the property -in the model-. Initially, depth of 1, this is the property uri
    // Later on, ns:prop[4].ns:subprop  (subprop within the 4th instance of prop)
    var property = propdef.property_uri;

    // Make sure we have an object for this property in the model
    var root_object = the_model.__graphmap[root_object_uri];
    var property_model = root_object[property];

    // If this is the first time we have seen this property, create a new empty metamodel (Will contain info about the values and the binding to the controls)
    if ( property_model == null ) {
      console.log("Create new property model for resource:"+root_object_uri+" property:"+property);
      property_model = {
        '__metamodel' : {
          property_value_containers : [],
          num_value_controls : 1
        },
        'values' : [
        ]
      };
      root_object[property] = property_model;
    }
    else {
      console.log("Model for resource "+root_object_uri+" already contains info for property "+property);
      // Check if we need to initialise the metamodel (Which we will need to do if this graph has been loaded from the server)
      if ( ! property_model.__metamodel ) {
        console.log("Initialise metamodel for property defn");
        property_model.__metamodel = {
          property_value_containers : [],
          num_value_controls : 1
        };
      }
    }

    var new_ul = $(document.createElement('ul'));
    var new_td = $(document.createElement('td'));

    // Add a property to the control so it knows where the binding info in the metamodel is
    // new_ul.setAttribute('data-metamodel',property_model);
    // new_ul.get(0).setAttribute('data-metamodel','test');

    // Add this control (parent container) to the list of controls for this property
    property_model.__metamodel.property_value_containers.push(new_ul);

    new_td.append(new_ul);

    // Append a row to the table for every property. Each row will support muiltiple values if cardinality says so
    var mandatory_flag = "";
    if ( propdef.mandatory == true )
      mandatory_flag="*";

    new_table.append($('<tr>').append("<td style=\"vertical-align: top; text-align:right\"><label for=\""+base_property_path+"["+i+"]\">"+propdef.label+mandatory_flag+"</label></td>",new_td));

    // Work out the root of the property name (The path to an array of values)
    var base_property_path = parent_context+propdef.property_uri

    // For each property, see if there is a value in the model, if so, output an appropriate control
    // For(blah...)

    // Finally, output an empty control to act as a "Next" value (If permitted by cardinality rules)
    // keydown to capture deletes etc keypress for only sensible keys
    if ( propdef.control == 'text' ) {
      createTextControl(new_ul,propdef,root_object_uri,i,p,property_model.values);
    }
    else if ( propdef.control == 'assoc_combo' ) {
      createAssocComboControl(new_ul,propdef,root_object_uri,target_repository_id,i,p,property_model.values);
    }
    else if ( propdef.control == 'assoc_list' ) {
      createAssocListControl(new_ul,propdef,root_object_uri,target_repository_id,i,p,property_model.values)
    }
    else {
      var cc = new_ul.append("<li>Currently unhandled control type \""+propdef.control+"\" label: "+propdef.label+" of type "+propdef.type+"</li>");
    }

    //var input_control = cc.find('input');
    //input_control.get(0).setAttribute("data-metamodel",property_model);

    // parent_element.append("</ul></td></tr>");
  }

  parent_element.append("</table>");

  // var data = $('<div id="'+tab_id+'"></div>').append(tab_content);
  // this.tabs.append(data).tabs('add', '#' + tab_id, tab_name);
  // this.tabs.tabs('select', '#' + tab_id);


}

function scalarUpdated(control,defidx,mandatory,cardinality) {

  // alert("updateModel "+control.dataset["data-property-path"]);
  var resource_uri = control.getAttribute("data-resource-uri");
  var data_property_path = control.getAttribute("data-property");
  var resource = the_model.__graphmap[resource_uri];
  var metamodel = resource[data_property_path];
  var control_index = parseInt(control.getAttribute('data-property-idx'));

  console.log("scalarUpdated res:"+resource_uri+" prop:"+data_property_path+" idx:"+control_index);
  console.log("old value: "+metamodel.values[control_index]+" new value: "+control.value);

  console.log("metamodel for selected prop %o",metamodel);

  // alert("path:"+data_property_path+" metamodel:"+metamodel+" idx:"+control_index+" count:"+metamodel.__metamodel.property_value_containers.length+" value:"+control.value);

  if ( ( control_index >= metamodel.values.length ) && ( control.value.length > 0 ) ) {
    // We are setting a value on a property which has not been set before. Need to create a new entry in the values array
    var new_value_info = {
      value:"",
      __metamodel:{
        status:"new"
      }
    };
    metamodel.values.push(new_value_info);
  }

  var value_info = metamodel.values[control_index];

  if ( value_info.__metamodel.status != "new" )
    value_info.__metamodel.status = "updated"

  value_info.value = control.value;

  console.log("scalarupdate - control index "+control_index+", len %d",metamodel.__metamodel.num_value_controls);

  // Have we typed in to the last control in the list? If so, append a new blank control for the user to use when adding another value.. IF cardinality > 1
  if ( ( control_index+1 == metamodel.__metamodel.num_value_controls ) &&
       ( metamodel.values.length < cardinality ) ) {
    console.log("Add new control....")
    addScalarControl(resource_uri, metamodel, data_property_path);
  }
  else {
    console.log(""+control_index+"+1 != "+metamodel.__metamodel.num_value_controls+" do not add "+(control_index+1));
  }
}

function assocComboChanged(control,defidx,mandatory,cardinality) {

  var resource_uri = control.getAttribute("data-resource-uri");
  var data_property_path = control.getAttribute("data-property");
  var resource = the_model.__graphmap[resource_uri];
  var metamodel = resource[data_property_path];
  var control_index = parseInt(control.getAttribute('data-property-idx'));

  console.log("assocComboChanged res:"+resource_uri+" prop:"+data_property_path+" idx:"+control_index);
  console.log("old value: "+metamodel.values[control_index]+" new value: "+control.value);
  console.log("metamodel for selected prop %o",metamodel);

  if ( ( control_index >= metamodel.values.length ) && ( control.value.length > 0 ) ) {
    // We are setting a value on a property which has not been set before. Need to create a new entry in the values array
    var new_value_info = {
      reference:control.value,
      __metamodel:{
        status:"new"
      }
    };
    metamodel.values.push(new_value_info);
  }
  else {
    alert("Not implemented - set reference value.. please fix!");
  }

  var value_info = metamodel.values[control_index];

  if ( value_info.__metamodel.status != "new" )
    value_info.__metamodel.status = "updated"

  value_info.reference = control.value;
}



function loadTemplateFrom(template_uri) {
  console.log("Loading template "+template_uri);
  var template
  $.ajax({
    async:false,
    url: template_uri,
    success: function(data){
      template=data;
    }
  });
  return template;
}

function sendFormData(url) {

  // Send the model to the resource update service.

  var clean_model = {
    target_repository_id : the_model.__target_repository_id,
    graph : {}
  };

  // We need to walk the list of objects in the model, and create a clean update to send to the server.
  // The model as it exists in here is full of functions and circular references. 

  console.log("create clean model %o", the_model.__graphmap);

  for ( resource_uri in the_model.__graphmap ) {
    console.log("resource: "+resource_uri);

    var resource_data = the_model.__graphmap[resource_uri]

    var clean_resource = {}

    for ( property_uri in resource_data ) {
      var property_info = resource_data[property_uri];
      if ( property_uri == '__metamodel' ) {
        clean_resource['__metamodel'] = property_info;
      }
      else {
        clean_resource[property_uri] = {};
        clean_resource[property_uri].values = property_info.values;
      }
    }

    clean_model.graph[resource_uri] = clean_resource;
  }

  $.ajax({
    type: 'POST',
    async: false,
    url: url,
    data: {model: JSON.stringify(clean_model)},
    success: function(result) {
    },
    error: function(result) {
      alert("error");
    }
  });

}

// We are adding a control for a property. Because each property can appear in multiple locations
// we iterate over each occurence of this property in the layout. (These will be the parent <li> elements)
// and we add a control to each occurence of this property in the form.
function addScalarControl(resource_uri, 
                          propdef, 
                          property) {
  var i = propdef.__metamodel.num_value_controls++;

  // For each panel where a control appears for this property...
  for ( idx in propdef.__metamodel.property_value_containers ) {
    // Get hold of the container element for each occurence of this property on a layout
    var c = propdef.__metamodel.property_value_containers[idx];
    var new_control_id = "fc"+(the_model.__form_control_counter++);

    c.append("<li><input id=\""+new_control_id+"["+idx+
                                       "]\"  data-resource-uri=\""+resource_uri+
                                       "\" data-property=\""+property+
                                       "\" data-property-idx=\""+i+
                                       "\" onkeyup=\"scalarUpdated(this);\" type=\"text\"/>["+i+"]</li>")
  }
}

function createTextControl(parent_element,
                           propdef,
                           root_object_uri,
                           i,
                           p,
                           values) {
  // new_control_id used to be just propdef.property_uri but decided an opaque generated id is better.
  var new_control_id = "fc"+(the_model.__form_control_counter++);
  var value = ""

  console.log("create control, values = %o",values);

  if ( values.length > 0 ) {
    value=values[0].value;
    console.log("Setting value to "+value);
  }
  else {
    console.log("values.length <= 0");
  }

  var cc = parent_element.append("<li><input id=\""+new_control_id+"["+i+
                                       "]\" data-resource-uri=\""+root_object_uri+
                                        "\" data-property=\""+propdef.property_uri+
                                        "\" data-property-idx=\""+i+
                                        "\" data-propdef-idx=\""+p+
                                        "\" onkeyup=\"scalarUpdated(this,"+p+","+propdef.mandatory+","+propdef.cardinality+");\" type=\"text\""+
                                        " value=\""+value+"\"/>[0]</li>");
  return cc;
}

/**
 * An association combo control represents a reference to another object (Which can also be in the graph, or can just be a full uri reference.
 * For DB schemas, this is essentially a foreign key reference.
 */
function createAssocComboControl(parent_element,
                                 propdef,
                                 root_object_uri,
                                 target_repository_id,
                                 i,
                                 p,
                                 values) {

  // new_control_id used to be just propdef.property_uri but decided an opaque generated id is better.
  var new_control_id = "fc"+(the_model.__form_control_counter++);
  var ref = ""
  if ( values.length > 0 ) {
    ref=values[0].reference;
  }

  var cc = parent_element.append("<li><select id=\""+new_control_id+
                                          "\" data-resource-uri=\""+root_object_uri+
                                          "\" data-property=\""+propdef.property_uri+
                                          "\" data-property-idx=\""+i+
                                          "\" data-propdef-idx=\""+p+
                                          "\" value=\""+ref+
                                          "\" onchange=\"assocComboChanged(this,"+p+","+propdef.mandatory+","+propdef.cardinality+");\"></select></li>");

  var select_control = $("#"+new_control_id);

  // Having created the select control, populate it with data from the data/list service
  populateAssocCombo(target_repository_id,propdef.refTypeURI,select_control,propdef.displayProps);

  if ( ref != "" ) {
    console.log("Setting reference for %o combo to "+ref, select_control);
    select_control.val(ref);
    // select_control.selectedIndex = 1;
  }

  return cc;
}

function populateAssocCombo(repository, type_uri, target_combo, display_props) {
  var url = the_model.__base_url+"data/list?repo="+repository+"&typeUri="+type_uri+"&displayProps="+display_props;

  // Add a not-set which will be default, at least for now
  target_combo.append("<option value=\"uri:sip:null\">Not set</option>");

  $.ajax({
    type: 'GET',
    async: false,
    url: url,
    success: function(result) {
      // Add options for each entry in the results section
      for ( i in result ) {
        target_combo.append("<option value=\""+result[i].uri+"\">"+result[i].display+"</option>");      
      }
    },
    error: function(result) {
      alert("error");
    }
  });


}

function createAssocListControl(parent_element,
                                propdef,
                                root_object_uri,
                                target_repository_id,
                                i,
                                p,
                                values) {

  var table_control_id = "fc"+(the_model.__form_control_counter++);
  var cc = parent_element.append("<li>New assoc_list \""+propdef.control+"\" label: "+propdef.label+" of type "+propdef.type+
                                 "<table><thead><tr id=\""+table_control_id+"_h\"><th>child uri</th></tr></thead><tbody id=\""+table_control_id+"_b\"></tbody></table>"+
                                 "</li>");
  var headers = $("#"+table_control_id+"_h")
  var tbody = $("#"+table_control_id+"_b")

  for ( c in propdef.cols ) {
    var coldef = propdef.cols[c];
    headers.append("<th>"+coldef.label+"</th>");
  }

  for ( v in values ) {
    var value = values[v];
    tbody.append("<tr><td>"+value.reference+"</td></tr>");
  }

  // We now add a footer row with a button to pop up an add dialog (Search for rows / create)
  tbody.append("<tr id=\""+table_control_id+"_f\"><td colspan=\""+propdef.cols.length+"\"><button type=\"button\" id=\""+table_control_id+"_button\">Add</button></td></tr>");

  var popupDivJQ=$(document.createElement("div"));
  popupDivJQ.attr("id",""+table_control_id+"_popup");
  popupDivJQ.attr("class","popup");

  var popup_info_id = "p"+(the_model.__popup_form_counter++);
  var popup_info = {};
  the_model.__popup_form_info[popup_info_id] = popup_info;

  // Table for input fields
  // JQuery data table for results.
  var input_tableJQ = $(document.createElement("table"));
  var input_table_theadJQ = $(document.createElement("thead"));
  var input_table_tbodyJQ = $(document.createElement("thead"));
  var label_rowJQ = $(document.createElement("tr"));
  var input_rowJQ = $(document.createElement("tr"));

  // Input controls array is a list of controls that are used to form a request to the data service.
  // Each value is passed both as a parameter and a result column to the data service.
  var input_controls = [];

  popup_info.control_array = input_controls;
  popup_info.propdef = propdef;
  popup_info.display_props = "";
  popup_info.root_object_uri = root_object_uri;

  // Append a blank node at the start of the table.. For data rows this will hold the "Add" link.
  label_rowJQ.append("<td></td>");
  input_rowJQ.append("<td></td>");

  // Create the form at the top of the popup that will be used to search, or to create new rows
  for ( c in propdef.cols ) {
    var coldef = propdef.cols[c];
    label_rowJQ.append("<td>"+coldef.label+"</td>");

    // Add the property for this column to the list of props to display.
    if (popup_info.display_props.length > 0 )
      popup_info.display_props = popup_info.display_props + ",";

    popup_info.display_props = popup_info.display_props + coldef.property_uri;

    var new_controlJQ = null;

    switch ( coldef.type ) {
      case 'string':
        // input_row.append("<td><input type=\"text\"/></td>");
        new_controlJQ = $(document.createElement("input"));
        new_controlJQ.attr("type","text");
        new_controlJQ.attr("onkeyup","popupControlsChanged('"+popup_info_id+"');");
        input_controls.push(new_controlJQ);
        break;
      case 'boolean':
        // input_row.append("<td><input type=\"checkbox\"/></td>");
        new_controlJQ = $(document.createElement("input"));
        new_controlJQ.attr("type","checkbox");
        new_controlJQ.attr("value","true");
        new_controlJQ.attr("onchange","popupControlsChanged('"+popup_info_id+"');");
        input_controls.push(new_controlJQ);
        break;
      default:
        new_controlJQ = $(document.createElement("div"));
        new_controlJQ.html(coldef.type);
        break;
    }
    var new_tdJQ = $(document.createElement("td"));
    new_tdJQ.append(new_controlJQ);
    input_rowJQ.append(new_tdJQ);
  }
  
  input_table_theadJQ.append(label_rowJQ);
  input_table_theadJQ.append(input_rowJQ);

  input_tableJQ.append(input_table_theadJQ);
  input_tableJQ.append(input_table_tbodyJQ);

  //var results_gridJQ = $(document.createElement("div"));
  //results_gridJQ.attr("id",table_control_id+"_resultsgrid");

  popup_info.results_tbodyJQ = input_table_tbodyJQ;

  popupDivJQ.append(input_tableJQ);
  
  // add row popup
  var table_add_dialog = popupDivJQ.dialog({
              autoOpen: false,
              title: "Add Row Popup Dialog",
              closeOnEscape: true,
              modal: true,
              width: "90%",
              beforeClose: function(event, ui) {
              }
  });

  $('#'+table_control_id+'_button').click(function() {
    table_add_dialog.dialog('open');
    return false;
  });

  // Final step is to paint the text boxes that will act as the search / create controls
}

function popupControlsChanged(popup_id) {
  var popup_info = the_model.__popup_form_info[popup_id];
  var msg = "Changes to popup "+popup_id;

  for ( c in popup_info.control_array ) {
    var ctrl = popup_info.control_array[c];
    switch ( ctrl.attr("type") ) {
      case "text":
        msg = msg + " : " + ctrl.val();
        break;
      case "checkbox":
        if ( ctrl.attr("checked") ) {
          msg = msg + " : true ";
        }
        else {
          msg = msg + " : false ";
        }
        break;
    }
  }

  msg = msg + "<br/> Search for: " + popup_info.propdef.refTypeURI;

  // Clear down any previous results... Really we should be filling out a jquery datagrid or something here
  popup_info.results_tbodyJQ.empty();

  performPopupSearch(the_model.__target_repository_id,
                     popup_info.propdef.refTypeURI,
                     popup_info.propdef.property_uri,
                     popup_info.root_object_uri,
                     popup_info.results_tbodyJQ,
                     popup_info.display_props);

  // Form the query we will send off to the data service
  // popup_info.results_gridJQ.html(msg);

}

// parent_tbody is the parent table body to which the data rows should be appended.
function performPopupSearch(repository, 
                            type_uri, 
                            property_uri, 
                            root_object_uri,
                            parent_tbodyJQ, 
                            display_props) {

  var url = the_model.__base_url+"data/qry?repo="+repository+"&typeUri="+type_uri+"&displayProps="+display_props;

  // Add a not-set which will be default, at least for now
  // target_combo.append("<option value=\"uri:sip:null\">Not set</option>");

  $.ajax({
    type: 'GET',
    async: false,
    url: url,
    success: function(result) {
      // Add options for each entry in the results section
      for ( i in result ) {
        var new_trJQ = $(document.createElement("tr"));
        for ( val in result[i] ) {
          if ( val == 'uri' ) {
            // Link actions
            new_trJQ.append("<td><a href=\"javascript:linkObjectToCollection('"+result[i][val]+"','"+root_object_uri+"','"+property_uri+"');\">Add</a></td>");
          }
          else {
            new_trJQ.append("<td>"+result[i][val]+"</td>");
          }
        }
        parent_tbodyJQ.append(new_trJQ);
      //  target_combo.append("<option value=\""+result[i].uri+"\">"+result[i].display+"</option>");      
    
      }
    },
    error: function(result) {
      alert("error");
    }
  });
}

function linkObjectToCollection(resource_to_add_uri, target_resource_uri, target_property_uri) {
  alert("link "+resource_to_add_uri+" to collection identified by property "+target_property_uri+" of resource "+target_resource_uri);
  // Check that the item is not already in the collection
  // Add it if present
  // Call method to display (Which will cause the new item graph to be loaded)
}

function importGraph(repository, graphmap, target_uri) {
  var url = the_model.__base_url+"data/graph?repo="+repository+"&uri="+target_uri;
  console.log("get graph "+url);

  $.ajax({
    type: 'GET',
    async: false,
    url: url,
    success: function(result) {
      // alert("Loaded graph for "+url);      
      console.log("loading graph %o",result);
      for (var uri in result) {
        the_model.__graphmap[uri] = result[uri]
      }
      console.log("OK");
      console.log("Loaded graph model: %o",the_model);
    },
    error: function(result) {
      alert("importGraph error for "+url);
    }
  });
}
