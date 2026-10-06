import java.nio.charset.StandardCharsets;

public class DES {

    private static final int[] IP = {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final int[] FP = {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    };

    private static final int[] PC1 = {
        57, 49, 41, 33, 25, 17, 9,
        1, 58, 50, 42, 34, 26, 18,
        10, 2, 59, 51, 43, 35, 27,
        19, 11, 3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7, 62, 54, 46, 38, 30, 22,
        14, 6, 61, 53, 45, 37, 29,
        21, 13, 5, 28, 20, 12, 4
    };

    private static final int[] PC2 = {
        14, 17, 11, 24, 1, 5,
        3, 28, 15, 6, 21, 10,
        23, 19, 12, 4, 26, 8,
        16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };

    private static final int[] E = {
        32, 1, 2, 3, 4, 5,
        4, 5, 6, 7, 8, 9,
        8, 9, 10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    };

    private static final int[] P = {
        16, 7, 20, 21,
        29, 12, 28, 17,
        1, 15, 23, 26,
        5, 18, 31, 10,
        2, 8, 24, 14,
        32, 27, 3, 9,
        19, 13, 30, 6,
        22, 11, 4, 25
    };

    private static final int[] SHIFTS = {
        1, 1, 2, 2, 2, 2, 2, 2,
        1, 2, 2, 2, 2, 2, 2, 1
    };

    private static final int[][][] S_BOX = {
        {
            {14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
            {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
            {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
            {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}
        },
        {
            {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
            {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
            {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
            {13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}
        },
        {
            {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
            {13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
            {13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
            {1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}
        },
        {
            {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
            {13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
            {10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
            {3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}
        },
        {
            {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
            {14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
            {4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
            {11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}
        },
        {
            {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
            {10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
            {9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
            {4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}
        },
        {
            {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
            {13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
            {1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
            {6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}
        },
        {
            {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
            {1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
            {7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
            {2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
        }
    };

    public static String encrypt(String plaintext, String key) {
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] padded = addPadding(plaintextBytes);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        validateKey(keyBytes);

        long[] roundKeys = generateRoundKeys(keyBytes);
        byte[] ciphertext = new byte[padded.length];

        for (int i = 0; i < padded.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(padded, i, block, 0, 8);

            byte[] encryptedBlock = encryptBlock(block, roundKeys);
            System.arraycopy(encryptedBlock, 0, ciphertext, i, 8);
        }

        return bytesToHex(ciphertext);
    }

    public static String decrypt(String ciphertextHex, String key) {
        byte[] ciphertext = hexToBytes(ciphertextHex);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        validateKey(keyBytes);

        if (ciphertext.length == 0 || ciphertext.length % 8 != 0) {
            throw new IllegalArgumentException(
                "Ciphertext must contain one or more complete 8-byte blocks."
            );
        }

        long[] roundKeys = generateRoundKeys(keyBytes);
        byte[] plaintextPadded = new byte[ciphertext.length];

        for (int i = 0; i < ciphertext.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(ciphertext, i, block, 0, 8);

            byte[] decryptedBlock = decryptBlock(block, roundKeys);
            System.arraycopy(decryptedBlock, 0, plaintextPadded, i, 8);
        }

        byte[] plaintext = removePadding(plaintextPadded);
        return new String(plaintext, StandardCharsets.UTF_8);
    }

    private static void validateKey(byte[] keyBytes) {
        if (keyBytes.length != 8) {
            throw new IllegalArgumentException(
                "Key must be exactly 8 ASCII characters."
            );
        }
    }

    private static byte[] encryptBlock(byte[] block, long[] roundKeys) {
        long data = permute(bytesToLong(block), IP, 64);

        int left = (int) (data >>> 32);
        int right = (int) data;

        for (int round = 0; round < 16; round++) {
            int oldRight = right;
            right = left ^ feistel(right, roundKeys[round]);
            left = oldRight;
        }

        long combined =
            ((long) right << 32) | (left & 0xFFFFFFFFL);

        return longToBytes(permute(combined, FP, 64));
    }

    private static byte[] decryptBlock(byte[] block, long[] roundKeys) {
        long data = permute(bytesToLong(block), IP, 64);

        int left = (int) (data >>> 32);
        int right = (int) data;

        for (int round = 15; round >= 0; round--) {
            int oldRight = right;
            right = left ^ feistel(right, roundKeys[round]);
            left = oldRight;
        }

        long combined =
            ((long) right << 32) | (left & 0xFFFFFFFFL);

        return longToBytes(permute(combined, FP, 64));
    }

    private static int feistel(int right, long roundKey) {
        long expanded = permute(
            right & 0xFFFFFFFFL,
            E,
            32
        );

        expanded ^= roundKey;

        int sBoxOutput = 0;

        for (int i = 0; i < 8; i++) {
            int sixBits =
                (int) ((expanded >> (42 - 6 * i)) & 0x3F);

            int row =
                ((sixBits & 0x20) >> 4) | (sixBits & 0x01);

            int column =
                (sixBits >> 1) & 0x0F;

            int value =
                S_BOX[i][row][column];

            sBoxOutput =
                (sBoxOutput << 4) | value;
        }

        long result =
            permute(
                sBoxOutput & 0xFFFFFFFFL,
                P,
                32
            );

        return (int) result;
    }

    private static long[] generateRoundKeys(byte[] keyBytes) {
        long key = bytesToLong(keyBytes);

        long permutedKey =
            permute(key, PC1, 64);

        int c =
            (int) ((permutedKey >> 28) & 0x0FFFFFFF);

        int d =
            (int) (permutedKey & 0x0FFFFFFF);

        long[] roundKeys = new long[16];

        for (int round = 0; round < 16; round++) {
            c = leftRotate28(c, SHIFTS[round]);
            d = leftRotate28(d, SHIFTS[round]);

            long combined =
                ((long) c << 28) | (d & 0x0FFFFFFFL);

            roundKeys[round] =
                permute(combined, PC2, 56);
        }

        return roundKeys;
    }

    private static long permute(
        long input,
        int[] table,
        int inputSize
    ) {
        long output = 0;

        for (int position : table) {
            output <<= 1;

            long bit =
                (input >> (inputSize - position)) & 1L;

            output |= bit;
        }

        return output;
    }

    private static int leftRotate28(int value, int shifts) {
        value &= 0x0FFFFFFF;

        return (
            (value << shifts)
            | (value >>> (28 - shifts))
        ) & 0x0FFFFFFF;
    }

    private static byte[] addPadding(byte[] input) {
        int paddingLength =
            8 - (input.length % 8);

        byte[] result =
            new byte[input.length + paddingLength];

        System.arraycopy(
            input,
            0,
            result,
            0,
            input.length
        );

        for (int i = input.length; i < result.length; i++) {
            result[i] = (byte) paddingLength;
        }

        return result;
    }

    private static byte[] removePadding(byte[] input) {
        if (input.length == 0) {
            throw new IllegalArgumentException(
                "Invalid plaintext."
            );
        }

        int paddingLength =
            input[input.length - 1] & 0xFF;

        if (paddingLength < 1 || paddingLength > 8) {
            throw new IllegalArgumentException(
                "Invalid padding."
            );
        }

        for (
            int i = input.length - paddingLength;
            i < input.length;
            i++
        ) {
            if ((input[i] & 0xFF) != paddingLength) {
                throw new IllegalArgumentException(
                    "Invalid padding."
                );
            }
        }

        byte[] result =
            new byte[input.length - paddingLength];

        System.arraycopy(
            input,
            0,
            result,
            0,
            result.length
        );

        return result;
    }

    private static long bytesToLong(byte[] bytes) {
        long value = 0;

        for (int i = 0; i < 8; i++) {
            value =
                (value << 8) | (bytes[i] & 0xFFL);
        }

        return value;
    }

    private static byte[] longToBytes(long value) {
        byte[] result = new byte[8];

        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>>= 8;
        }

        return result;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(
                String.format("%02X", b & 0xFF)
            );
        }

        return result.toString();
    }

    private static byte[] hexToBytes(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException(
                "Invalid hexadecimal ciphertext."
            );
        }

        byte[] result =
            new byte[hex.length() / 2];

        for (int i = 0; i < result.length; i++) {
            int high =
                Character.digit(hex.charAt(i * 2), 16);

            int low =
                Character.digit(hex.charAt(i * 2 + 1), 16);

            if (high == -1 || low == -1) {
                throw new IllegalArgumentException(
                    "Invalid hexadecimal ciphertext."
                );
            }

            result[i] =
                (byte) ((high << 4) | low);
        }

        return result;
    }
}
