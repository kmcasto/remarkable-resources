$(document).ready(function() {
	console.log('hello');
});

function magic() {
	// $("keyword")
	var keyword = $("#keyword").val();
	console.log(keyword);
	var neoRequest = {
			  "statements": [
			                 {
			                   "statement": "match (p1:Person )-[s1:Emailed {subject:\"" + keyword + "\"}]-(p2:Person)\nwith p1, count(p1) as rels\norder by rels desc\nlimit 5\nreturn p1.name as Name, p1.email as Email, rels as Hits",
			                   "resultDataContents": [
			                     "row",
			                     "graph"
			                   ],
			                   "includeStats": true
			                 }
			               ]
			             };
	
	var url = "http://192.168.1.138:7474/db/data/transaction";
	$.ajax(url, {
	    data : JSON.stringify(neoRequest),
	    contentType : 'application/json',
	    type : 'POST',
	    crossDomain: true,
	    success: function(response){buildPage(response)},
		failure: function(errMsg) {alert(errMsg);}
	});

}

function buildPage(responseQuery) {
	
	console.log("Build Page");
	console.log(responseQuery);
	//person = data.results[0].data[0].row[0];
	//console.log(responseQuery.results[0].data[0].length);
	var list = $("#results");
	var template = document.getElementById("sample").innerHTML;
	
	for(var i = 0; i < responseQuery.results[0].data.length; i++) {
		console.log(i);
		var name1, email1, weight1;
		name1 = responseQuery.results[0].data[i].row[0];
		email1 = responseQuery.results[0].data[i].row[1];
		weight1 = responseQuery.results[0].data[i].row[2];
		var jsonObjPerson = {
				   		name: name1,
				   		email: email1,
				   		hits: weight1
					};
		
		var output = Mustache.render(template, jsonObjPerson);
		//document.write(output);
		list.append(output);
		console.log(output);
	}



}