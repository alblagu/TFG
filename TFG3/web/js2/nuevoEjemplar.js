angular.module("Ejemplar",["barraNavegacion" , "prestamos", "piePagina"])
.controller("Ejemplar2",function($scope,$http){
	$scope.isbn="";
	$scope.codigo="";
	$scope.error=false;
	$scope.textoError="";
	
	$scope.cancelar=function(){
		$scope.parte=false;
	};
	
	$scope.errores=function(){
		$scope.error=false;
		$scope.textoError="";
		if($scope.isbn.length===0){
			$scope.error=true;
			$scope.textoError="El isbn o el codigo no pueden ser campos vacios";
			}
		else{
			if($scope.isbn.length!==10&&$scope.isbn.length!==13){
				$scope.error=true;
				$scope.textoError="El isbn tiene que tener 10 o 13 digitos";
			}
			else{
				if(isNaN($scope.isbn)){
					$scope.error=true;
					$scope.textoError="El isbn tiene que tener solo numeros";
				}
		}
		}
		};

	
	$scope.getLibro=function(){
		$scope.errores();	
		if(!$scope.error){	
			$http({
  			method: 'GET',
  			url: 'http://localhost:8080/TFG3/webresources/generic/libro/'+$scope.isbn
				}).then(function successCallback(response) {
				$scope.error=false;
				$scope.libro=response.data;
				$scope.parte=true;
				
 	 		}, 
	 			function errorCallback(response) {
					$scope.error=true;
					$scope.textoError="Error en la conexion a internet o no hay ningun libro con ese isbn";
  				});
			}
			};

	$scope.addEjemplar=function(){
		$scope.error=false;
		$scope.textoError="";
		if($scope.codigo.length===0){
			$scope.error=true;
			$scope.textoError="El codigo no puede ser una cadena vacia";
		}
		else{
			$http({
				headers: { 
        				'Accept': 'application/json',
        				'Content-Type': 'application/json' 
    },
  		 		method: 'POST',
  				url: 'http://localhost:8080/TFG3/webresources/generic/ejemplares/'+$scope.codigo,
				data:JSON.stringify($scope.libro)
				}).then(function successCallback(response) {
					
					console.log(response);
					alert("Se ha añadido un ejemplar con el codigo "+$scope.codigo+" del libro con isbn "+$scope.isbn);
					$scope.isbn="";
					$scope.codigo="";
					$scope.parte=true;
					window.location="http://localhost:8080/TFG3/";
 	 		}, 
	 			function errorCallback(response) {
    	 				console.log(response);
					$scope.error=true;
					$scope.textoError="Ya hay un ejemplar con el codigo introducido";
  				});
			}
			};	
	});
