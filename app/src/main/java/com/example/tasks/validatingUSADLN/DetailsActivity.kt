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
        when {
            string.length >= 3 -> {
                string
            }
            string.length == 2 -> {
                string = "0$string"
            }
            string.length == 1 -> {
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

        val A = 0
        val H = 320
        val O = 640
        val V = 860
        val B = 60
        val I = 400
        val P = 660
        val W = 880
        val C = 100
        val J = 420
        val Q = 700
        val X = 940
        val D = 160
        val K = 500
        val R = 720
        val Y = 960
        val E = 200
        val L = 520
        val S = 780
        val Z = 980
        val F = 240
        val M = 540
        val T = 800
        val G = 280
        val N = 620
        val U = 840

        return when (letter) {
            'A', 'a' -> {
                A
            }
            'B', 'b' -> {
                B
            }
            'C', 'c' -> {
                C
            }
            'D', 'd' -> {
                D
            }
            'E', 'e' -> {
                E
            }
            'F', 'f' -> {
                F
            }
            'G', 'g' -> {
                G
            }
            'H', 'h' -> {
                H
            }
            'I', 'i' -> {
                I
            }
            'J', 'j' -> {
                J
            }
            'K', 'k' -> {
                K
            }
            'L', 'l' -> {
                L
            }
            'M', 'm' -> {
                M
            }
            'N', 'n' -> {
                N
            }
            'O', 'o' -> {
                O
            }
            'P', 'p' -> {
                P
            }
            'Q', 'q' -> {
                Q
            }
            'R', 'r' -> {
                R
            }
            'S', 's' -> {
                S
            }
            'T', 't' -> {
                T
            }
            'U', 'u' -> {
                U
            }
            'V', 'v' -> {
                V
            }
            'W', 'w' -> {
                W
            }
            'X', 'x' -> {
                X
            }
            'Y', 'y' -> {
                Y
            }
            else -> {
                Z
            }
        }
    }

    // gives middle name first letter value
    private fun checkMiddleName(letter: Char): Int {
        val A = 1
        val H = 8
        val O = 14
        val V = 18
        val B = 2
        val I = 9
        val P = 15
        val W = 19
        val C = 3
        val J = 10
        val Q = 15
        val X = 19
        val D = 4
        val K = 11
        val R = 16
        val Y = 19
        val E = 5
        val L = 12
        val S = 17
        val Z = 19
        val F = 6
        val M = 13
        val T = 18
        val G = 7
        val N = 14
        val U = 18

        return when (letter) {
            'A', 'a' -> {
                A
            }
            'B', 'b' -> {
                B
            }
            'C', 'c' -> {
                C
            }
            'D', 'd' -> {
                D
            }
            'E', 'e' -> {
                E
            }
            'F', 'f' -> {
                F
            }
            'G', 'g' -> {
                G
            }
            'H', 'h' -> {
                H
            }
            'I', 'i' -> {
                I
            }
            'J', 'j' -> {
                J
            }
            'K', 'k' -> {
                K
            }
            'L', 'l' -> {
                L
            }
            'M', 'm' -> {
                M
            }
            'N', 'n' -> {
                N
            }
            'O', 'o' -> {
                O
            }
            'P', 'p' -> {
                P
            }
            'Q', 'q' -> {
                Q
            }
            'R', 'r' -> {
                R
            }
            'S', 's' -> {
                S
            }
            'T', 't' -> {
                T
            }
            'U', 'u' -> {
                U
            }
            'V', 'v' -> {
                V
            }
            'W', 'w' -> {
                W
            }
            'X', 'x' -> {
                X
            }
            'Y', 'y' -> {
                Y
            }
            else -> {
                Z
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