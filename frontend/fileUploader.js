// File Uploader
window.uploadFile = function uploadFile(){
    console.log('JS part Executed');
    // window.document.getElementById('myVaadinUpload').setAttribute('target', 'http://localhost:4444/fortress/file_upload');
    // const target = window.document.getElementById('myVaadinUpload').getAttribute('target');
    // Assume you have received the base64-encoded server public key from the server
    const serverPublicKeyBase64 = localStorage.getItem('serverPublicKey'); // Replace with the actual value

// Decode the base64-encoded server public key
    const serverPublicKeyArrayBuffer = Uint8Array.from(atob(serverPublicKeyBase64), c => c.charCodeAt(0));

// Import the server's public key
    crypto.subtle.importKey(
        'spki',
        serverPublicKeyArrayBuffer,
        { name: 'ECDH', namedCurve: 'P-256' }, // Adjust the curve if needed
        true,
        ['deriveBits']
    )
        .then(serverPublicKey => {
            // Generate the client's key pair
            return crypto.subtle.generateKey(
                { name: 'ECDH', namedCurve: 'P-256' }, // Adjust the curve if needed
                true,
                ['deriveBits']
            );
        })
        .then(clientKeyPair => {
            // Perform the Diffie-Hellman key exchange
            return crypto.subtle.deriveKey(
                { name: 'ECDH', public: serverPublicKey},
                clientKeyPair.privateKey,
                { name: 'AES-GCM', length: 256 }, // Adjust the algorithm and key length as needed
                true,
                ['encrypt', 'decrypt']
            );
        })
        .then(sharedSecret => {
            // Use the shared secret for encryption or other purposes
            console.log('Shared secret:', sharedSecret);
        })
        .catch(error => {
            console.error('Error:', error);
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
