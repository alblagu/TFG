/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var app=angular.module("Busqueda",[]);
app.controller("Busqueda1",function($scope,$http){
	$scope.ejemplares=[];
	$scope.busqueda=getParameterByName("busqueda",window.location);
	if(!isNaN($scope.busqueda)){
		//Busqueda por ISBN
		$http({
			method: 'GET',
			url: 'http://localhost:8080/TFG3/webresources/generic/libros/busqueda/'+$scope.busqueda
		}).then(function successCallback(response) {
			console.log(response);
			$scope.ejemplares=response.data;
 		 }, 
		 function errorCallback(response) {
    			console.log(response);
  		});
	}
	else{ //BUSQUEDA por titulo
		$http({
			method: 'GET',
			url: 'http://localhost:8080/TFG3/webresources/generic/libros/busqueda2/'+$scope.busqueda
		}).then(function successCallback(response) {
			console.log(response);
			$scope.ejemplares=response.data;
 		 }, 
		 function errorCallback(response) {
    			console.log(response);
  		});
	
	}
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

