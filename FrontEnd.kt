import kotlinx.browser.*
import org.w3c.dom.*
import org.w3c.dom.events.MouseEvent

// Cria o HTML de um botão com base nos parâmetros recebidos
fun buttonString(_class: String, _Id: String, _onclick: String, _value: String): String =
   "<button class=\"${_class}\" id=\"${_Id}\" onmousedown=\"${_onclick}(event,\'${_Id}\')\">${_value}</button>\n"
// Matriz paramétrica funcional
class Matrix<T>(val values: List<List<T>>) {
   // Dimensões
   val w = values[0].size
   val h = values.size
   // Mapeamento para cada um dos valores, poderia por exemplo retornar o quadrado de todos os dados matriz
   fun <B> map(f: (T) -> B): Matrix<B> = buildMatrix(w, h) { x -> f(values[x / w][x % w]) }
   // Passa os dados para uma lista, como se estivesse "colando" uma linha a outra lado a lado
   fun toList():List<T>{
       fun appendLists(l:List<List<T>>):List<T> = if(l.size == 0) listOf() else l[0] + appendLists(l.drop(1))
       return appendLists(values)
   }
   // Encontra o valor numa posição x e y
   fun getValue(x: Int, y: Int): T? = if (x < 0 || y < 0 || x >= w || y >= h) null else values[y][x]
   // Define o valor numa posição da matriz e retorna uma cópia (por ser funcional)
   fun setValue(x: Int, y: Int, newValue: T): Matrix<T> =
         if (x < 0 || y < 0 || x >= w || y >= h) this
         else buildMatrix(w, h) { k -> if (k == ((w * y) + x)) newValue else values[k / w][k % w] }
   // Passa a matriz para String
   override fun toString(): String {
      if (values.size == 0 || values[0].size == 0) {
         return ""
      } else {
         val rows =
               values.map() { row -> row.fold("") { x, y -> "${x} ${y}" } }.map() { s ->
                  s.substring(1)
               }
         val matrixString = rows.fold("") { x, y -> "${x}\n${y}" }
         return matrixString.substring(1)
      }
   }
   // Cria uma matriz de botões HTML
   fun toButtons(_IdPrefix: String, _onclick: String, _classSelector: (T) -> String, valueSelector: (T) -> String): String {
      if (values.size == 0 || values[0].size == 0) {
         return ""
      } else {
         // Valor de um determinado botão
         fun defButtonValues(idx: Int): String =
            buttonString(_classSelector(values[idx / w][idx % w]), _IdPrefix + "_${idx}", _onclick, valueSelector(values[idx/w][idx%w]))
         // Matriz que indicará o número da ID de cada botão (para saber qual está sendo clicado)
         val indexMatrix: Matrix<Int> = buildMatrix(w, h) { x -> x }
         // Botões todos juntos num HTML em texto
         val rows = indexMatrix.values.map() { row -> row.map() { b -> defButtonValues(b) }.fold("") { x, y -> "${x}${y}" }}
         val matrixString = rows.fold("") { x, y -> "${x}${y}<br>" }
         return matrixString
      }
   }
}
// Matriz paramétrica mutável
class MutableMatrix<T>(val values: MutableList<MutableList<T>>) {
   val w = values[0].size
   val h = values.size
   // Mapeamento para cada um dos valores, neste caso retornando uma matriz também mutável
   fun <B> map(f: (T) -> B): MutableMatrix<B> =
         buildMutableMatrix(w, h) { x -> f(values[x / w][x % w]) }
   // Passa os dados para uma lista, como se estivesse "colando" uma linha a outra lado a lado
   fun toList():List<T>{
       fun appendLists(l:List<List<T>>):List<T> =
         if(l.size == 0) listOf() else l[0] + appendLists(l.drop(1))
       return appendLists(values)
   }
   // Encontra o valor numa posição x e y
   fun getValue(x: Int, y: Int): T? = if (x < 0 || y < 0 || x >= w || y >= h) null else values[y][x]
   // Define o valor numa posição da matriz, como é mutável o valor pode ser alterado na própria estrutura
   fun setValue(x: Int, y: Int, newValue: T) {
      if (!(x < 0 || y < 0 || x >= w || y >= h)) {values[y][x] = newValue}
   }
   // Passa a matriz para String
   override fun toString(): String {
      if (values.size == 0 || values[0].size == 0) {
         return ""
      } else {
         val rows =
               values.map() { row -> row.fold("") { x, y -> "${x} ${y}" } }.map() { s ->
                  s.substring(1)
               }
         val matrixString = rows.fold("") { x, y -> "${x}\n${y}" }
         return matrixString.substring(1)
      }
   }
   // Cria uma matriz de botões HTML
   fun toButtons(_IdPrefix: String, _onclick: String, _classSelector: (T) -> String, valueSelector: (T) -> String): String {
      if (values.size == 0 || values[0].size == 0) {
         return ""
      } else {
         // Valor de um determinado botão
         fun defButtonValues(idx: Int): String =
            buttonString(_classSelector(values[idx / w][idx % w]), _IdPrefix + "_${idx}", _onclick, valueSelector(values[idx/w][idx%w]))
         // Matriz que indicará o número da ID de cada botão (para saber qual está sendo clicado)
         val indexMatrix: Matrix<Int> = buildMatrix(w, h) { x -> x }
         // Botões todos juntos num HTML em texto
         val rows = indexMatrix.values.map() { row -> row.map() { b -> defButtonValues(b) }.fold("") { x, y -> "${x}${y}" }}
         val matrixString = rows.fold("") { x, y -> "${x}${y}<br>" }
         return matrixString
      }
   }
}
// Constrói uma matriz analisando os elementos de 0 a width*height-1 como se estivessem em linha
fun <T> buildMatrix(width: Int, height: Int, f: (Int) -> T): Matrix<T> {
   val valuesList = List(height) { y -> List(width) { x -> f((y * width) + x) } }
   return Matrix(valuesList)
}
// Constrói uma matriz mutável analisando os elementos de 0 a width*height-1 como se estivessem em linha
fun <T> buildMutableMatrix(width: Int, height: Int, f: (Int) -> T): MutableMatrix<T> {
   val valuesList = MutableList(height) { y -> MutableList(width) { x -> f((y * width) + x) } }
   return MutableMatrix(valuesList)
}
// Mergesort quebra uma lista em unidades e vai ordenado elas aumentando até o tamanho original
fun mergesort(l: List<Int>): List<Int> {
   // Realiza a ordenação de cada um dos blocos
   fun merge(l1: List<Int>, l2: List<Int>): List<Int> =
         if (l1.size == 0) l2
         else if (l2.size == 0) l1
         else if (l1[0] < l2[0]) listOf(l1[0]) + merge(l1.drop(1), l2)
         else listOf(l2[0]) + merge(l1, l2.drop(1))
   if (l.size <= 1) {
      return l
   } else if (l.size == 2) {
      return if (l[0] > l[1]) listOf(l[1], l[0]) else listOf(l[0], l[1])
   } else {
      val half = l.size / 2
      return merge(mergesort(l.dropLast(l.size - half)), mergesort(l.drop(half)))
   }
}
// Retorna uma lista aleatória de números que é subconjunto de uma lista original
fun randomSublist(l: List<Int>, size: Int): List<Int> =
      if (size <= 0) listOf()
      else if (size >= l.size) l
      else {
         val idx = (0..(l.size - 1)).random()
         listOf(l[idx]) + randomSublist(l.dropLast(l.size - idx) + l.drop(idx + 1), size - 1)
      }
