window.getServerPublic = function  getServerPublic(key){
        console.log('Server Public JS part Executed');
        console.log("Server send a Key: ", key);
        sessionStorage.setItem("serverPublic", key);
}
// window.sendClientPublic = function sendClientPublic(view) {
//        // view; // I want save this ui layout to call view.$server.setClientPublicKey(); this in other js class
// }
let mainView;
window.key = {
        init: function (view) {
                mainView = view.$server;
                mainView.setClientPublicKey("Init method invoking");
        },
        sendClientPublic: function (publicKey){
                mainView.setClientPublicKey(publicKey);
        }
}