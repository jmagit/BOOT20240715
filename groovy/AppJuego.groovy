teclado = new Scanner(System.in);
def pideEntrada(message) {
    print message
    teclado.nextLine()
    // javax.swing.JOptionPane.showInputDialog(message)
}

try {
    def juego = new JuegoDelNumero(args?.size() > 0 && args?[0] == '--debug')
    //juego.inicializar()
    juego.addNotificaListener(arg -> {
        println "NOTIFICA: $arg.message"
        if(!juego.finalizado) {
            arg.cancel = pideEntrada('¿Quieres cancelar? (s/N): ').toLowerCase() == 's'
        }
    })
    while (true) {
        try {
            juego.jugada(pideEntrada("Dame tu número del 1 al 100 (intento ${juego.jugada + 1} de $juego.MAX_INTENTOS): "))
            if (juego.finalizado) {
                break
            }
        } catch (JuegoDelNumero.JuegoException ex) {
            if (ex.getCause() instanceof NumberFormatException) {
                println ex.getMessage()
            } else {
                throw ex
            }
        }
    }
} catch (JuegoDelNumero.JuegoException ex) {
    ex.printStackTrace()
}

