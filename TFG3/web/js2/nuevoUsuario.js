angular.module("app")
.controller("Usuario1",function($scope,$http){
	$scope.dni="";
	$scope.password="";
	$scope.nombre="";
	$scope.apellidos="";
	$scope.telefono="";
	$scope.error=false;
	$scope.textoError="";
	
	$scope.errores=function(){
		return false;
	};

	$scope.nuevoUsuario=function(){
		var usuario={
			dni:$scope.dni,
			password:$scope.password,
			nombre:$scope.nombre,
			apellidos:$scope.apellidos,
			telefono:$scope.telefono
		};
		if(true){
			console.log(JSON.stringify(usuario));
		$http({
				headers: { 
        				'Accept': 'application/json',
        				'Content-Type': 'application/json' 
    },
  		 		method: 'POST',
  				url: 'http://localhost:8080/TFG3/webresources/generic/usuarios',
				data:JSON.stringify(usuario)
			}).then(function successCallback(response) {
				alert("Usuario Añadido");
				
 	 		}, 
	 			function errorCallback(response) {
					$scope.error=true;
					$scope.textoError="Ya hay un usuario con ese dni";
  				});
			}
	};
});


