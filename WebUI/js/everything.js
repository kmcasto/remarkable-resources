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
			                   "statement": "match (p1:Person )-[s1:Emailed {subject:\"" + keyword + "\"}]-(p2:Person)\nwith p1, count(p1) as rels\norder by rels desc\nlimit 15\nreturn p1.name as Name, p1.email as Email, rels as Hits, ID(p1) as ID",
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

function moreMagic(email, id) {
	console.log(email);
	var neoRequest ={
				  "statements": [
					{
					  "statement": "match (p1:Person {email:\"" + email + "\"}) -[e]-(p2:Person)\nreturn distinct(e.subject), count(e) as count order by count desc limit 3",
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
	    success: function(response){buildMoreInfo(response, email, id)},
		failure: function(errMsg) {alert(errMsg);}
	});
	
}

function buildMoreInfo(responseQuery, email, id) {
	console.log("Build more info");
	console.log(responseQuery);
	
	//person = data.results[0].data[0].row[0];
	//console.log(responseQuery.results[0].data[0].length);
	var list = $("#" + id);
	var template = document.getElementById("sample2").innerHTML;
	
	for(var i = 0; i < responseQuery.results[0].data.length; i++) {
		console.log(i);
		var subject1, weight1;
		subject1 = responseQuery.results[0].data[i].row[0];
		weight1 = responseQuery.results[0].data[i].row[1];
		var jsonObjSkill = {
				   		subject: subject1,
				   		weight: weight1
					};
		
		var output = Mustache.render(template, jsonObjSkill);
		//document.write(output);
		list.append(output);
		console.log(output);
	}

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
		var name1, email1, weight1, id1;
		name1 = responseQuery.results[0].data[i].row[0];
		email1 = responseQuery.results[0].data[i].row[1];
		weight1 = responseQuery.results[0].data[i].row[2];
		id1 = responseQuery.results[0].data[i].row[3];
		var jsonObjPerson = {
				   		name: name1,
				   		email: email1,
				   		hits: weight1,
						id: id1
					};
		
		var output = Mustache.render(template, jsonObjPerson);
		//document.write(output);
		list.append(output);
		console.log(output);
	}



}