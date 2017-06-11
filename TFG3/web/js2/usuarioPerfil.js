angular.module("usuarioPerfil", ["barraNavegacion","prestamos"])
	.controller("UsuarioPerfilController", function ($scope, $http) {
	
	$scope.dni= getParameterByName("dni", window.location);
	$scope.usuario;
	$scope.errorNombre=false;
	$scope.errorDNI=false;
	$scope.errorContrasena1=false;
	$scope.errorContrasena2=false;
	$scope.errorContrasena3=false;
	$scope.nombre="";
	$scope.dni2="";
	$scope.contrasenaAntigua="";
	$scope.contrasenaNueva="";
	$scope.contrasenaNueva2="";

	$http({
		method: 'GET',
		url: 'http://localhost:8080/TFG3/webresources/generic/usuario/' + $scope.dni
		}).then(function successCallback(response) {
			$scope.usuario=response.data;
			},
			function errorCallback(response) {
					console.log(response);
				});

	$scope.cambiaNombre=function(){
		$scope.errorNombre=false;
		if($scope.nombre.length===0){
			$scope.errorNombre=true;
		}	
	};

	$scope.cambiaDNI=function(){
		$scope.errorDNI=false;
		if($scope.dni2.length===0){
			$scope.errorDNI=true;
		}	
	};

	$scope.nuevaContrasena=function(){
		$scope.errorContrasena1=false;
		$scope.errorContrasena2=false;
		$scope.errorContrasena3=false;

		if($scope.contrasenaAntigua.length===0){
			$scope.errorContrasena1=true;
		}
		if($scope.contrasenaNueva.length===0){
			$scope.errorContrasena2=true;

		}
		if($scope.contrasenaNueva2.length===0){
			$scope.errorContrasena3=true;
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
};


