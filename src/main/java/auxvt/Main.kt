package auxvt

import java.io.File

fun main() {

    println("Hello, thank you for using the AuxVT interactive verifier.")
    println("You can get more information by executing the help command. \n")

    while (true) {
        print(">> ")

        val userInput = readLine()
        val inputWords = userInput?.split(" ")
        val command = inputWords?.get(0)!!
        val args = inputWords.subList(1, inputWords.size)

        if (command == "quit")
            break;

        val result = when(command) {
            "load"-> AuxVT.load(File(args[0]))
            "save" -> AuxVT.save()
            "signature" -> AuxVT.signature()
            "theorems" -> AuxVT.theorems()
            "proof" -> AuxVT.proof(args)
            "apply" -> AuxVT.apply(args)
            else -> error("Unknown command")
        }

        println(result.message)
    }

    println("Bye, thank you for using the AuxVT interactive verifier.")

}
