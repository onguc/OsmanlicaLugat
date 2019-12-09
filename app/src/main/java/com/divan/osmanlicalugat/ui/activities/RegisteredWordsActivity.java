package com.divan.osmanlicalugat.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.divan.osmanlicalugat.R;
import com.divan.osmanlicalugat.data.domain.Word;
import com.divan.osmanlicalugat.data.repo.WordRepo;
import com.divan.osmanlicalugat.utils.CustomUtils;

import java.util.ArrayList;
import java.util.List;

public class RegisteredWordsActivity extends Activity {

    EditText editText;
    ListView listView;
    RelativeLayout layoutToast;
    TextView textViewTitleR, textViewBodyR;
    ImageButton btnClearR;
    ImageButton btnSave;
    final List<Word> wordR = new ArrayList<Word>();
    ArrayAdapter<Word> adapter;
    WordRepo repo;
    private int positionSelected = -1;  //to access the selected item listView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("RegisteredWorsActivity 1");
        repo = new WordRepo();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_words);

        editText = findViewById(R.id.editText);
        textViewTitleR = findViewById(R.id.textVTitle);
        textViewBodyR = findViewById(R.id.textVBody);
        btnSave = findViewById(R.id.btnSave);
        btnClearR = findViewById(R.id.btnClear);
        layoutToast =  findViewById(R.id.lLayoutToast);
        listView = findViewById(R.id.listViewItems);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

        listView.setAdapter(adapter);

        btnClearR.setVisibility(ImageButton.INVISIBLE);

        List<Word> favoriteList = repo.getFavoriteList();
        adapter.clear();
        adapter.addAll(favoriteList);
        adapter.notifyDataSetChanged();

        btnClearR.setOnClickListener(v -> {
            editText.getText().clear();
            layoutToast.setVisibility(RelativeLayout.INVISIBLE);
        });

        editText.addTextChangedListener(new TextWatcher() {

            // Text değişirken
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.clear(); // for ListView(lViev) be null
                wordR.clear();

                int lengthS = s.toString().length();
                if (lengthS != 0)
                    btnClearR.setVisibility(ImageButton.VISIBLE);
                else
                    btnClearR.setVisibility(ImageButton.INVISIBLE);

                String callWord;
                if (lengthS == 0)
                    callWord = "";
                else
                    callWord = CustomUtils.getRegexString(s.toString());

                List<Word> favoriteListByName = repo.getFavoriteListByName(callWord);
                adapter.clear();
                adapter.addAll(favoriteListByName);
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
        }); // addTextChangedListener over

        listView.setOnItemClickListener((parent, view, position, id) -> {
            positionSelected = position;
            Word word = adapter.getItem(position);

            textViewTitleR.setText(word.getKelime());
            textViewBodyR.setText(word.getAnlam());
            if (word.getIndeks() == 0) {  //if word is not registered image inactive else another.
                btnSave.setImageResource(R.drawable.inactive);
            } else {
                btnSave.setImageResource(R.drawable.active);
            }
            layoutToast.setVisibility(RelativeLayout.VISIBLE);
        });

        btnSave.setOnClickListener(v -> {
            update();
        });

        editText.setOnClickListener(v -> {
            layoutToast.setVisibility(RelativeLayout.INVISIBLE);
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
            layoutToast.setVisibility(RelativeLayout.INVISIBLE);
        }
        repo.update(word);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
