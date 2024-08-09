//
// Calcula el factorial de un número
//
def factorial(int num) {
    num > 1 ? num * factorial(num-1) : 1
}

def num =javax.swing.JOptionPane.showInputDialog("Número a factorizar:") as Integer
println "El factorial de $num es ${factorial(num)}"

//
// Muestra los números primos entre 1 y 100.
//

def primos(maximo = 100) {
    def rslt = []
    def candidato = 2
    while (maximo > candidato) {
        var esPrimo = true
        for (var indice in rslt) {
            if (candidato % indice == 0) {
                esPrimo = false
                break
            }
        }

        candidato++
        if (esPrimo) {
            if (candidato < maximo)
                rslt << (candidato - 1)
        }
    }
    rslt
}
primos()

//
// Crear una función que valide un NIF
//
def esNIF(String nif) {
    if (nif =~ /^\d{1,8}[A-Za-z]$/) {
        def letterValue = nif[-1]
        def numberValue = Integer.parseInt(nif.substring(0, nif.length() - 1))
        return letterValue.toUpperCase() == 'TRWAGMYFPDXBNJZSQVHLCKE'[numberValue % 23]
    } 
    false
}
def noEsNIF(String nif) {
    !esNIF(nif)
}
def nif = javax.swing.JOptionPane.showInputDialog("NIF:")
println "$nif es un NIF ${esNIF(nif) ? 'valido' : 'invalido'}."
