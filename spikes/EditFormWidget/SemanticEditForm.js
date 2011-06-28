
function makeSIMEditor(editor_id) {
  alert("Making "+editor_id+" a Semantic Information Editor Form");
  // Get hold of the element
  root_element = $( editor_id )
  // Make the root element into a tabs control
  alert("1");
  var tab_control = root_element.tabs();
  alert("2");
  // Add the generic details tab
  // Working from http://jqueryui.com/demos/tabs/#...immediately_select_a_just_added_tab
  //tab_control.tabs('add','#default',"Default Properties");
  //tab_control.tabs('add','#t1',"Default Properties1");
  //tab_control.tabs('add','#t2',"Default Properties2");
  //tab_control.tabs('add','#t3',"Default Properties3");
  alert("Done");
}