// Classe que admite dois valores de tipos paramétricos
class Par<A, B>(val a: A, val b: B) {
   override fun toString() = "[${a},${b}]"
}
// Gera uma lista de pares dos elementos em todas as ordenações possíveis
// ex: listOf(1,2) -> listOf(Par(1,1),Par(1,2),Par(2,1),Par(2,2))
fun <T> combinatoricList(lista: List<T>): List<Par<T, T>> {
   fun <T> pairValueToList(value: T, l: List<T>): List<Par<T, T>> = l.map() { x -> Par(value, x) }
   return lista.map() { valor -> pairValueToList(valor, lista) }.fold(listOf()) { sub1, sub2 -> sub1 + sub2 }
}
// Função simples de conversão de segundos para o contador
fun seconds2CounterString(t:Int):String{
    val min = t/60
    val sec = t%60
    return (if(min < 10) "0${min}" else "${min}") + ":" + (if(sec < 10) "0${sec}" else "${sec}")
}
// Classe do tabuleiro do jogo
class Board(val width: Int, val height: Int, val bombCells:List<Int>) {
   // Quantidade de bombas não muda
   val bombCount = bombCells.size
   // Quantidade de bandeiras muda
   var flagCount = 0
   // Matriz que originou o tabuleiro, os valores da lista de bombas serão marcados como negativos
   private val originMatrix = buildMatrix(width, height) { x -> if (bombCells.any() { b -> b == x }) -1 else x }
   // Valores são definidos com base na quantidade de bombas adjacentes a eles
   private val board = buildMatrix(width, height) { x -> adjacentBombCount(x % width, x / width) }
   // Parte do tabuleiro que o jogador vê na tela
   private var visibleBoard = buildMutableMatrix(width, height) {"#"}
   // Libera os campos contendo números em volta do que foi clicado pelo usuário caso este seja zero
   private fun clearNumbersAround(x: Int, y: Int) {
      val visibleValue = visibleBoard.getValue(x, y)
      val actualValue = board.getValue(x, y)
      // A verificação abaixo controla para que o algoritmo não atinja um loop infinito
      if (actualValue == null || actualValue == -1 || visibleValue != "#") {
         return
      } else {
         // Revela o número da casa em questão
         visibleBoard.setValue(x, y, "${actualValue}")
         // Se o número for zero, o valor dos campos em volta é revelado
         if (actualValue == 0) {
            val adjacentIndexes =
                  combinatoricList(listOf(-1, 0, 1)).filter() { par -> (par.a != 0 || par.b != 0) }
            adjacentIndexes.forEach() { cell -> clearNumbersAround(x + cell.a, y + cell.b) }
         }
      }
   }
   // Faz com que todas as células cobertas em volta sejam reveladas, independente de ter ou não bomba
   private fun clearCellsAround(x: Int, y: Int) {
      val adjacentIndexes = combinatoricList(listOf(-1, 0, 1)).filter() { par -> (par.a != 0 || par.b != 0) }
      // Desobstrui cada índice em volta do valor
      adjacentIndexes.forEach() { cell ->
         val new_x = x + cell.a
         val new_y = y + cell.b
         val visibleValue = visibleBoard.getValue(new_x, new_y)
         val actualValue = board.getValue(new_x, new_y)
         if (actualValue != null && visibleValue == "#") {
            if (actualValue == -1) {
               visibleBoard.setValue(new_x, new_y, "x") // Se achar uma bomba o jogador perdeu
            }
            else {
               clearNumbersAround(new_x, new_y) // Se for um número, segue o jogo
            }
         }
      }
   }
   // Mostra todas as bombas não marcadas por bandeiras no tabuleiro
   fun revealAllBombs(){
      val actualValueList = board.toList()
      val visibleValueList = visibleBoard.toList()
      visibleBoard = buildMutableMatrix(width,height){
         x ->
         if(actualValueList[x] == -1 && visibleValueList[x] == "#") "x"
         else{
            val visibleValue = visibleBoard.getValue(x%width,x/width)
            if(visibleValue != null) visibleValue else "#"
         }
      }
   }
   // Toda a interação feita entre o usuário e o tabuleiro
   fun interact(idx: Int, inputType: Int) {
      val y = idx / width
      val x = idx % width
      val visibleValue = visibleBoard.getValue(x, y)
      val actualValue = board.getValue(x, y)
      if (visibleValue == null || actualValue == null) {
         println("Invalid input!")
      } else {
         if (inputType == 0) {
            // Clique
            if (actualValue == -1 && visibleValue == "#") {visibleBoard.setValue(x, y, "x")}
            else if(visibleValue == "#") {clearNumbersAround(x, y)}
         } else if (inputType == 1) {
            // Scroll
            val cellValue = board.getValue(x, y)
            val visibleNumber = !(listOf("#", "x", "F").any() { value -> value == visibleValue })
            if (visibleNumber && cellValue != null && adjacentFlagCount(x, y) >= cellValue) {clearCellsAround(x, y)}
         } else if (inputType == 2) {
            // Direito
            if(visibleValue == "#"){
               if(flagCount < bombCount){
                  visibleBoard.setValue(x, y, "F")
                  flagCount += 1
               }
            } else if(visibleValue == "F") {
               visibleBoard.setValue(x, y, "#")
               flagCount -= 1
            }
         }
      }
   }
   // Conta quantidade de bandeira adjacentes à uma célula, bom para saber se pode habilitar o scroll
   private fun adjacentFlagCount(x: Int, y: Int): Int {
      val visibleValue = visibleBoard.getValue(x, y)
      if (visibleValue == null) {
         return -1
      } else {
         val adjacentIndexes = combinatoricList(listOf(-1, 0, 1)).filter() { par -> (par.a != 0 || par.b != 0) }
         val adjacentValues = adjacentIndexes.map() { value -> visibleBoard.getValue(x + value.a, y + value.b) }
         return adjacentValues.filter() { value -> value == "F" }.size
      }
   }
   // Conta quantidade de bombas adjacentes à uma célula
   private fun adjacentBombCount(x: Int, y: Int): Int {
      val cellValue = originMatrix.getValue(x, y)
      if (cellValue == null || cellValue == -1) {
         return -1
      } else {
         val adjacentIndexes = combinatoricList(listOf(-1, 0, 1)).filter() { par -> (par.a != 0 || par.b != 0) }
         val adjacentValues = adjacentIndexes.map() { value -> originMatrix.getValue(x + value.a, y + value.b) }
         return adjacentValues.filter() { value -> value == -1 }.size
      }
   }
   // Detecta se o jogador perdeu
   fun perdeu():Boolean = visibleBoard.toList().any(){x -> x == "x"}
   // Detecta se o jogador ganhou
   fun ganhou():Boolean {
      val totalDescobertos = visibleBoard.toList().filter(){x -> !listOf("#", "x", "F").any() { v -> v == x }}.size
      return totalDescobertos == width*height - bombCount
   }
   // Retorna o tabuleiro de botões visível em HTML
   fun getVisibleBoard():String {
      val classSelector:(String) -> String = {x -> when(x){"#" -> "generic_cell"; "F" -> "flag_cell"; "x" -> "bomb_cell"; else -> "number_cell"}}
      val valueSelector:(String) -> String = {s -> if (listOf("#","F","x","0").any(){v -> v == s}) "&nbsp;" else "${s}"}
      return visibleBoard.toButtons("cell", "FrontEnd.clicaCelula", classSelector, valueSelector)
   }
}

