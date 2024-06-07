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
    // משתנים עבור Firestore ו-Firebase Storage
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    Context context;

    public FirebaseHelper(Context context) {
        this.context = context;
    }

    public interface productsFetched {
        void onProductsFetched(LinkedList<Map<String, Object>> list);
    }

    // ממשק להחזרת כמות מוצרים
    public interface Counter {
        void onCounted(int Amount);
    }

    LinkedList<Integer> currentUser = new LinkedList<>();

    // פונקציה לספירת מוצרים
    public void CountProducts(Counter callback) {
        // קבלת רשימת המשתמשים מהמסד נתונים
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskUsers) {
                int totalUsers = taskUsers.getResult().size();
                for (DocumentSnapshot document : taskUsers.getResult()) {
                    // קבלת רשימת המוצרים של כל משתמש
                    db.collection("users").document(document.getId()).collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                currentUser.add(task.getResult().size());
                                String num1 = String.valueOf(totalUsers);
                                String num2 = String.valueOf(currentUser.size());
                                // בדיקה אם כל המשתמשים נבדקו
                                if (num1.equals(num2)) {
                                    int total = 0;
                                    // חישוב סך כל המוצרים
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

    public void getProducts(productsFetched callback) {
        CountProducts(new Counter() {
            @Override
            public void onCounted(int productsAmount) {
                LinkedList<Map<String, Object>> productList = new LinkedList<>();
                // אם אין מוצרים
                if (productsAmount == 0) {
                    callback.onProductsFetched(productList);
                }

                // קבלת רשימת המשתמשים
                db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {
                            // קבלת רשימת המוצרים של כל משתמש
                            db.collection("users").document(document.getId()).collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot documentProduct : task.getResult()) {
                                            Map<String, Object> product = new HashMap<>();
                                            // קבלת כתובת התמונה מהמוצר
                                            StorageReference reportImageRef = storage.getReferenceFromUrl("gs://oritoledanoproject.appspot.com" + documentProduct.getData().get("photoPath").toString());
                                            reportImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    // הוספת פרטי המוצר למפה
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

                                                    // בדיקה אם כל המוצרים נוספו לרשימה
                                                    if (productList.size() == productsAmount) {
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

    // ממשק להחזרת משתמש נמצא
    public interface UserFound {
        void onUserFound();
    }

    // פונקציה לקבלת משתמש לפי אימייל וסיסמה
    public void getUser(String email, String password, UserFound callback) {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean flag = false;
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    // בדיקה אם האימייל והסיסמה תואמים
                    if (documentSnapshot.getData().get("email").toString().equals(email) && documentSnapshot.getData().get("password").toString().equals(password)) {
                        // אתחול המשתמש הנוכחי
                        CurrentUser.initializeUser(documentSnapshot.getData().get("name").toString(), documentSnapshot.getData().get("email").toString(), documentSnapshot.getId());
                        callback.onUserFound();
                        flag = true;
                    }
                }
                // אם המשתמש לא נמצא
                if (!flag) {
                    Toast.makeText(context, "איימיל או סיסמא לא נכונים", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

public interface gotuser{
        void ongotuser(String name,String pass,String email,String phone,String addres);
    }


    public void getUser(String FireId, gotuser gotuser) {
        db.collection("users").document(FireId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                gotuser.ongotuser(task.getResult().getData().get("name").toString(),task.getResult().getData().get("password").toString(),task.getResult().getData().get("email").toString(),task.getResult().getData().get("phone").toString(),task.getResult().getData().get("address").toString());

            }
        });
    }

    public interface userprodute{
        void userProduct(LinkedList<Map<String,Object>> products);
    }
    public void getUsersProduct(String fireId,userprodute userprodute) {
        db.collection("users").document(fireId).collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<Map<String,Object>> products = new LinkedList<>();
                for (DocumentSnapshot documentSnapshot :task.getResult()) {
                    storageRef.child(documentSnapshot.getData().get("photoPath").toString()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task2) {
                            Map<String,Object> m = new HashMap();
                            m.put("description",documentSnapshot.getData().get("description"));
                            m.put("price",documentSnapshot.getData().get("price"));
                            m.put("situation",documentSnapshot.getData().get("situation"));
                            m.put("gender",documentSnapshot.getData().get("gender"));
                            m.put("type",documentSnapshot.getData().get("type"));
                            m.put("photo",task2.getResult());
                            m.put("id",documentSnapshot.getId());
                            products.add(m);
                            if (products.size()==task.getResult().size()){
                                userprodute.userProduct(products);
                            }
                        }
                    });
                }
                userprodute.userProduct(products);

                }

        });
    }


    public void deleteProduct(String fireId,String productId ) {
        db.collection("users").document(fireId).collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (DocumentSnapshot documentSnapshot :task.getResult()) {
                    if (documentSnapshot.getId().equals(productId)) {
                        storageRef.child(documentSnapshot.getData().get("photoPath").toString()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                db.collection("users").document(fireId).collection("products").document(documentSnapshot.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "נמחק בהצלחה", Toast.LENGTH_SHORT).show();


                                    }
                                });
                            }
                        });
                    }

                }
            }
        });
    }

    // ממשק להחזרת אימייל קיים
    public interface UserFetched {
        void onUserFetched(boolean flag);
    }

    // פונקציה לבדוק אם אימייל קיים
    public void emailIsExist(String email, UserFetched callback) {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean emailFound = false;
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    // בדיקה אם האימייל קיים
                    if (documentSnapshot.getData().get("email").toString().equals(email)) {
                        emailFound = true;
                    }
                }
                callback.onUserFetched(emailFound);
            }
        });
    }

    // פונקציה להוספת מוצר
    public void addProduct(String gender, String type, String situation, String description, String price, Bitmap photo) {
        StorageReference ImageRef = storageRef.child("images/" + UUID.randomUUID());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        ImageRef.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("gender", gender);
                    product.put("type", type);
                    product.put("situation", situation);
                    product.put("description", description);
                    product.put("price", price);
                    product.put("photoPath", ImageRef.getPath());

                    // הוספת המוצר למסד הנתונים של המשתמש הנוכחי
                    db.collection("users").document(CurrentUser.getFireID()).collection("products").add(product).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "הוסף בהצלחה", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, StoreActivity.class);
                                startActivity(context, intent, null);
                            } else {
                                Toast.makeText(context, "התמונה לא עלתה", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    // פונקציה להוספת משתמש
    public void addUser(String name, String password, String email, String address, String phone) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("password", password);
        user.put("email", email);
        user.put("address", address);
        user.put("phone", phone);

        // הוספת משתמש חדש עם מזהה שנוצר
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // אתחול המשתמש הנוכחי
                        CurrentUser.initializeUser(name, email, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public interface userupdated{
        void onupdatecomleted();

    }

    public void updateUser(String name, String password, String email, String address, String phone,userupdated userupdated) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("password", password);
        user.put("email", email);
        user.put("address", address);
        user.put("phone", phone);


        // הוספת משתמש חדש עם מזהה שנוצר
        db.collection("users").document(CurrentUser.getFireID())
                .update(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        userupdated.onupdatecomleted();
                    }
                });

    }

}
