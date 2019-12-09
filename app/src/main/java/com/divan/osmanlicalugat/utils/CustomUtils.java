package com.divan.osmanlicalugat.utils;

import com.divan.osmanlicalugat.R;
import com.divan.osmanlicalugat.data.domain.Word;

public class CustomUtils {
    public static String getRegexString(String callWord) {
        callWord = callWord.replace("ç", "c").toString();
        callWord = callWord.replace("ö", "o").toString();
        callWord = callWord.replace("ş", "s").toString();
        callWord = callWord.replace("ğ", "g").toString();
        callWord = callWord.replace("ı", "i").toString();
        callWord = callWord.replace("-", "").toString();
        callWord = callWord.replace("ü", "u").toString();
        callWord = callWord.replace(" ", "").toString();
        callWord = callWord.replace("'", "").toString();
        callWord = callWord + "%";
        return callWord;
    }

}
