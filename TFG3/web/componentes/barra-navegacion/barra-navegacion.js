angular.module("barraNavegacion",[])
.controller("Busqueda",function($scope){
	$scope.busqueda="";
	
	$scope.realizarBusqueda=function(){
		if($scope.busqueda.length!==0){
			window.location='http://localhost:8080/TFG3/busquedaLibros.html?busqueda='+$scope.busqueda;
		}		
	};
})
.component("barraNavegacion",{
	templateUrl: "./componentes/barra-navegacion/barraNavegacion.html",
	controller: "Busqueda"
});
