import { Upload } from '@vaadin/upload';

const upload = new Upload();

upload.beforeUpload = async (event) => {
    const file = event.detail.file;

    const reader = new FileReader();
    reader.readAsArrayBuffer(file);

    reader.onload = async (event) => {
        const fileData = event.target.result;

        const encryptedData = CryptoJS.AES.encrypt(fileData, 'your-encryption-key');
        const encryptedBlob = new Blob([encryptedData.toString()], { type: file.type });

        event.detail.file = encryptedBlob;
    };
};
