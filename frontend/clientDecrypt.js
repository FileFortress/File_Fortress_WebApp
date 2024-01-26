// // File Downloader
// window.downloadPaper = function downloadPaper() {
//     console.log('Download Paper button clicked');
//
//     // Assuming you have the encrypted file content and key stored
//     const encryptedFileArrayBuffer = getEncryptedFileFromStorage(); // Replace this with your method to retrieve the encrypted file
//     const key = sessionStorage.getItem("sharedSecret");
//     console.error('Encrypted file:', encryptedFileArrayBuffer);
//
//     // Convert base64 string to ArrayBuffer
//     // const encryptedFileArrayBuffer = Uint8Array.from(atob(encryptedFile), c => c.charCodeAt(0)).buffer;
//
//     // Decrypt the file
//     decryptFile(encryptedFileArrayBuffer, key)
//         .then((decryptedFile) => {
//             // Now you can proceed to use the decrypted file data
//             console.log(decryptedFile);
//
//             // For example, you can create a Blob from the decrypted data
//             const decryptedBlob = new Blob([decryptedFile], { type: 'application/octet-stream' });
//
//             // Then create a download link and trigger the download
//             const downloadLink = document.createElement('a');
//             downloadLink.href = URL.createObjectURL(decryptedBlob);
//             downloadLink.download = 'decrypted_file.txt';
//             document.body.appendChild(downloadLink);
//             downloadLink.click();
//             document.body.removeChild(downloadLink);
//         })
//         .catch((error) => {
//             console.error('Decryption error:', error);
//         });
// }
//
// // Add this function to your existing code
// function decryptFile(encryptedArrayBuffer, key) {
//     return new Promise((resolve, reject) => {
//         // Decode the base64-encoded public key
//         const sharedKeyBuffer = Uint8Array.from(atob(key), c => c.charCodeAt(0));
//         crypto.subtle.importKey('raw', sharedKeyBuffer, { name: 'AES-GCM' }, false, ['decrypt'])
//             .then(cryptoKey => {
//                 return crypto.subtle.decrypt({ name: 'AES-GCM', iv: new Uint8Array(12) }, cryptoKey, encryptedArrayBuffer);
//             })
//             .then(decryptedArrayBuffer => resolve(decryptedArrayBuffer))
//             .catch(reject);
//     });
// }
// function getEncryptedFileFromStorage() {
//     const encryptedFileBase64 = sessionStorage.getItem('encryptedFile');
//     if (encryptedFileBase64) {
//         return Uint8Array.from(atob(encryptedFileBase64), c => c.charCodeAt(0));
//     } else {
//         throw new Error('Encrypted file not found in session storage.');
//     }
// }
//
//
