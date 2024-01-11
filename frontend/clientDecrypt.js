// Function to decrypt the file using the encryption key
function decryptFile(encryptedFile, key) {
    return window.crypto.subtle.importKey(
        'raw',
        new TextEncoder().encode(key),
        'AES-GCM',
        true,
        ['decrypt']
    ).then((cryptoKey) => {
        return window.crypto.subtle.decrypt(
            {
                name: 'AES-GCM',
            },
            cryptoKey,
            encryptedFile
        );
    });
}

// Assuming you have retrieved the encrypted file content and key
const encryptedFile = getEncryptedFileFromStorage();
const key = getKeyFromSessionStorage();

// Convert base64 string to ArrayBuffer
const encryptedFileArrayBuffer = Uint8Array.from(atob(encryptedFile), c => c.charCodeAt(0)).buffer;

// Decrypt the file
decryptFile(encryptedFileArrayBuffer, key)
    .then((decryptedFile) => {
        // Now you can proceed to use the decrypted file data
        console.log(decryptedFile);

        // For example, you can create a Blob from the decrypted data
        const decryptedBlob = new Blob([decryptedFile], { type: 'application/octet-stream' });

        // Then create a download link and trigger the download
        const downloadLink = document.createElement('a');
        downloadLink.href = URL.createObjectURL(decryptedBlob);
        downloadLink.download = 'decrypted_file.txt';
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
    })
    .catch((error) => {
        console.error('Decryption error:', error);
    });
