package com.example.oritoledanoproject.Data.FirebaseHelper;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.oritoledanoproject.Data.CurrentUser.CurrentUser;
import com.example.oritoledanoproject.UI.Store.StoreActivity;
import com.example.oritoledanoproject.UI.Store.newProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class FirebaseHelper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    Context context;

    public FirebaseHelper(Context context) {
        this.context = context;
    }

    public interface productsFetched
    {
        void onProductsFetched(LinkedList<Map<String, Object>> list);
    }
    public interface Counter
    {
        void onCounted(int Amount);
    }

    LinkedList<Integer> currentUser = new LinkedList<>();
    public void CountProducts(Counter callback)
    {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskUsers) {

                int totalUsers = taskUsers.getResult().size();
                for (DocumentSnapshot document : taskUsers.getResult())
                {
                    db.collection("users").document(document.getId()).collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                currentUser.add(task.getResult().size());
                                String num1 =String.valueOf(totalUsers);
                                String num2 = String.valueOf(currentUser.size());
                                if(num1.equals(num2))
                                {
                                    int total = 0;
                                    for (int i = 0; i < currentUser.size(); i++) {
                                        total += currentUser.get(i);
                                    }
                                    callback.onCounted(total);
                                }

                            }
                        }
                    });
                }

            }
        });
    }

    public void getProducts(productsFetched callback)
    {
        CountProducts(new Counter() {
            @Override
            public void onCounted(int productsAmount) {
                LinkedList<Map<String, Object>> productList = new LinkedList<>();
                if(productsAmount == 0)
                {
                    callback.onProductsFetched(productList);
                }

                db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (DocumentSnapshot document : task.getResult())
                        {
                            db.collection("users").document(document.getId()).collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        for (DocumentSnapshot documentProduct : task.getResult())
                                        {
                                            Map<String, Object> product = new HashMap<>();

                                            StorageReference reportImageRef = storage.getReferenceFromUrl("gs://oritoledanoproject.appspot.com" + documentProduct.getData().get("photoPath").toString());
                                            reportImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    product.put("picture", uri);
                                                    product.put("description", documentProduct.getData().get("description"));
                                                    product.put("gender", documentProduct.getData().get("gender"));
                                                    product.put("situation", documentProduct.getData().get("situation"));
                                                    product.put("type", documentProduct.getData().get("type"));
                                                    product.put("price", documentProduct.getData().get("price"));
                                                    product.put("name", document.getData().get("name"));
                                                    product.put("phone", document.getData().get("phone"));
                                                    product.put("address", document.getData().get("address"));

                                                    productList.add(product);

                                                    if(productList.size() == productsAmount)
                                                    {
                                                        callback.onProductsFetched(productList);
                                                    }
                                                }

                                            });
                                        }

                                    }
                                }
                            });
                        }

                    }
                });


            }
        });
    }



    public interface UserFound
    {
        void onUserFound();
    }

    public void getUser(String email, String password ,UserFound callback)
    {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean flag = false;
                for(DocumentSnapshot documentSnapshot : task.getResult())
                {
                    if(documentSnapshot.getData().get("email").toString().equals(email) && documentSnapshot.getData().get("password").toString().equals(password))
                    {
                        CurrentUser.initializeUser(documentSnapshot.getData().get("name").toString(), documentSnapshot.getData().get("email").toString(), documentSnapshot.getId());
                        callback.onUserFound();
                        flag = true;
                    }
                }
                if(!flag)
                {
                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface UserFetched
    {
        void onUserFetched(boolean flag);
    }
    public void emailIsExist(String email ,UserFetched callback)
    {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean emailFound = false;
                for(DocumentSnapshot documentSnapshot : task.getResult())
                {
                    if(documentSnapshot.getData().get("email").toString().equals(email))
                    {
                        emailFound = true;
                    }
                }

                callback.onUserFetched(emailFound);
            }
        });

    }


    public void addProduct(String gender, String type, String situation, String description, String price ,Bitmap photo) {
        StorageReference ImageRef = storageRef.child("images/" + UUID.randomUUID());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        ImageRef.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    Map<String, Object> product = new HashMap<>();
                    product.put("gender", gender);
                    product.put("type", type);
                    product.put("situation", situation);
                    product.put("description", description);
                    product.put("price", price);
                    product.put("photoPath", ImageRef.getPath());



                    db.collection("users").document(CurrentUser.getFireID()).collection("products").add(product).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, StoreActivity.class);
                                startActivity(context,intent, null);
                            }
                            else Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    public void addUser(String name, String password, String email, String address, String phone) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("password", password);
        user.put("email", email);
        user.put("address", address);
        user.put("phone", phone);


// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        CurrentUser.initializeUser(name, email, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


}
