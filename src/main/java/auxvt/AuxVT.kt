package auxvt

import io.parser.Parser
import io.parser.ParsingException
import logic.theory.Theory
import logic.proof.OpenProofNode
import logic.proof.ProofNode
import printing.getStringRepresentation
import java.io.File
import java.lang.NumberFormatException
import io.TheoryStorage

object AuxVT {

    private var theoryFile: File? = null

    private var theory: Theory? = null

    private var curProof: ProofNode? = null

    fun load(file: File): ApiResponse {
        if (!file.exists())
            return error("File does not exist")

        return try {
            theory = Parser.parseTheory(file)
            this.theoryFile = file

            theory!!.theorems.forEach { name, sequent ->
                theory!!.proofs[name] = OpenProofNode(sequent)
            }

            success("Successfully loaded theory")
        } catch (e : ParsingException) {
            error(e.message)
        }
    }

    fun save(): ApiResponse {
        if (theory == null)
            return error("Please load a theory first")

        TheoryStorage.SaveTheory(theory!!, theoryFile!!)

        return message("Successfully stored proofs")
    }

    fun signature(): ApiResponse {
        return error("")
    }

    fun functions(): ApiResponse {
        return error("")
    }

    fun predicates(): ApiResponse {
        return error("")
    }

    fun axioms(): ApiResponse {
        return error("")
    }

    fun theorems(): ApiResponse {
        if (theory == null)
            return error("Please load a theory first")

        var resultString = ""
        theory!!.theorems.forEach { name, sequent ->
            resultString += name + ": " + sequent.getStringRepresentation()
        }

        return message(resultString)
    }

    fun proof(args: List<String>): ApiResponse {
        if (args.size != 1)
            return error("Wrong argument count")

        if (theory == null)
            return error("Please load a theory first")

        val theoremName = args[0]
        val proofs = theory!!.proofs
        if(!proofs.containsKey(theoremName))
            return error("No such theorem")

        curProof = proofs[theoremName]
        if (curProof!!.isComplete())
            return message("Proof already completed")

        return getNextOpenGoal()
    }

    fun apply(args: List<String>): ApiResponse {
        if (args.isEmpty() || args.size > 3)
            return error("Wrong argument count")

        if (theory == null)
            return error("Please load a theory first")

        if (curProof == null)
            return error("Please start proof first")

        if (curProof!!.isComplete())
            return error("Proof already completed")

        val ruleName = args[0]
        if (args.size == 1)
            return proofByAxiom(ruleName)

        val position = args[1]
        if (args.size == 2)
            return proofByPositionDependendRule(ruleName, position)

        val substitute = args[2]
        return proofBySubstitutionRule(ruleName, position, substitute)
    }

    private fun proofByAxiom(axiomName: String): ApiResponse {
        if (!ProofCommandMapper.hasAxiomRule(axiomName))
            return error("Unknown rule")

        val axiom = ProofCommandMapper.mapToAxiomRule(axiomName)
        if (!axiom.applicable(curProof!!.getFirstNodeWithOpenGoals()!!.sequent))
            return error("Rule not applicable")

        curProof = curProof!!.withProofOfLeftmost(axiom)

        return getNextOpenGoal()
    }

    private fun proofByPositionDependendRule(ruleName: String, position: String): ApiResponse {
        if (!ProofCommandMapper.hasPositionDependendRule(ruleName))
            return error("Unknown rule")

        val pos: Int
        try {
            pos = Integer.parseInt(position)
        } catch (e : NumberFormatException) {
            return error("Second argument must be an integer")
        }

        val rule = ProofCommandMapper.mapToPositionDependendRule(ruleName, pos)
        if (!rule.applicable(curProof!!.getFirstNodeWithOpenGoals()!!.sequent))
            return error("Rule not applicable")

        curProof = curProof!!.withProofOfLeftmost(rule)

        return getNextOpenGoal()
    }

    private fun proofBySubstitutionRule(ruleName: String, position: String, substitute: String): ApiResponse {
        if (!ProofCommandMapper.hasSubstitutionRule(ruleName))
            return error("Unknown rule")

        val pos: Int
        try {
            pos = Integer.parseInt(position)
        } catch (e : NumberFormatException) {
            return error("Second argument must be an integer")
        }

        try {
            val substitutionTerm = Parser.parseTerm(substitute, theory!!.signature)
            val rule = ProofCommandMapper.mapToSubstitutionRule(ruleName, pos, substitutionTerm)
            if (!rule.applicable(curProof!!.getFirstNodeWithOpenGoals()!!.sequent))
                return error("Rule not applicable")

            curProof = curProof!!.withProofOfLeftmost(rule)

            return getNextOpenGoal()
        } catch (e: ParsingException) {
            return error("Parsing exception: " + e.message)
        }
    }

    private fun getNextOpenGoal() : ApiResponse
    {
        val openGoal =  curProof!!.getFirstNodeWithOpenGoals()
        if (openGoal == null)
            return message("Proof completed")

        return message("Next open goal: " + openGoal.sequent.getStringRepresentation())
    }

}
