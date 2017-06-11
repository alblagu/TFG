angular.module("nuevoPrestamo",["barraNavegacion","piePagina","prestamos"])
	.controller("prestamoController", function ($scope, $http) {
		$scope.NUM_MAX_PRESTAMOS=3;
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
		
		$scope.cancelar=function(){
			$scope.avanzar=false;
		};
		$scope.obtener = function () {
			$scope.textoError="";
			if ($scope.dni.length === 0 || $scope.codigo.length === 0){
				$scope.textoError = "Los campos no pueden estar vacios";
			}
			else {
				$http({
					method: 'GET',
					url: 'http://localhost:8080/TFG3/webresources/generic/ejemplares/ejemplar/' + $scope.codigo
				}).then(function successCallback(response) {
					$scope.ejemplar = response.data;
					if(!$scope.ejemplar.disponible){
						$scope.textoError="Ese ejemplar no esta disponible";	
					}
					else{
					$http({
						method: 'GET',
						url: 'http://localhost:8080/TFG3/webresources/generic/usuario/' + $scope.dni
					}).then(function successCallback(response) {
						$scope.usuario = response.data;
						$scope.comprobarMasXPrestamos();
					},
						function errorCallback(response) {
							$scope.textoError = "No hay ningun usuario con ese dni";
						});
					}		
				},
					function errorCallback(response) {
						$scope.textoError = "No hay ningun ejemplar con ese codigo";
					});
				}
			};
		
		
		$scope.comprobarMasXPrestamos=function(){
			$http({
				url: 'http://localhost:8080/TFG3/webresources/generic/prestamosUsu/'+$scope.usuario.dni
		}).then(function successCallback(response) {
			if(response.data.length>=3){
				$scope.textoError = "Ese usuario ya tiene "+$scope.NUM_MAX_PRESTAMOS+", No puede tener mas";
			}
			else{
				$scope.avanzar=true;
			}
			});
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
						alert("Se ha añadido un prestamo al ejemplar con el codigo " + $scope.codigo+" hasta el dia ");
						location.reload();	
					},
						function errorCallback(response) {
							console.log(response);
							$scope.error = true;
							$scope.textoError = "Ya hay un prestamo con el codigo introducido";
						});
				}
			}
		};


	
	});
