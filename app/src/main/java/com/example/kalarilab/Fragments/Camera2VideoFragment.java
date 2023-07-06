package com.example.kalarilab.Fragments;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;

import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.kalarilab.AutoFitTextureView;
import com.example.kalarilab.MediaLink;
import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.Post;
import com.example.kalarilab.R;
import com.example.kalarilab.Repo.MainRepo;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.ViewModels.AuthViewModel;
import com.example.kalarilab.db.AdminEntry;
import com.example.kalarilab.db.AuthEntry;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Camera2VideoFragment extends androidx.fragment.app.Fragment
        implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, MainRepo.RepoCallBack {

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;

    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();
    private static final String TAG = "Camera2VideoFragment";
    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    private String VIDEO_DIRECTORY_NAME = "KalariLab";
    private AuthViewModel authViewModel;
    private AuthModel authModel1 = new AuthModel();

    private SessionManagement sessionManagement;
    private Post post = new Post();


    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };

    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }
    private VideoView videoView;
    private CountDownTimer mCountDownTimer;
    private ImageButton sendBtn, deleteVidBtn, flipCamera;


    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    private MediaLink mediaLink;
    private TextureView mTextureView;
    private View view;
    /**
     * Button to record video
     */
    private ImageButton mButtonVideo;

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private CameraDevice mCameraDevice;

    /**
     * A reference to the current {@link CameraCaptureSession} for
     * preview.
     */
    private CameraCaptureSession mPreviewSession;
    private  CameraManager manager;
    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                              int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                                int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

    };
    private TextView counter;


    /**
     * The {@link Size} of camera preview.
     */
    private Size mPreviewSize;
    private String cameraId;

    /**
     * The {@link Size} of video recording.
     */
    private Size mVideoSize;

    /**
     * MediaRecorder
     */
    private MediaRecorder mMediaRecorder;

    /**
     * Whether the app is recording video now
     */
    private boolean mIsRecordingVideo;

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its status.
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {

            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            if (null != mTextureView) {
                configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }

    };
    private Integer mSensorOrientation;
    private String mNextVideoAbsolutePath;
    private CaptureRequest.Builder mPreviewBuilder;

    public static Camera2VideoFragment newInstance() {
        return new Camera2VideoFragment();
    }

    /**
     * In this sample, we choose a video size with 3x4 aspect ratio. Also, we don't use sizes
     * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
     *
     * @param choices The list of available sizes
     * @return The video size
     */
    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the respective requested values, and whose aspect
     * ratio matches with the specified value.
     *
     * @param choices     The list of sizes that the camera supports for the intended output class
     * @param width       The minimum desired width
     * @param height      The minimum desired height
     * @param aspectRatio The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera2_video, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        this.view  = view;
        sessionManagement = new SessionManagement(getActivity());
        counter  = (TextView) view.findViewById( R.id.count_down );
        videoView = (VideoView) view.findViewById(R.id.videoView);
        sendBtn = (ImageButton) view.findViewById(R.id.sendBtn);
        deleteVidBtn = (ImageButton) view.findViewById(R.id.deleteVidBtn);
        mTextureView = (TextureView) view.findViewById(R.id.texture);
        mButtonVideo = (ImageButton) view.findViewById(R.id.video);
        mButtonVideo.setOnClickListener(this);
        mediaLink = new MediaLink();
        authViewModel = new AuthViewModel();
        flipCamera = (ImageButton) view.findViewById(R.id.flipCamera);
        flipCamera.setOnClickListener(this);
        authViewModel.setActivity(getActivity());
        try {
            authViewModel.init();

        }catch (Exception e){
            android.util.Log.d(TAG, e.getMessage());
        }
        manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId = manager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        observeData();
        if(!sessionManagement.returnChallengesFragmentAlreadyVisited()){
        showPrimaryTutorialSequence();}
    }

    private void showPrimaryTutorialSequence() {

        TapTargetView.showFor(getActivity(),                 // `this` is the current Activity
                TapTarget.forView(view.findViewById(R.id.flipCamera), "Change The camera!", "Click on it to flip to another camera.").outerCircleColor(R.color.KalariLAbSecondary).cancelable(true));


    }

    private void observeData() {
        authViewModel.getmAuthModel().observe(getActivity(), new Observer<AuthModel>() {
            @Override
            public void onChanged(AuthModel authModel) {
                authModel1 = authModel;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video: {
                if (mIsRecordingVideo) {
                    changeRecordIcon();
                    counter.setText("");
                    mCountDownTimer.cancel();
                    stopRecordingVideo();
                    if(!sessionManagement.returnChallengesFragmentAlreadyVisited()){
                        showSecondaryTutorialSequence();
                    }
                } else {
                    changeRecordIcon();
                    countDownTimer();
                    flipCamera.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.info: {
                Activity activity = getActivity();
                if (null != activity) {
                    new AlertDialog.Builder(activity)
                            .setMessage(R.string.intro_message)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
                break;
            }
            case R.id.deleteVidBtn:
                videoView.setVisibility(View.GONE);
                deleteVidBtn.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
                mButtonVideo.setVisibility(View.VISIBLE);
                deleteVideo();
                mNextVideoAbsolutePath = "";

                break;
            case  R.id.sendBtn:
                videoView.setVisibility(View.GONE);
                deleteVidBtn.setVisibility(View.GONE);
                sendBtn.setVisibility(View.GONE);
                mButtonVideo.setVisibility(View.VISIBLE);
                flipCamera.setVisibility(View.VISIBLE);
                sendVideoToDB();
                uploadVideoDownloadLink();
                break;
            case R.id.flipCamera:
                try {
                    flipToAnotherCamera();
                } catch (CameraAccessException e) {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();
                }

        }
    }

    private void showSecondaryTutorialSequence() {
        new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(view.findViewById(R.id.deleteVidBtn), "Delete Video!", "This button removes video from storage.").outerCircleColor(R.color.KalariLAbSecondary),
                        TapTarget.forView(view.findViewById(R.id.sendBtn), "Send Video!", "This button sends video to database.")

                                .outerCircleColor(R.color.KalariLAbSecondary)
                                .cancelable(true)
                ).start();

        sessionManagement.setChallengesFragmentAlreadyVisited();
    }

    private void flipToAnotherCamera() throws CameraAccessException {




            closeCamera();
//swap the id of the camera to be used
                if(cameraId == manager.getCameraIdList()[1] ){
                    cameraId = manager.getCameraIdList()[0];
                }
                else {
                    cameraId = manager.getCameraIdList()[1];
                }
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
            }


    private void uploadVideoDownloadLink() {
        Thread uploadVideoDownloadLinkThread = new Thread(new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mediaLink.changed()){
                    synchronized (mediaLink){
                        try {
                            Log.d("Waiting_thread", "waiting");
                            mediaLink.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                post.setUserId(FirebaseAuth.getInstance().
                        getCurrentUser().getUid());
                post.setLevel(String.valueOf(sessionManagement.returnCurrLevel()));
                post.setChallenge(String.valueOf(sessionManagement.returnCurrChallenge()));
                post.setFullName(authModel1.getFullName());
                post.setUri(mediaLink.getUri());
                getToken();





            }
        }));
        uploadVideoDownloadLinkThread.start();

    }

    private void getToken() {
        MainRepo mainRepo = MainRepo.getInstance(getActivity()); //getting a singleton instance of the repository.
        mainRepo.setRepoCallback(this);
        mainRepo.getToken();
    }

    public Uri myUri(Uri originalUri){
        Uri returnedUri = null;
        if (originalUri.getScheme() == null){
            returnedUri = Uri.fromFile(new File(mNextVideoAbsolutePath));
            // or you can just do -->
            // returnedUri = Uri.parse("file://"+camUri.getPath());
        }else{
            returnedUri = originalUri;
        }
        return returnedUri;
    }


    private void sendVideoToDB() {



        Thread sendVideoThread = new Thread(new Runnable()  {

            @Override
            public void run() {
                if (mNextVideoAbsolutePath == null){
                }
                else {
                }
                final StorageReference reference = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+
                        sessionManagement.returnCurrLevel()+"/"+sessionManagement.returnCurrChallenge());
                reference.putFile(myUri(Uri.parse(mNextVideoAbsolutePath))).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mediaLink.setUri(uri.toString());
                                mediaLink.changeLink();
                                synchronized (mediaLink) {
                                    mediaLink.notifyAll();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                });
            }
        });

        sendVideoThread.start();

    }





    private void deleteVideo() {
        File fdelete = new File(mNextVideoAbsolutePath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.d(TAG, "Video deleted!");
            } else {
                Log.d(TAG, "Video not deleted!");
            }
        }
        flipCamera.setVisibility(View.VISIBLE);

    }

    private void changeRecordIcon() {
        if (mIsRecordingVideo){
            mButtonVideo.setImageResource(R.drawable.rec);
        }
        else {
            mButtonVideo.setImageResource(R.drawable.stop_record);

        }
    }

    private void countDownTimer() {
        mButtonVideo.setEnabled(false);
        mCountDownTimer = new CountDownTimer(5200, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText(String.valueOf(millisUntilFinished / 1000));
                callBeep(millisUntilFinished);


            }

            public void onFinish() {
                mButtonVideo.setEnabled(true);
                startRecordingForTwentySeconds();
            }
        }.start();
    }

    private void callBeep(long millisUntilFinished) {
        Thread beepThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 300);
                if (millisUntilFinished / 1000 != 0) {
                    toneGen.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 200);
                }else {


                        toneGen.startTone(ToneGenerator.TONE_CDMA_ABBR_REORDER, 200);


                }
            }
        });
        beepThread.run();
    }

    private void startRecordingForTwentySeconds() {
        startRecordingVideo();
        mCountDownTimer =  new CountDownTimer(15200, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                changeRecordIcon();
                stopRecordingVideo();
            }
        }.start();
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets whether you should show UI with rationale for requesting permissions.
     *
     * @param permissions The permissions your app wants to request.
     * @return Whether you can show permission rationale UI.
     */
    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Requests permissions needed for recording video.
     */
    private void requestVideoPermissions() {
        if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            if (grantResults.length == VIDEO_PERMISSIONS.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        break;
                    }
                }
            } else {

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "granted");
            } else {
                Log.d(TAG, "denied");
            }
        }
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tries to open a {@link CameraDevice}. The result is listened by `mStateCallback`.
     */
    private void checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "01");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void openCamera(int width, int height) {
        checkAndRequestCameraPermission();
        if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
            requestVideoPermissions();
            return;
        }
        if (null == getActivity() || getActivity().isFinishing()) {
            return;
        }
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }

            // Choose the sizes for camera preview and video recording
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (map == null) {
                throw new RuntimeException("Cannot get available preview/video sizes");
            }
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    width, height, mVideoSize);

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                //mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
            configureTransform(width, height);
            mMediaRecorder = new MediaRecorder();

            manager.openCamera(cameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            Toast.makeText(getActivity(), "Cannot access the camera.", Toast.LENGTH_SHORT).show();
           getActivity().finish();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.

        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Start the camera preview.
     */
    private void startPreview() {
        Log.d("previewDebug", "4");

        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            Log.d("previewDebug", "0");
            return;
        }
        try {

            closePreviewSession();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface previewSurface = new Surface(texture);
            mPreviewBuilder.addTarget(previewSurface);

            mCameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            mPreviewSession = session;
                            Log.d("previewDebug", "7");

                            updatePreview();
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Log.d("previewDebug", "1");


                            if (null != getActivity()) {
                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            Log.d("previewDebug", e.getMessage());

            e.printStackTrace();
        }
    }

    /**
     * Update the camera preview. {@link #startPreview()} needs to be called in advance.
     */
    private void updatePreview() {

        if (null == mCameraDevice) {

            return;
        }
        try {

            setUpCaptureRequestBuilder(mPreviewBuilder);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {

            e.printStackTrace();
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    }

    /**
     * Configures the necessary {@link Matrix} transformation to `mTextureView`.
     * This method should not to be called until the camera preview size is determined in
     * openCamera, or until the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void setUpMediaRecorder() throws IOException {
        final Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty()) {
            mNextVideoAbsolutePath = getVideoFilePath(getActivity());
        }
        mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (mSensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        mMediaRecorder.prepare();
    }

    private String getVideoFilePath(Context context) {

        final File dir = context.getExternalFilesDir(null);

        // Create storage directory if it does not exist
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + VIDEO_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        return dir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4";
    }

    private void startRecordingVideo() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            closePreviewSession();
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            // Set up Surface for the camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);

            // Set up Surface for the MediaRecorder
            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            mPreviewBuilder.addTarget(recorderSurface);

            // Start a capture session
            // Once the session starts, we can update the UI and start recording
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // UI

                            mIsRecordingVideo = true;

                            // Start recording
                            mMediaRecorder.start();
                        }
                    });
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Activity activity = getActivity();
                    if (null != activity) {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException | IOException e) {
            e.printStackTrace();
        }

    }

    private void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

    private void stopRecordingVideo() {
        // UI
        mIsRecordingVideo = false;
        // Stop recording
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
        }catch (Exception e){

        }



        if (null != getActivity()) {

            Log.d(TAG, "Video saved: " + mNextVideoAbsolutePath);
        }
        startPlayingVideo();
        startPreview();
    }

    private void startPlayingVideo() {
        videoView.setVideoPath(mNextVideoAbsolutePath);
        videoView.setVisibility(View.VISIBLE);
        mButtonVideo.setVisibility(View.GONE);
        deleteVidBtn.setVisibility(View.VISIBLE);
        sendBtn.setVisibility(View.VISIBLE);
        deleteVidBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        videoView.start();

    }

    @Override
    public void getPanelLiveData(List<String> fullNames, List<String> challenges, List<String> levels, List<String> uris, List<String> usersIds, List<String> tokens) throws JSONException {

    }

    @Override
    public void getAdminsList(LiveData<List<AdminEntry>> adminsList) {

    }

    @Override
    public void getAuthData(LiveData<AuthEntry> authEntryLiveData) {

    }

    @Override
    public void getToken(String token) {
        post.setToken(token);
        FirebaseDatabase.getInstance().getReference("Posts").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()+sessionManagement.returnCurrLevel()+sessionManagement.returnCurrChallenge()).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(getActivity() != null) Toast.makeText(getActivity(), "video uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
 * Compares two {@code Size}s based on their areas.
 */
static class CompareSizesByArea implements Comparator<Size> {

    @Override
    public int compare(Size lhs, Size rhs) {
        // We cast here to ensure the multiplications won't overflow
        return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                (long) rhs.getWidth() * rhs.getHeight());
    }

}

public static class ErrorDialog extends android.app.DialogFragment {

    private static final String ARG_MESSAGE = "message";

    public static ErrorDialog newInstance(String message) {
        ErrorDialog dialog = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        return new AlertDialog.Builder(activity)
                .setMessage(getArguments().getString(ARG_MESSAGE))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                })
                .create();
    }

}

public static class ConfirmationDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final android.app.Fragment parent = getParentFragment();
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.permission_request)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(parent.getActivity(), VIDEO_PERMISSIONS,
                                REQUEST_VIDEO_PERMISSIONS);
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                parent.getActivity().finish();
                            }
                        })
                .create();
    }

}}



