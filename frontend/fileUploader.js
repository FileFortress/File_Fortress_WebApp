
document.addEventListener('DOMContentLoaded', function() {
    // Assuming 'upload' is the Vaadin Upload component
    const upload = document.getElementById('myVaadinUpload');

// Save the original addFile method
    const originalAddFile = upload.addFile;

// Override the addFile method
    upload.addFile = function(file) {
        // Call the original addFile method
        originalAddFile.call(upload, file);

        // Your custom code here
        var formdata = new FormData();
        formdata.append("file", file);

        var requestOptions = {
            method: 'POST',
            body: formdata,
            redirect: 'follow'
        };

        fetch("http://localhost:4444/fortress/file_upload", requestOptions)
            .then(response => response.text())
            .then(result => console.log('Done !!!'+result))
            .catch(error => console.log('error my ', error));
    };

});
