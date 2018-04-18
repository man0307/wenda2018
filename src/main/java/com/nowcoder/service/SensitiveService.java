package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SensitiveService implements InitializingBean {
    private  String replacement = "敏感词";

    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWord.txt");
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String lineText;
        while ((lineText = bufferedReader.readLine()) != null) {
            addWord(lineText.trim());
        }
    }

    //abc 增加关键词
    private void addWord(String word) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == word.length() - 1) {
                tempNode.setWordEnd(true);
            }
        }
    }

    //前缀树
    private class TrieNode {
        private boolean end = false;

        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TrieNode trieNode) {
            subNodes.put(key, trieNode);
        }

        public TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        public boolean isKeyWordEnd() {
            return end;
        }

        public void setWordEnd(Boolean end) {
            this.end = end;
        }
    }

    //0x2E80 0x9fff东亚文字
    private boolean isSymbol(char c) {
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9fff);
    }

    //过滤敏感词 原创
    public String filer(String text) {
        if (StringUtils.isBlank(text)) return text;

        int slow = 0;
        int fast = 0;
        StringBuilder sb = new StringBuilder();
        while (fast < text.length()) {


            if (rootNode.subNodes.get(text.charAt(slow)) != null) {
                TrieNode temp = rootNode.subNodes.get(text.charAt(fast));
                boolean f = false;

                while (fast < text.length() && temp != null) {

                    if (temp.isKeyWordEnd()) {
                        f = true;
                        sb.append(replacement);
                        fast++;
                        slow = fast;
                        break;
                    }
                    fast++;
                    if (fast < text.length()) {
                        if (isSymbol(text.charAt(fast))) {
                            fast++;
                            temp = temp.subNodes.get(text.charAt(fast));
                            continue;
                        }
                        else temp = temp.subNodes.get(text.charAt(fast));
                    }
                }

                if (!f) {
                    sb.append(text.substring(slow, fast + 1));
                    fast++;
                    slow++;
                }
            } else {
                sb.append(text.charAt(slow));
                slow++;
                fast++;
            }
        }
        return sb.toString();
    }

    private TrieNode rootNode = new TrieNode();

    public static void main(String[] args) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        System.out.println(s.filer("赌博博主色情"));
    }
}
