package org.delphy.tokenheroserver.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mutouji
 */
class WordNode {
    private Map<Character, WordNode> children;
    private boolean isEnd;
    private int level;

    WordNode() {
        children = new HashMap<>(0);
        setEnd(false);
        setLevel(0);
    }

    WordNode addChar(Character c) {
        WordNode node = this.children.get(c);
        if (node == null) {
            node = new WordNode();
            this.children.put(c, node);
        }
        return node;
    }

    WordNode findChar(Character c) {
        return this.children.get(c);
    }

    boolean isEnd() {
        return this.isEnd;
    }

    void setEnd(boolean b) {
        this.isEnd = b;
    }

    int getLevel() {
        return this.level;
    }

    void setLevel(int level) {
        this.level = level;
    }
}
