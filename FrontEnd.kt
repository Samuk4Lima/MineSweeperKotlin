import kotlinx.browser.*
import org.w3c.dom.*
import org.w3c.dom.events.MouseEvent

fun buttonString(_class: String, _Id: String, _onclick: String, _value: String): String =
   "<button class=\"${_class}\" id=\"${_Id}\" onmousedown=\"${_onclick}(event,\'${_Id}\')\">${_value}</button>\n"

// Matriz paramétrica funcional
class Matrix<T>(val values: List<List<T>>) {
   val w = values[0].size
   val h = values.size

   fun <B> map(f: (T) -> B): Matrix<B> = buildMatrix(w, h) { x -> f(values[x / w][x % w]) }

   fun toList():List<T>{
       fun appendLists(l:List<List<T>>):List<T> =
         if(l.size == 0) listOf() else l[0] + appendLists(l.drop(1))
       return appendLists(values)
   }

   fun getValue(x: Int, y: Int): T? = if (x < 0 || y < 0 || x >= w || y >= h) null else values[y][x]

   fun setValue(x: Int, y: Int, newValue: T): Matrix<T> =
         if (x < 0 || y < 0 || x >= w || y >= h) this
         else buildMatrix(w, h) { k -> if (k == ((w * y) + x)) newValue else values[k / w][k % w] }

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

