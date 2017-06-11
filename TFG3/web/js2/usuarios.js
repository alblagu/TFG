angular.module("usuarios", ["barraNavegacion","prestamos"])
	.controller("UsuariosController", function ($scope, $http) {

		$scope.usuario = JSON.parse(localStorage.getItem('usuario'));
		$scope.usuarios = [];
		$scope.usuarios2=[];
		$scope.dni = "";
		$scope.nombre = "";
		$scope.mostrarFiltros = false;
		$scope.MAX_USUARIOS_PANTALLA=6;
		$scope.contador=0;

		$scope.mostrarOcultarFiltros = function () {
			$scope.mostrarFiltros = !$scope.mostrarFiltros;
		};


		$scope.buscaUsuarios = function () {
			var usuario = {
					dni: $scope.dni,
					nombre: $scope.nombre
				};
			$http({
				method: 'GET',
				url: 'http://localhost:8080/TFG3/webresources/generic/usuarios/'+JSON.stringify(usuario)
			}).then(function successCallback(response) {
				$scope.usuarios = response.data;
				$scope.usuarios2=[];
				$scope.contador=0;
				$scope.siguientes();
			},
				function errorCallback(response) {
				});
		};
		
		$scope.buscaUsuarios();

		$scope.deleteUsuario = function (index) {
			if (confirm("Estas seguro que quieres eliminar al usuario " + $scope.usuarios2[index].nombre + " " + $scope.usuarios2[index].apellidos)) {
				$http({
					method: 'DELETE',
					url: 'http://localhost:8080/TFG3/webresources/generic/usuario/' + $scope.usuarios2[index].dni
				}
				).then(function successCallback(response) {
					alert("Usuario Eliminado Correctamente");
					location.reload();	
				},
					function errorCallback(response) {
						alert("error intentelo de nuevo");
					});
			}
		};

		$scope.perfilUsuario= function(index){
			window.location='http://localhost:8080/TFG3/usuarioPerfil.html?dni='+$scope.usuarios2[index].dni;
		};

		$scope.siguientes=function(){
			if($scope.usuarios.length/$scope.MAX_USUARIOS_PANTALLA>$scope.contador){
				$scope.contador++;
				if($scope.usuarios.length/$scope.MAX_USUARIOS_PANTALLA<$scope.contador){
					$scope.contador--;
					$scope.usuarios2=[];
					for(i=0;i<$scope.usuarios.length-$scope.contador*$scope.MAX_USUARIOS_PANTALLA;i++){
						$scope.usuarios2[i]=$scope.usuarios[$scope.contador*$scope.MAX_USUARIOS_PANTALLA+i];	
					}
					
				}
				else{
					$scope.contador--;
					for(i=0;i<$scope.MAX_USUARIOS_PANTALLA;i++){
						$scope.usuarios2[i]=$scope.usuarios[$scope.contador*$scope.MAX_USUARIOS_PANTALLA+i];	
					}
				}
				$scope.contador++;
			}
			
		};
		$scope.anteriores=function(){
			if(1!==$scope.contador){
				$scope.contador--;
				$scope.contador--;
				for(i=0;i<$scope.MAX_USUARIOS_PANTALLA;i++){
					$scope.usuarios2[i]=$scope.usuarios[$scope.contador*$scope.MAX_USUARIOS_PANTALLA+i];	
				}
				$scope.contador++;
			}
			
		};

		$scope.resultado=function(){
			return ($scope.contador*$scope.MAX_USUARIOS_PANTALLA>$scope.usuarios.length)? $scope.usuarios.length:$scope.contador*$scope.MAX_USUARIOS_PANTALLA;
		};
	});

