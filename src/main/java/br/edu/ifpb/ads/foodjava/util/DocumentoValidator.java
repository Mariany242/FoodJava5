package br.edu.ifpb.ads.foodjava.util;

public final class DocumentoValidator {
    private DocumentoValidator() {
    }

    public static boolean cpfValido(String cpf) {
        String doc = apenasDigitos(cpf);
        if (doc.length() != 11 || todosIguais(doc)) {
            return false;
        }
        int d1 = digito(doc.substring(0, 9), 10);
        int d2 = digito(doc.substring(0, 9) + d1, 11);
        return doc.equals(doc.substring(0, 9) + d1 + d2);
    }

    public static boolean cnpjValido(String cnpj) {
        String doc = apenasDigitos(cnpj);
        if (doc.length() != 14 || todosIguais(doc)) {
            return false;
        }
        int d1 = digitoCnpj(doc.substring(0, 12));
        int d2 = digitoCnpj(doc.substring(0, 12) + d1);
        return doc.equals(doc.substring(0, 12) + d1 + d2);
    }

    public static String apenasDigitos(String valor) {
        return valor == null ? "" : valor.replaceAll("\\D", "");
    }

    private static int digito(String base, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * (pesoInicial - i);
        }
        int resto = 11 - (soma % 11);
        return resto >= 10 ? 0 : resto;
    }

    private static int digitoCnpj(String base) {
        int[] pesos = base.length() == 12
                ? new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2}
                : new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private static boolean todosIguais(String doc) {
        return doc.chars().distinct().count() == 1;
    }
}
