package com.keyboard.funstickers;

/*
 Created by Shalu Dhochak on 5/8/2018.
*/

import android.app.AppOpsManager;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.design.widget.TabLayout;
import android.support.v13.view.inputmethod.EditorInfoCompat;
import android.support.v13.view.inputmethod.InputConnectionCompat;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomKeyboard  extends InputMethodService implements TabLayout.OnTabSelectedListener, View.OnClickListener, View.OnLongClickListener, KeyboardView.OnKeyboardActionListener {
    public static final String TAG = "ImageKeyboard";
    public static final String AUTHORITY = "com.apnaji.keyboard";
    public static final String MIME_TYPE_PNG = "image/png";
    private static final String MIME_TYPE_GIF = "image/gif";

    File mPngFile = null;
    public static File imagesDir, tempDir;
    Context context;
    HorizontalScrollView mHorizontalScrollView;
    public View tabView;
    private Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_q, btn_w, btn_e, btn_r, btn_t, btn_y, btn_u, btn_i, btn_o, btn_p, btn_a, btn_s, btn_d, btn_f,
            btn_g, btn_h, btn_j, btn_k, btn_l, btn_z, btn_x, btn_c, btn_v, btn_b, btn_n, btn_m, btn_questionMark, btn_spacebar, btn_num_switch;
    private Button btn_exclamation, btn_atRate, btn_hash, btn_dollar, btn_divide, btn_carrat, btn_ampersand, btn_mult, btn_dot,
            btn_openBrac, btn_closingBrac;
    private Button btn_caps, btn_backspace, btn_enter, btn_go;
    private RelativeLayout layout_keyboard;
    private LinearLayout level1,level2, level5;
    private LinearLayout.LayoutParams params, layoutParamsKeyboard;
    InputConnection currentInputConnection;
    private DatabaseHelper databaseHelper;


    private boolean validatePackageName(@Nullable EditorInfo editorInfo) {
        if (editorInfo == null) {
            return false;
        }
        final String packageName = editorInfo.packageName;
        if (packageName == null) {
            return false;
        }

        final InputBinding inputBinding = getCurrentInputBinding();
        if (inputBinding == null) {
            return false;
        }
        final int packageUid = inputBinding.getUid();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final AppOpsManager appOpsManager =
                    (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            try {
                appOpsManager.checkPackage(packageUid, packageName);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        final PackageManager packageManager = getPackageManager();
        final String possiblePackageNames[] = packageManager.getPackagesForUid(packageUid);
        for (final String possiblePackageName : possiblePackageNames) {
            if (packageName.equals(possiblePackageName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        context = this;
        super.onCreate();

        imagesDir = new File(getFilesDir(), "images");
        imagesDir.mkdirs();

        tempDir = new File(getFilesDir(), "com/apnaji/keyboard/stickers");
        tempDir.mkdirs();

        final File imagesDir = new File(getFilesDir(), "images");
        imagesDir.mkdirs();

    }

    @Override
    public boolean onEvaluateInputViewShown() {
        return super.onEvaluateInputViewShown();
    }

    @Override
    public boolean isInputViewShown() {
        return super.isInputViewShown();
    }

    @Override
    public boolean isShowInputRequested() {
        return super.isShowInputRequested();
    }

    @Override
    public void updateInputViewShown() {
        super.updateInputViewShown();
    }

    @Override
    public View onCreateInputView() {
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tabView = layoutInflater.inflate(R.layout.layout_custom_keyboard, null);
        tabView.findViewById(R.id.main_layout).setBackgroundColor(0xFFFFFFFF);

        layout_keyboard = (RelativeLayout) tabView.findViewById(R.id.layout_keyboard);
        initKeyBoardUI();
        initKeyBoardListener();
        mHorizontalScrollView = (HorizontalScrollView) tabView.findViewById(R.id.horizontalScrollView);
        mHorizontalScrollView.setVisibility(View.GONE);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        tabView.setLayoutParams(params);
        layout.addView(tabView);

        return layout;
    }

    private void initKeyBoardListener() {

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);

        btn_exclamation.setOnClickListener(this);
        btn_atRate.setOnClickListener(this);
        btn_hash.setOnClickListener(this);
        btn_dollar.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
        btn_carrat.setOnClickListener(this);
        btn_ampersand.setOnClickListener(this);
        btn_mult.setOnClickListener(this);
        btn_openBrac.setOnClickListener(this);
        btn_closingBrac.setOnClickListener(this);

        btn_q.setOnClickListener(this);
        btn_w.setOnClickListener(this);
        btn_e.setOnClickListener(this);
        btn_r.setOnClickListener(this);
        btn_t.setOnClickListener(this);
        btn_y.setOnClickListener(this);
        btn_u.setOnClickListener(this);
        btn_i.setOnClickListener(this);
        btn_o.setOnClickListener(this);
        btn_p.setOnClickListener(this);
        btn_a.setOnClickListener(this);
        btn_s.setOnClickListener(this);
        btn_d.setOnClickListener(this);
        btn_f.setOnClickListener(this);
        btn_g.setOnClickListener(this);
        btn_h.setOnClickListener(this);
        btn_j.setOnClickListener(this);
        btn_k.setOnClickListener(this);
        btn_l.setOnClickListener(this);
        btn_z.setOnClickListener(this);
        btn_x.setOnClickListener(this);
        btn_c.setOnClickListener(this);
        btn_v.setOnClickListener(this);
        btn_b.setOnClickListener(this);
        btn_n.setOnClickListener(this);
        btn_m.setOnClickListener(this);
        btn_enter.setOnClickListener(this);
        btn_questionMark.setOnClickListener(this);
        btn_spacebar.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        btn_num_switch.setOnClickListener(this);
        btn_caps.setOnClickListener(this);
        btn_spacebar.setOnLongClickListener(this);
        btn_backspace.setOnClickListener(this);
        btn_backspace.setOnLongClickListener(this);
        btn_dot.setOnClickListener(this);

        btn_backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                currentInputConnection = getCurrentInputConnection();
                CharSequence currentText = currentInputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
                CharSequence beforCursorText = currentInputConnection.getTextBeforeCursor(currentText.length(), 0);
                CharSequence afterCursorText = currentInputConnection.getTextAfterCursor(currentText.length(), 0);
                currentInputConnection.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());

                charSequence = currentInputConnection.getTextBeforeCursor(15, 0);

                if (charSequence != null && !containsDigit(charSequence + ""))
                    showStickerInfoFromDatabase("ZF");

                return false;
            }
        });
    }

    private void initKeyBoardUI() {

        level1 = tabView.findViewById(R.id.level1);
        level2 = tabView.findViewById(R.id.level2);
        level5 = tabView.findViewById(R.id.level5);

        btn_0 = tabView.findViewById(R.id.btn_0);
        btn_1 = tabView.findViewById(R.id.btn_1);
        btn_2 =  tabView.findViewById(R.id.btn_2);
        btn_3 =  tabView.findViewById(R.id.btn_3);
        btn_4 =  tabView.findViewById(R.id.btn_4);
        btn_5 = tabView.findViewById(R.id.btn_5);
        btn_6 =  tabView.findViewById(R.id.btn_6);
        btn_7 =  tabView.findViewById(R.id.btn_7);
        btn_8 =  tabView.findViewById(R.id.btn_8);
        btn_9 =  tabView.findViewById(R.id.btn_9);

        btn_exclamation = (Button) tabView.findViewById(R.id.btn_exclamation);
        btn_atRate = (Button) tabView.findViewById(R.id.btn_atRate);
        btn_hash = (Button) tabView.findViewById(R.id.btn_hash);
        btn_dollar = (Button) tabView.findViewById(R.id.btn_dollar);
        btn_divide = (Button) tabView.findViewById(R.id.btn_divide);
        btn_carrat = (Button) tabView.findViewById(R.id.btn_carrat);
        btn_ampersand = (Button) tabView.findViewById(R.id.btn_ampersand);
        btn_mult = (Button) tabView.findViewById(R.id.btn_mult);
        btn_openBrac = (Button) tabView.findViewById(R.id.btn_openBrac);
        btn_closingBrac = (Button) tabView.findViewById(R.id.btn_closingBrac);

        btn_q = (Button) tabView.findViewById(R.id.btn_q);
        btn_w = (Button) tabView.findViewById(R.id.btn_w);
        btn_e = (Button) tabView.findViewById(R.id.btn_e);
        btn_r = (Button) tabView.findViewById(R.id.btn_r);
        btn_t = (Button) tabView.findViewById(R.id.btn_t);
        btn_y = (Button) tabView.findViewById(R.id.btn_y);
        btn_u = (Button) tabView.findViewById(R.id.btn_u);
        btn_i = (Button) tabView.findViewById(R.id.btn_i);
        btn_o = (Button) tabView.findViewById(R.id.btn_o);
        btn_p = (Button) tabView.findViewById(R.id.btn_p);
        btn_a = (Button) tabView.findViewById(R.id.btn_a);
        btn_s = (Button) tabView.findViewById(R.id.btn_s);
        btn_d = (Button) tabView.findViewById(R.id.btn_d);
        btn_f = (Button) tabView.findViewById(R.id.btn_f);
        btn_g = (Button) tabView.findViewById(R.id.btn_g);
        btn_h = (Button) tabView.findViewById(R.id.btn_h);
        btn_j = (Button) tabView.findViewById(R.id.btn_j);
        btn_k = (Button) tabView.findViewById(R.id.btn_k);
        btn_l = (Button) tabView.findViewById(R.id.btn_l);
        btn_z = (Button) tabView.findViewById(R.id.btn_z);
        btn_x = (Button) tabView.findViewById(R.id.btn_x);
        btn_c = (Button) tabView.findViewById(R.id.btn_c);
        btn_v = (Button) tabView.findViewById(R.id.btn_v);
        btn_b = (Button) tabView.findViewById(R.id.btn_b);
        btn_n = (Button) tabView.findViewById(R.id.btn_n);
        btn_m = (Button) tabView.findViewById(R.id.btn_m);
        btn_questionMark = (Button) tabView.findViewById(R.id.btn_questionMark);
        btn_spacebar = (Button) tabView.findViewById(R.id.btn_spacebar);
        btn_num_switch = (Button) tabView.findViewById(R.id.btn_num_switch);
        btn_dot = (Button) tabView.findViewById(R.id.btn_dot);

        btn_go = (Button) tabView.findViewById(R.id.btn_go);
        btn_enter = (Button) tabView.findViewById(R.id.btn_enter);
        btn_backspace = (Button) tabView.findViewById(R.id.btn_backspace);
        btn_caps = (Button) tabView.findViewById(R.id.btn_caps);
        btn_caps.setTag("uppercase");
        layoutParamsKeyboard = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_keyboard.setLayoutParams(layoutParamsKeyboard);
    }


    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
       /* System.out.println("pager: onStartInputView: ================");
        String[] mimeTypes = EditorInfoCompat.getContentMimeTypes(info);

        boolean gifSupported = false;
        for (String mimeType : mimeTypes) {
            if (ClipDescription.compareMimeTypes(mimeType, "image/gif")) {
                gifSupported = true;
            }
        }

        if (gifSupported) {
            // the target editor supports GIFs. enable corresponding content
        } else {
            // the target editor does not support GIFs. disable corresponding content
        }*/
    }

    public static File getFileForResource(
            @NonNull Context context, @RawRes int res, @NonNull File outputDir,
            @NonNull String filename) {
        final File outputFile = new File(outputDir, filename);
        final byte[] buffer = new byte[4096];
        InputStream resourceReader = null;
        try {
            try {
                resourceReader = context.getResources().openRawResource(res);
                OutputStream dataWriter = null;
                try {
                    dataWriter = new FileOutputStream(outputFile);
                    while (true) {
                        final int numRead = resourceReader.read(buffer);
                        if (numRead <= 0) {
                            break;
                        }
                        dataWriter.write(buffer, 0, numRead);
                    }
                    return outputFile;
                } finally {
                    if (dataWriter != null) {
                        dataWriter.flush();
                        dataWriter.close();
                    }
                }
            } finally {
                if (resourceReader != null) {
                    resourceReader.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean onShowInputRequested(int flags, boolean configChange) {
        return super.onShowInputRequested(flags, configChange);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype newSubtype) {
        super.onCurrentInputMethodSubtypeChanged(newSubtype);
    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private boolean isCommitContentSupported(@Nullable EditorInfo editorInfo) {
        if (editorInfo == null) {
            return false;
        }

        final InputConnection ic = getCurrentInputConnection();
        if (ic == null) {
            return false;
        }

        if (!validatePackageName(editorInfo)) {
            return false;
        }
        return true;
    }

    CharSequence charSequence;

    @Override
    public void onClick(View view) {

        view.playSoundEffect(SoundEffectConstants.CLICK);
        currentInputConnection = getCurrentInputConnection();
        switch (view.getId()) {
            case R.id.btn_0:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("0", 0);
                break;
            case R.id.btn_1:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("1", 0);
                break;
            case R.id.btn_2:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("2", 0);
                break;
            case R.id.btn_3:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("3", 0);
                break;
            case R.id.btn_4:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("4", 0);
                break;
            case R.id.btn_5:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("5", 0);
                break;
            case R.id.btn_6:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("6", 0);
                break;
            case R.id.btn_7:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("7", 0);
                break;
            case R.id.btn_8:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("8", 0);
                break;
            case R.id.btn_9:
                if (isCommitContentSupported(getCurrentInputEditorInfo()))
                    currentInputConnection.commitText("9", 0);
                //currentInputConnection.commitText("\ud83d\ude01", 0);
                break;

            case R.id.btn_q:
                if (isCommitContentSupported(getCurrentInputEditorInfo())) {
                    if (btn_num_switch.getTag().equals("alphabetical")) {

                        if (btn_caps.getTag().equals("uppercase")) {
                            currentInputConnection.commitText("Q", 0);
                            setCaps(false);
                            btn_caps.setTag("lowercase");
                        } else if (btn_caps.getTag().equals("lowercase")) {
                            btn_q.setText("q");
                            currentInputConnection.commitText("q", 0);
                        }
                    }
                }
                break;
            case R.id.btn_w:
                if (isCommitContentSupported(getCurrentInputEditorInfo())) {
                    if (btn_num_switch.getTag().equals("alphabetical")) {

                        if (btn_caps.getTag().equals("uppercase")) {
                            currentInputConnection.commitText("W", 0);
                            setCaps(false);
                            btn_caps.setTag("lowercase");
                        } else if (btn_caps.getTag().equals("lowercase")) {
                            btn_w.setText("w");
                            currentInputConnection.commitText("w", 0);
                        }
                    }
                }
                break;
            case R.id.btn_e:
                if (isCommitContentSupported(getCurrentInputEditorInfo())) {
                    if (btn_num_switch.getTag().equals("alphabetical")) {

                        if (btn_caps.getTag().equals("uppercase")) {
                            currentInputConnection.commitText("E", 0);
                            setCaps(false);
                            btn_caps.setTag("lowercase");
                        } else if (btn_caps.getTag().equals("lowercase")) {
                            btn_e.setText("e");
                            currentInputConnection.commitText("e", 0);
                        }
                    }
                }
                break;
            case R.id.btn_r:
                if (isCommitContentSupported(getCurrentInputEditorInfo())) {
                    if (btn_num_switch.getTag().equals("alphabetical")) {

                        if (btn_caps.getTag().equals("uppercase")) {
                            currentInputConnection.commitText("R", 0);
                            setCaps(false);
                            btn_caps.setTag("lowercase");
                        } else if (btn_caps.getTag().equals("lowercase")) {
                            btn_r.setText("r");
                            currentInputConnection.commitText("r", 0);
                        }
                    }
                }
                break;
            case R.id.btn_t:
                if (isCommitContentSupported(getCurrentInputEditorInfo())) {
                    if (btn_num_switch.getTag().equals("alphabetical")) {

                        if (btn_caps.getTag().equals("uppercase")) {
                            currentInputConnection.commitText("T", 0);
                            setCaps(false);
                            btn_caps.setTag("lowercase");
                        } else if (btn_caps.getTag().equals("lowercase")) {
                            btn_t.setText("t");
                            currentInputConnection.commitText("t", 0);
                        }
                    }
                }
                break;
            case R.id.btn_y:
                if (isCommitContentSupported(getCurrentInputEditorInfo())) {
                    if (btn_num_switch.getTag().equals("alphabetical")) {

                        if (btn_caps.getTag().equals("uppercase")) {
                            currentInputConnection.commitText("Y", 0);
                            setCaps(false);
                            btn_caps.setTag("lowercase");
                        } else if (btn_caps.getTag().equals("lowercase")) {
                            btn_y.setText("y");
                            currentInputConnection.commitText("y", 0);
                        }
                    }
                }
                break;
            case R.id.btn_u:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("U", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        btn_u.setText("u");
                        currentInputConnection.commitText("u", 0);
                    }
                }
                break;
            case R.id.btn_i:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("I", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("i", 0);
                    }
                }
                break;
            case R.id.btn_o:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("O", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("o", 0);
                    }
                }
                break;
            case R.id.btn_p:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("P", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("p", 0);
                    }
                }
                break;
            case R.id.btn_a:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("A", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("a", 0);
                    }
                }
                break;
            case R.id.btn_s:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("S", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("s", 0);
                    }
                }
                break;
            case R.id.btn_d:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("D", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("d", 0);
                    }
                }
                break;
            case R.id.btn_f:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("F", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("f", 0);
                    }
                }
                break;
            case R.id.btn_g:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("G", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("g", 0);
                    }
                }
                break;
            case R.id.btn_h:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("H", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("h", 0);
                    }
                }
                break;
            case R.id.btn_j:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("J", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("j", 0);
                    }
                }
                break;
            case R.id.btn_k:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("K", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("k", 0);
                    }
                }
                break;
            case R.id.btn_l:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("L", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("l", 0);
                    }
                }
                break;
            case R.id.btn_z:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("Z", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("z", 0);
                    }
                } else if (btn_num_switch.getTag().equals("numeric")) {
                    btn_z.setText("-");
                    currentInputConnection.commitText("-", 0);
                }
                break;
            case R.id.btn_x:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("X", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("x", 0);
                    }
                } else if (btn_num_switch.getTag().equals("numeric")) {
                    btn_x.setText("'");
                    currentInputConnection.commitText("'", 0);
                }
                break;
            case R.id.btn_c:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("C", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("c", 0);
                    }
                } else if (btn_num_switch.getTag().equals("numeric")) {
                    btn_c.setText("\"");
                    currentInputConnection.commitText("\"", 0);
                }
                break;
            case R.id.btn_v:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("V", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("v", 0);
                    }
                } else if (btn_num_switch.getTag().equals("numeric")) {
                    btn_v.setText(":");
                    currentInputConnection.commitText(":", 0);
                }
                break;
            case R.id.btn_b:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("B", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("b", 0);
                    }
                } else if (btn_num_switch.getTag().equals("numeric")) {
                    btn_b.setText(";");
                    currentInputConnection.commitText(";", 0);
                }
                break;
            case R.id.btn_n:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("N", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("n", 0);
                    }
                } else if (btn_num_switch.getTag().equals("numeric")) {

                    btn_n.setText(",");
                    currentInputConnection.commitText(",", 0);
                }
                break;
            case R.id.btn_m:

                if (btn_num_switch.getTag().equals("alphabetical")) {

                    if (btn_caps.getTag().equals("uppercase")) {
                        currentInputConnection.commitText("M", 0);
                        setCaps(false);
                        btn_caps.setTag("lowercase");
                    } else if (btn_caps.getTag().equals("lowercase")) {
                        currentInputConnection.commitText("m", 0);
                    }
                } else if (btn_num_switch.getTag().equals("numeric")) {
                    btn_m.setText(".");
                    currentInputConnection.commitText(".", 0);
                }
                break;

            case R.id.btn_exclamation:
                currentInputConnection.commitText("!", 0);

                break;

            case R.id.btn_atRate:

                currentInputConnection.commitText("@", 0);

                break;

            case R.id.btn_hash:
                currentInputConnection.commitText("#", 0);

                break;

            case R.id.btn_dollar:
                currentInputConnection.commitText("$", 0);

                break;

            case R.id.btn_divide:
                currentInputConnection.commitText("/", 0);

                break;

            case R.id.btn_carrat:
                currentInputConnection.commitText("^", 0);

                break;

            case R.id.btn_ampersand:
                currentInputConnection.commitText("&", 0);

                break;

            case R.id.btn_mult:
                currentInputConnection.commitText("*", 0);

                break;

            case R.id.btn_openBrac:
                currentInputConnection.commitText("(", 0);

                break;

            case R.id.btn_closingBrac:
                currentInputConnection.commitText(")", 0);

                break;

            case R.id.btn_enter:
                currentInputConnection.commitText("\n", 0);
                break;
            case R.id.btn_questionMark:
                currentInputConnection.commitText("?", 0);
                break;
            case R.id.btn_spacebar:

                currentInputConnection.commitText(" ", 0);
                break;
            case R.id.btn_go:

                break;

            case R.id.btn_num_switch:

                if (btn_num_switch.getTag().equals("alphabetical")) {
                    btn_num_switch.setTag("numeric");
                    level2.setVisibility(View.GONE);
                    level5.setVisibility(View.VISIBLE);
                    // level1.setVisibility(View.GONE);
                    btn_z.setText("-");
                    btn_x.setText("'");
                    btn_c.setText("\"");
                    btn_v.setText(":");
                    btn_b.setText(";");
                    btn_n.setText(",");
                    btn_m.setText(".");

                    btn_q.setText("");
                    btn_w.setText("");
                    btn_e.setText("");
                    btn_r.setText("");
                    btn_t.setText("");
                    btn_y.setText("");
                    btn_u.setText("");
                    btn_i.setText("");
                    btn_o.setText("");
                    btn_p.setText("");

                    btn_caps.setEnabled(false);

                } else if (btn_num_switch.getTag().equals("numeric")) {
                    btn_num_switch.setTag("alphabetical");
                    level2.setVisibility(View.VISIBLE);
                    level5.setVisibility(View.GONE);
                    //  level1.setVisibility(View.VISIBLE);
                    btn_z.setText("z");
                    btn_x.setText("x");
                    btn_c.setText("c");
                    btn_v.setText("v");
                    btn_b.setText("b");
                    btn_n.setText("n");
                    btn_m.setText("m");

                    btn_q.setText("q");
                    btn_w.setText("w");
                    btn_e.setText("e");
                    btn_r.setText("r");
                    btn_t.setText("t");
                    btn_y.setText("y");
                    btn_u.setText("u");
                    btn_i.setText("i");
                    btn_o.setText("o");
                    btn_p.setText("p");

                    btn_caps.setEnabled(true);
                }

                break;

            case R.id.btn_backspace:

                currentInputConnection.deleteSurroundingText(1, 0);

                break;
            case R.id.btn_dot:
                currentInputConnection.commitText(".", 0);
                break;
            case R.id.btn_caps:

                if (btn_caps.getTag().equals("uppercase")) {
                    btn_caps.setTag("lowercase");
                } else if (btn_caps.getTag().equals("lowercase")) {
                    btn_caps.setTag("uppercase");
                }

                if (btn_caps.getTag().equals("uppercase")) {
                    setCaps(true);
                } else if (btn_caps.getTag().equals("lowercase")) {
                    setCaps(false);

                    btn_q.setText("q");
                    btn_w.setText("w");
                    btn_e.setText("e");
                    btn_r.setText("r");
                    btn_t.setText("t");
                    btn_y.setText("y");
                    btn_u.setText("u");
                    btn_i.setText("i");
                    btn_o.setText("o");
                }
                break;
        }
        charSequence = getCurrentInputConnection().getTextBeforeCursor(15, 0);
        System.out.println("PAGER charS: " + charSequence);


        if (charSequence != null && !containsDigit(charSequence + "") && !containsSpecialCharacter(charSequence+""))
            showStickerInfoFromDatabase(charSequence);

    }

    private void setCaps(boolean flag)
    {
        btn_q.setAllCaps(flag);
        btn_w.setAllCaps(flag);
        btn_e.setAllCaps(flag);
        btn_r.setAllCaps(flag);
        btn_t.setAllCaps(flag);
        btn_y.setAllCaps(flag);
        btn_u.setAllCaps(flag);
        btn_i.setAllCaps(flag);
        btn_o.setAllCaps(flag);
        btn_p.setAllCaps(flag);
        btn_a.setAllCaps(flag);
        btn_s.setAllCaps(flag);
        btn_d.setAllCaps(flag);
        btn_f.setAllCaps(flag);
        btn_g.setAllCaps(flag);
        btn_h.setAllCaps(flag);
        btn_j.setAllCaps(flag);
        btn_k.setAllCaps(flag);
        btn_l.setAllCaps(flag);
        btn_z.setAllCaps(flag);
        btn_x.setAllCaps(flag);
        btn_c.setAllCaps(flag);
        btn_v.setAllCaps(flag);
        btn_b.setAllCaps(flag);
        btn_n.setAllCaps(flag);
        btn_m.setAllCaps(flag);
    }

    private final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }

    private boolean containsSpecialCharacter(String s)
    {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        boolean b = m.find();

        if(b)
            return true;

        else
            return false;

    }

    private void doCommitContent(@NonNull String description, @NonNull String mimeType,
                                 @NonNull File file) {
        try {
            final EditorInfo editorInfo = getCurrentInputEditorInfo();

            if (!validatePackageName(editorInfo)) {
                return;
            }

            final Uri contentUri = FileProvider.getUriForFile(this, AUTHORITY, file);

            final int flag;
            if (Build.VERSION.SDK_INT >= 25) {
                flag = InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
            } else {
                flag = 0;
                try {
                    grantUriPermission(
                            editorInfo.packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    Log.e(TAG, "grantUriPermission failed packageName=" + editorInfo.packageName
                            + " contentUri=" + contentUri, e);
                }
            }

            final InputContentInfoCompat inputContentInfoCompat = new InputContentInfoCompat(
                    contentUri,
                    new ClipDescription(description, new String[]{mimeType}),
                    null /* linkUrl */);
            InputConnectionCompat.commitContent(
                    getCurrentInputConnection(), getCurrentInputEditorInfo(), inputContentInfoCompat,
                    flag, null);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    LinearLayout linearLayout = null;

    private void showStickerInfoFromDatabase(CharSequence tag) {
        if (tag != null && tag.length() <= 0) {
            return;
        } else {
            databaseHelper = new DatabaseHelper(getApplicationContext());
            Cursor c = databaseHelper.getSticker(tag.toString());
            String[] items = new String[c.getCount()];
            c.moveToFirst();
            for (int k = 0; k < c.getCount(); k++) {
                if (c != null && !c.isClosed()) {
                    System.out.println("PAGER ROW: " + c.getString(c.getColumnIndex(DatabaseHelper.STICKER_ID)) + "," + c.getString(c.getColumnIndex(DatabaseHelper.STICKER_NAME)) + "," + c.getString(c.getColumnIndex(DatabaseHelper.STICKER_PUBLISH_TAGS)) + "," + c.getString(c.getColumnIndex(DatabaseHelper.STICKER_USER_TAGS)));
                    String name = c.getString(c.getColumnIndex(DatabaseHelper.STICKER_ID));
                    items[k] = name.replace("R.drawable.", "");
                    c.moveToNext();
                }
            }
            c.moveToFirst();
            c.close();
            databaseHelper.close();

            System.out.println("PAGER ITEMS: " + items.length);


            if (items.length > 0) {
                mHorizontalScrollView.setVisibility(View.VISIBLE);
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 250);
                params.gravity = Gravity.CENTER;

                mHorizontalScrollView.removeAllViews();

                LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(240, 240);
                imageViewParams.gravity = Gravity.CENTER;

                linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                final ImageView[] imageView = new ImageView[items.length];
                Resources resources = context.getResources();


                for (int l = 0; l < items.length; l++) {
                    final int i = l;
                    imageView[l] = new ImageView(context);
                    imageView[l].setLayoutParams(imageViewParams);
                    imageView[l].setOnClickListener(this);

                    String mDrawableName = items[l];
                    imageView[l].setTag(mDrawableName);

                    final int resourceId = resources.getIdentifier(mDrawableName, "drawable", context.getPackageName());

                    imageView[l].setImageResource(resourceId);

                    imageView[l].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            final File imagesDir = new File(context.getFilesDir(), "images");
                            imagesDir.mkdirs();
                            mPngFile = ((CustomKeyboard) context).getFileForResource(context, context.getResources().getIdentifier(imageView[i].getTag().toString(), "drawable", context.getPackageName()), imagesDir, "" + imageView[i].getTag() + ".gif");

                            final EditorInfo editorInfo = getCurrentInputEditorInfo();
                            EditorInfoCompat.setContentMimeTypes(editorInfo, new String[]{"image/gif"});

                            doCommitContent("A waving flag", MIME_TYPE_GIF, mPngFile);

                            //  InputConnectionCompat.commitContent(inputConnection, editorInfo, inputContentInfo, flags, null);


                             /*   final int flag;
                                if (Build.VERSION.SDK_INT >= 25) {
                                    flag = InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
                                } else {
                                    flag = 0;
                                    try {
                                        context.grantUriPermission(editorInfo.packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    } catch (Exception e) {
                                    }
                                }
                                try {
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    sendIntent.setPackage(editorInfo.packageName);
                                    sendIntent.putExtra(Intent.EXTRA_STREAM,contentUri);//.toString());
                                    sendIntent.setType(CustomKeyboard.MIME_TYPE_PNG);
                                    context.startActivity(sendIntent);//Intent.createChooser(sendIntent, "Send to"));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }*/


                        }
                    });


                    linearLayout.addView(imageView[l]);

                }

                mHorizontalScrollView.addView(linearLayout);


            } else {

                if (linearLayout != null)
                    linearLayout.removeAllViews();

                mHorizontalScrollView.removeAllViewsInLayout();
                mHorizontalScrollView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return true;
    }

    @Override
    public void onFinishInput() {
        System.out.println("imageKeyboard: onFinishInput");
        super.onFinishInput();
    }

    @Override
    public void onBindInput() {
        super.onBindInput();
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        System.out.println("imageKeyboard: onFinishInputView: " + finishingInput);
        super.onFinishInputView(finishingInput);
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {

    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
