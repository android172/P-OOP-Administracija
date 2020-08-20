function checkRedirect(){
	getToken();

	if(isNaN(token))
		window.location.replace("/login");

	if(token != "") {
		makeRequest("/access_allowed",[],function(response){
			if(!response){
				window.location.replace("/login");
			}
		});
	}else{
		window.location.replace("/login");
	}
}