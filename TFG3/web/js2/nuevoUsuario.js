angular.module("nuevoUsuario", ["barraNavegacion", "piePagina", "prestamos"])
	.controller("Usuario1", function ($scope, $http) {
		$scope.nombre = "";
		$scope.apellidos = "";
		$scope.dni = "";
		$scope.password = "";
		$scope.telefono = "";
		$scope.errorDNIrepetido = false;
		$scope.errorNombre = false;
		$scope.errorApellidos = false;
		$scope.errorDNI = false;
		$scope.errorPassword = false;
		$scope.errorTelefono = false;

		$scope.textoErrorDNIRepetido = "";

		$scope.errores = function () {
			$scope.errorDNIrepetido = false;
			$scope.errorNombre = false;
			$scope.errorApellidos = false;
			$scope.errorDNI = false;
			$scope.errorPassword = false;
			$scope.errorTelefono = false;
			
			if ($scope.nombre.length === 0) {
				$scope.errorNombre = true;
			}
			if ($scope.apellidos.length === 0) {
				$scope.errorApellidos = true;
			}
			if ($scope.dni.length === 0) {
				$scope.errorDNI = true;
			}
			if ($scope.password.length < 8) {
				$scope.errorPassword = true;
			}
			if ($scope.telefono.length !== 9 || isNaN($scope.telefono)||$scope.telefono%1!==0) {
				$scope.errorTelefono=true;
			}

			return ($scope.errorNombre||$scope.errorApellidos||$scope.errorDNI||$scope.errorPassword||$scope.errorTelefono)?false:true;
		};

		$scope.nuevoUsuario = function () {

			if ($scope.errores()) {

				var usuario = {
					dni: $scope.dni,
					password: $scope.password,
					nombre: $scope.nombre,
					apellidos: $scope.apellidos,
					telefono: $scope.telefono
				};
				$http({
					headers: {
						'Accept': 'application/json',
						'Content-Type': 'application/json'
					},
					method: 'POST',
					url: 'http://localhost:8080/TFG3/webresources/generic/usuarios',
					data: JSON.stringify(usuario)
				}).then(function successCallback(response) {
					alert("Usuario Añadido");
					location.reload();
				},
					function errorCallback(response) {
						$scope.errorDNIRepetido = true;
						$scope.textoErrorDNIRepetido = "Ya hay un usuario con ese dni";
					});
			}
		};
	});


