package org.someone120.anki_dict.separate

class Separater {
    fun separateEnglish(sentence: String):List<String> {
        return sentence.split(" ")
    }
}