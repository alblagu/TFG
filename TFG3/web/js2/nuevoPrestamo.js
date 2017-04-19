angular.module("app")
	.controller("Prestamo", function ($scope, $http) {
		$scope.dni = "";
		$scope.codigo = "";
		$scope.textoError = "";
		$scope.diasMax = "";
		$scope.fechaHoy = new Date();
		$scope.fechaFin = new Date();
		$scope.fechaFin.setSeconds(14 * 86400); //Añado el tiempo
		$scope.dia = $scope.fechaFin.getDate();
		$scope.mes = $scope.fechaFin.getMonth() + 1;
		$scope.anio = $scope.fechaFin.getFullYear();
		$scope.obtener = function () {
			if ($scope.dni.length === 0 || $scope.codigo.length === 0)
				$scope.textoError = "Los campos no pueden estar vacios";
			else {
				$http({
					method: 'GET',
					url: 'http://localhost:8080/TFG3/webresources/generic/ejemplares/ejemplar/' + $scope.codigo
				}).then(function successCallback(response) {
					$scope.ejemplar = response.data;
					$http({
						method: 'GET',
						url: 'http://localhost:8080/TFG3/webresources/generic/usuario/' + $scope.dni
					}).then(function successCallback(response) {
						$scope.textoError = "";
						$scope.usuario = response.data;
						$scope.avanzar = true;
					},
						function errorCallback(response) {
							$scope.textoError = "No hay ningun usuario con ese dni";
						});
				},
					function errorCallback(response) {
						$scope.textoError = "No hay ningun libro con ese codigo";
					});
			}
		};
		$scope.nuevoPrestamo = function () {
			if ($scope.diasMax === "")
				$scope.diasMax = 0;
			if (isNaN($scope.diasMax) || $scope.diasMax % 1 !== 0)
				$scope.textoError = "Se tiene que añadir un numero o dejarlo vacio";
			else {
				if ($scope.diasMax > 200)
					$scope.textoError = "No se pueden añadir mas de 200 dias";
				else {
					$scope.fechaFin.setSeconds($scope.diasMax*86400); //Añado el tiempo
					var fecha={
						dia:$scope.fechaFin.getDate(),
						mes:$scope.fechaFin.getMonth() + 1,
						anio:$scope.fechaFin.getFullYear()
					};

					$http({
						headers: {
							'Accept': 'application/json',
							'Content-Type': 'application/json'
						},
						method: 'POST',
						url: 'http://localhost:8080/TFG3/webresources/generic/prestamos/'+$scope.dni+"/"+$scope.codigo,
						data: JSON.stringify(fecha)
					}).then(function successCallback(response) {

						console.log(response);
						alert("Se ha añadido un prestamo al ejemplar con el codigo " + $scope.codigo);
						window.location = "http://localhost:8080/TFG3/";
					},
						function errorCallback(response) {
							console.log(response);
							$scope.error = true;
							$scope.textoError = "Ya hay un ejemplar con el codigo introducido";
						});
				}
			}
		};

	});
