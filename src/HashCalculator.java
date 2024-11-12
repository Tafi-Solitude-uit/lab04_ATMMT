import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class HashCalculator {

    public static String calculateHash(String input, String algorithm)
    throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hash = digest.digest(input.getBytes());
        return bytesToHex(hash);
    }

    public static String calculateHashFromFile(String filePath, String algorithm)
    throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        int bytesRead = -1;
        while((bytesRead = fis.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        fis.close();
        byte[] hash = digest.digest();
        return bytesToHex(hash);
    }

    public static String calculateHashFromHexString(String hexString, String algorithm)
    throws NoSuchAlgorithmException {
        byte[] bytes = hexStringToByteArray(hexString);
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hash = digest.digest(bytes);
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    private static void displayMenu() {
        System.out.println("\nHãy chọn data type để băm:");
        System.out.println("1. Băm text string");
        System.out.println("2. Băm hex string");
        System.out.println("3. Băm nội dung từ file");
        System.out.println("4. Thoát khỏi chương trình");
        System.out.print("Lựa chọn của bạn: ");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        try {
            while (running) {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.println("Nhập chuỗi văn bản (text string) để băm:");
                        String textString = scanner.nextLine();
                        System.out.println("\nBăm chuỗi văn bản:");
                        System.out.println("MD5 (Text): " + calculateHash(textString, "MD5"));
                        System.out.println("SHA-1 (Text): " + calculateHash(textString, "SHA-1"));
                        System.out.println("SHA-256 (Text): " + calculateHash(textString, "SHA-256"));
                        break;

                    case 2:
                        System.out.println("Nhập chuỗi hex (hex string) để băm:");
                        String hexString = scanner.nextLine();
                        System.out.println("\nBăm chuỗi hex:");
                        System.out.println("MD5 (Hex): " + calculateHashFromHexString(hexString, "MD5"));
                        System.out.println("SHA-1 (Hex): " + calculateHashFromHexString(hexString, "SHA-1"));
                        System.out.println("SHA-256 (Hex): " + calculateHashFromHexString(hexString, "SHA-256"));
                        break;

                    case 3:
                        System.out.println("Nhập đường dẫn đến file để băm:");
                        String filePath = scanner.nextLine();
                        System.out.println("\nBăm file:");
                        System.out.println("MD5 (File): " + calculateHashFromFile(filePath, "MD5"));
                        System.out.println("SHA-1 (File): " + calculateHashFromFile(filePath, "SHA-1"));
                        System.out.println("SHA-256 (File): " + calculateHashFromFile(filePath, "SHA-256"));
                        break;

                    case 4:
                        running = false;
                        System.out.println("Đã thoát khỏi chương trình!!! Hẹn gặp lại.");
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                }
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        scanner.close();
    }
}