angular.module("barraNavegacion",[])
.controller("BarraNavegacion",function($scope){
	$scope.usuario=JSON.parse(localStorage.getItem('usuario'));
	

	$scope.logout=function(){
		localStorage.setItem('usuario', "");
		window.location='http://localhost:8080/TFG3/index.html';
	};
})
.component("barraNavegacion",{
	templateUrl: "./componentes/barra-navegacion/barraNavegacion.html",
	controller: "BarraNavegacion",
	bindings: {
    		num: "@"
 	 }
});

