import java.io.*;
import java.util.*;

class Node<K,V>{
    int N, aux;
    K key;
    V value;
    Node<K,V> left, right, parent;

    public Node(K key, V val){
        this.key = key; this.value = val;
        this.N = 1;
    }

    public int getAux() {
        return aux;
    }

    public void setAux(int value) {
        aux = value;
    }
}

class BST<K extends  Comparable<K>, V>{
    protected Node<K,V> root;

    public int size() {
        return (root != null) ? root.N : 0;
    }


}



public class HW2 {
    public static void main(String[] args) {

        //파일 열기
        BufferedReader br1, br2;
        try {
            Scanner scanner = new Scanner(System.in);
            while(true) {
                try {
                    System.out.print("첫 번째 파일명: ");
                    br1 = new BufferedReader(new InputStreamReader(new FileInputStream(scanner.nextLine()),"EUC-KR"));
                    System.out.print("두 번째 파일명: ");
                    br2 = new BufferedReader(new InputStreamReader(new FileInputStream(scanner.nextLine()),"EUC-KR"));
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

            sb.delete(0,sb.length());

            while ((line = br2.readLine()) != null) {
                sb.append(line).append("\n");
            }
            List<String> token2 = tokenizer(sb.toString());

            List<String> shingle1 = tokenToShingle(token1, 5);
            List<String> shingle2 = tokenToShingle(token2, 5);

            System.out.println(token1);
            for(String s: shingle1){
                System.out.println(s);
            }
            for(String s: shingle2){
                System.out.println(s);
            }





        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<String> tokenizer(String s){
        StringTokenizer st = new StringTokenizer(s, " \t\n=;,<>()");
        List<String> token = new ArrayList<>();
        while (st.hasMoreTokens()) {
            token.add(st.nextToken());
        }
        return token;
    }

    public static List<String> tokenToShingle(List<String> token, int shingleSize){
        List<String> shingle = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<shingleSize; i++){
            sb.append(" ").append(token.get(i));
        }
        sb.deleteCharAt(0);
        shingle.add(sb.toString());

        for(int i = shingleSize; i < token.size(); i++){
        sb.delete(0,sb.indexOf(" ")+1).append(" ").append(token.get(i));
        shingle.add(sb.toString());
        }
        return shingle;
    }
}
