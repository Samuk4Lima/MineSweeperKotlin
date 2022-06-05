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

// Classe que admite dois valores de tipos paramétricos
class Par<A, B>(val a: A, val b: B) {
   override fun toString() = "[${a},${b}]"
}
// Quicksort para valores em par, passando um tipo qualquer com um inteiro ordenador
fun <T>quicksortPar(lista:List<Par<T,Int>>):List<Par<T,Int>>{
   if(lista.size <= 1){return lista}
   else {
       val pivot = lista[0]
       val tail = lista.drop(1)
       return quicksortPar(tail.filter(){p -> p.b < pivot.b}) + listOf(pivot) + quicksortPar(tail.filter(){p -> p.b >= pivot.b})
   }
}
// Ordena strings com base em um número em meio a caracteres identificadores
// identificador é o padrão em volta da string para encontrá-la, por exemplo **9** identificador ** encontra 9
// conversor é a função que devolverá um inteiro para fazer a ordenação, com o exemplo acima seria "9".toInt()
fun sortStringsByParam(lista:List<String>, identificador:String, conversor:(String)->Int):List<String>{
   if(lista.size <= 1){return lista}
   val pares = lista.map(){
      x->
      val first = x.indexOf(identificador) + identificador.length
      val last = x.length - x.lastIndexOf(identificador)
      val num = conversor((x.drop(first).dropLast(last)))
      Par(x,num)
   }
   var minP = pares[0].b
   pares.forEach(){
      p->
      if(minP <= p.b){minP = p.b}
      else {return quicksortPar(pares).map(){par->par.a}}
   }
   return pares.map(){par->par.a}
}
// Gera uma sequência aleatória a partir de elementos de uma lista paramétrica sem repetir em um determinado size
fun <T>randSequence(lista:List<T>,size:Int):List<T> =
   if(lista.size == 0 || size <= 0) listOf()
   else listOf(lista[(0..(lista.size-1)).random()]) + randSequence(lista,size-1)
// Escreve elementos de uma lista de Strings num arquivo .txt um por linha
fun writeText(filename:String, content:List<String>){
   if(content.size > 0){File(filename).writeText(content.joinToString(separator="\n"))}
}
// Adiciona elementos de uma lista de Strings num arquivo .txt um por linha
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
// Recebe conteúdo de um arquivo de texto em String
fun getFile(filename:String):String{
   val file = File(filename)
   return if(file.length() == 0L) "" else file.readText()
}
// Limita os scores das 3 listas (fácil, médio e difícil) à uma quantidade, ordenada do menor para o maior tempo
fun sortScores(melhores:Int){
   val file = File("stored_data/scores.txt")
   if(file.length() > 0L){
      // Converte string pra número de segundos
      val str2sec:(String)->Int = {
         str->
         val minsAndSecs = str.split(":")
         if(minsAndSecs.size < 2) -1
         else minsAndSecs[0].toInt()*60 + minsAndSecs[1].toInt()
      }
      val stringList = file.readText().split("\n")
      // Lista fácil
      val f = sortStringsByParam(stringList.filter(){s->s.contains("(Fácil):")},"%%",str2sec)
      val facil = if(f.size > melhores) f.dropLast(f.size-melhores) else f
      // Lista médio
      val m = sortStringsByParam(stringList.filter(){s->s.contains("(Médio):")},"%%",str2sec)
      val medio = if(m.size > melhores) m.dropLast(m.size-melhores) else m
      // Lista difícil
      val d = sortStringsByParam(stringList.filter(){s->s.contains("(Difícil):")},"%%",str2sec)
      val dificil = if(d.size > melhores) d.dropLast(d.size-melhores) else d
      // Junta tudo no novo texto
      val newText = listOf("<ol>Difícil") + dificil + listOf("</ol><ol>Médio") + medio + listOf("</ol><ol>Fácil") + facil + listOf("</ol>")
      file.writeText(newText.joinToString(separator="\n"))
   }
}
// Limita o número de mensagens recebidas no servidor ao inteiro passado
fun limitMessages(max:Int){
   val file = File("stored_data/messages.txt")
   if(file.length() > 0L){
      val messages = file.readText().split("\n")
      // Remove as primeiras mensagens (mais antigas) caso passe do limite
      if(messages.size > max){
         writeText("stored_data/messages.txt", messages.drop(messages.size-max))
      }
   }
}
// Lista de letras maiúsculas, minúsculas e números
val caracteres = List(26){x->(65+x).toChar()} + List(26){x->(97+x).toChar()} + List(10){x->(48+x).toChar()}
// Página aonde serão enviadas as solicitações de score, assim não tem como o alterar com facilidade
val randomPage = randSequence(caracteres,20).map(){x->x.toString()}.fold(""){x,y->x+y}
// Páginas HTML
val gameHTML = File("static/FrontEnd.html").readText().replace("http://localhost:7654/score", "http://localhost:7654/" + randomPage)
val indexHTML = File("static/index.html").readText()
val scoresHTML = File("static/scores.html").readText()

fun main() {
   println("Iniciando servidor...")
   println("Página dos scores localizada em http://localhost:7654/" + randomPage)
   embeddedServer(Netty, port = 7654) {
      routing {
         get("/" + randomPage + "/{jogador}/{dificuldade}/{tempo}") {
            // Dados coletados para fazer o armazenamento
            val currentTime = LocalTime.now().toString().substring(0,8)
            val jogador = call.parameters["jogador"] as String
            val dificuldade = call.parameters["dificuldade"] as String
            val tempo = (call.parameters["tempo"] as String).replace("a",":")
            // Mensagem que será enviada ao servidor e também visível na página dos scores
            val serverMessage = "[${currentTime}] " + (if(jogador == " ") "Anônimo" else jogador) + " ganhou no nível ${dificuldade} em ${tempo}"
            appendToText("stored_data/messages.txt",listOf("<li>" + serverMessage + "</li>"))
            println(serverMessage)
            // Só salva o score se não for anônimo
            if(jogador != " "){
               val info = "<li>[${currentTime}] ${jogador} (${dificuldade}): %%${tempo}%%</li>"
               appendToText("stored_data/scores.txt",listOf(info))
            }
            sortScores(3)
            // Para evitar que a página interprete que o servidor não respondeu
            call.respondText("<html></html>",ContentType.Text.Html)
         }
         // Página inicial
         get("/"){
            call.respondText(indexHTML,ContentType.Text.Html)
         }
         // Página do jogo
         get("/game"){
            call.respondText(gameHTML,ContentType.Text.Html)
         }
         // Pontuações de outros jogadores
         get("/scores"){
            call.respondText(scoresHTML,ContentType.Text.Html)
         }
         // Solicitação para receber o arquivo dos scores
         get("/gScores"){
            sortScores(3)
            call.respondText(getFile("stored_data/scores.txt").replace("%%",""),ContentType.Text.Html)
         }
         // Solicitação para receber o arquivo das mensagens
         get("/gMessages"){
            limitMessages(12)
            call.respondText("<ul>Jogos recentes"+getFile("stored_data/messages.txt")+"</ul>",ContentType.Text.Html)
         }
         // Arquivos estáticos
         static("/") {files("static/")}
      }
   }.start(wait=true)
}
