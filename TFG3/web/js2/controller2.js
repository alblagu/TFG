/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var app=angular.module("Usuarios",[]);
app.controller("Usuario1",function($scope,$http){
	$scope.nombre="";
	$scope.apellidos="";
	$scope.dni="";
	$scope.password="";
	$scope.error=false;
	$scope.textoError="";
	
	$scope.errores=function(){
	};

	$scope.nuevoUsuario=function(){
		if(!errores){
		$http({
			method:'POST',
			url: ''
			}).then(function successCallback(response) {
				
 	 		}, 
	 			function errorCallback(response) {
					$scope.error=true;
					$scope.textoError="Error en la conexion";
  				});
			}
	};
});


