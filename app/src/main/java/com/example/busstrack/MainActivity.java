package com.example.busstrack;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PlayGamesAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Accounts.Admin;
import Accounts.StandardUser;
import Accounts.User;
import Traffic.Bus;
import Traffic.Station;
import Utils.Coordinate;
import Utils.CustomArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static Integer userCount;
    Server fire = new Server();
    ArrayList<User> users = new ArrayList<>();
    public CustomArrayList<User> usersWithNotification = new CustomArrayList<>(this);
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference;

    boolean loadedData = false;
    public PopupWindow loadingWindow;

    boolean neverSwitchedBefore = true;
    public static User currentUser;

    private Button button;
    private Button register;
    private Button signUp;

    View v;

    private String username;
    private String email;
    private String password;
    private String confirm;

    public static Dialog notification;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

         fire.<Station>getData( this);

        notification = new Dialog(this);

        super.onCreate(savedInstanceState);
        v = findViewById(android.R.id.content).getRootView();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                onButtonLoadingWindowClick(v, R.layout.loading);

            }
        },1000);


        setContentView(R.layout.activity_main);

        loginMenu();
    }

    public void loginMenu() {
        button = findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("click");
                EditText usrname = findViewById(R.id.edt_userName);
                EditText pass = findViewById(R.id.edt_password);
                username = usrname.getText().toString();
                password = pass.getText().toString();
                System.out.println("U:" + username + "P:" + password);
                checkCurrentUser();
            }
        });
        register = findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("click");
                setContentView(R.layout.register);

                signUp = findViewById(R.id.btnSignUp);
                signUp.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("click");

                        EditText emailf = findViewById(R.id.edt_email);
                        EditText usrname = findViewById(R.id.edt_userName);
                        EditText passf = findViewById(R.id.edt_password);
                        EditText conf = findViewById(R.id.edt_confirmPassword);

                        email = emailf.getText().toString();
                        username = usrname.getText().toString();
                        password = passf.getText().toString();
                        confirm = conf.getText().toString();

                        if (password.equals(confirm)) {

                            StandardUser user = new StandardUser(username, password, email);
                            ArrayList<User> listToAdd = new ArrayList<>();
                            listToAdd.add(user);
                            fire.<User>pushData("Accounts", listToAdd);

                            setContentView(R.layout.activity_main);
                            loginMenu();
                        }
                        else
                            onButtonShowPopupWindowClick(v, R.layout.wrongpassword);
                    }
                });
            }
        });
    }

    public void onButtonShowPopupWindowClick(View view, int layout) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onButtonLoadingWindowClick(View view, int layout) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        loadingWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        loadingWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MapsActivity.class);
        startActivity(switchActivityIntent);
    }

    public void getUsers() {

        reference = rootNode.getReference("Accounts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                users.clear();
                int ok = 0;
                String pass = new String();
                String usr = new String();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (ok == 0) {
                        pass = snapshot.getValue(String.class);
                        ok++;
                    }
                    else
                        usr = snapshot.getValue(String.class);
                }
                Admin station = new Admin(usr, pass);
                System.out.println(station);
                users.add(station);
                checkCurrentUser();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void checkCurrentUser() {
        for (User acc : usersWithNotification.getList()) {
            if (acc.getUsername().equals(username) && acc.getPassword().equals(password)) {
                if(acc instanceof Admin)
                {
                    System.out.println("I am ADMIN");
                    setContentView(R.layout.admin);
                    adminMenu();
                }
                else
                {
                    currentUser = acc;
                    switchActivities();
                }
            }
        }
    }

    public void adminMenu() {
        Button addBus = findViewById(R.id.btnAddBus);
        addBus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("click");

                EditText busid = findViewById(R.id.edt_busId);
                String busidtxt = busid.getText().toString();

                EditText longitude = findViewById(R.id.edt_busLongitude);
                String longitudetxt = longitude.getText().toString();
                String[] coords = longitudetxt.split("/");
                Float degrees = Float.parseFloat(coords[0]);
                Float minutes = Float.parseFloat(coords[1]);
                Float seconds = Float.parseFloat(coords[2]);
                Coordinate longitudeCoord = new Coordinate(degrees, minutes, seconds);

                EditText latitude = findViewById(R.id.edt_busLatitude);
                String latitudetxt = latitude.getText().toString();
                coords = latitudetxt.split("/");
                degrees = Float.parseFloat(coords[0]);
                minutes = Float.parseFloat(coords[1]);
                seconds = Float.parseFloat(coords[2]);
                Coordinate latitudeCoord = new Coordinate(degrees, minutes, seconds);

                EditText busline = findViewById(R.id.edt_busLine);
                String buslinetxt = busline.getText().toString();

                Bus newBus = new Bus(Integer.parseInt(busidtxt), longitudeCoord, latitudeCoord);
            }
        });

        Button addStation = findViewById(R.id.btnAddStation);
        addStation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("click");

                EditText stationName = findViewById(R.id.edt_stationName);
                String stationNametxt = stationName.getText().toString();

                EditText longitude = findViewById(R.id.edt_stationLongitude);
                String longitudetxt = longitude.getText().toString();
                String[] coords = longitudetxt.split("/");
                Float degrees = Float.parseFloat(coords[0]);
                Float minutes = Float.parseFloat(coords[1]);
                Float seconds = Float.parseFloat(coords[2]);
                Coordinate longitudeCoord = new Coordinate(degrees, minutes, seconds);

                EditText latitude = findViewById(R.id.edt_stationLatitude);
                String latitudetxt = latitude.getText().toString();
                coords = latitudetxt.split("/");
                degrees = Float.parseFloat(coords[0]);
                minutes = Float.parseFloat(coords[1]);
                seconds = Float.parseFloat(coords[2]);
                Coordinate latitudeCoord = new Coordinate(degrees, minutes, seconds);

                Station newStation = new Station(stationNametxt, longitudeCoord, latitudeCoord);
            }
        });
    }

    public void getUserProfile() {
        // [START get_user_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        // [END get_user_profile]
    }

    public void getProviderData() {
        // [START get_provider_data]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
            }
        }
        // [END get_provider_data]
    }

    public void updateProfile() {
        // [START update_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
        // [END update_profile]
    }

    public void updateEmail() {
        // [START update_email]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail("user@example.com")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
        // [END update_email]
    }

    public void updatePassword() {
        // [START update_password]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = "SOME-SECURE-PASSWORD";

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
        // [END update_password]
    }

    public void sendEmailVerification() {
        // [START send_email_verification]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
        // [END send_email_verification]
    }

    public void sendEmailVerificationWithContinueUrl() {
        // [START send_email_verification_with_continue_url]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String url = "http://www.example.com/verify?uid=" + user.getUid();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setIOSBundleId("com.example.ios")
                // The default for this is populated with the current android package name.
                .setAndroidPackageName("com.example.android", false, null)
                .build();

        user.sendEmailVerification(actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });

        // [END send_email_verification_with_continue_url]
        // [START localize_verification_email]
        auth.setLanguageCode("fr");
        // To apply the default app language instead of explicitly setting it.
        // auth.useAppLanguage();
        // [END localize_verification_email]
    }

    public void sendPasswordReset() {
        // [START send_password_reset]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = "user@example.com";

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
        // [END send_password_reset]
    }

    public void deleteUser() {
        // [START delete_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
        // [END delete_user]
    }

    public void reauthenticate() {
        // [START reauthenticate]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                    }
                });
        // [END reauthenticate]
    }

    public void authWithGithub() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // [START auth_with_github]
        String token = "<GITHUB-ACCESS-TOKEN>";
        AuthCredential credential = GithubAuthProvider.getCredential(token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
        // [END auth_with_github]
    }


    public void linkAndMerge(AuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // [START auth_link_and_merge]
        FirebaseUser prevUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser currentUser = task.getResult().getUser();
                        // Merge prevUser and currentUser accounts and data
                        // ...
                    }
                });
        // [END auth_link_and_merge]
    }


    public void unlink(String providerId) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // [START auth_unlink]
        mAuth.getCurrentUser().unlink(providerId)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Auth provider unlinked from account
                            // ...
                        }
                    }
                });
        // [END auth_unlink]
    }

    public void buildActionCodeSettings() {
        // [START auth_build_action_code_settings]
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.example.android",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();
        // [END auth_build_action_code_settings]
    }

    public void sendSignInLink(String email, ActionCodeSettings actionCodeSettings) {
        // [START auth_send_sign_in_link]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
        // [END auth_send_sign_in_link]
    }

    public void verifySignInLink() {
        // [START auth_verify_sign_in_link]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String emailLink = intent.getData().toString();

        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it
            String email = "someemail@domain.com";

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Successfully signed in with email link!");
                                AuthResult result = task.getResult();
                                // You can access the new user via result.getUser()
                                // Additional user info profile *not* available via:
                                // result.getAdditionalUserInfo().getProfile() == null
                                // You can check if the user is new or existing:
                                // result.getAdditionalUserInfo().isNewUser()
                            } else {
                                Log.e(TAG, "Error signing in with email link", task.getException());
                            }
                        }
                    });
        }
        // [END auth_verify_sign_in_link]
    }

    public void linkWithSignInLink(String email, String emailLink) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // [START auth_link_with_link]
        // Construct the email link credential from the current URL.
        AuthCredential credential =
                EmailAuthProvider.getCredentialWithLink(email, emailLink);

        // Link the credential to the current user.
        auth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully linked emailLink credential!");
                            AuthResult result = task.getResult();
                            // You can access the new user via result.getUser()
                            // Additional user info profile *not* available via:
                            // result.getAdditionalUserInfo().getProfile() == null
                            // You can check if the user is new or existing:
                            // result.getAdditionalUserInfo().isNewUser()
                        } else {
                            Log.e(TAG, "Error linking emailLink credential", task.getException());
                        }
                    }
                });
        // [END auth_link_with_link]
    }

    public void reauthWithLink(String email, String emailLink) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // [START auth_reauth_with_link]
        // Construct the email link credential from the current URL.
        AuthCredential credential =
                EmailAuthProvider.getCredentialWithLink(email, emailLink);

        // Re-authenticate the user with this credential.
        auth.getCurrentUser().reauthenticateAndRetrieveData(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User is now successfully reauthenticated
                        } else {
                            Log.e(TAG, "Error reauthenticating", task.getException());
                        }
                    }
                });
        // [END auth_reauth_with_link]
    }

    public void differentiateLink(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // [START auth_differentiate_link]
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                // User can sign in with email/password
                            } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
                                // User can sign in with email/link
                            }
                        } else {
                            Log.e(TAG, "Error getting sign in methods for user", task.getException());
                        }
                    }
                });
        // [END auth_differentiate_link]
    }

    public void getGoogleCredentials() {
        String googleIdToken = "";
        // [START auth_google_cred]
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
        // [END auth_google_cred]
    }

    public void getEmailCredentials() {
        String email = "";
        String password = "";
        // [START auth_email_cred]
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        // [END auth_email_cred]
    }

    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }

    public void testPhoneVerify() {
        // [START auth_test_phone_verify]
        String phoneNum = "+16505554567";
        String testVerificationCode = "123456";

        // Whenever verification is triggered with the whitelisted number,
        // provided it is not set for auto-retrieval, onCodeSent will be triggered.
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Save the verification id somewhere
                        // ...

                        // The corresponding whitelisted code above should be used to complete sign-in.
                        MainActivity.this.enableUserManuallyInputCode();
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                        // ...
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // ...
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END auth_test_phone_verify]
    }

    private void enableUserManuallyInputCode() {
        // No-op
    }

    public void testPhoneAutoRetrieve() {
        // [START auth_test_phone_auto]
        // The test phone number and code should be whitelisted in the console.
        String phoneNumber = "+16505554567";
        String smsCode = "123456";

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

        // Configure faking the auto-retrieval with the whitelisted numbers.
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // Instant verification is applied and a credential is directly returned.
                        // ...
                    }

                    // [START_EXCLUDE]
                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                    }
                    // [END_EXCLUDE]
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END auth_test_phone_auto]
    }

    // [START games_auth_with_firebase]
    // Call this both in the silent sign-in task's OnCompleteListener and in the
    // Activity's onActivityResult handler.
    private void firebaseAuthWithPlayGames(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithPlayGames:" + acct.getId());

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        AuthCredential credential = PlayGamesAuthProvider.getCredential(acct.getServerAuthCode());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    // [END games_auth_with_firebase]

    private void gamesGetUserInfo() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // [START games_get_user_info]
        FirebaseUser user = mAuth.getCurrentUser();
        String playerName = user.getDisplayName();

        // The user's Id, unique to the Firebase project.
        // Do NOT use this value to authenticate with your backend server, if you
        // have one; use FirebaseUser.getIdToken() instead.
        String uid = user.getUid();
        // [END games_get_user_info]
    }

    private void updateUI(@Nullable FirebaseUser user) {
        // No-op
    }
}