// Dimensões
var largura = 15
var altura = 10
// Variáveis do jogo
var bombas = 0
var dificuldade = 0
// Tabuleiro de teste
var probeBoard = buildMatrix(largura,altura){"&nbsp;"}
// Tabuleiro real
var board = Board(largura,altura,listOf())
// HTMLs
val cronometro = document.getElementById("cronometro") as HTMLDivElement
val bandeira = document.getElementById("bandeira") as HTMLDivElement
val tabuleiro = document.getElementById("tabuleiro") as HTMLDivElement
// Cronômetro
var timer = 0
var seconds = 0
// Define o tempo no cronômetro
fun setCronometro(str:String){
   cronometro.innerHTML = "${str}"
}
// Define quantas bandeiras faltam ser descobertas no HTML da bandeira
fun setBandeira(remainingBombs:Int){
   val txt = (if(remainingBombs < 10) "00" else if(remainingBombs < 100) "0" else "") + "${remainingBombs}"
   bandeira.innerHTML = txt
}
// Define a dificuldade do jogo, mudando as variáveis que dela dependem
@JsName("setDificuldade")
fun setDificuldade(dif:Int){
   dificuldade = dif
   largura = when(dificuldade){0->8; 1->13; else->21}
   altura = when(dificuldade){0->5; 1->8; else->13}
   bombas = when(dificuldade){0->5; 1->20; else->50}
   probeBoard = buildMatrix(largura,altura){"&nbsp;"}
   setBandeira(bombas)
}
// Retorna a dificuldade
@JsName("getDificuldade")
fun getDificuldade() = when(dificuldade){0->"Fácil"; 1->"Médio"; else->"Difícil"}
// Cria o tabuleiro de teste
@JsName("criaProbe")
fun criaProbe(){
   setBandeira(bombas)
   setCronometro("00:00")
   tabuleiro.innerHTML = probeBoard.toButtons("probe", "FrontEnd.clicaProbe", {"generic_cell"}, {"&nbsp;"})
   window.clearInterval(timer);
}
// Detecta um clique no tabuleiro de teste para evitar que o usuário comece já clicando em bomba
@JsName("clicaProbe")
fun clicaProbe(event:MouseEvent,cellId:String){
   if(event.button.toInt() == 0){
      val idx = cellId.substring(6).toInt()
      val w = probeBoard.w
      val h = probeBoard.h
      // Detecção de células adjacentes
      var spareCells = listOf(idx) + when {
         idx == 0 -> listOf(idx+1,idx+w,idx+w+1)                                        // cima esquerda
         idx == w-1 -> listOf(idx-1, idx+w-1, idx+w)                                    // cima direita
         idx < w-1 -> listOf(idx-1, idx+1, idx+w-1, idx+w, idx+w+1)                     // cima centro
         idx == w*(h-1) -> listOf(idx+1, idx-w, idx-w+1)                                // baixo esquerda
         idx == (w*h)-1 -> listOf(idx-1, idx-w, idx-w-1)                                // baixo direita
         idx > w*(h-1) -> listOf(idx-1, idx+1, idx-w, idx-w-1, idx-w+1)                 // baixo centro
         idx % w == 0 -> listOf(idx+1, idx-w, idx-w+1, idx+w, idx+w+1)                  // centro esquerda
         idx % w == w-1 -> listOf(idx-1, idx-w, idx-w-1, idx+w, idx+w-1)                // centro esquerda
         else -> listOf(idx-w-1, idx-w, idx-w+1, idx-1, idx+1, idx+w-1, idx+w, idx+w+1) // demais casos
      }
      // Seleciona células que podem conter bombas
      val possibleBombCells = List(w*h){x->x}.filter(){c->!spareCells.any(){v -> v == c}}
      board = Board(w, h, randomSublist(possibleBombCells, bombas))
      tabuleiro.innerHTML = board.getVisibleBoard()
      clicaCelula(event,"cell_${idx}")
      // Timer
      seconds = 0
      timer = window.setInterval({setCronometro(seconds2CounterString(++seconds))}, 1000)
   }
}
// Realiza uma interação no tabuleiro clicando numa célula
@JsName("clicaCelula")
fun clicaCelula(event:MouseEvent,cellId:String){
   if(!board.ganhou() && !board.perdeu()){
      val matrix_idx = cellId.substring(5).toInt()
      board.interact(matrix_idx,event.button.toInt())
      if(board.perdeu()){
         window.clearInterval(timer)
         board.revealAllBombs()
      } else if(board.ganhou()) {
         var problematic = document.getElementById("problematic") as HTMLDivElement
         problematic.innerHTML = "<img width=\"0\" height=\"0\" src onerror=\"msg()\">"
         window.clearInterval(timer)
      }
      tabuleiro.innerHTML = board.getVisibleBoard()
   }
   setBandeira(board.bombCount - board.flagCount)
}
// Define alguns detalhes inicialmente
fun main() {
   setDificuldade(0)
   setBandeira(0)
   setCronometro("00:00")
}
