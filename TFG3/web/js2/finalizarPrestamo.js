angular.module("finalizarPrestamo",["barraNavegacion","piePagina","prestamos","busqueda","catalogo"])
	.controller("prestamoController", function ($scope, $http) {
		$scope.NUM_MAX_PRESTAMOS=3;
		$scope.dni = "";
		$scope.codigo = "";
		$scope.textoError = "";
		$scope.avanzar=false;
		

	
		$scope.cancelar=function(){
			$scope.avanzar=false;
		};
		$scope.obtenerPrestamo = function () {
			$scope.textoError="";
			if ($scope.dni.length === 0 || $scope.codigo.length === 0){
				$scope.textoError = "Los campos no pueden estar vacios";
			}
			else {
				$http({
					method: 'GET',
					url: 'http://localhost:8080/TFG3/webresources/generic/prestamo/' + $scope.codigo+'/'+$scope.dni
				}).then(function successCallback(response) {
					$scope.prestamo=response.data;
					$scope.avanzar=true;
					},
						function errorCallback(response) {
						$scope.textoError = "No hay ningun prestamo en activo con esos datos";
						});
					}		
			};
		
		

	

		
		$scope.nuevoPrestamo = function () {
			$scope.textoErrorFecha="";
			if($scope.fechaFin===null){
				$scope.textoErrorFecha="Seleccione una fecha";
			}
			else{
				if($scope.fechaFin.getDay()===6||$scope.fechaFin.getDay()===0){
					$scope.textoErrorFecha="El prestamo no puede acabar en finde semana";		
				}
				else{
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

						alert("Se ha añadido un prestamo al ejemplar con el codigo " + $scope.codigo+" hasta el dia ");
						location.reload();	
					});
				}
			}	
		};


	});
