package com.divan.osmanlicalugat.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.divan.osmanlicalugat.R;
import com.divan.osmanlicalugat.data.db.DbManager;
import com.divan.osmanlicalugat.data.domain.Word;
import com.divan.osmanlicalugat.data.repo.WordRepo;
import com.divan.osmanlicalugat.utils.CustomUtils;

import java.util.List;

public class MainActivity extends Activity {

    EditText editText;
    ListView listViev;
    RelativeLayout layoutToast;
    TextView textViewTitle, textViewBody;
    ImageButton btnClear;
    ImageButton btnSave, btnGoRegister;
    ArrayAdapter<Word> adapter;
    Toast toast;
    View layout;

    WordRepo repo;

    private int positionSelected = -1; // to access the selected item listView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbManager.initializeInstance(getApplicationContext());
        repo = new WordRepo();

        editText = findViewById(R.id.editText);
        textViewTitle = findViewById(R.id.textVTitle);
        textViewBody = findViewById(R.id.textVBody);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnGoRegister = findViewById(R.id.btnGoRegister);
        layoutToast =  findViewById(R.id.rLayoutToast);
        listViev = findViewById(R.id.listViewItems);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

        listViev.setAdapter(adapter);
        btnClear.setVisibility(ImageButton.INVISIBLE);

        btnGoRegister.setOnClickListener(v -> {
            layoutToast.setVisibility(RelativeLayout.INVISIBLE);
            Intent intent = new Intent(MainActivity.this, RegisteredWordsActivity.class);
            startActivity(intent);
        });

        btnClear.setOnClickListener(v -> {
            layoutToast.setVisibility(RelativeLayout.INVISIBLE);
            editText.getText().clear();
        });


        editText.addTextChangedListener(new TextWatcher() {

            // Text değişirken
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.clear(); // for ListView(lViev) be null

                int lengthS = s.toString().length();

                if (lengthS != 0)
                    btnClear.setVisibility(ImageButton.VISIBLE);
                else
                    btnClear.setVisibility(ImageButton.INVISIBLE);

                String callWord;
                if (lengthS == 0)
                    callWord = "a";
                else
                    callWord = CustomUtils.getRegexString(s.toString());

                List<Word> listByName = repo.getListByName(callWord);
                adapter.clear();
                adapter.addAll(listByName);
                adapter.notifyDataSetChanged();
            }


            // Text değişmeden önce
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            // Text değiştikten sonra
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        }); // addTextChangedListener over.


        toast = new Toast(getApplicationContext());
        listViev.setOnItemClickListener((parent, view, position, id) ->{
            Word word = adapter.getItem(position);
            layoutToast.setVisibility(RelativeLayout.INVISIBLE);
            positionSelected = position;

            textViewTitle.setText(word.getKelime());
            textViewBody.setText(word.getAnlam());
//				textViewTitle.setText(word.get(position).getKelime());
//				textViewBody.setText(word.get(position).getAnlam());

            if (word.getIndeks() == 0) {
                btnSave.setImageResource(R.drawable.inactive);
            } else {
                btnSave.setImageResource(R.drawable.active);
            }
            layoutToast.setVisibility(RelativeLayout.VISIBLE);
        });


        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                update();
            }
        });


        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutToast.setVisibility(RelativeLayout.INVISIBLE);
            }
        });

    }

    private void update() {
        Word word = adapter.getItem(positionSelected);
        int indeksSelected = word.getIndeks();
        if (indeksSelected == 0) {
            word.setIndeks(1);
            btnSave.setImageResource(R.drawable.active);
        } else {
            word.setIndeks(0);
            btnSave.setImageResource(R.drawable.inactive);
        }
        repo.update(word);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (layoutToast.getVisibility() == View.VISIBLE) {
            layoutToast.setVisibility(RelativeLayout.INVISIBLE);
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            try {
                new AlertDialog.Builder(
                        this)
                        .setTitle("Programdan Çıkılsın Mı?")
                        .setCancelable(false).setPositiveButton("Evet", ((dialog, which) -> {
                    dialog.dismiss();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }))
                        .setNegativeButton("Hayır", ((dialog, which) -> {

                        }))
                        .create()
                        .show();

                return super.onKeyDown(keyCode, event);

            } catch (IllegalStateException e) {
                e.printStackTrace();
                return true;
            }
        }

        return true;

    }

}
