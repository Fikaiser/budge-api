package hr.fika.budge.utils

import com.google.crypto.tink.*
import com.google.crypto.tink.aead.AeadConfig
import hr.fika.budge.Secrets
import java.io.File
import java.security.GeneralSecurityException
import java.util.*

object EncryptionManager {
    private var keysetHandle: KeysetHandle? = null

    fun initEncryption() {
        AeadConfig.register()
        val keysetFile = File(Secrets.KEYSET_FILENAME.value)
        if (keysetFile.exists()) {
            keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(File(Secrets.KEYSET_FILENAME.value)))
        } else {
            keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES128_GCM"))
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile( File(Secrets.KEYSET_FILENAME.value)))
        }
    }

    private fun getHandle() : KeysetHandle {
        keysetHandle?.let {
            return it
        }
        error("Keyset not loaded")
    }

    fun encryptPassword(pass: String) : String {
        with(getHandle()) {
            val aead = getPrimitive(Aead::class.java)
            val encryptedText = aead.encrypt(pass.toByteArray(Charsets.UTF_8), null)
            return Base64.getEncoder().encodeToString(encryptedText)
        }
    }

    fun verifyPassword(pass: String, hash: String) : Boolean {
        with(getHandle()) {
            val aead = getPrimitive(Aead::class.java)
            try {
                val decrypted = Base64.getDecoder().decode(hash)
                val decryptedText = aead.decrypt(decrypted, null)
                return decryptedText.toString(Charsets.UTF_8) == pass
            }
            catch (ex: GeneralSecurityException){
                println("Error decrypting: ${ex.message}")
                return false
            }
        }
    }

}