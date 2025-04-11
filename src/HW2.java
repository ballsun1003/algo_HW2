import java.io.*;
import java.util.*;

class Node<K, V> {
    int N, aux;
    K key;
    V value;
    Node<K, V> left, right, parent;

    public Node(K key, V val) {
        this.key = key;
        this.value = val;
        this.N = 1;
    }

    public int getAux() {
        return aux;
    }

    public void setAux(int value) {
        aux = value;
    }
}

class BST<K extends Comparable<K>, V> {
    protected Node<K, V> root;

    public int size() {
        return (root != null) ? root.N : 0;
    }

    public int size(Node<K, V> x) {
        return (x != null) ? x.N : 0;
    }

    protected Node<K, V> treeSearch(K key) {
        Node<K, V> x = root;
        while (true) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            else if (cmp < 0) {
                if (x.left == null) return x;
                else x = x.left;
            } else {
                if (x.right == null) return x;
                else x = x.right;
            }
        }
    }

    protected void rebalancedInsert(Node<K, V> x) {
        resetSize(x.parent, 1);
    }

    protected void rebalancedDelete(Node<K, V> p, Node<K, V> deleted) {
        resetSize(p, -1);
    }

    private void resetSize(Node<K, V> x, int value) {
        for (; x != null; x = x.parent)
            x.N += value;
    }

    private void inorder(Node<K, V> x, ArrayList<K> keyList) {
        if (x != null) {
            inorder(x.left, keyList);
            keyList.add(x.key);
            inorder(x.right, keyList);
        }
    }

    protected boolean isLeaf(Node<K, V> x) {
        return x.left == null && x.right == null;
    }

    protected boolean isTwoNode(Node<K, V> x) {
        return x.left != null && x.right != null;
    }

    protected void relink(Node<K, V> parent, Node<K, V> child, boolean makeLeft) {
        if (child != null) child.parent = parent;
        if (makeLeft) parent.left = child;
        else parent.right = child;
    }

    protected Node<K, V> min(Node<K, V> x) {
        while (x.left != null) x = x.left;
        return x;
    }

    public V get(K key) {
        if (root == null) return null;
        Node<K, V> x = treeSearch(key);
        if (key.equals(x.key)) return x.value;
        else return null;
    }

    public void put(K key, V val) {
        if (root == null) {
            root = new Node<K, V>(key, val);
            return;
        }
        Node<K, V> x = treeSearch(key);
        int cmp = key.compareTo(x.key);
        if (cmp == 0) x.value = val;
        else {
            Node<K, V> newNode = new Node<K, V>(key, val);
            if (cmp < 0) x.left = newNode;
            else x.right = newNode;
            newNode.parent = x;
            rebalancedInsert(newNode);
        }
    }

    public Iterable<K> keys() {
        if (root == null) return null;
        ArrayList<K> keyList = new ArrayList<K>(size());
        inorder(root, keyList);
        return keyList;
    }

    public void delete(K key) {
        if (root == null) return;
        Node<K, V> x, y, p;
        x = treeSearch(key);

        if (!key.equals(x.key)) return;

        if (x == root || isTwoNode(x)) {
            if (isLeaf(x)) {
                root = null;
                return;
            } else if (!isTwoNode(x)) {
                root = (x.right == null) ? x.left : x.right;
                root.parent = null;
                return;
            } else {
                y = min(x.right);
                x.key = y.key;
                x.value = y.value;
                p = y.parent;
                relink(p, y.right, y == p.left);
                rebalancedDelete(p, y);
            }
        } else {
            p = x.parent;
            if (x.right == null) relink(p, x.left, x == p.left);
            else if (x.left == null) relink(p, x.right, x == p.left);
            rebalancedDelete(p, x);
        }
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public K min() {
        if (root == null) return null;
        Node<K, V> x = root;
        while (x.left != null) x = x.left;
        return x.key;
    }

    public K max() {
        if (root == null) return null;
        Node<K, V> x = root;
        while (x.right != null) x = x.right;
        return x.key;
    }

    public K floor(K key) {
        if (root == null || key == null) return null;
        Node<K, V> x = floor(root, key);
        if (x == null) return null;
        else return x.key;
    }

    private Node<K, V> floor(Node<K, V> x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node<K, V> t = floor(x.right, key);
        if (t != null) return t;
        else return x;
    }

    public int rank(K key) {
        if (root == null || key == null) return 0;
        Node<K, V> x = root;
        int num = 0;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) {
                num += 1 + size(x.left);
                x = x.right;
            } else {
                num += size(x.left);
                break;
            }
        }
        return num;
    }

    public K select(int rank) {
        if (root == null || rank < 0 || rank >= size()) return null;
        Node<K, V> x = root;
        while (true) {
            int t = size(x.left);
            if (rank < t)
                x = x.left;
            else if (rank > t) {
                rank = rank - t - 1;
                x = x.right;
            } else return x.key;
        }
    }

    public void deleteMin() {
        delete(min());
    }

    public void deleteMax() {
        delete(max());
    }

    public int size(K lo, K hi) {
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

    public Iterable<K> keys(K lo, K hi) {


        int rankHi = rank(hi);
        if (contains(hi)) {
            rankHi++;
        }

        ArrayList<K> keyList = new ArrayList<>();
        for (int i = rank(lo); i < rankHi; i++) {
            keyList.add(select(i));
        }
        return keyList;
    }
}

