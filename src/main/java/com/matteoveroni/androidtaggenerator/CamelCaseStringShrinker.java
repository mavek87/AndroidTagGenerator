package com.matteoveroni.androidtaggenerator;

import com.matteoveroni.myutils.Str;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matteo Veroni
 */
class CamelCaseStringShrinker {

    private int totalSubstringsLetters;
    private int maxSubstringLength;

    /**
     * @param string a passed camel case string to shrink
     * @param maxLength Max possible length. It MUST be less than
     * string.length()
     * @return optimal shrunk substring of the original string passed
     */
    public String shrink(String string, int maxLength) throws IllegalArgumentException {
        if (Str.isNullOrEmpty(string) || maxLength < 1) {
            throw new IllegalArgumentException("empty/null string or maxLength < 1");
        }
        totalSubstringsLetters = 0;
        maxSubstringLength = 0;
        final List<String> substrings = findSubstrings(string);
        return calculateOptimalShrinkString(substrings, maxLength);
    }

    private List<String> findSubstrings(String string) {
        final List<String> substrings = new ArrayList<>();
        StringBuilder substringBuilder = null;
        for (char letter : string.toCharArray()) {
            if (Character.isUpperCase(letter)) {
                if (substringBuilder != null) {
                    saveSubstring(substringBuilder.toString(), substrings);
                }
                substringBuilder = new StringBuilder(String.valueOf(letter));
            } else {
                substringBuilder.append(String.valueOf(letter));
            }
        }
        saveSubstring(substringBuilder.toString(), substrings);
        return substrings;
    }

    private String calculateOptimalShrinkString(List<String> substrings, int maxLength) {
        String optimalString = Str.concat(substrings);
        while (totalSubstringsLetters > maxLength) {

            maxSubstringLength = 1;
            int indexOfMaxSubstringLength = findIndexOfMaxSubstringLength(substrings);

            if (maxSubstringLength == 1) {
                optimalString = optimalString.substring(0, maxLength);
                break;
            } else {
                String maxSubStringDecreased = getStringDecreasedByOne(substrings.get(indexOfMaxSubstringLength));
                substrings.set(indexOfMaxSubstringLength, maxSubStringDecreased);
                totalSubstringsLetters--;
                optimalString = Str.concat(substrings);
            }
        }
        return optimalString;
    }

    private int findIndexOfMaxSubstringLength(List<String> substrings) {
        int indexOfMaxSubstringLength = substrings.size();

        for (int i = substrings.size() - 1; i >= 0; i--) {
            String substring = substrings.get(i);
            if (substring.length() > maxSubstringLength) {
                maxSubstringLength = substring.length();
                indexOfMaxSubstringLength = i;
            }
        }
        return indexOfMaxSubstringLength;
    }

    private String getStringDecreasedByOne(String string) {
        if (string.length() > 1) {
            string = Str.removeLastLetter(string);
        }
        return string;
    }

    private void saveSubstring(String substring, List<String> substrings) {
        int length = substring.length();
        if (length > 0) {
            if (length > maxSubstringLength) {
                maxSubstringLength = length;
            }
            substrings.add(substring);
            totalSubstringsLetters += length;
        }
    }
}
