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

fun transforma(lista:List<String>):List<String>{
 
    val maxLinha = 70
    if(lista.size>1){
        if(lista.get(0).length + lista.get(1).length < maxLinha){
    		return transforma ( listOf( lista.get(0) +" " + lista.get(1) ) + lista.drop(2) )
        }else{
            if(lista.get(0).length<maxLinha){           
          	  return listOf(lista.get(0)) + transforma(lista.drop(1))
      	  	}else{
              return listOf(lista.get(0).substring(0,maxLinha)) + transforma( listOf(lista.get(0).substring(maxLinha,lista.get(0).length)) + lista.drop(1))
            }
        }
    }else{
        if(lista.get(0).length<maxLinha){
          	  return lista
      	  	}else{
              return listOf(lista.get(0).substring(0,maxLinha)) + transforma( listOf(lista.get(0).substring(maxLinha,lista.get(0).length)))
            }
    }
    
}

fun main() {
    embeddedServer(Netty, port = 7654){
        routing{
            get("/"){
                call.respondText("""
                <html>
                <head>
                    <title>Campo Minado</title> 
                </head>
                <form method="POST" action="chat" target="_blank">
                <input type="submit" value="Abrir sala de bate-papo"> 
                </form>
                </html> 
                """,ContentType.Text.Html)
            }
            post("/chat") {
                val hora =LocalTime.now().plusHours(-3)
                val parameters = call.receiveParameters() 
                val nome = if(parameters["nome"] == null || parameters["nome"] == "") "Anônimo" else parameters["nome"]
                
                if( parameters["texto"] != null && parameters["texto"] != ""){
                     File("conversas.txt").writeText( File("conversas.txt").readText()+ ("("+hora.toString().substring(0,8)+
                     ") <b>"+nome!!.replace("<","&lt").replace(">","&gt") +":</b> "+ parameters["texto"]!!.replace("<","&lt").replace(">","&gt") +"\n") )
                }

                val conversaSize = File("conversas.txt").readText().split("\n").size
                val mensagens = File("conversas.txt").readText().split("\n").drop(conversaSize - (if(conversaSize>=35) 35 else 0))
                File("conversas.txt").writeText(mensagens.joinToString(separator="\n"))

                call.respondText("""
                <html>
                <script>
                setInterval(function() { 
                     fetch('/conversas').then(response => response.text()).then(text => document.getElementById("conversas").innerHTML = text)
                }, 100);             
                </script>
                <p style="font-family:monospace" id="conversas">
                ${ mensagens.map{x -> transforma(x.split(" ")).joinToString(separator="<br>")}.joinToString(separator="<br>") }
                </p>
                <head><title>Sala de bate-papo</title> </head>
                <form method="POST" action="chat">
                <input type="text" size="15" maxlength="15" placeholder="Nome de Usuário" value="${nome}" name="nome">
                <input type="text" size="50" maxlength="100" placeholder="Insira seu texto" name="texto">
                <input type="submit" value="Enviar"> 
                </form>
                </html>              
                """,ContentType.Text.Html)
            }
            get("/conversas"){
                call.respondText( File("conversas.txt").readText().split("\n").map{x -> transforma(x.split(" ")).joinToString(separator="<br>")}.joinToString(separator="<br>")  )
            }
            static("/estatico/"){
                files("static/")
            }
        }
    }.start(wait=true)
}