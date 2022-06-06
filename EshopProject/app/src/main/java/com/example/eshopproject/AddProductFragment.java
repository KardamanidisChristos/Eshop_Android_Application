package com.example.eshopproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;


import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class AddProductFragment extends Fragment {
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private boolean product_photo_added = false;
    Category[] categories;
    int selectedCategoryIndex;
    String[] categoriesNames;

    Spinner categorieSpinner;
    Button button;
    ImageButton attachProductImageButton;
    ImageView productImageView;

    public AddProductFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_fragments_add_product,container, false);

        findAllViews(view);
        fillCategoriesSpinner(view);

        attachProductImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProductPhoto(v);
            }
        });

        categorieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int categoryid = categories[selectedCategoryIndex].getCategoryid();

                Product product = new Product();

                product.setCategoryid(categoryid);
                product.setCost(Double.valueOf(((EditText)(getActivity().findViewById(R.id.manager_add_product_product_cost))).getText().toString()));

                product.setTitle(((EditText)(getActivity().findViewById(R.id.manager_add_product_product_title))).getText().toString());

                Bitmap bitmap = ((BitmapDrawable)(productImageView.getDrawable())).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);

                product.setBytesImage(baos.toByteArray());


                try {

                    postProduct(product,categoryid);

                } catch (InterruptedException  i) {
                    i.printStackTrace();
                }
                catch (JSONException j ) {
                    j.printStackTrace();
                }
            }
        });

        return view;
    }

    private void uploadProductPhoto(View v) {

        getView().findViewById(R.id.manager_product_attachment_button).setVisibility(View.INVISIBLE);
        openGallery();
        getView().findViewById(R.id.manager_add_product_uploaded_photo).setVisibility(View.VISIBLE);

        int dps = 250;
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        getView().findViewById(R.id.add_product_imageFL).getLayoutParams().height = pixels;
        getView().findViewById(R.id.add_product_imageFL).requestLayout();
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        getView().findViewById(R.id.manager_add_product_uploaded_photo).setVisibility(View.VISIBLE);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {

            imageUri = data.getData();
            productImageView.setImageURI(imageUri);
            product_photo_added = true;

        }

        if (resultCode == RESULT_CANCELED) {

            getView().findViewById(R.id.manager_product_attachment_button).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.add_product_imageFL).getLayoutParams().height = 0;
            getView().findViewById(R.id.add_product_imageFL).requestLayout();

        }
    }



    private void postProduct(Product product, int categoryid) throws InterruptedException, JSONException {

        byte[] image_bytes = product.getByteImage();
        String base64image = Base64.encodeToString(image_bytes, Base64.NO_WRAP);

        JSONObject postJsonObject = new JSONObject();
        postJsonObject.put("categoryid", categoryid);
        postJsonObject.put("product_title", product.getTitle());
        postJsonObject.put("product_cost", product.getCost());
        postJsonObject.put("product_image", base64image);
        postJsonObject.put("quantity", product.getQuantity());


        Log.d("Data sent", postJsonObject.toString());
        final CountDownLatch latch = new CountDownLatch(1);
        final JSONObject[] jsonObject = new JSONObject[1];

        HandlerThread thread = new HandlerThread("NetworkThread") {
            @Override
            public void run() {
                RestClient restClient = new RestClient();
                try {
                    jsonObject[0] = restClient.doPost("http://192.168.1.70//ptyxiaki/index.php/api/v1/Products", postJsonObject);

                    latch.countDown();

                } catch (IOException e) {
                    e.printStackTrace();
                    showResultDialogBox(false);

                } catch (JSONException j) {
                    j.printStackTrace();
                    showResultDialogBox(false);
                }
            };
        };

        thread.start();
        latch.await();

        if (jsonObject[0].get("pid") != null) {

            showResultDialogBox(true);

        }
    }


    private JSONArray getCategories() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);
        final JSONArray[] jsonArray = new JSONArray[1];

        HandlerThread thread = new HandlerThread("NetworkThread") {
            @Override
            public void run() {
                RestClient restClient = new RestClient();
                try {
                    jsonArray[0] = restClient.doGetRequest("http://192.168.1.70/ptyxiaki/index.php/api/v1/Categories");
                    latch.countDown();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Connection:"," Could not be established");
                    return;

                } catch (JSONException j) {
                    j.printStackTrace();
                }
            };
        };

        thread.start();
        latch.await();

        return jsonArray[0];

    }

    private Category[] getAllCategories() throws InterruptedException {
        Gson gson = new Gson();
        JSONArray categories = getCategories();
        try {
            JSONArray jsonObject = getCategories();

        } catch (InterruptedException e) {

        }

        final Category[] categoriesArray = gson.fromJson(categories.toString(), Category[].class);
        return categoriesArray;
    }

    private void fillCategoriesSpinner(View view) {
        try {
            categories = getAllCategories();
            categoriesNames = Arrays.stream(categories).map(category -> category.getCategoryname()).toArray(s->new String[categories.length]);
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, categoriesNames);
            stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorieSpinner.setAdapter(stringArrayAdapter);
        }catch (InterruptedException e) {
            Log.d("Error","Categories spinner not populated");

        }

    }

    private void findAllViews(View view) {
        categorieSpinner = view.findViewById(R.id.categories_spinner);
        button = view.findViewById(R.id.manager_add_product_button);
        attachProductImageButton = view.findViewById(R.id.manager_product_attachment_button);
        productImageView = view.findViewById(R.id.manager_add_product_uploaded_photo);
    }
    private void showResultDialogBox (boolean productPosted) {

        String message;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (productPosted) {

            message = "Product added successfully";
            builder.setIcon(R.drawable.ic_done_black_24dp);
            builder.setTitle("Information");

        } else {
            message = "Error adding product";
            builder.setIcon(R.drawable.ic_error_black_24dp);
            builder.setTitle("Warning");

        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });



        builder.setMessage(message);
        builder.setCancelable(true);


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
