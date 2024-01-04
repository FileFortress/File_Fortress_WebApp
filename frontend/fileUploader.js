// File Uploader
window.uploadFile = function uploadFile(){
    console.log('JS part Executed');
    // window.document.getElementById('myVaadinUpload').setAttribute('target', 'http://localhost:4444/fortress/file_upload');
    // const target = window.document.getElementById('myVaadinUpload').getAttribute('target');

    // Assume you have received the base64-encoded server public key from the server
    const serverPublicKeyBase64 = sessionStorage.getItem('serverPublic'); // Replace with the actual value
    const fileUploader = Object.create(window.key); // create object

// Decode the base64-encoded server public key
    const serverPublicKeyArrayBuffer = Uint8Array.from(atob(serverPublicKeyBase64), c => c.charCodeAt(0));
    // console.log("Server Public Array Buffer : ",serverPublicKeyArrayBuffer);
    let privateKey;
// Generate the client's key pair using ECDH and the secp256r1 curve
    crypto.subtle.generateKey(
        {
            name: 'ECDH',
            namedCurve: 'P-256', // Same curve as server's key
        },
        true, // Generate a key pair (private and public)
        ['deriveKey'] // Required for deriving a shared secret
    ).then(keyPair => {
        const publicKey = keyPair.publicKey;
        // const privatek = keyPair.privateKey;
        privateKey = keyPair.privateKey;
        // Export the client's public key to base64 (if needed for server)
        crypto.subtle.exportKey('spki', publicKey)
            .then(publicKeyArrayBuffer => {
                const clientPublicKeyBase64 = btoa(String.fromCharCode(...new Uint8Array(publicKeyArrayBuffer)));
                console.log("Client Public Key (base64):", clientPublicKeyBase64);
                // Send clientPublicKeyBase64 to the server (if required)
                fileUploader.sendClientPublic(clientPublicKeyBase64);
            })
            .catch(error => console.error('Error exporting public key:', error));
    }).catch(error => {
        console.error('Error generating key pair:', error);
    });

    const sharedSecretPromise = crypto.subtle.deriveKey(
        {
            name: 'ECDH',
            public: serverPublicKeyBase64,
        },
        privateKey,
        {
            name: 'AES-GCM', // Specify desired algorithm for the shared secret (e.g., AES-GCM for encryption)
            length: 256, // Specify desired key length (e.g., 256 for AES-GCM)
        },
        true,
        ['deriveKey']
    );

    sharedSecretPromise.then(sharedSecretKey => {
        // Use the shared secret for your desired cryptographic operations (e.g., encryption, signing)
        console.log("Shared Secret:", sharedSecretKey);
    }).catch(error => {
        console.error('Error deriving shared secret:', error);
    });



    const upload = window.document.getElementById('myVaadinUpload');
    upload.addEventListener('upload-request', function(event) {
        event.preventDefault();
        console.log('File selected:', event.detail.file.name);
        event.detail.xhr.setRequestHeader('File-Name', event.detail.file.name);
        let formData = new FormData();
        formData.append('file', event.detail.file, event.detail.file.name);
        event.detail.xhr.send(formData);
    });
}
