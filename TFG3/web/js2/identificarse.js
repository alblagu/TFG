angular.module("identificarse",[])	
	.controller("IdentificarseController", function ($scope,  $http) {
		$scope.dni = "";
		$scope.password = "";
		$scope.textoError = "";

		$scope.identificarse = function () {
			if ($scope.dni.length === 0 || $scope.password.length === 0)
				$scope.textoError = "No puede haber campos vacios";
			else {
				$http({
					method: 'GET',
					url: 'http://localhost:8080/TFG3/webresources/generic/usuario/' + $scope.dni
				}).then(function successCallback(response) {
					$scope.usuario=response.data;
					if($scope.password!==$scope.usuario.password)
						$scope.textoError="La contraseña es incorrecta";
					else{
						localStorage.setItem('usuario', (JSON.stringify($scope.usuario || {})));
						window.location='http://localhost:8080/TFG3/inicio.html';
					}
				},
					function errorCallback(response) {
						$scope.textoError = "No hay ningun usuario con ese dni";
					});
			}
		};

	});