   fun toButtons(_IdPrefix: String, _onclick: String, _classSelector: (T) -> String, valueSelector: (T) -> String): String {
      if (values.size == 0 || values[0].size == 0) {
         return ""
      } else {
         fun defButtonValues(idx: Int): String =
            buttonString(_classSelector(values[idx / w][idx % w]), _IdPrefix + "_${idx}", _onclick, valueSelector(values[idx/w][idx%w]))
         val indexMatrix: Matrix<Int> = buildMatrix(w, h) { x -> x }
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

   fun <B> map(f: (T) -> B): MutableMatrix<B> =
         buildMutableMatrix(w, h) { x -> f(values[x / w][x % w]) }

   fun toList():List<T>{
       fun appendLists(l:List<List<T>>):List<T> =
         if(l.size == 0) listOf() else l[0] + appendLists(l.drop(1))
       return appendLists(values)
   }

   fun getValue(x: Int, y: Int): T? = if (x < 0 || y < 0 || x >= w || y >= h) null else values[y][x]

   fun setValue(x: Int, y: Int, newValue: T) {
      if (!(x < 0 || y < 0 || x >= w || y >= h)) {
         values[y][x] = newValue
      }
   }

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

   fun toButtons(_IdPrefix: String, _onclick: String, _classSelector: (T) -> String, valueSelector: (T) -> String): String {
      if (values.size == 0 || values[0].size == 0) {
         return ""
      } else {
         fun defButtonValues(idx: Int): String =
            buttonString(_classSelector(values[idx / w][idx % w]), _IdPrefix + "_${idx}", _onclick, valueSelector(values[idx/w][idx%w]))
         val indexMatrix: Matrix<Int> = buildMatrix(w, h) { x -> x }
         val rows = indexMatrix.values.map() { row -> row.map() { b -> defButtonValues(b) }.fold("") { x, y -> "${x}${y}" }}
         val matrixString = rows.fold("") { x, y -> "${x}${y}<br>" }
         return matrixString
      }
   }
}

// Constrói uma matriz analisando os elementos de 0 a width*height-1 como se estivessem em linha
fun <T> buildMatrix(w: Int, h: Int, f: (Int) -> T): Matrix<T> {
   val valuesList = List(h) { y -> List(w) { x -> f((y * w) + x) } }
   return Matrix(valuesList)
}

// Constrói uma matriz mutável analisando os elementos de 0 a width*height-1 como se estivessem em
// linha
fun <T> buildMutableMatrix(w: Int, h: Int, f: (Int) -> T): MutableMatrix<T> {
   val valuesList = MutableList(h) { y -> MutableList(w) { x -> f((y * w) + x) } }
   return MutableMatrix(valuesList)
}

// Mergesort quebra uma lista em unidades e vai ordenado elas aumentando até o tamanho original
fun mergesort(l: List<Int>): List<Int> {
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

// Retorna uma lista com valores aleatórios no intervalo [from,to] inclusivo
fun randomList(from: Int, to: Int, size: Int, repeat: Boolean): List<Int> {
   if (size <= 0 || from > to) {
      return listOf()
   } else if (repeat) {
      return mergesort(List(size) { (from..to).random() })
   } else {
      val generator = List(to - from + 1) { x -> x + from }
      if (size > to - from) {
         return generator
      } else {
         fun subRandomList(l: List<Int>, s: Int): List<Int> =
               if (s > 0) {
                  val rand = l[(0..l.size - 1).random()]
                  listOf(rand) + subRandomList(l.filter() { x -> x != rand }, s - 1)
               } else listOf()
         return mergesort(subRandomList(generator, size))
      }
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

class Par<A, B>(val a: A, val b: B) {
   override fun toString() = "[${a},${b}]"
}

// Gera uma lista de pares dos elementos em todas as ordenações possíveis
// ex: listOf(1,2) -> listOf(Par(1,1),Par(1,2),Par(2,1),Par(2,2))
fun <T> combinatoricList(list: List<T>): List<Par<T, T>> {
   fun <T> pairValueToList(value: T, l: List<T>): List<Par<T, T>> = l.map() { x -> Par(value, x) }
   return list.map() { valor -> pairValueToList(valor, list) }.fold(listOf()) { sub1, sub2 -> sub1 + sub2 }
}

fun seconds2CounterString(t:Int):String{
    val min = t/60
    val sec = t%60
    val minString = if(min < 10) "0${min}" else "${min}"
    val secString = if(sec < 10) "0${sec}" else "${sec}"
    return minString + ":" + secString
}

class Board(val width: Int, val height: Int, val bombCells:List<Int>) {
   val bombCount = bombCells.size
   private var flagCount = 0
   private val originMatrix = buildMatrix(width, height) { x -> if (bombCells.any() { b -> b == x }) -1 else x }
   private val board = buildMatrix(width, height) { x -> adjacentBombCount(x % width, x / width) }
   private var visibleBoard = buildMutableMatrix(width, height) {"#"}

   fun getFlagCount() = flagCount

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

   // Faz com que todas as células cobertas em volta sejam reveladas, independente de ter ou não
   // bomba
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
               visibleBoard.setValue(new_x, new_y, "x")
            } // Se achar uma bomba o jogador perdeu
            else {
               clearNumbersAround(new_x, new_y)
            } // Se for um número, segue o jogo
         }
      }
   }

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

   // Flag é pra adicionar aquela bandeirinha vermelha
   // 0 clique
   // 1 scroll
   // 2 direito
   fun interact(idx: Int, inputType: Int) {
      val y = idx / width
      val x = idx % width
      val visibleValue = visibleBoard.getValue(x, y)
      val actualValue = board.getValue(x, y)
      if (visibleValue == null || actualValue == null) {
         println("Invalid input!")
      } else {
         if (inputType == 2) { // Marca uma flag na célula
            if(visibleValue == "#"){
               if(flagCount < bombCount){
                  visibleBoard.setValue(x, y, "F")
                  flagCount += 1
               }
            } else if(visibleValue == "F") {
               visibleBoard.setValue(x, y, "#")
               flagCount -= 1
            }
         } else if (inputType == 0) {
            if (visibleValue == "#") {
               if (actualValue == -1) {visibleBoard.setValue(x, y, "x")} else {clearNumbersAround(x, y)}
            }
         } else if (inputType == 1) {
            val cellValue = board.getValue(x, y)
            val visibleNumber = !(listOf("#", "x", "F").any() { value -> value == visibleValue })
            if (visibleNumber && cellValue != null && adjacentFlagCount(x, y) >= cellValue) {
               clearCellsAround(x, y)
            }
         }
      }
   }

   fun getVisibleBoard():String {
      val classSelector:(String) -> String = {x -> when(x){"#" -> "generic_cell"; "F" -> "flag_cell"; "x" -> "bomb_cell"; else -> "number_cell"}}
      val valueSelector:(String) -> String = {s -> if (listOf("#","F","x","0").any(){v -> v == s}) "&nbsp;" else "${s}"}
      return visibleBoard.toButtons("cell", "FrontEnd.clicaCelula", classSelector, valueSelector)
   }

   private fun adjacentFlagCount(x: Int, y: Int): Int {
      val visibleValue = visibleBoard.getValue(x, y)
      if (visibleValue == null) {
         return -1
      } else {
         val adjacentIndexes =
               combinatoricList(listOf(-1, 0, 1)).filter() { par -> (par.a != 0 || par.b != 0) }
         val adjacentValues =
               adjacentIndexes.map() { value -> visibleBoard.getValue(x + value.a, y + value.b) }
         return adjacentValues.filter() { value -> value == "F" }.size
      }
   }

   private fun adjacentBombCount(x: Int, y: Int): Int {
      val cellValue = originMatrix.getValue(x, y)
      if (cellValue == null || cellValue == -1) {
         return -1
      } else {
         val adjacentIndexes =
               combinatoricList(listOf(-1, 0, 1)).filter() { par -> (par.a != 0 || par.b != 0) }
         val adjacentValues =
               adjacentIndexes.map() { value -> originMatrix.getValue(x + value.a, y + value.b) }
         return adjacentValues.filter() { value -> value == -1 }.size
      }
   }

   fun perdeu():Boolean = visibleBoard.toList().any(){x -> x == "x"}

   fun ganhou():Boolean {
      val totalDescobertos = visibleBoard.toList().filter(){x -> !listOf("#", "x", "F").any() { v -> v == x }}.size
      return totalDescobertos == width*height - bombCount
   }
}

var largura = 15
var altura = 10
var probeBoard = buildMatrix(largura,altura){"&nbsp;"}
var board = Board(largura,altura,listOf())

var cronometro = document.getElementById("cronometro") as HTMLDivElement
var bandeira = document.getElementById("bandeira") as HTMLDivElement
var tabuleiro = document.getElementById("tabuleiro") as HTMLDivElement

var timer = 0
var seconds = 0
var bombas = 0
var dificuldade = 0

fun setCronometro(str:String){
   cronometro.innerHTML = "${str}"
}

fun setBandeira(remainingBombs:Int){
   val txt = (if(remainingBombs < 10) "00" else if(remainingBombs < 100) "0" else "") + "${remainingBombs}"
   bandeira.innerHTML = txt
}

@JsName("setDificuldade")
fun setDificuldade(dif:Int){
   dificuldade = dif
   largura = when(dificuldade){0->8; 1->13; else->21}
   altura = when(dificuldade){0->5; 1->8; else->13}
   bombas = when(dificuldade){0->5; 1->20; else->50}
   probeBoard = buildMatrix(largura,altura){"&nbsp;"}
   setBandeira(bombas)
}

@JsName("getDificuldade")
fun getDificuldade() = when(dificuldade){0->"Fácil"; 1->"Médio"; else->"Difícil"}

@JsName("criaProbe")
fun criaProbe(){
   setBandeira(bombas)
   setCronometro("00:00")
   tabuleiro.innerHTML = probeBoard.toButtons("probe", "FrontEnd.clicaProbe", {"generic_cell"}, {"&nbsp;"})
   window.clearInterval(timer);
}

@JsName("clicaProbe")
fun clicaProbe(event:MouseEvent,cellId:String){
   if(event.button.toInt() == 0){
      val idx = cellId.substring(6).toInt()
      val w = probeBoard.w
      val h = probeBoard.h
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

@JsName("clicaCelula")
fun clicaCelula(event:MouseEvent,cellId:String){
   if(!board.ganhou() && !board.perdeu()){
      val matrix_idx = cellId.substring(5).toInt()
      board.interact(matrix_idx,event.button.toInt())
      if(board.perdeu()){
         window.clearInterval(timer)
         board.revealAllBombs()
      } else if(board.ganhou()) {
         println("GANHAMO")
         var problematic = document.getElementById("problematic") as HTMLDivElement
         problematic.innerHTML = "<img width=\"0\" height=\"0\" src onerror=\"msg()\">"
         window.clearInterval(timer)
      }
      tabuleiro.innerHTML = board.getVisibleBoard()
   }
   setBandeira(board.bombCount - board.getFlagCount())
}

fun main() {
   setDificuldade(0)
   setBandeira(0)
   setCronometro("00:00")
}
