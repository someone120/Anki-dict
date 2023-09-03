package org.someone120.anki_dict.dicts

interface Dictionary {
    fun getParaphrase(word:String):List<Paraphrase>
    fun getExampleSentence(word:String):List<ExampleSentence>
}

data class Paraphrase(val pos:String,val paraphrase:String)
data class ExampleSentence(val origin:String,val translation:String)