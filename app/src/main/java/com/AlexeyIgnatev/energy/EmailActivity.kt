package com.AlexeyIgnatev.energy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_email.*
import kotlinx.android.synthetic.main.home_icon.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class EmailActivity : AppCompatActivity() {
    private var path: String? = null
    var isNotify = true
    private lateinit var appExecutors: AppExecutors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        appExecutors = AppExecutors()

        notify_btn.setOnClickListener {
            isNotify = !isNotify
            if (isNotify) {
                it.background = getDrawable(R.drawable.checkbox_active)
            } else {
                it.background = getDrawable(R.drawable.checkbox_inactive)
            }
        }

        home_icon.setOnClickListener {
            startActivity(BackActivity::class.java)
        }

        path = intent.getStringExtra("path")

        send_image_btn.setOnClickListener {
            sendEmail()
            startActivity(ShareActivity::class.java)
            finish()
        }
    }

    private fun sendEmail(){
        appExecutors.diskIO().execute {
            val props = System.getProperties()
            props.put("mail.smtp.host", "smtp.gmail.com")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")

            val session =  Session.getInstance(props,
                object : javax.mail.Authenticator() {
                    //Authenticating the password
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(Credentials.EMAIL, Credentials.PASSWORD)
                    }
                })

            try {
                //Creating MimeMessage object
                val mm = MimeMessage(session)
                val email = input_email.text.toString()
                //Setting sender address
                mm.setFrom(InternetAddress(Credentials.EMAIL))
                //Adding receiver
                mm.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(email))
                //Adding subject
                mm.subject = "Energy"

                var messageBodyPart = MimeBodyPart()
                //BOdy message
                messageBodyPart.setText("")

                val multipart = MimeMultipart()
                multipart.addBodyPart(messageBodyPart)

                //attachment
                messageBodyPart = MimeBodyPart()
                val source = FileDataSource(path)
                messageBodyPart.dataHandler = DataHandler(source)
                messageBodyPart.fileName = path

                multipart.addBodyPart(messageBodyPart)

                mm.setContent(multipart)

                //Sending email
                Transport.send(mm)

                appExecutors.mainThread().execute {

                }

            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }
}
