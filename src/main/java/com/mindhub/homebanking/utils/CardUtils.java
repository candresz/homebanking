package com.mindhub.homebanking.utils;

public final class CardUtils {
    public static String generateRandomCardNumber() {

        // String holaMundo = "Hola";
        //    holaMundo+= "Mundo";
        StringBuilder cardNumber; // String Builder(Clase de Java) me permite modificar el contenido de mi cadena sin necesitar de instanciar.

            cardNumber = new StringBuilder(); // Se crea un objeto nuevo cada vez que se completa un numero de tarjeta.
            for (int i = 0; i < 16; i++) { // 16 iteraciones
                int digit = (int) (Math.random() * 10); // Genera un número de 0 a 9 y lo guardamos en digit
                cardNumber.append(digit); // El número generado lo guardamos con .append gracias a StringBuilder

                if ((i + 1) % 4 == 0 && i != 15) {
                    cardNumber.append("-"); // Agregar un guión después de cada 4 iteraciones.
                }
            }

        return cardNumber.toString();
    }


    // Metodo para generar un número de tarjeta de crédito aleatorio
    public static String generateRandomCvvNumber() {
        StringBuilder cardCvvNumber;

            cardCvvNumber = new StringBuilder();
            for (int i = 0; i < 3; i++) { // 16 iteraciones
                int digit = (int) (Math.random() * 3); // Genera un número de 0 a 9 y lo guardamos en digit
                cardCvvNumber.append(digit); // El número generado lo guardamos con .append gracias a StringBuilder

            }

        return cardCvvNumber.toString();
    }
}
