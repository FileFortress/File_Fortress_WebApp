
// File Uploader
window.uploadFile = function uploadFile(){
    console.log('JS part Executed');
    // window.document.getElementById('myVaadinUpload').setAttribute('target', 'http://localhost:4444/fortress/file_upload');
    // const target = window.document.getElementById('myVaadinUpload').getAttribute('target');

    const upload = window.document.getElementById('myVaadinUpload');
    upload.addEventListener('upload-request', function(event) {
        event.preventDefault();
        console.log('File selected:', event.detail.file.name);
        event.detail.xhr.setRequestHeader('File-Name', event.detail.file.name);
        let formData = new FormData();
        //TODO: I want to encrypt the file here and send the encrypted one to the server.
        // here is the shared secret AES-GCM /256 key


        // Encrypt the file using the shared secret key
        encryptFile(event.detail.file, sessionStorage.getItem("sharedSecret"))
            .then(encryptedFile => {
                const encryptedBlob = new Blob([encryptedFile], { type: event.detail.file.type });

                // Create a new FormData and append the encrypted file
                let formData = new FormData();
                formData.append('file', encryptedBlob, event.detail.file.name);

                // Set the request header and send the encrypted file to the server
                event.detail.xhr.setRequestHeader('File-Name', event.detail.file.name);
                event.detail.xhr.send(formData);
            })
            .catch(error => {
                console.error('Error encrypting file:', error);
            });
    });
}
function encryptFile(file, key) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = function() {
            const arrayBuffer = reader.result;

            // Decode the base64-encoded public key
            const sharedKeyBuffer = Uint8Array.from(atob(key), c => c.charCodeAt(0));
            crypto.subtle.importKey('raw', sharedKeyBuffer, { name: 'AES-GCM' }, false, ['encrypt'])
                .then(cryptoKey => {
                    return crypto.subtle.encrypt({ name: 'AES-GCM', iv: new Uint8Array(12) }, cryptoKey, arrayBuffer);
                })
                .then(encryptedArrayBuffer => resolve(encryptedArrayBuffer))
                .catch(reject);
        };
        reader.onerror = reject;
        reader.readAsArrayBuffer(file);
    });
}
