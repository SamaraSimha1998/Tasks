package com.example.tasks.validatingUSADLN

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var sss: String
    private lateinit var ssss: String
    private lateinit var fff: String
    private lateinit var dlnValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        btn_details_validate.setOnClickListener {
            // validate DLN number.
            checkRequiredFields()
            val givenDLN = dln_number_edit_text.text.toString().substring(0,12).uppercase()
            Log.d("dlnGiven", givenDLN)
            if (givenDLN == dlnValue) {
                Toast.makeText(this, "DLN verified successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "DLN number is not valid to user details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // checks fields of details empty or not
    private fun checkRequiredFields() {
        // Checks required fields filled or not and proceeds validation.
        val firstName = details_first_name_edit_text.text.toString()
        val middleName = details_middle_name_edit_text.text.toString()
        val lastName = details_last_name_edit_text.text.toString()
        val date = details_date_edit_text.text.toString()
        val month = details_month_edit_text.text.toString()
        val year = details_year_edit_text.text.toString()
        val dln = dln_number_edit_text.text.toString()

        when {
            firstName == "" -> {
                Toast.makeText(this, "required first name", Toast.LENGTH_SHORT).show()
            }
            middleName == "" -> {
                Toast.makeText(this, "required middle name", Toast.LENGTH_SHORT).show()
            }
            lastName == "" -> {
                Toast.makeText(this, "required last name", Toast.LENGTH_SHORT).show()
            }
            date == "" -> {
                Toast.makeText(this, "required date", Toast.LENGTH_SHORT).show()
            }
            month == "" -> {
                Toast.makeText(this, "required month", Toast.LENGTH_SHORT).show()
            }
            year == "" -> {
                Toast.makeText(this, "required year", Toast.LENGTH_SHORT).show()
            }
            dln == "" -> {
                Toast.makeText(this, "required DLN", Toast.LENGTH_SHORT).show()
            }
            else -> {
                dlnValue = florida()
            }
        }
    }

    // validates DLN for Florida state
    private fun florida() : String{
        // Pattern
        val lastName = details_last_name_edit_text.text.toString()

        val lastNameFirstLetter: Char = lastName[0]

        val date = Integer.parseInt(details_date_edit_text.text.toString())
        val month = Integer.parseInt(details_month_edit_text.text.toString())
        val year = details_year_edit_text.text.toString()
        val gender: String = if (gender_radio_group.checkedRadioButtonId == male_radio_button.id) {
            "Male"
        } else {
            "Female"
        }
        val genderValue = if (gender == "Male") {
            0
        } else {
            500
        }

        val encodeDOB = (month - 1) * 40 + date + genderValue
        val dddCode = String.format("%03d", encodeDOB)
        val yyCode = year.substring(2, 4)

        soundexCode(lastNameFirstLetter)
        ssssCode()
        fffCode()
        return ssss + fff + yyCode + dddCode
    }

    // SSSS-FFF-YY-DDD

    // SSSS encoding functions
    // SSSS alphabets values
    private fun soundexCode(lastNameLetters: Char): Char {
        return when (lastNameLetters) {
            'A', 'a' -> {
                '0'
            }
            'B', 'b' -> {
                '1'
            }
            'C', 'c' -> {
                '2'
            }
            'D', 'd' -> {
                '3'
            }
            'E', 'e' -> {
                '0'
            }
            'F', 'f' -> {
                '1'
            }
            'G', 'g' -> {
                '2'
            }
            'H', 'h' -> {
                '0'
            }
            'I', 'i' -> {
                '0'
            }
            'J', 'j' -> {
                '2'
            }
            'K', 'k' -> {
                '2'
            }
            'L', 'l' -> {
                '4'
            }
            'M', 'm' -> {
                '5'
            }
            'N', 'n' -> {
                '5'
            }
            'O', 'o' -> {
                '0'
            }
            'P', 'p' -> {
                '1'
            }
            'Q', 'q' -> {
                '2'
            }
            'R', 'r' -> {
                '6'
            }
            'S', 's' -> {
                '2'
            }
            'T', 't' -> {
                '3'
            }
            'U', 'u' -> {
                '0'
            }
            'V', 'v' -> {
                '1'
            }
            'W', 'w' -> {
                '0'
            }
            'X', 'x' -> {
                '2'
            }
            'Y', 'y' -> {
                '0'
            }
            else -> {
                '2'
            }
        }
    }

    // removes letters ( a e i o u h w y ) from last name
    private fun removeLettersFromLastName(lastName: String): String {
        val result = StringBuilder()
        for (char: Char in lastName) {
            if (!"aeiouhwy".contains(char)) {
                result.append(char)
            }
        }
        return result.toString()
    }

    // encodes string to SoundexCode String
    private fun encodeLastName(requiredLastName: String): String {
        var s = ""
        when {
            requiredLastName.length > 3 -> {
                for (element in requiredLastName) {
                    val ssss: Char = soundexCode(element)
                    s = java.lang.StringBuilder(s).append(ssss.toString()).toString()
                }
            }
            requiredLastName.length == 3 -> {
                for (i in 0..2) {
                    val ssss: Char = soundexCode(requiredLastName[i])
                    s = java.lang.StringBuilder(s).append(ssss.toString()).toString()
                }
            }
            requiredLastName.length == 2 -> {
                for (i in 0..1) {
                    val ssss: Char = soundexCode(requiredLastName[i])
                    s = java.lang.StringBuilder(s).append(ssss.toString()).toString()
                    Log.d("namer", soundexCode(requiredLastName[i]).toString())
                    Log.d("namer", s)
                }
                s = java.lang.StringBuilder(s).append('0').toString()
            }
            requiredLastName.length == 1 -> {
                val ssss: Char = soundexCode(requiredLastName[0])
                s = java.lang.StringBuilder(s).append(ssss.toString()).toString()
                s = java.lang.StringBuilder(s).append('0').toString()
                s = java.lang.StringBuilder(s).append('0').toString()
            }
            else -> {
                s = java.lang.StringBuilder(s).append('0').toString()
                s = java.lang.StringBuilder(s).append('0').toString()
                s = java.lang.StringBuilder(s).append('0').toString()
            }
        }
        return s
    }

    // encodes to required _SSS string
    private fun requiredEncodedLastName(encodedLastName: String): String {
        val x = encodedLastName.length - 1
        var string = encodedLastName[0].toString()
        for (i in 1..x) {
            if (i == 1) {
                if (encodedLastName[i - 1].toString() != encodedLastName[i].toString()) {
                    string += encodedLastName[i]
                }
            } else if (i == 2) {
                if (encodedLastName[i - 1].toString() != encodedLastName[i].toString()) {
                    string += encodedLastName[i]
                }
            } else {
                string += encodedLastName[i]
            }
        }
        when (string.length) {
            2 -> {
                string = "0$string"
            }
            1 -> {
                string = "00$string"
            }
        }
        return string
    }

    // SSSS value
    private fun ssssCode() {
        val lastName = details_last_name_edit_text.text.toString()
        val lastNameFirstLetter: Char = lastName[0]
        val requiredLastName = removeLettersFromLastName(lastName)
        if (requiredLastName == "") {
            sss = "000000"
        } else {
            val s = encodeLastName(requiredLastName)
            val ss = requiredEncodedLastName(s) + "00000"
            if (lastNameFirstLetter == requiredLastName[0]) {
                sss = ss.substring(1)
            } else if (lastNameFirstLetter != requiredLastName[0]) {
                sss = ss
            }
        }
        ssss = lastNameFirstLetter.uppercase() + sss.substring(0, 3)
    }

    // FFF encoding functions
    // first name first letter values and Common name values
    private fun commonFirstNames(firstName: String): Int {
        return when (firstName) {
            "Albert", "albert" -> {
                20
            }
            "Frank", "frank" -> {
                260
            }
            "Marvin", "marvin" -> {
                80
            }
            "Alice", "alice" -> {
                20
            }
            "George", "george" -> {
                300
            }
            "Mary", "mary" -> {
                580
            }
            "Ann", "ann" -> {
                40
            }
            "Grace", "grace" -> {
                300
            }
            "Melvin", "melvin" -> {
                600
            }
            "Anna", "anna" -> {
                40
            }
            "Harold", "harold" -> {
                340
            }
            "Mildred", "mildred" -> {
                600
            }
            "Anne", "anne" -> {
                40
            }
            "Harriet", "harriet" -> {
                340
            }
            "Patricia", "patricia" -> {
                680
            }
            "Annie", "annie" -> {
                40
            }
            "Harry", "harry" -> {
                360
            }
            "Paul", "paul" -> {
                680
            }

            "Arthur", "arthur" -> {
                40
            }
            "Hazel", "hazel" -> {
                360
            }
            "Richard", "richard" -> {
                740
            }
            "Bernard", "bernard" -> {
                80
            }
            "Helen", "helen" -> {
                380
            }
            "Robert", "robert" -> {
                760
            }
            "Bette", "bette" -> {
                80
            }
            "Henry", "henry" -> {
                380
            }
            "Ruby", "ruby" -> {
                740
            }
            "Bettie", "bettie" -> {
                80
            }
            "James", "james" -> {
                440
            }
            "Ruth", "ruth" -> {
                760
            }
            "Betty", "betty" -> {
                80
            }
            "Jane", "jane" -> {
                440
            }
            "Thelma", "thelma" -> {
                820
            }
            "Carl", "carl" -> {
                120
            }
            "Jayne", "jayne" -> {
                440
            }
            "Thomas", "thomas" -> {
                820
            }
            "Catherine", "catherine" -> {
                120
            }
            "Jean", "jean" -> {
                460
            }
            "Walter", "walter" -> {
                900
            }
            "Charles", "charles" -> {
                140
            }
            "Joan", "joan" -> {
                480
            }
            "Wanda", "wanda" -> {
                900
            }
            "Dorthy", "dorthy" -> {
                180
            }
            "John", "john" -> {
                460
            }
            "William", "william" -> {
                920
            }
            "Edward", "edward" -> {
                220
            }
            "Joseph", "joseph" -> {
                480
            }
            "Wilma", "wilma" -> {
                920
            }
            "Elizabeth", "elizabeth" -> {
                220
            }
            "Margaret", "margaret" -> {
                560
            }
            "Florence", "florence" -> {
                260
            }
            "Martin", "martin" -> {
                560
            }
            "Donald", "donald" -> {
                180
            }
            "Clara", "clara" -> {
                140
            }
            else -> {
                0
            }
        }
    }

    // gives first name first letter value
    private fun checkFirstName(letter: Char): Int {

        val a = 0
        val h = 320
        val o = 640
        val v = 860
        val b = 60
        val i = 400
        val p = 660
        val w = 880
        val c = 100
        val j = 420
        val q = 700
        val x = 940
        val d = 160
        val k = 500
        val r = 720
        val y = 960
        val e = 200
        val l = 520
        val s = 780
        val z = 980
        val f = 240
        val m = 540
        val t = 800
        val g = 280
        val n = 620
        val u = 840

        return when (letter) {
            'A', 'a' -> {
                a
            }
            'B', 'b' -> {
                b
            }
            'C', 'c' -> {
                c
            }
            'D', 'd' -> {
                d
            }
            'E', 'e' -> {
                e
            }
            'F', 'f' -> {
                f
            }
            'G', 'g' -> {
                g
            }
            'H', 'h' -> {
                h
            }
            'I', 'i' -> {
                i
            }
            'J', 'j' -> {
                j
            }
            'K', 'k' -> {
                k
            }
            'L', 'l' -> {
                l
            }
            'M', 'm' -> {
                m
            }
            'N', 'n' -> {
                n
            }
            'O', 'o' -> {
                o
            }
            'P', 'p' -> {
                p
            }
            'Q', 'q' -> {
                q
            }
            'R', 'r' -> {
                r
            }
            'S', 's' -> {
                s
            }
            'T', 't' -> {
                t
            }
            'U', 'u' -> {
                u
            }
            'V', 'v' -> {
                v
            }
            'W', 'w' -> {
                w
            }
            'X', 'x' -> {
                x
            }
            'Y', 'y' -> {
                y
            }
            else -> {
                z
            }
        }
    }

    // gives middle name first letter value
    private fun checkMiddleName(letter: Char): Int {
        val a = 1
        val h = 8
        val o = 14
        val v = 18
        val b = 2
        val i = 9
        val p = 15
        val w = 19
        val c = 3
        val j = 10
        val q = 15
        val x = 19
        val d = 4
        val k = 11
        val r = 16
        val y = 19
        val e = 5
        val l = 12
        val s = 17
        val z = 19
        val f = 6
        val m = 13
        val t = 18
        val g = 7
        val n = 14
        val u = 18

        return when (letter) {
            'A', 'a' -> {
                a
            }
            'B', 'b' -> {
                b
            }
            'C', 'c' -> {
                c
            }
            'D', 'd' -> {
                d
            }
            'E', 'e' -> {
                e
            }
            'F', 'f' -> {
                f
            }
            'G', 'g' -> {
                g
            }
            'H', 'h' -> {
                h
            }
            'I', 'i' -> {
                i
            }
            'J', 'j' -> {
                j
            }
            'K', 'k' -> {
                k
            }
            'L', 'l' -> {
                l
            }
            'M', 'm' -> {
                m
            }
            'N', 'n' -> {
                n
            }
            'O', 'o' -> {
                o
            }
            'P', 'p' -> {
                p
            }
            'Q', 'q' -> {
                q
            }
            'R', 'r' -> {
                r
            }
            'S', 's' -> {
                s
            }
            'T', 't' -> {
                t
            }
            'U', 'u' -> {
                u
            }
            'V', 'v' -> {
                v
            }
            'W', 'w' -> {
                w
            }
            'X', 'x' -> {
                x
            }
            'Y', 'y' -> {
                y
            }
            else -> {
                z
            }
        }
    }

    // FFF alphabet values
    private fun requiredEncodedFirstMiddleNames(firstName: String, firstNameFirstLetterValue: Int, middleNameFirstLetterValue: Int): Int {
        var f = 0
        if (commonFirstNames(firstName) != 0) {
            f = commonFirstNames(firstName) + middleNameFirstLetterValue
        } else if (commonFirstNames(firstName) == 0) {
            f = firstNameFirstLetterValue + middleNameFirstLetterValue
        }
        return f
    }

    // FFF value
    private fun fffCode() {
        val firstName = details_first_name_edit_text.text.toString()
        val middleName = details_middle_name_edit_text.text.toString()

        val firstNameFirstLetter: Char = firstName[0]
        val firstNameLetterValue = checkFirstName(firstNameFirstLetter)

        val middleNameFirstLetter: Char = middleName[0]
        val middleNameLetterValue = checkMiddleName(middleNameFirstLetter)

        // if middle name not exists ( out of algorithm )
//            val middleNameFirstLetter: Char = if (middleName == "") { '0' } else { middleName[0] }
//            val middleNameLetterValue = if (middleNameFirstLetter == '0') { 0 } else { checkMiddleName(middleNameFirstLetter) }

        val ff = requiredEncodedFirstMiddleNames(firstName, firstNameLetterValue, middleNameLetterValue)
        fff = String.format("%03d", ff)
    }
}