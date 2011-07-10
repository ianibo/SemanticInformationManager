
function makeSearchForm(access_points_list_element_id, 
                        results_div_element_id, 
                        base_template_uri, 
                        target_repository_id, root_types) {
  var template=loadSearchTemplateFrom(base_template_uri);

  var ap_list_element = $(access_points_list_element_id);
  var results_div = $(results_div_element_id);
  searchLayout(template,ap_list_element,results_div);
}

function loadSearchTemplateFrom(template_uri) {
  console.log("Loading search template "+template_uri);
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

function searchLayout(template, access_points_list_element, results_div_element) {

  for ( prop_idx in template.access_points ) {
    var prop = template.access_points[prop_idx];
    access_points_list_element.append("<li>"+prop+":<input type=\"text\" name=\""+prop+"\"</li>")
  }

  for ( prop_idx in template.search_columns ) {
    var prop = template.search_columns[prop_idx];
    results_div_element.append("<li>"+prop+"</li>")
  }

}

