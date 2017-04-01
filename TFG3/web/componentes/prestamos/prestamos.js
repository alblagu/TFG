angular.module("prestamos", [])
	.controller("prestamosController", function ($scope, $http) {
		$scope.prestamos = [];
		$http ({
			method: 'GET',
			url: 'http://localhost:8080/TFG3/webresources/generic/prestamosUsu/122'
		}).then(function successCallback(response) {
			console.log(response);
			$scope.prestamos = response.data;
		},
			function errorCallback(response) {
			console.log(response);
			});
})
	.component("prestamos", {
		templateUrl: "./componentes/prestamos/prestamos.html",
		controller: "prestamosController"
	});

