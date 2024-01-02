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
        formData.append('file', event.detail.file, event.detail.file.name);
        event.detail.xhr.send(formData);
    });
}