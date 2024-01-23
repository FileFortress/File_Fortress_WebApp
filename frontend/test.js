
window.getServerPublic = function getServerPublic(key) {
    console.log('Server Public JS part Executed');
    console.log("Server send a Key: ", key);
    sessionStorage.setItem("serverPublic", key);

    // Decode the base64-encoded public key
    const publicKeyArrayBuffer = Uint8Array.from(atob(key), c => c.charCodeAt(0));


    crypto.subtle.importKey(
        'spki',
        publicKeyArrayBuffer,
        {name: 'ECDH', namedCurve: 'P-256'}, // Adjust the curve if needed
        true,
        [] // key usage
    ).then(serverPublicKey => {
            // the CryptoKey object representing the public key
            console.log('Imported server public key:', serverPublicKey);
        // sessionStorage.setItem("serverPublic", serverPublicKey);
            // Decode the base64-encoded public key
            crypto.subtle.generateKey(
                {
                    name: 'ECDH',
                    namedCurve: 'P-256', // Same curve as server's key
                },
                true, // Generate a key pair (private and public)
                ['deriveKey'] // Required for deriving a shared secret
            ).then(keyPair => {
                const publicKey = keyPair.publicKey;
                const privateKey = keyPair.privateKey;
                // sessionStorage.setItem("clientPrivate", keyPair.privateKey);
                // Export the client's public key to base64 (if needed for server)
                crypto.subtle.exportKey('spki', publicKey)
                    .then(publicKeyArrayBuffer => {
                        const clientPublicKeyBase64 = btoa(String.fromCharCode(...new Uint8Array(publicKeyArrayBuffer)));
                        sessionStorage.setItem("clientPublic", clientPublicKeyBase64);
                        // Send clientPublicKeyBase64 to the server
                        window.key.sendClientPublic(clientPublicKeyBase64);
                    })
                    .catch(error => console.error('Error exporting public key:', error));
                crypto.subtle.deriveKey(
                    {
                        name: 'ECDH',
                        public: serverPublicKey
                    },
                    privateKey,
                    {
                        name: "AES-GCM",
                        length: 256,
                    },
                    true,
                    ["encrypt", "decrypt"],
                ).then(r =>{
                    console.log("secret Key : ",r);
                    crypto.subtle.exportKey('raw', r)
                        .then(publicKeyArrayBuffer => {
                            const clientSecretKeyBase64 = btoa(String.fromCharCode(...new Uint8Array(publicKeyArrayBuffer)));
                            console.log("Client Secret Key (base64):", clientSecretKeyBase64);
                            // set it on session storage
                            sessionStorage.setItem("sharedSecret", clientSecretKeyBase64);
                        })
                        .catch(error => console.error('Error exporting Secret key:', error));
                }).catch(error=>{
                    console.error("Return type Error: ",error);
                });
            }). catch(error => {
                console.error('Error generating key pair:', error);
            });
        })
        .catch(error => {
            console.error('Error importing public key:', error);
        });
}

let mainView;
window.key = {
    init: function (view) {
        mainView = view.$server;
    },
    sendClientPublic: function (publicKey){
        mainView.setClientPublicKey(publicKey);
    }
}

