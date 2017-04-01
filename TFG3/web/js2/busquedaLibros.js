angular.module("Busqueda", ["barraNavegacion", "prestamos", "piePagina"])
	.controller("Busqueda1", function ($scope, $http) {
		$scope.libros = [];
		$scope.busqueda = getParameterByName("busqueda", window.location);
		$scope.librosVacio = true;

		if (!isNaN($scope.busqueda)) {
			//Busqueda por ISBN
			$http({
				method: 'GET',
				url: 'http://localhost:8080/TFG3/webresources/generic/libros/busqueda/' + $scope.busqueda
			}).then(function successCallback(response) {
				console.log(response);
				$scope.libros = response.data;
				if ($scope.libros.length === 0)
					$scope.librosVacio = false;
			},
				function errorCallback(response) {
					console.log(response);
				});
		} else { //BUSQUEDA por titulo
			$http({
				method: 'GET',
				url: 'http://localhost:8080/TFG3/webresources/generic/libros/busqueda2/' + $scope.busqueda
			}).then(function successCallback(response) {
				console.log(response);
				$scope.libros = response.data;
				if ($scope.libros.length === 0)
					$scope.librosVacio = false; 
			},
				function errorCallback(response) {
					console.log(response);
				});

		}

		$scope.muestraEjemplares = function (indice) {
			window.location = "http://localhost:8080/TFG3/busquedaEjemplares.html?isbn=" + $scope.libros[indice].isbn10;
		};
	});

function getParameterByName(name, url) {
	if (!url) {
		url = window.location.href;
	}
	name = name.replace(/[\[\]]/g, "\\$&");
	var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
		results = regex.exec(url);
	if (!results)
		return null;
	if (!results[2])
		return '';
	return decodeURIComponent(results[2].replace(/\+/g, " "));
}

