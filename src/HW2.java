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
            inorder(x.left, keyList);
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
        if (makeLeft) parent = child;
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
            if (rank < t) {
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
        if (contains(hi)) return rank(hi) - rank(lo) + 1;//hi 포함
        else return rank(hi) - rank(lo);//hi 미포함
    }

    public Iterable<K> keys(K lo, K hi) {


        int rankHi = rank(hi);// hi보다 작은 키의 수
        // hi가 트리에 존재하면 hi도 범위에 포함되도록 인덱스 보정
        if (contains(hi)) {
            rankHi++;
        }

        ArrayList<K> keyList = new ArrayList<>();
        for (int i = rank(lo); i < rankHi; i++) {// i = lo보다 작은 키의 수: lo 이후부터 포함
            keyList.add(select(i));// Lo부터 Hi-1까지 select를 이용해 해당 순위의 키를 keyList에 추가
        }
        return keyList;
    }
}


public class HW2 {
    public static void main(String[] args) {

        //파일 열기
        BufferedReader br1, br2;
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                try {
                    System.out.print("첫 번째 파일명: ");
                    br1 = new BufferedReader(new InputStreamReader(new FileInputStream(scanner.nextLine()), "EUC-KR"));
                    System.out.print("두 번째 파일명: ");
                    br2 = new BufferedReader(new InputStreamReader(new FileInputStream(scanner.nextLine()), "EUC-KR"));
                    break;
                } catch (FileNotFoundException e) {
                    System.err.println("없는 파일입니다.");
                }
            }

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br1.readLine()) != null) {
                sb.append(line).append("\n");
            }
            List<String> token1 = tokenizer(sb.toString());

            sb.delete(0, sb.length());

            while ((line = br2.readLine()) != null) {
                sb.append(line).append("\n");
            }
            List<String> token2 = tokenizer(sb.toString());

            List<String> shingle1 = tokenToShingle(token1, 5);
            List<String> shingle2 = tokenToShingle(token2, 5);

            System.out.println(token1);
            for (String s : shingle1) {
                System.out.println(s);
            }
            for (String s : shingle2) {
                System.out.println(s);
            }


        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<String> tokenizer(String s) {
        StringTokenizer st = new StringTokenizer(s, " \t\n=;,<>()");
        List<String> token = new ArrayList<>();
        while (st.hasMoreTokens()) {
            token.add(st.nextToken());
        }
        return token;
    }

    public static List<String> tokenToShingle(List<String> token, int shingleSize) {
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
}
