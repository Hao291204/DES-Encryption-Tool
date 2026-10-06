import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("        DES Encryption Tool");
        System.out.println("=================================");
        System.out.println();

        System.out.println(
            "DES uses an 8-character secret key."
        );
        System.out.println(
            "Example key: 12345678"
        );
        System.out.println();

        System.out.print("Enter plaintext: ");
        String plaintext = scanner.nextLine();

        System.out.print("Enter secret key (8 characters): ");
        String key = scanner.nextLine();

        try {
            if (key.getBytes(java.nio.charset.StandardCharsets.UTF_8).length != 8) {
                System.out.println();
                System.out.println(
                    "ERROR: The key must contain exactly 8 ASCII characters."
                );
                scanner.close();
                return;
            }

            System.out.println();
            System.out.println("Starting DES encryption...");

            String ciphertext = DES.encrypt(plaintext, key);

            System.out.println();
            System.out.println("Plaintext:");
            System.out.println(plaintext);

            System.out.println();
            System.out.println("Ciphertext (HEX):");
            System.out.println(ciphertext);

            System.out.println();
            System.out.println("Starting DES decryption...");

            String decryptedText = DES.decrypt(ciphertext, key);

            System.out.println();
            System.out.println("Decrypted plaintext:");
            System.out.println(decryptedText);

            System.out.println();

            if (plaintext.equals(decryptedText)) {
                System.out.println(
                    "SUCCESS: Decryption produced the original plaintext."
                );
            } else {
                System.out.println(
                    "ERROR: Decryption did not match the original plaintext."
                );
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("ERROR: " + e.getMessage());
        }

        scanner.close();
    }
}
