package org.someone120.anki_dict.anki

import android.content.Context
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.someone120.anki_dict.appScope


class DeckConfig(context: Context) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    var deskName: String = "Dict"
    val fields: List<String> =
        listOf(
            "Word",
            "Definition",
            "Phonetic",
            "ExampleSentence",
            "ExampleSentenceMeans",
            "Voice"
        )
    val cardName = "Anki-Dict"

    val CSS="""
        </style>

        <!-- 英语单词模板-vocab配色 ninja33.github.io -->

        <style>
        @import url('_collins_c.css');

        .card {
         font-family: sans-serif;
         font-size: 16px;
         text-align: left;
         color:#686868;
        }

        .section {
         color: #444;
         border-bottom: 3px solid #666;
         background-color: #fff;
         margin-top:5px;
         padding: 10px;
         position: relative;
         text-align: left;
        }

        .extension {
         font-size: 16px;
         line-height: 1.4em;
         border-bottom: 1px solid #ddd;
         background-color: #f5f5f5;
         padding: 10px;
         display:block;
        }


        .word_header_star{
         font-size: 20px;
         font-family: 'SS Standard';
         color: #fdac00;
         position: absolute;
         right: 15px;
         top: 5px;
        }

        #english{
         font-size: 45px;
         line-height: 80%;
         padding-bottom:2px;
        }


        #chinese a {
        	text-decoration: none;
        	padding: 1px 6px 2px 5px;
        	margin: 0 5px 0 0;
        	font-size: 12px;
        	color: white;
        	font-weight: normal;
        	border-radius: 4px
        }

        #chinese a.pos_n {
        	background-color: #e3412f
        }

        #chinese a.pos_v {
        	background-color: #539007
        }

        #chinese a.pos_a {
        	background-color: #f8b002
        }

        #chinese a.pos_r {
        	background-color: #684b9d
        }

        #vocab{
         font-size: 12px;
         background:#69ac1d;
         background-image: url('_vocab_logo.png');
         background-repeat: no-repeat;
         background-size:contain;
        }

        #collins{
         font-size: 12px;	
         background-color: rgb(2,16,79);
         background-image: url('_collins_logo4.png');
         background-repeat: no-repeat;
         background-size:contain;
        }

        /* 缺省打开，把block改成none，缺省关闭*/
        #ext_vocab{
         display:block;
        }

        /* 缺省打开，把block改成none，缺省关闭*/
        #ext_collins{
         display:block;
        }

        .phonetic{
         font-size:14px;
        }

        .voice img{
         margin-left:5px;
         width: 24px;
         height: 24px;
        }
        </style>

        <script src='_jquery.js'></script>
        <script>
        ${'$'}(document).ready(function(){
          ${'$'}.each(["vocab","collins"],function(i,x){
            ${'$'}("#"+x).click(function(){
              ${'$'}("#ext_"+x).slideToggle();
            });
            ${'$'}("#ext_"+x).click(function(){
              ${'$'}("#ext_"+x).slideToggle();
            });
          });
        /*
          var html = ${'$'}("#chinese").html();
          html = html.replace(/n\./g,"<a class='pos_n'>n.</a>");
          html = html.replace(/adj\./g,"<a class='pos_a'>adj.</a>");
          html = html.replace(/adv\./g,"<a class='pos_r'>adv.</a>");
          html = html.replace(/vt\./g,"<a class='pos_v'>vt.</a>");
          html = html.replace(/vi\./g,"<a class='pos_v'>vi.</a>");
          html = html.replace(/v\./g,"<a class='pos_v'>v.</a>");
          ${'$'}("#chinese").html(html);
        */
        });
        </script>

        <style>
    """.trimIndent()
    val Front = """
        <div id="english" class="section">{{Word}}<span class="voice">{{Voice}}</span>
        <div class="phonetic">{{Phonetic}}</div>
        </div>
    """.trimIndent()
    val Back="""
        {{FrontSide}}

        <div id='ext_english'>
        <div id="chinese" class="extension">{{Definition}}</div>
        {{#ExampleSentence}}
          <div id="sample_en" class="extension">{{ExampleSentence}}</div>
        {{/ExampleSentence}}
        {{#ExampleSentenceMeans}}
          <div id="sample_cn" class="extension">{{ExampleSentenceMeans}}</div>
        {{/ExampleSentenceMeans}}
        </div>
    """.trimIndent()

    init {
        appScope.launch {
            context.dataStore.edit { body ->
                deskName = body[stringPreferencesKey("deskName")] ?: "Dict"
            }
        }
    }

    fun setDeskName(context: Context, name: String) {
        deskName = name
        appScope.launch {
            context.dataStore.edit { body ->
                body[stringPreferencesKey("deskName")] = name
            }
        }
    }
}