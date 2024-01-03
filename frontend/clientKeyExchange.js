window.getServerPublic = function  getServerPublic(key){
        console.log('Server Public JS part Executed');
        console.log("Server send a Key: ", key);
        localStorage.setItem("serverPublic", key);
}