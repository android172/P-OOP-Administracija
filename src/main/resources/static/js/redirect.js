window.onload = function(){
	getToken();
	var role = getCookie("role");
	var path = window.location.pathname;
	if(token=="") alert("no token!");
	alert(path);
	/*switch(path){
		case "/student":
			getIndex();
			//alert("redirect: "+path+"?token="+token+"&index="+index);
			window.location.replace(path+"?token="+token+"&index="+index);
		break;

		case "/admin":
		case "/staff":
		case "/users":
		case "/majors":
		case "/students":
		case "/courses":
			//alert("redirect: "+path+"?token="+token);
			window.location.replace(path);
		break;

		default:
			//alert("default");
			window.location.replace("/");
	}*/
	if(token!="")
		window.location.replace("/"+role);
	else
		window.location.replace("/login");
}