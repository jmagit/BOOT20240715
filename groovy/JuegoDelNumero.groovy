import java.util.function.Consumer

// groovydoc -d doc com.example.juegos JuegoDelNumero.groovy

/**
 * Juego de adivinar numeros
 *
 * @author Javier
 * @version 1.0
 */
class JuegoDelNumero {

    /**
    * Excepciones propias de las reglas de los juegos
    */
    public static class JuegoException extends Exception {

        JuegoException(String message) {
            super(message);
        }

        JuegoException(String message, Throwable cause) {
            super(message, cause);
        }
        
    }
    /**
        Clase con los argumentos de los eventos de notificación, son cancelables
    */
    public static class NotificaEventArgs {

        private final String message
        /*
         * Permite calcelar el evento
         */
        boolean cancel = false

        /**
        * Costructor
        * @parameter message Mensaje a notificar
        */
        NotificaEventArgs(String message) {
            this.message = message
        }

        /*
         * Devuelve el mensaje de la notificacion
         */
        String getmessage() {
            return message
        }

        // boolean isCancel() {
        //     return cancel
        // }

        // void setCancel(boolean cancel) {
        //     this.cancel = cancel
        // }

    }

    /*
     * Numero maximo de intentos
     */
    public final int MAX_INTENTOS = 10
    /*
     * Numero maximo del rango de valores a adivinar, siempre enpieza en 1
     */
    public final int MAX_RANGO = 100
    private int numeroBuscado
    private int intentos
    private boolean encontrado
    private String resultado
    private boolean depurar

    private Consumer<NotificaEventArgs> notifica

    /**
    * Constructor por defecto
    * @paremeter depurar Activa que muestre el numero buscado en la depuración
    */
    JuegoDelNumero(depurar = false) {
        this.depurar = depurar
        inicializar()
    }

    /**
     * Cadena con el mensaje de la ultima jugada
     */
    String getResultado() {
        return resultado
    }

    /**
     * Indica si el juego ya ha finalizado
     */
    boolean isFinalizado() {
        return intentos >= MAX_INTENTOS || encontrado
    }

    /**
     * Indica el numero de intentos de la partida
     */
    int getJugada() {
        return intentos
    }

    /**
     * Inicializa el juego
     */
    void inicializar() {
        numeroBuscado = (new Random()).nextInt(MAX_RANGO) + 1
        if(depurar) println "DEBUG: $numeroBuscado"
        intentos = 0
        encontrado = false
        resultado = 'Pendiente de empezar'
        onNotifica(new NotificaEventArgs('Inicializado'))
    }

    /**
    * Realiza la jugada o intento
    * @paremeter numeroIntroducido Número introducido por el jugador en formato cadena
    */
    void jugada(String numeroIntroducido) {
        try {
            jugada(numeroIntroducido as Integer)
        } catch (NumberFormatException e) {
            throw new JuegoException('No es un número.', e)
        }
    }

    /**
    * Realiza la jugada o intento
    * @paremeter numeroIntroducido Número introducido por el jugador en formato numerico
    */
    void jugada(int numeroIntroducido) throws JuegoException {
        if (finalizado) {
            throw new JuegoException('El juego a finalizado')
        }
        intentos += 1
        if (numeroBuscado == numeroIntroducido) {
            encontrado = true
            resultado = 'Bieeen!!! Acertaste.'
        } else if (intentos >= MAX_INTENTOS) {
            resultado = "Upsss! Se acabaron los intentos, el número era el $numeroBuscado"
        } else if (numeroBuscado > numeroIntroducido) {
            resultado = 'Mi número es mayor.'
        } else {
            resultado = 'Mi número es menor.'
        }
        NotificaEventArgs arg = new NotificaEventArgs(resultado)
        onNotifica(arg)
        if (arg.cancel) {
            encontrado = true
            resultado = "CANCELADO: ${arg.message}"
        }
    }

    /*
     * Obtiene el controlador de eventos actual
     */
    Optional<Consumer<NotificaEventArgs>> getNotifica() {
        Optional.ofNullable(notifica)
    }

    /*
     * Añade el controlador de eventos
     */
    void addNotificaListener(Consumer<NotificaEventArgs> notifica) {
        this.notifica = notifica
    }

    /*
     * Quita el controlador de eventos
     */
    void removeNotificaListener() {
        this.notifica = null
    }

    /*
     * Ejecuta el controlador de eventos en caso de estar asociado, puede ser sobre escrito por los herederos
     */
    protected void onNotifica(NotificaEventArgs arg) {
        if (notifica != null) {
            notifica.accept(arg)
        }
    }

}

