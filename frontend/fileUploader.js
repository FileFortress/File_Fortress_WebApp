// // fileUploader.js
//
// function handleCustomFileUpload(event) {
//     const fileInput = event.detail.file;
//
//     // Create a FormData object and append the file to it
//     const formData = new FormData();
//     formData.append("file", fileInput);
//
//     // Send the FormData to your custom endpoint using the fetch API
//     fetch("http://your-custom-endpoint", {
//         method: "POST",
//         body: formData,
//         headers: {
//             // Add any required headers, e.g., authorization headers
//         },
//     })
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error(`File upload failed with status: ${response.status}`);
//             }
//             return response.text(); // You can adjust this based on your server response
//         })
//         .then(data => {
//             console.log("File upload successful:", data);
//             // Handle success, if needed
//         })
//         .catch(error => {
//             console.error("File upload error:", error);
//             // Handle error, if needed
//         });
// }
//
//
// // Access the existing vaadin-upload element
// const vaadinUpload = document.getElementById("myVaadinUpload");
//
// // Set the target URL and other properties
// vaadinUpload.target = "http://localhost:4444/uploadFile";
// vaadinUpload.headers = { "Content-Type": "multipart/form-data" };
// vaadinUpload.formDataName = "file";
//
// // Add your custom upload function
// vaadinUpload.addEventListener("upload", handleCustomFileUpload);
//


// // Assuming you have a fileInput element in your HTML
// var fileInput = document.getElementById('myVaadinUpload');
//
// // Create a FormData object and append the file
// var formData = new FormData();
// formData.append('file', fileInput.files[0]);
//
// // Create the request options
// var requestOptions = {
//     method: 'POST',
//     body: formData,
//     headers: {
//         // You may need to set additional headers based on your server requirements
//         'Cookie': 'JSESSIONID=6F887D7FA60F21504B2396AE95AD1BA0',
//     },
// };
//
// // Make the fetch request
// fetch('http://localhost:4444/fortress/file_upload', requestOptions)
//     .then(response => response.text())
//     .then(result => console.log(result))
//     .catch(error => console.error('Error:', error));
