angular.module("app")
	.controller("Busqueda1", function ($scope, $http) {
		$scope.cantidadMostrada=2;
		$scope.contador=0;
		$scope.libros = [];
		$scope.libros2  = [];
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
				else{
					$scope.contadorMaximo= parseInt($scope.libros.length/$scope.cantidadMostrada, 10);
					$scope.muestraLibros();
				}
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
				else{
					$scope.contadorMaximo= parseInt($scope.libros.length/$scope.cantidadMostrada, 10);
					$scope.muestraLibros();
				}
			},
				function errorCallback(response) {
					console.log(response);
				});

		}

		$scope.muestraEjemplares = function (indice) {
			window.location = "http://localhost:8080/TFG3/busquedaEjemplares.html?isbn=" + $scope.libros[indice].isbn10;
		};

		$scope.muestraLibros = function (){
			$scope.libros2=[];
			if($scope.contador<$scope.contadorMaximo){
				for(i=0;i<$scope.cantidadMostrada;i++){
					$scope.libros2[i]=$scope.libros[$scope.contador*$scope.cantidadMostrada+i];	
				}
				$scope.contador++;
			}
			else{
				var j=0;
				for(i=$scope.contador*$scope.cantidadMostrada;i<$scope.libros.length;i++){
					$scope.libros2[j]=$scope.libros[i];
					j++;
				}
				$scope.desactivado=true;
			}
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

