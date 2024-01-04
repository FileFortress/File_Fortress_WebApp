// function yourEncryptionFunction(password) {
//     // Implement your password encryption logic here
//     // This is just a placeholder, replace it with your actual encryption algorithm
//     const encryptedPassword = $0.encryptPassword(password);
//     return encryptedPassword;
//     // return btoa(password); // Base64 encoding as an example
// }
// let uiView
// window.ns = {
//     init: function (view){
//         uiView = view;
//     },
//     passwordEncryption: function(password){
//         return uiView.$server.encryptPassword(password);
//     }
// }
// document.addEventListener('DOMContentLoaded', function() {
//     const passwordField = document.getElementById('input-vaadin-password-field-7');
//
//     passwordField.addEventListener('input', function() {
//         const password = passwordField.value;
//         passwordField.value = ns.passwordEncryption(password);
//         uiView.$server.encryptPassword(password);
//         console.log("Password input "+ password);
//     });
// });