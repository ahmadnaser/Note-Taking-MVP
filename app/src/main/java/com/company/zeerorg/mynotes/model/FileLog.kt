package com.company.zeerorg.mynotes.model

import java.io.*


/**
 * Created by zeerorg on 7/3/17.
 */
class FileLog(val dir: String) : FileLogInterface {

    val FILENAME = "script_file"

    companion object {
        var editted: Boolean = false
    }

    override fun logCreateNote(id: Long) {
        editted = true
        val file = File(dir, FILENAME)
        val fos = OutputStreamWriter(FileOutputStream(file, true))
        fos.write("CREATE " + id.toString() + "\n")
        fos.close()
    }

    override fun logUpdateNote(id: Long) {
        editted = true
        val file = File(dir, FILENAME)

        if(file.exists()) {
            val inputStreamReader = InputStreamReader(FileInputStream(file))
            val bufferedReader = BufferedReader(inputStreamReader)
            var receiveString = bufferedReader.readLine()
            while (receiveString != null) {
                receiveString.trim()
                if (receiveString.contains(id.toString())) {
                    bufferedReader.close()
                    inputStreamReader.close()
                    return
                }
                receiveString = bufferedReader.readLine()
            }
            bufferedReader.close()
            inputStreamReader.close()
        }

        val fos = OutputStreamWriter(FileOutputStream(file, true))
        fos.write("UPDATE " + id.toString() + "\n")
        fos.close()
    }

    override fun logDeleteNote(id: Long) {
        editted = true
        val file = File(dir, FILENAME)
        val fileData = ""

        if(file.exists()) {
            val inputStreamReader = InputStreamReader(FileInputStream(file))
            val bufferedReader = BufferedReader(inputStreamReader)
            var receiveString = bufferedReader.readLine()
            while (receiveString != null) {
                receiveString.trim()
                if (!receiveString.contains(id.toString())) {
                    fileData.plus(receiveString)
                    fileData.plus("\n")
                }
                receiveString = bufferedReader.readLine()
            }
            bufferedReader.close()
            inputStreamReader.close()
        }

        val fos = OutputStreamWriter(FileOutputStream(file, true))
        fos.write(fileData)
        fos.write("DELETE " + id.toString() + "\n")
        fos.close()
    }

    override fun executeLog(createFun: (id: Long) -> Unit, updateFun: (id: Long) -> Unit, deleteFun: (id: Long) -> Unit) {

        val file = File(dir, FILENAME)

        if(!file.exists()) {
            return
        }

        val inputStreamReader = InputStreamReader(FileInputStream(file))
        val bufferedReader = BufferedReader(inputStreamReader)

        var receiveString = bufferedReader.readLine()
        while (receiveString != null) {
            receiveString.trim()

            val instr = receiveString.split(" ")[0]
            val id = receiveString.split(" ")[1].toLong()

            if(instr == "CREATE") {
                createFun(id)
            } else if(instr == "UPDATE") {
                updateFun(id)
            } else if(instr == "DELETE") {
                deleteFun(id)
            }

            receiveString = bufferedReader.readLine()
        }
        bufferedReader.close()
        inputStreamReader.close()

        val fos = FileOutputStream(file)
        fos.write(kotlin.ByteArray(0))
        fos.close()

        editted = false
    }
}