package com.example.tasks.aapoonLoginPage.menuView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.tasks.aapoonLoginPage.dashboard.DashBoardActivity
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailSender {
    val username = "your-gmail-account"
    val password = "your-account-password"

     fun sendMail(activity: Activity, subject: String, body: String) {
        val props =Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"

        val session = Session.getInstance(props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password)
                }
            })
        try {
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress(username))
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("receivers-gmail-account")
            )
            message.subject = subject
            message.setText(body)
            Transport.send(message)
            Toast.makeText(activity, "email send successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(activity, DashBoardActivity::class.java)
            startActivity(activity, intent, Bundle())
            activity.finish()
        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }
}