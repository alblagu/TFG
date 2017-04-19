angular.module("Usuario", [])
.factory("UsuarioFactory", function(){
    var dni="";

    var interfaz = {
        getDNI: function(){
            return dni;
        },
        anadirDNI: function(dni2){
            dni=dni2;
        }
    };
    return interfaz;
});