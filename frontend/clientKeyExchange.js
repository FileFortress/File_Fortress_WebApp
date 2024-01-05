
window.getServerPublic = function  getServerPublic(key){
    console.log('Server Public JS part Executed');
    console.log("Server send a Key: ", key);
    crypto.subtle.generateKey(
        {
            name: 'ECDH',
            namedCurve: 'P-256', // Same curve as server's key
        },
        true, // Generate a key pair (private and public)
        ['deriveKey'] // Required for deriving a shared secret
    ).then(keyPair => {
        const publicKey = keyPair.publicKey;
        // const privateKey = keyPair.privateKey;
        sessionStorage.setItem("clientPublic", keyPair.publicKey);
        sessionStorage.setItem("clientPrivate", keyPair.privateKey);
        // Export the client's public key to base64 (if needed for server)
        crypto.subtle.exportKey('spki', publicKey)
            .then(publicKeyArrayBuffer => {
                const clientPublicKeyBase64 = btoa(String.fromCharCode(...new Uint8Array(publicKeyArrayBuffer)));
                console.log("Client Public Key (base64):", clientPublicKeyBase64);
                // Send clientPublicKeyBase64 to the server
                window.key.sendClientPublic(clientPublicKeyBase64);
            })
            .catch(error => console.error('Error exporting public key:', error));
    }).catch(error => {
        console.error('Error generating key pair:', error);
    });

    // Decode the base64-encoded public key
    const publicKeyArrayBuffer = Uint8Array.from(atob(key), c => c.charCodeAt(0));

// Importing the public key into a CryptoKey object
    crypto.subtle.importKey(
        'spki',
        publicKeyArrayBuffer,
        { name: 'ECDSA', namedCurve: 'P-256' }, // Adjust the curve if needed
        true,
        ['verify'] // key usage
    )
        .then(publicKey => {
            // the CryptoKey object representing the public key
            console.log('Imported public key:', publicKey);
            sessionStorage.setItem("serverPublic", publicKey);
        })
        .catch(error => {
            console.error('Error importing public key:', error);
        });
    //TODO: have to implement client shared secret part

//     const serverPublicKey = sessionStorage.getItem("serverPublic");
//     const clientPrivateKey = sessionStorage.getItem("clientPrivate");
//
// // Decode the base64-encoded client private key
//     const clientPrivateKeyArrayBuffer = Uint8Array.from(atob(clientPrivateKey), c => c.charCodeAt(0));
//
// // Import the client's private key
//     crypto.subtle.importKey(
//         'spki',
//         clientPrivateKeyArrayBuffer,
//         { name: 'ECDH', namedCurve: 'P-256' }, // Adjust the curve if needed
//         true,
//         ['deriveKey']
//     )
//         .then(clientPrivateKey => {
//             // Derive the shared secret
//             return crypto.subtle.deriveKey(
//                 { name: 'ECDH', public: serverPublicKey },
//                 clientPrivateKey,
//                 { name: 'AES-GCM', length: 256 },
//                 true,
//                 ['encrypt', 'decrypt']
//             );
//         })
//         .then(sharedSecretKey => {
//             // Use the shared secret for your desired cryptographic operations
//             console.log("Shared Secret:", sharedSecretKey);
//         })
//         .catch(error => {
//             console.error('Error:', error);
//         });
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


