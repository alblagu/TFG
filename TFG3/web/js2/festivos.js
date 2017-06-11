angular.module("festivos", ["barraNavegacion","prestamos"])
	.controller("FestivosController", function ($scope, $http) {
		$scope.fechaFestivo="";

		$scope.nuevoFestivo=function(){
			console.log($scope.fechaFestivo);
			if($scope.fechaFestivo!==null){
				$http({
					method: 'GET',
					url: 'http://localhost:8080/TFG3/webresources/generic/usuario/' + $scope.fechaFestivo
			}).then(function successCallback(response) {
			},
			function errorCallback(response) {
				});
			}
		};

});


