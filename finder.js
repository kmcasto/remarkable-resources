var Client = require('node-rest-client').Client;
var client = new Client();

module.exports = {
	findBobBarker: function(subject){
		return "Bob Barker is the best at " + subject;
	},
	findExpert: function(subject, url){ 

		var thisData = 
			{
			  "statements": [
			    {
			      "statement": "MATCH (n:Person) RETURN n LIMIT 25",
			      "resultDataContents": [
			        "row",
			        "graph"
			      ],
			      "includeStats": true
			    }
			  ]
			}

		var results = ""
		var args = {
			headers: {
				'Accept': "application/json, text/plain, */*",
				'X-stream': true,
				'Content-Type': "application/json; charset=utf-8",
				'X-Ajax-': true,
			},
			data: thisData,
		}

		client.post(url, args, function(data, response){
			console.log(data);
			console.log(response);
			return (data);
		})
	}
}	