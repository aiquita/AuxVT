package io

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.parser.Parser
import logic.theory.Theory
import java.io.File

object TheoryStorage {

    fun LoadTheory(file: File): Theory {
        val theory = Parser.parseTheory(file)

        val theoryFileName = file.name
        val proofFile = File(theoryFileName + ".proof")

        return theory
    }

    fun SaveTheory(theory: Theory, file: File) {
        val theoryFileName = file.name
        val proofFileName = theoryFileName + ".proof"

        val proofFile = File(proofFileName)
        if (!proofFile.exists())
            proofFile.createNewFile()

        val mapper = jacksonObjectMapper()
        val content = mapper.writeValueAsString(theory)

        proofFile.writeText(content)
    }

}
