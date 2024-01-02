import { initializeApp } from 'firebase/app';
import { getAuth, GoogleAuthProvider, signInWithPopup, signInWithRedirect , getRedirectResult } from "firebase/auth";
import { getFirestore, collection, setDoc, doc } from "firebase/firestore";

// Replace the following with your app's Firebase project configuration
const firebaseConfig = {
    apiKey: "AIzaSyDzKiQ0WY_Wg3r8OLoO2vCghvd12y0oBtc",
    authDomain: "file-fortress-51d7e.firebaseapp.com",
    projectId: "file-fortress-51d7e",
    storageBucket: "file-fortress-51d7e.appspot.com",
    messagingSenderId: "325669946515",
    appId: "1:325669946515:web:cd9a7c9996ac4e205eb966"
};

const app = initializeApp(firebaseConfig);
// Initialize Cloud Firestore and get a reference to the service
const db = getFirestore(app);

// Initialize Firebase Authentication and get a reference to the service
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

provider.addScope('https://www.googleapis.com/auth/contacts.readonly');

auth.languageCode = 'it';
// To apply the default browser preference instead of explicitly setting it.
// auth.useDeviceLanguage();

provider.setCustomParameters({
    'login_hint': 'user@example.com'
});

window.ns = {
    googleSignInPopup : function(view) {
        async function updateDatabase(user) {
            // Save user details to Firestore
            const userDocRef = doc(db, 'users', user.uid); // Replace collection() with doc()
            await setDoc(userDocRef, {
                name: user.displayName,
                firstName: "", // Set this to the first name if available
                email: user.email,
                pictureUrl: user.photoURL,
                userRole: 'ROLE_USER'
            });
        }
        signInWithPopup(auth, provider)
            .then((result) => {
                // This gives you a Google Access Token. You can use it to access the Google API.
                const credential = GoogleAuthProvider.credentialFromResult(result);
                const token = credential.accessToken;
                // The signed-in user info.
                const user = result.user;
                // Save user details to Firestore
                updateDatabase(user).then(r => {console.log("Update Database Then")});
                // Call the Java function to handle the token
                view.$server.login(user.accessToken, user.uid);
            }).catch((error) => {
            // Handle Errors here.
            const errorCode = error.code;
            const errorMessage = error.message;
            // The email of the user's account used.
            // const email = error.customData.email;
            // The AuthCredential type that was used.
            const credential = GoogleAuthProvider.credentialFromError(error);
            // ...
        });
    }
}


// signInWithRedirect(auth, provider).then((result) => {
//     // This gives you a Google Access Token. You can use it to access Google APIs.
//     const credential = GoogleAuthProvider.credentialFromResult(result);
//     const token = credential.accessToken;
//     // The signed-in user info.
//     const user = result.user;
//     // IdP data available using getAdditionalUserInfo(result)
//     // Save user details to Firestore
//     updateDatabase(user).then(r => {console.log("Update Database Then")});
//     // Call the Java function to handle the token
//     view.$server.login(user.accessToken, user.uid);
// }).catch((error) => {
//     // Handle Errors here.
//     const errorCode = error.code;
//     const errorMessage = error.message;
//     // The email of the user's account used.
//     const email = error.customData.email;
//     // The AuthCredential type that was used.
//     const credential = GoogleAuthProvider.credentialFromError(error);
//     // ...
// });

// [START auth_google_signin_popup]
