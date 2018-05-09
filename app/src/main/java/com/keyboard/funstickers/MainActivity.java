package lms.autovista.com.customkeyboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static DatabaseHelper databaseHelper;
    static int WINDOW_HEIGHT,WINDOW_WIDTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WINDOW_HEIGHT = displayMetrics.heightPixels;
        WINDOW_WIDTH = displayMetrics.widthPixels;

        databaseHelper = new DatabaseHelper(this);
        System.out.println("PAGER: TABLECOUNT: "+ databaseHelper.getProfilesCount());
        if(databaseHelper.getProfilesCount() >= 0)
        {
            deleteDatabase(databaseHelper.DB_NAME);
            insertStickerInfoIntoDatabase();
        }
        getDatabaseTable();

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setMessage("Do you want to Enable Apnaji Keyboard ?");
        alertDialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                MainActivity.this.startActivityForResult(i,1001);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialogBuilder.show();

        Intent intent=new Intent(MainActivity.this,CustomKeyboard.class);
        startService(intent);
}

    private void getDatabaseTable() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        Cursor c = databaseHelper.getTableInfo();
        String[] items = new String[c.getCount()];
        c.moveToFirst();
        System.out.println("PAGER =================Table row ==================");
        for (int k = 0; k < c.getCount(); k++)
        {
            if (c != null && !c.isClosed())
            {
                System.out.println("PAGER ROW: "+ k +": "+ c.getString(c.getColumnIndex(DatabaseHelper.STICKER_NAME)) + "," + c.getString(c.getColumnIndex(DatabaseHelper.STICKER_NAME)) + "," + c.getString(c.getColumnIndex(DatabaseHelper.STICKER_PUBLISH_TAGS)) + "," + c.getString(c.getColumnIndex(DatabaseHelper.STICKER_USER_TAGS)));
                c.moveToNext();
            }
        }
        c.moveToFirst();
        c.close();
        System.out.println("PAGER =================Table row ==================");
        databaseHelper.close();
    }

    private void insertStickerInfoIntoDatabase()
    {
        databaseHelper = new DatabaseHelper(this);

        // aby yaar
        databaseHelper.addSticker("R.drawable.a00_kya_yaar", "Kya Yaar", "Friends","Friends");
        databaseHelper.addSticker("R.drawable.a00_kya_yaar", "Kya Yaar", "JKJsd","");
        databaseHelper.addSticker("R.drawable.a00_kya_yaar", "Kya Yaar", "fsdfsdf","");

        databaseHelper.addSticker("R.drawable.a01_chal_nikal","Chal Nikal", "RAVI","GANDU");
        databaseHelper.addSticker("R.drawable.a02_gayi_bhains", "Gayi Bhains", "RAVI","GANDU");
        databaseHelper.addSticker("R.drawable.a03_vaat_lag_gayi", "Vaat Lag Gayi", "YAR","FALTU");
        databaseHelper.addSticker("R.drawable.a04_pagal_hai_tu", "Pagal Hai tu", "TEJA","CHAADDI");
        databaseHelper.addSticker("R.drawable.a05_chup_kar", "Chup kar", "YAARA","MC");
        databaseHelper.addSticker("R.drawable.a06_abbey_gadhe","Abbey Gadhe","","LOVE");
        databaseHelper.addSticker("R.drawable.a07_ullu_ke_patthe","Ullu ke patthe","","ULLU");
        databaseHelper.addSticker("R.drawable.a08_abbey_yaar", "Abbey Yaar","","YAAR");
        databaseHelper.addSticker("R.drawable.a09_daal_me_kuch_kaala_hai","Daal me kuch kaala hai","","KAALA");
        databaseHelper.addSticker("R.drawable.a10_jaa_apni_baja","Jaa apni baja","","BAJA");
        databaseHelper.addSticker("R.drawable.a11_bore_mat_kar","Bore mat kar","","LOVE");
        databaseHelper.addSticker("R.drawable.a13_abbey_jaane_de", "Abbey Jaane De","","LOVE");
        databaseHelper.addSticker("R.drawable.a14_abe_hatt","Abe Hatt","","LOVE");
        databaseHelper.addSticker("R.drawable.a15_thanda_ley_yaar", "Thanda Ley Yaar","","LOVE");
        databaseHelper.addSticker("R.drawable.a16_abe_pagal","Abe Pagal","","PAGAL");
        databaseHelper.addSticker("R.drawable.a17_abe_chor","Abe Chor","","YAAR");
        databaseHelper.addSticker("R.drawable.a18_kya_biddu","Kya Biddu","","YAAR");
        databaseHelper.addSticker("R.drawable.a19_saale_harami","Saale Harami","","MC");
        databaseHelper.addSticker("R.drawable.a20_teri_jaat_ka_maaroo","Teri jaat ka maaroo","","MC");

        // dosti
        databaseHelper.addSticker("R.drawable.d1_apni_yaari","Apni Yaari","LOVE","DOST");
        databaseHelper.addSticker("R.drawable.d2_tu_mera_langotiyaar","Tu mera Langotiyaar","","DOST");
        databaseHelper.addSticker("R.drawable.d3_first_class_yaar","First Class Yaar","","YAAR");
        databaseHelper.addSticker("R.drawable.d4_paandu_hai_bhai","Paandu Hai Bhai","","DOST");
        databaseHelper.addSticker("R.drawable.d5_jhakaas_yaar","Jhakaas Yaar","","MAST");
        databaseHelper.addSticker("R.drawable.d6_ghanta_boss","Ghanta Boss","","GHANTA");
        databaseHelper.addSticker("R.drawable.d7_satkela_hai_kya_01","Satkela hai kya","","BAJA");
        databaseHelper.addSticker("R.drawable.d8_abe_fattu","Abe Fattu","","MC");
        databaseHelper.addSticker("R.drawable.d9_gadha_majoori","Gadha Majoori","","GANDU");
        databaseHelper.addSticker("R.drawable.d10_keeda_mat_kar","Keeda mat kar","","LOVE");
        databaseHelper.addSticker("R.drawable.d11_alibag_ka_hai_tu","Alibag ka hai tu","","LOVE");
        databaseHelper.addSticker("R.drawable.d12_chikna_bun_ke_aaja","Chikna bun ke aaja","","LOVE");
        databaseHelper.addSticker("R.drawable.d13_abe_chindi","Abe Chindi","","LOVE");
        databaseHelper.addSticker("R.drawable.d14_aaja_cutting_aur_sutta","Aaja Cutting aur Sutta","","LOVE");
        databaseHelper.addSticker("R.drawable.d15_kantala_aa_raha_hai","Kantala aa raha hai","","");
        databaseHelper.addSticker("R.drawable.d16_bas_kya","Bas Kya","","");
        databaseHelper.addSticker("R.drawable.d17_kya_re_bunty","Kya re Bunty","","");
        databaseHelper.addSticker("R.drawable.d18_hard_hai_tu_bhai","Hard hai tu bhai","","MAST");
        databaseHelper.addSticker("R.drawable.d19_dimag_mat_chaat","Dimag mat chaat","","DOST");
        databaseHelper.addSticker("R.drawable.d20_ekk_jhapat_du_kya","Ekk jhapat du kya","","DOST");
        databaseHelper.addSticker("R.drawable.enough_dont_talk_to_me_now","Enough","","LOVE");
        databaseHelper.addSticker("R.drawable.itna_kyu_chipak_rahi_thi_wo","Itna","","LOVE");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("RequestCode: " + requestCode);
        if (requestCode == 1001) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            List<InputMethodInfo> inputMethodList = imm.getEnabledInputMethodList();

            boolean found = false;

            for (int i = 0; i < inputMethodList.size(); i++) {
                InputMethodInfo inputMethodInfo = inputMethodList.get(i);
                if (inputMethodInfo.getPackageName().equalsIgnoreCase(MainActivity.this.getPackageName())) {
                    found = true;
                    System.out.println("Name: " + inputMethodInfo.getServiceName());
                    System.out.println("ID: " + inputMethodInfo.getId());
                    System.out.println("Acti: " + inputMethodInfo.getSettingsActivity());

                }else{
                    found = false;
                }
            }
            if (!found) {
                Toast.makeText(MainActivity.this, "Please choose Apnaji Keyboard", Toast.LENGTH_LONG).show();
            }
        }
    }
}
