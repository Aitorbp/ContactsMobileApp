package com.example.contactosapp.ui.detail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.example.contactosapp.data.server.Contact

class ContactDetailInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    fun setMovie(contact: Contact) = with(contact) {

        text = buildSpannedString {

            bold { append("Gender: ") }
            appendLine(gender)

            bold { append("Username: ") }
            appendLine(login.username)

            bold { append("Email: ") }
            appendLine(email)

            bold { append("Phone: ") }
            appendLine(phone)

            bold { append("Address: ") }
            appendLine(location.street.name  + ' ' + location.city + ' ' + location.state + ' ' + location.postcode)
        }
    }
}