class SimilarityCheck {
    public List<String> tokenizer(String s) {
        StringTokenizer st = new StringTokenizer(s, " \t\n=;,<>()");
        List<String> token = new ArrayList<>();
        while (st.hasMoreTokens()) {
            token.add(st.nextToken());
        }
        return token;
    }

    public List<String> tokenToShingleList(List<String> token, int shingleSize) {
        if (token.size() < shingleSize)
            return null;

        List<String> shingle = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shingleSize; i++) {
            sb.append(" ").append(token.get(i));
        }
        sb.deleteCharAt(0);
        shingle.add(sb.toString());

        for (int i = shingleSize; i < token.size(); i++) {
            sb.delete(0, sb.indexOf(" ") + 1).append(" ").append(token.get(i));
            shingle.add(sb.toString());
        }
        return shingle;
    }

    public BST<String, Integer> shingleListToBST(List<String> shingle) {
        BST<String, Integer> bst = new BST<String, Integer>();
        Integer value;
        for (String s : shingle) {
            value = bst.get(s);
            if (value == null) bst.put(s, 1);
            else bst.put(s, ++value);
        }
        return bst;
    }

    public Node<Double, Integer> similarity(BST<String, Integer> bst1, BST<String, Integer> bst2) {
        int intersection = 0;
        int union = 0;
        int sameShingle = 0;
        Integer bst1Val, bst2Val;

        for (String key : bst1.keys()) {
            bst1Val = bst1.get(key);
            bst2Val = bst2.get(key);

            if (bst2Val == null) bst2Val = 0;
            else sameShingle++;

            if (bst1Val.compareTo(bst2Val) < 0) {
                intersection += bst1Val;
                union += bst2Val;
            } else {
                intersection += bst2Val;
                union += bst1Val;
            }
        }

        for (String key : bst2.keys()) {
            if (!bst1.contains(key)) {
                union += bst2.get(key);
            }
        }

        if (union == 0) return new Node<Double, Integer>(0.0, sameShingle);
        else return new Node<Double, Integer>(((double) intersection / union), sameShingle);
    }
}


public class HW2 {
    public static void main(String[] args) {
        final int SHINGLE_SIZE = 5;
        try {
            BufferedReader br1, br2;
            String file1, file2;

            Scanner scanner = new Scanner(System.in);
            while (true) {
                try {
                    System.out.print("첫번째 파일 이름? ");
                    file1 = scanner.nextLine();
                    br1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), "EUC-KR"));
                    System.out.print("두번째 파일 이름? ");
                    file2 = scanner.nextLine();
                    br2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), "EUC-KR"));
                    break;
                } catch (FileNotFoundException e) {
                    System.err.println("파일 이름을 다시 확인해 주세요!");
                }
            }

            StringBuilder sb = new StringBuilder();
            SimilarityCheck sc = new SimilarityCheck();
            String line;

            while ((line = br1.readLine()) != null) {
                sb.append(line).append("\n");
            }
            List<String> token1 = sc.tokenizer(sb.toString());

            sb.delete(0, sb.length());

            while ((line = br2.readLine()) != null) {
                sb.append(line).append("\n");
            }
            List<String> token2 = sc.tokenizer(sb.toString());

            List<String> shingle1 = sc.tokenToShingleList(token1, SHINGLE_SIZE);
            List<String> shingle2 = sc.tokenToShingleList(token2, SHINGLE_SIZE);

            System.out.println("파일 " + file1 + "의 Shingle의 수 = " + shingle1.size());
            System.out.println("파일 " + file2 + "의 Shingle의 수 = " + shingle2.size());

            BST<String, Integer> bst1 = sc.shingleListToBST(shingle1);
            BST<String, Integer> bst2 = sc.shingleListToBST(shingle2);

            Node<Double, Integer> similarity = sc.similarity(bst1, bst2);

            System.out.println("두 파일에서 공통된 shingle의 수 = " + similarity.value);
            System.out.println(file1 + "과 " + file2 + "의 유사도 = " + similarity.key);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

