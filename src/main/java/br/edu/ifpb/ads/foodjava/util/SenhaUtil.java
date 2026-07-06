package br.edu.ifpb.ads.foodjava.util;

import br.edu.ifpb.ads.foodjava.exception.SenhaInvalidaException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SenhaUtil {
    private SenhaUtil() {
    }

    public static void validar(String senha) throws SenhaInvalidaException {
        if (senha == null || senha.length() < 8 || senha.chars().noneMatch(Character::isDigit)) {
            throw new SenhaInvalidaException("Senha deve ter pelo menos 8 caracteres e um dígito numérico.");
        }
    }

    public static String hash(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível.", e);
        }
    }
}
