angular.module("catalogo",[])
.controller("Catalogo",function($scope,$http){
	
	$http({
				method: 'GET',
				url: 'http://localhost:8080/TFG3/webresources/generic/libros'
			}).then(function successCallback(response) {
				$scope.libros=response.data;	
				console.log($scope.libros.length);
			});

})
.component("catalogo",{
	templateUrl: "./componentes/catalogo/catalogo.html",
	controller: "Catalogo"
});



