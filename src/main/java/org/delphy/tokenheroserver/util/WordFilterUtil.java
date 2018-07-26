package org.delphy.tokenheroserver.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mutouji
 */
@Component
public class WordFilterUtil {
    private WordNode tree;
    private Pattern p;

    public WordFilterUtil(@Value("${sensitiveWords.filePath}") String sensitiveWordsFilePath) {
        tree = new WordNode();
        FileInputStream fileInputStream = null;
        try {
            File file = new File(sensitiveWordsFilePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (null != files && files.length > 0) {
                    for (File sensitiveFile : files) {
                        fileInputStream = new FileInputStream(sensitiveFile);
                        InputStreamReader reader = new InputStreamReader(fileInputStream, "utf-8");
                        StringBuilder result = new StringBuilder();
                        char [] buffer = new char[1024 * 1024];
                        int len;
                        while ((len = reader.read(buffer)) != -1) {
                            result.append(buffer, 0, len);
                        }
                        String words = result.toString().replaceAll(" ", "");
                        InputStream myIn = new ByteArrayInputStream(words.getBytes());
                        InputStreamReader ir = new InputStreamReader(myIn);
                        Properties prop = new Properties();
                        prop.load(ir);
                        Enumeration en = prop.propertyNames();
                        while (en.hasMoreElements()) {
                            String word = (String) en.nextElement();
                            insertWord(word, Integer.valueOf(prop.getProperty(word).trim()));
                        }
                    }
                }
            }
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }
        }

        String regex = "[\\pP\\pZ\\pS\\pM\\pC]";
        p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public int sensitiveLevel(String original) {
        WordFilterUtil.PunctuationOrHtmlFilteredResult pohResult = filterPunctationAndHtml(original);
        StringBuffer sentence = pohResult.getFilteredString();
        int level = 0;
        WordNode node = tree;

        for (int i = 0; i < sentence.length(); ++i) {
            int end = i - 1;

            int k;
            for (k = i; k < sentence.length(); ++k) {
                if (node != null) {
                    node = node.findChar(sentence.charAt(k));
                }
                if (node == null) {
                    break;
                }
                if (node.isEnd()) {
                    end = k;
                    level = node.getLevel();
                    break;
                }
            }

            if (end >= i) {
                return level;
            }
        }

        return level;
    }


    private void insertWord(String word, int level) {
        word = word.toLowerCase();
        WordNode node = tree;

        for (int i = 0; i < word.length(); ++i) {
            node = node.addChar(word.charAt(i));
        }

        node.setEnd(true);
        node.setLevel(level);
    }

    private boolean isPunctuationChar(String c) {
        Matcher m = p.matcher(c);
        return m.find();
    }


    private WordFilterUtil.PunctuationOrHtmlFilteredResult filterPunctationAndHtml(String originalString) {
        StringBuffer filteredString = new StringBuffer();

        for (int i = 0; i < originalString.length(); ++i) {
            String c = String.valueOf(originalString.charAt(i));
            if (originalString.charAt(i) != '<') {
                if (!isPunctuationChar(c)) {
                    filteredString.append(c);
                }
            } else {
                int k;
                for (k = i + 1; k < originalString.length(); ++k) {
                    if (originalString.charAt(k) == '<') {
                        k = i;
                        break;
                    }

                    if (originalString.charAt(k) == '>') {
                        break;
                    }
                }

                i = k;
            }
        }

        WordFilterUtil.PunctuationOrHtmlFilteredResult result = new WordFilterUtil.PunctuationOrHtmlFilteredResult();
        result.setFilteredString(filteredString);
        return result;
    }

    private static class PunctuationOrHtmlFilteredResult {
        private StringBuffer filteredString;

        private PunctuationOrHtmlFilteredResult() {
        }

        StringBuffer getFilteredString() {
            return this.filteredString;
        }

        void setFilteredString(StringBuffer filteredString) {
            this.filteredString = filteredString;
        }
    }
}
