angular.module("prestamos",[])
	.controller("prestamosController", function ($scope, $http ) {
		$scope.usuario=JSON.parse(localStorage.getItem('usuario'));
		
		$scope.prestamos = [];
		$http ({
			method: 'GET',
			url: 'http://localhost:8080/TFG3/webresources/generic/prestamosUsu/122'
		}).then(function successCallback(response) {
			$scope.prestamos = response.data;
		},
			function errorCallback(response) {
			});

		$scope.final=function(indice) {
			if($scope.prestamos.length-1===indice){
				return "bordeBottom";
			}
			else{
				return "otro";
			}
		};
			
})
	.component("prestamos", {
		templateUrl: "./componentes/prestamos/prestamos.html",
		controller: "prestamosController"
	});

