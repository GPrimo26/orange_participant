package com.maksat.participantapp.ui.more.parts.feedback;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.maksat.participantapp.BuildConfig;
import com.maksat.participantapp.R;
import com.maksat.participantapp.Requests;
import com.maksat.participantapp.Variables;
import com.maksat.participantapp.interfaces.IOnBackPressed;
import com.maksat.participantapp.interfaces.Profile;
import com.maksat.participantapp.interfaces.Server;
import com.maksat.participantapp.models.ErrorBody;
import com.maksat.participantapp.ui.SharedProfileViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackFragment extends Fragment  {

    private FeedbackViewModel mViewModel;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    private MaterialButton back_btn, send_btn, clear_btn;
    private EditText title_et, text_et;
    private LinearLayout attachPhoto_ll;
    private TextView someTxt_tv;
    private ImageView imageView;
    private static final int GALLERY_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int REQUEST = 112;
    private String cameraFilePath;
    private File image;
    private File uploadFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                title_et.setText("");
                someTxt_tv.setText("");
                clear_btn.performClick();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_feedbackFragment_to_navigation_more);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feedback, container, false);
        mViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);

        findsIDs(view);
        setActions();
        return view;
    }

    private void findsIDs(View view) {
        back_btn=view.findViewById(R.id.back_btn);
        send_btn=view.findViewById(R.id.send_btn);
        title_et=view.findViewById(R.id.title_et);
        text_et=view.findViewById(R.id.text_et);
        attachPhoto_ll=view.findViewById(R.id.attach_photo_ll);
        someTxt_tv=view.findViewById(R.id.sometxt_tv);
        imageView=view.findViewById(R.id.feedback_iv);
        clear_btn=view.findViewById(R.id.remove_btn);
    }

    private void setActions() {
        back_btn.setOnClickListener(v->{
            requireActivity().onBackPressed();
        });
        attachPhoto_ll.setOnClickListener(v -> {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Загрузить из...");
                builder.setItems(new CharSequence[]{"Галереи", "Камеры"},
                        (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    captureFromGallery();
                                    break;
                                case 1:
                                    captureFromCamera();
                                    break;
                                default:
                                    break;
                            }
                        });
                builder.show();
            }catch (Exception e){
                Toast.makeText(getContext(), "Произошла ошибка. Попробуйте еще раз.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        clear_btn.setOnClickListener(v -> {
            imageView.setVisibility(View.GONE);
            clear_btn.setVisibility(View.GONE);
            someTxt_tv.setVisibility(View.VISIBLE);
            uploadFile=null;
            imageView.setImageBitmap(null);
        });
        send_btn.setOnClickListener(v -> {
            if (title_et.getText().toString().equals("")){
                title_et.setError("Данное поле должно быть заполнено");
                return;
            }

            Requests request=new Requests(requireActivity());
            RequestBody filePart = RequestBody.create(MediaType.parse("image/png"), uploadFile);
            request.setOnSentFeedbackListener(() -> {
                title_et.setText("");
                someTxt_tv.setText("");
                clear_btn.performClick();
            });
            request.sendFeedBack(title_et.getText().toString(), text_et.getText().toString(), filePart);
        });
    }

    //region Work with file
    private void captureFromGallery(){
        String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, REQUEST);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }
    }
    private boolean hasPermissions(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    private void captureFromCamera() {
        String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("RU")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
            cameraFilePath = String.valueOf(Uri.fromFile(image));
            cameraFilePath=cameraFilePath.replaceAll("file:///", "");
        } catch (IOException e) {
            Toast.makeText(getContext(), "При загрузке данных произошла ошибка. Попробуйте еще раз.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case GALLERY_REQUEST_CODE:
                        Uri selectedGaleryImage = data.getData();
                        String path = getPathFromURI(selectedGaleryImage);
                        uploadFile = new File(path);
                        uploadFile = compressImage(uploadFile, path);
                        break;
                    case CAMERA_REQUEST_CODE:
                        uploadFile = new File(cameraFilePath);
                        uploadFile = compressImage(uploadFile, cameraFilePath);
                        break;
                }
                if (uploadFile != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(uploadFile.getPath());
                    someTxt_tv.setVisibility(View.GONE);
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                    clear_btn.setVisibility(View.VISIBLE);
                }
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "Произошла ошибка. Попробуйте еще раз.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String getPathFromURI(Uri selectedGaleryImage) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(selectedGaleryImage, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;

    }

    @org.jetbrains.annotations.Nullable
    private File compressImage(File file, String FilePath) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            final int REQUIRED_SIZE = 75;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();
            ExifInterface exifInterface=null;
            try {
                exifInterface=new ExifInterface(FilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert exifInterface != null;
            int orientation=exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Matrix matrix=new Matrix();
            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                default:
            }
            assert selectedBitmap != null;
            Bitmap rotatedBitmap=Bitmap.createBitmap(selectedBitmap, 0, 0, selectedBitmap.getWidth(), selectedBitmap.getHeight(), matrix, true);

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return file;
        } catch (Exception e) {
            Log.d("Не удалось сжать", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    //endregion Work with file


}