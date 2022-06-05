import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.request.*
import java.io.*
import kotlin.io.*
import java.time.LocalTime

// Quicksort para valores em par, passando um tipo qualquer com um inteiro ordenador
fun <T>quicksortPar(l:List<Par<T,Int>>):List<Par<T,Int>>{
   if(l.size <= 1){return l}
   else {
       val pivot = l[0]
       val tail = l.drop(1)
       return quicksortPar(tail.filter(){p -> p.b < pivot.b}) + listOf(pivot) + quicksortPar(tail.filter(){p -> p.b >= pivot.b})
   }
}

class Par<A, B>(val a: A, val b: B) {
   override fun toString() = "[${a},${b}]"
}

// Ordena strings com base em um número em meio a caracteres identificadores
fun sortStringsByParam(l:List<String>, identifier:String, stringConverter:(String)->Int):List<String>{
   if(l.size <= 1){return l}
   val pairs = l.map(){
      x->
      val first = x.indexOf(identifier) + identifier.length
      val last = x.length - x.lastIndexOf(identifier)
      val integer = stringConverter((x.drop(first).dropLast(last)))
      Par(x,integer)
   }
   var minP = pairs[0].b
   pairs.forEach(){
      p->
      if(minP <= p.b){minP = p.b}
      else {return quicksortPar(pairs).map(){par->par.a}}
   }
   return pairs.map(){par->par.a}
}

// Gera uma
fun <T>randSequence(l:List<T>,size:Int):List<T> =
   if(l.size == 0 || size <= 0) listOf()
   else listOf(l[(0..(l.size-1)).random()]) + randSequence(l,size-1)

fun writeText(filename:String, content:List<String>){
   if(content.size > 0){File(filename).writeText(content.joinToString(separator="\n"))}
}

fun appendToText(filename:String, content:List<String>){
   if(content.size > 0){
      val file = File(filename)
      if(file.length() == 0L){
         writeText(filename,content)
      } else {
         file.writeText((file.readText().split("\n") + content).joinToString(separator="\n"))
      }
   }
}

fun getFile(filename:String):String{
   val file = File(filename)
   return if(file.length() == 0L) "" else file.readText()
}

fun sortScores(best:Int){
   // Converte string pra número de segundos
   val file = File("stored_data/scores.txt")
   if(file.length() > 0L){
      val str2sec:(String)->Int = {
         str->
         val minsAndSecs = str.split(":")
         if(minsAndSecs.size < 2) -1 else minsAndSecs[0].toInt()*60 + minsAndSecs[1].toInt()
      }
      val stringList = file.readText().split("\n")

      val f = sortStringsByParam(stringList.filter(){s->s.contains("(Fácil):")},"%%",str2sec)
      val facil = if(f.size > best) f.dropLast(f.size-best) else f

      val m = sortStringsByParam(stringList.filter(){s->s.contains("(Médio):")},"%%",str2sec)
      val medio = if(m.size > best) m.dropLast(m.size-best) else m

      val d = sortStringsByParam(stringList.filter(){s->s.contains("(Difícil):")},"%%",str2sec)
      val dificil = if(d.size > best) d.dropLast(d.size-best) else d
      
      val newText = listOf("<ol>Difícil") + dificil + listOf("</ol><ol>Médio") + medio + listOf("</ol><ol>Fácil") + facil + listOf("</ol>")
      file.writeText(newText.joinToString(separator="\n"))
   }
}

fun limitMessages(max:Int){
   val file = File("stored_data/messages.txt")
   if(file.length() > 0L){
      val messages = file.readText().split("\n")
      if(messages.size > max){writeText("stored_data/messages.txt", messages.drop(messages.size-max))}
   }
}

// Lista de letras maiúsculas, minúsculas e números
val caracteres = List(26){x->(65+x).toChar()} + List(26){x->(97+x).toChar()} + List(10){x->(48+x).toChar()}
// Página aonde serão enviadas as solicitações de score, assim não tem como o usuário saber
val randomPage = randSequence(caracteres,20).map(){x->x.toString()}.fold(""){x,y->x+y}
var accesses:Int = 0
var gameHTML = File("static/FrontEnd.html").readText().replace("http://localhost:7654/score", "http://localhost:7654/" + randomPage)
var indexHTML = File("static/index.html").readText()
var scoresHTML = File("static/scores.html").readText()

fun main() {
   println(randomPage)
   println("iniciando servidor...")
   embeddedServer(Netty, port = 7654) {
      routing {
         get("/" + randomPage + "/{jogador}/{dificuldade}/{tempo}") {
            val currentTime = LocalTime.now().toString().substring(0,8)
            val jogador = call.parameters["jogador"] as String
            val dificuldade = call.parameters["dificuldade"] as String
            val tempo = (call.parameters["tempo"] as String).replace("a",":")

            val serverMessage = "[${currentTime}] " + (if(jogador == " ") "Anônimo" else jogador) + " ganhou no nível ${dificuldade} em ${tempo}"
            appendToText("stored_data/messages.txt",listOf("<li>" + serverMessage + "</li>"))
            
            // Só salva o score se não for anônimo
            if(jogador != " "){
               val info = "<li>[${currentTime}] ${jogador} (${dificuldade}): %%${tempo}%%</li>"
               appendToText("stored_data/scores.txt",listOf(info))
            }

            sortScores(3)
            println(serverMessage)
            call.respondText("<html></html>",ContentType.Text.Html)
         }
         get("/"){
            call.respondText(indexHTML,ContentType.Text.Html)
         }
         get("/game"){
            call.respondText(gameHTML,ContentType.Text.Html)
         }
         get("/scores"){
            call.respondText(scoresHTML,ContentType.Text.Html)
         }
         get("/gScores"){
            sortScores(3)
            call.respondText(getFile("stored_data/scores.txt").replace("%%",""),ContentType.Text.Html)
         }
         get("/gMessages"){
            limitMessages(12)
            call.respondText("<ul>Jogos recentes"+getFile("stored_data/messages.txt")+"</ul>",ContentType.Text.Html)
         }
         static("/") {files("static/")}
      }
   }.start(wait=true)
}
