// document.addEventListener('DOMContentLoaded', function() {
//     // Assuming 'upload' is the Vaadin Upload component
//     console.log("executing js")
//     const upload = document.getElementById('myVaadinUpload');
//     console.log("got the element id")
//
// // Add an event listener for the 'upload' event
//     upload.addEventListener('upload', function(event) {
//         // Access the uploaded files
//         alert(1)
//         const files = event.detail.files;
//
//         if (files.length > 0) {
//             const file = files[0];
//
//             // Read the content of the file
//             const reader = new FileReader();
//             reader.onload = function(event) {
//                 // Handle the content of the file
//                 const fileContent = event.target.result;
//
//                 // Process the file content or upload it to the server
//                 uploadFile(fileContent);
//             };
//
//             // Read the file as text (you might need to adjust this based on the file type)
//             reader.readAsText(file);
//         } else {
//             alert('No file selected.');
//         }
//     });

// Function to upload file content using fetch
//     function uploadFile() {
//
// // Assuming 'upload' is the Vaadin Upload component
//         console.log("executing js")
//         const upload = document.getElementById('myVaadinUpload');
//         console.log("got the element id")
//
// // Add an event listener for the 'upload' event
//         upload.addEventListener('upload', function (event) {
//             // Access the uploaded files
//             alert(1)
//             const files = event.detail.files;
//
//             if (files.length > 0) {
//                 const file = files[0];
//
//                 // Read the content of the file
//                 const reader = new FileReader();
//                 reader.onload = function (event) {
//                     // Handle the content of the file
//                     const fileContent = event.target.result;
//
//                     // Process the file content or upload it to the server
//                     uploadFile(fileContent);
//                 };
//
//                 // Read the file as text (you might need to adjust this based on the file type)
//                 reader.readAsText(file);
//             } else {
//                 alert('No file selected.');
//             }
//         });
//
//     }

//
// function uploadFile(){
//     const upload = document.getElementById('myVaadinUpload');
//     alert(1);
//     // Your custom code here
//     var formdata = new FormData();
//     formdata.append("file", file);
//
//     var requestOptions = {
//         method: 'POST',
//         body: formdata,
//         redirect: 'follow'
//     }
//
//     fetch("http://localhost:4444/fortress/file_upload", requestOptions)
//         .then(response => response.arrayBuffer())  // Use arrayBuffer() to get the array buffer of the response
//         .then(buffer => {
//             // Convert the array buffer to a byte array
//             const byteArray = new Uint8Array(buffer);
//
//             // Log the byte array
//             console.log('Byte Array:', byteArray);
//         })
//         .catch(error => console.log('Error:', error));
//



// var fileInput = document.getElementById('myVaadinUpload').$.fileInput;



// }

// $("#myVaadinButton").on('click', function(){
//     var fileInput = document.getElementById('fileInput');
//     var file = fileInput.files[0];
//
//     if (file) {
//         var formData = new FormData();
//         formData.append('file', file);
//
//         var reader = new FileReader();
//         reader.onload = function (e) {
//             // Log file content (data URL)
//             console.log('Content:', e.target.result);
//         };
//
//         // Read the file content
//         reader.readAsDataURL(file);
//
//         var xhr = new XMLHttpRequest();
//         xhr.open('POST', 'http://localhost:4444/file_upload', true);
//
//         // Optional: Set any headers if needed
//         // xhr.setRequestHeader('Authorization', 'Bearer YOUR_ACCESS_TOKEN');
//
//         xhr.onreadystatechange = function() {
//             if (xhr.readyState === 4) {
//                 if (xhr.status === 200) {
//                     // File uploaded successfully
//                     console.log('File uploaded successfully');
//                 } else {
//                     // Handle the error
//                     console.error('Error uploading file:', xhr.statusText);
//                 }
//             }
//         };
//
//         // Send the FormData object which contains the file
//         xhr.send(formData);
//     } else {
//         console.error('No file selected.');
//     }
// });

// function uploadFile(){
//     const upload = document.getElementById('myVaadinUpload');
//     const originalAddFile = upload.addFile;
//
//     upload.addFile = function(file) {
//         originalAddFile.call(upload, file);
//
//         // Create a new FormData object
//         const formData = new FormData();
//         formData.append("file", file);
//
//         // Create an XMLHttpRequest object
//         const xhr = new XMLHttpRequest();
//
//         // Configure the request
//         xhr.open("POST", "http://localhost:4444/fortress/file_upload", true);
//         xhr.onload = function() {
//             if (xhr.status === 200) {
//                 const byteArray = new Uint8Array(xhr.response);
//                 console.log('Byte Array:', byteArray);
//             } else {
//                 console.error('Error:', xhr.status);
//             }
//         };
//         xhr.onerror = function() {
//             console.error('Network error occurred');
//         };
//
//         // Send the request
//         xhr.send(formData);
//     };
// }

// $("#uploadbtn").on('click', function(){
//     var fileInput = document.getElementById('fileInput');
//     var file = fileInput.files[0];
//
//     if (file) {
//         var formData = new FormData();
//         formData.append('file', file);
//
//         var reader = new FileReader();
//         reader.onload = function (e) {
//             // Log file content (data URL)
//             console.log('Content:', e.target.result);
//         };
//
//         // Read the file content
//         reader.readAsDataURL(file);
//
//         var xhr = new XMLHttpRequest();
//         xhr.open('POST', 'http://localhost:4444/file_upload', true);
//
//         // Optional: Set any headers if needed
//         // xhr.setRequestHeader('Authorization', 'Bearer YOUR_ACCESS_TOKEN');
//
//         xhr.onreadystatechange = function() {
//             if (xhr.readyState === 4) {
//                 if (xhr.status === 200) {
//                     // File uploaded successfully
//                     console.log('File uploaded successfully');
//                 } else {
//                     // Handle the error
//                     console.error('Error uploading file:', xhr.statusText);
//                 }
//             }
//         };
//
//         // Send the FormData object which contains the file
//         xhr.send(formData);
//     } else {
//         console.error('No file selected.');
//     }
// });