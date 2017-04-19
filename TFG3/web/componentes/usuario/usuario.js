angular.module("usuario", ["Servicio"])
	.controller("usuarioController", function ($scope, ServicioUsuario) {
		$scope.muestraUsuario = function () {
			$scope.usuario = ServicioUsuario.data.usuario;
			console.log($scope.usuario);
		};
	})
	.component("usuario", {
		templateUrl: "./componentes/usuario/usuario.html",
		controller: "usuarioController"
	});
