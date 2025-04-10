import java.io.*;
import java.util.*;





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

            List<String> shingle1 = shingle(token1);
            List<String> shingle2 = shingle(token2);

            for(String s: shingle1){
                System.out.println(s);
            }





        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<String> tokenizer(String s) throws Exception{
        StringTokenizer st = new StringTokenizer(s, " \t\n=;,<>()");
        List<String> token = new ArrayList<>();
        while (st.hasMoreTokens()) {
            token.add(st.nextToken());
        }
        return token;
    }

    public static List<String> shingle(List<String> token) throws Exception{
        List<String> shingle = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<token.size()-5; i++){
            sb.delete(0,sb.length());
            for(int j = i; j < i+5; j++){
                sb.append(token.get(j)).append(" ");
            }
            sb.deleteCharAt(sb.length()-1);
            shingle.add(sb.toString());
        }
        return shingle;
    }
}
