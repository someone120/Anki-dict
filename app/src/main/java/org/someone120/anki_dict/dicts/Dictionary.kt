package org.someone120.anki_dict.dicts

interface Dictionary {
    /***
     * Get Paraphrase of this word.
     * @param word
     * @return definition
    */
    fun getDefinition(word:String):List<Definition>
    fun getExampleSentence(word:String):List<ExampleSentence>
}

/***
 * the definition of word.
 * @param pos part of speech (noun, verb, adjective etc).
 * @param definition
 */
data class Definition(val pos:String, val definition:String)

/***
 * Example sentence.
 * @param origin the sentence written by origin language.
 * @param translation the translation of this sentence.
 */
data class ExampleSentence(val origin:String,val translation:String)