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
            String[] token1 = tokenizer(sb.toString());

            sb.delete(0,sb.length());
            while ((line = br1.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String[] token2 = tokenizer(sb.toString());








        } catch (IOException e) {
            System.err.println("파일 읽기 중 에러 발생: " + e.getMessage());
        }
    }

    public static String[] tokenizer(String s) {
        StringTokenizer st = new StringTokenizer(s, " \t\n=;,<>()");
        List<String> token = new ArrayList<>();
        while (st.hasMoreTokens()) {
            token.add(st.nextToken());
        }
        return token.toArray(new String[0]);
    }
}
