package util;
public class Util {

    public static String formatDecimal(double value) {
        // Convertir le double en String
        String stringValue = Double.toString(value);
        
        // Remplacer la virgule par un point
        return stringValue.replace(',', '.');
    }
}
