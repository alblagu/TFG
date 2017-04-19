angular.module("app")
.controller("Busqueda1",function($scope,$http){
	$scope.ejemplares=[];
	$scope.isbn=getParameterByName("isbn",window.location);
		$http({
			method: 'GET',
			url: 'http://localhost:8080/TFG3/webresources/generic/ejemplares/'+$scope.isbn
		}).then(function successCallback(response) {
			$scope.ejemplares=response.data;
 		 }, 
		 function errorCallback(response) {
  		});
		$http({
			method: 'GET',
			url: 'http://localhost:8080/TFG3/webresources/generic/libro/'+$scope.isbn
		}).then(function successCallback(response) {
			$scope.libroBuscado=response.data;
 		 }, 
		 function errorCallback(response) {
  		});
});

	function getParameterByName(name, url) {
    		if (!url) {
      			url = window.location.href;
    		}
    		name = name.replace(/[\[\]]/g, "\\$&");
    		var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        	results = regex.exec(url);
    		if (!results) return null;
    		if (!results[2]) return '';
    		return decodeURIComponent(results[2].replace(/\+/g, " "));
}

