if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'FrontEnd'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'FrontEnd'.");
}var FrontEnd = function (_, Kotlin) {
  'use strict';
  var drop = Kotlin.kotlin.collections.drop_ba2ldo$;
  var plus = Kotlin.kotlin.collections.plus_mydzjv$;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var listOf = Kotlin.kotlin.collections.listOf_mh5how$;
  var listOf_0 = Kotlin.kotlin.collections.listOf_i5x0yv$;
  var dropLast = Kotlin.kotlin.collections.dropLast_yzln2o$;
  var IntRange = Kotlin.kotlin.ranges.IntRange;
  var equals = Kotlin.equals;
  var toString = Kotlin.toString;
  var Unit = Kotlin.kotlin.Unit;
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var throwCCE = Kotlin.throwCCE;
  var toInt = Kotlin.kotlin.text.toInt_pdl1vz$;
  var emptyList = Kotlin.kotlin.collections.emptyList_287e2$;
  var collectionSizeOrDefault = Kotlin.kotlin.collections.collectionSizeOrDefault_ba2ldo$;
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_ww73n8$;
  var Random = Kotlin.kotlin.random.Random;
  var random = Kotlin.kotlin.ranges.random_xmiyix$;
  var ArrayList_init_0 = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  var Collection = Kotlin.kotlin.collections.Collection;
  function buttonString(_class, _Id, _onclick, _value) {
    return '<button class=' + '"' + _class + '"' + ' id=' + '"' + _Id + '"' + ' onmousedown=' + '"' + _onclick + '(event,' + "'" + _Id + "'" + ')' + '"' + '>' + _value + '<\/button>' + '\n';
  }
  function Matrix(values) {
    this.values = values;
    this.w = this.values.get_za3lpa$(0).size;
    this.h = this.values.size;
  }
  function Matrix$map$lambda(closure$f, this$Matrix) {
    return function (x) {
      return closure$f(this$Matrix.values.get_za3lpa$(x / this$Matrix.w | 0).get_za3lpa$(x % this$Matrix.w));
    };
  }
  Matrix.prototype.map_2o04qz$ = function (f) {
    return buildMatrix(this.w, this.h, Matrix$map$lambda(f, this));
  };
  function Matrix$toList$appendLists(l) {
    return l.size === 0 ? emptyList() : plus(l.get_za3lpa$(0), Matrix$toList$appendLists(drop(l, 1)));
  }
  Matrix.prototype.toList = function () {
    var appendLists = Matrix$toList$appendLists;
    return appendLists(this.values);
  };
  Matrix.prototype.getValue_vux9f0$ = function (x, y) {
    return x < 0 || y < 0 || x >= this.w || y >= this.h ? null : this.values.get_za3lpa$(y).get_za3lpa$(x);
  };
  function Matrix$setValue$lambda(this$Matrix, closure$y, closure$x, closure$newValue) {
    return function (k) {
      return k === (Kotlin.imul(this$Matrix.w, closure$y) + closure$x | 0) ? closure$newValue : this$Matrix.values.get_za3lpa$(k / this$Matrix.w | 0).get_za3lpa$(k % this$Matrix.w);
    };
  }
  Matrix.prototype.setValue_vq7693$ = function (x, y, newValue) {
    return x < 0 || y < 0 || x >= this.w || y >= this.h ? this : buildMatrix(this.w, this.h, Matrix$setValue$lambda(this, y, x, newValue));
  };
  Matrix.prototype.toString = function () {
    if (this.values.size === 0 || this.values.get_za3lpa$(0).size === 0) {
      return '';
    } else {
      var $receiver = this.values;
      var destination = ArrayList_init(collectionSizeOrDefault($receiver, 10));
      var tmp$;
      tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var item = tmp$.next();
        var tmp$_0 = destination.add_11rb$;
        var tmp$_1;
        var accumulator = '';
        tmp$_1 = item.iterator();
        while (tmp$_1.hasNext()) {
          var element = tmp$_1.next();
          accumulator = accumulator + ' ' + element;
        }
        tmp$_0.call(destination, accumulator);
      }
      var destination_0 = ArrayList_init(collectionSizeOrDefault(destination, 10));
      var tmp$_2;
      tmp$_2 = destination.iterator();
      while (tmp$_2.hasNext()) {
        var item_0 = tmp$_2.next();
        destination_0.add_11rb$(item_0.substring(1));
      }
      var rows = destination_0;
      var tmp$_3;
      var accumulator_0 = '';
      tmp$_3 = rows.iterator();
      while (tmp$_3.hasNext()) {
        var element_0 = tmp$_3.next();
        accumulator_0 = accumulator_0 + '\n' + element_0;
      }
      var matrixString = accumulator_0;
      return matrixString.substring(1);
    }
  };
  function Matrix$toButtons$defButtonValues(closure$_classSelector, this$Matrix, closure$_IdPrefix, closure$_onclick, closure$valueSelector) {
    return function (idx) {
      return buttonString(closure$_classSelector(this$Matrix.values.get_za3lpa$(idx / this$Matrix.w | 0).get_za3lpa$(idx % this$Matrix.w)), closure$_IdPrefix + ('_' + idx), closure$_onclick, closure$valueSelector(this$Matrix.values.get_za3lpa$(idx / this$Matrix.w | 0).get_za3lpa$(idx % this$Matrix.w)));
    };
  }
  function Matrix$toButtons$lambda(x) {
    return x;
  }
  Matrix.prototype.toButtons_e8cgui$ = function (_IdPrefix, _onclick, _classSelector, valueSelector) {
    if (this.values.size === 0 || this.values.get_za3lpa$(0).size === 0) {
      return '';
    } else {
      var defButtonValues = Matrix$toButtons$defButtonValues(_classSelector, this, _IdPrefix, _onclick, valueSelector);
      var indexMatrix = buildMatrix(this.w, this.h, Matrix$toButtons$lambda);
      var $receiver = indexMatrix.values;
      var destination = ArrayList_init(collectionSizeOrDefault($receiver, 10));
      var tmp$;
      tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var item = tmp$.next();
        var tmp$_0 = destination.add_11rb$;
        var destination_0 = ArrayList_init(collectionSizeOrDefault(item, 10));
        var tmp$_1;
        tmp$_1 = item.iterator();
        while (tmp$_1.hasNext()) {
          var item_0 = tmp$_1.next();
          destination_0.add_11rb$(defButtonValues(item_0));
        }
        var tmp$_2;
        var accumulator = '';
        tmp$_2 = destination_0.iterator();
        while (tmp$_2.hasNext()) {
          var element = tmp$_2.next();
          accumulator = accumulator + element;
        }
        tmp$_0.call(destination, accumulator);
      }
      var rows = destination;
      var tmp$_3;
      var accumulator_0 = '';
      tmp$_3 = rows.iterator();
      while (tmp$_3.hasNext()) {
        var element_0 = tmp$_3.next();
        accumulator_0 = accumulator_0 + element_0 + '<br>';
      }
      var matrixString = accumulator_0;
      return matrixString;
    }
  };
  Matrix.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Matrix',
    interfaces: []
  };
  function MutableMatrix(values) {
    this.values = values;
    this.w = this.values.get_za3lpa$(0).size;
    this.h = this.values.size;
  }
  function MutableMatrix$map$lambda(closure$f, this$MutableMatrix) {
    return function (x) {
      return closure$f(this$MutableMatrix.values.get_za3lpa$(x / this$MutableMatrix.w | 0).get_za3lpa$(x % this$MutableMatrix.w));
    };
  }
  MutableMatrix.prototype.map_2o04qz$ = function (f) {
    return buildMutableMatrix(this.w, this.h, MutableMatrix$map$lambda(f, this));
  };
  function MutableMatrix$toList$appendLists(l) {
    return l.size === 0 ? emptyList() : plus(l.get_za3lpa$(0), MutableMatrix$toList$appendLists(drop(l, 1)));
  }
  MutableMatrix.prototype.toList = function () {
    var appendLists = MutableMatrix$toList$appendLists;
    return appendLists(this.values);
  };
  MutableMatrix.prototype.getValue_vux9f0$ = function (x, y) {
    return x < 0 || y < 0 || x >= this.w || y >= this.h ? null : this.values.get_za3lpa$(y).get_za3lpa$(x);
  };
  MutableMatrix.prototype.setValue_vq7693$ = function (x, y, newValue) {
    if (!(x < 0 || y < 0 || x >= this.w || y >= this.h)) {
      this.values.get_za3lpa$(y).set_wxm5ur$(x, newValue);
    }};
  MutableMatrix.prototype.toString = function () {
    if (this.values.size === 0 || this.values.get_za3lpa$(0).size === 0) {
      return '';
    } else {
      var $receiver = this.values;
      var destination = ArrayList_init(collectionSizeOrDefault($receiver, 10));
      var tmp$;
      tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var item = tmp$.next();
        var tmp$_0 = destination.add_11rb$;
        var tmp$_1;
        var accumulator = '';
        tmp$_1 = item.iterator();
        while (tmp$_1.hasNext()) {
          var element = tmp$_1.next();
          accumulator = accumulator + ' ' + element;
        }
        tmp$_0.call(destination, accumulator);
      }
      var destination_0 = ArrayList_init(collectionSizeOrDefault(destination, 10));
      var tmp$_2;
      tmp$_2 = destination.iterator();
      while (tmp$_2.hasNext()) {
        var item_0 = tmp$_2.next();
        destination_0.add_11rb$(item_0.substring(1));
      }
      var rows = destination_0;
      var tmp$_3;
      var accumulator_0 = '';
      tmp$_3 = rows.iterator();
      while (tmp$_3.hasNext()) {
        var element_0 = tmp$_3.next();
        accumulator_0 = accumulator_0 + '\n' + element_0;
      }
      var matrixString = accumulator_0;
      return matrixString.substring(1);
    }
  };
  function MutableMatrix$toButtons$defButtonValues(closure$_classSelector, this$MutableMatrix, closure$_IdPrefix, closure$_onclick, closure$valueSelector) {
    return function (idx) {
      return buttonString(closure$_classSelector(this$MutableMatrix.values.get_za3lpa$(idx / this$MutableMatrix.w | 0).get_za3lpa$(idx % this$MutableMatrix.w)), closure$_IdPrefix + ('_' + idx), closure$_onclick, closure$valueSelector(this$MutableMatrix.values.get_za3lpa$(idx / this$MutableMatrix.w | 0).get_za3lpa$(idx % this$MutableMatrix.w)));
    };
  }
  function MutableMatrix$toButtons$lambda(x) {
    return x;
  }
  MutableMatrix.prototype.toButtons_e8cgui$ = function (_IdPrefix, _onclick, _classSelector, valueSelector) {
    if (this.values.size === 0 || this.values.get_za3lpa$(0).size === 0) {
      return '';
    } else {
      var defButtonValues = MutableMatrix$toButtons$defButtonValues(_classSelector, this, _IdPrefix, _onclick, valueSelector);
      var indexMatrix = buildMatrix(this.w, this.h, MutableMatrix$toButtons$lambda);
      var $receiver = indexMatrix.values;
      var destination = ArrayList_init(collectionSizeOrDefault($receiver, 10));
      var tmp$;
      tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var item = tmp$.next();
        var tmp$_0 = destination.add_11rb$;
        var destination_0 = ArrayList_init(collectionSizeOrDefault(item, 10));
        var tmp$_1;
        tmp$_1 = item.iterator();
        while (tmp$_1.hasNext()) {
          var item_0 = tmp$_1.next();
          destination_0.add_11rb$(defButtonValues(item_0));
        }
        var tmp$_2;
        var accumulator = '';
        tmp$_2 = destination_0.iterator();
        while (tmp$_2.hasNext()) {
          var element = tmp$_2.next();
          accumulator = accumulator + element;
        }
        tmp$_0.call(destination, accumulator);
      }
      var rows = destination;
      var tmp$_3;
      var accumulator_0 = '';
      tmp$_3 = rows.iterator();
      while (tmp$_3.hasNext()) {
        var element_0 = tmp$_3.next();
        accumulator_0 = accumulator_0 + element_0 + '<br>';
      }
      var matrixString = accumulator_0;
      return matrixString;
    }
  };
  MutableMatrix.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'MutableMatrix',
    interfaces: []
  };
  function buildMatrix(w, h, f) {
    var list = ArrayList_init(h);
    for (var index = 0; index < h; index++) {
      var tmp$ = list.add_11rb$;
      var list_0 = ArrayList_init(w);
      for (var index_0 = 0; index_0 < w; index_0++) {
        list_0.add_11rb$(f(Kotlin.imul(index, w) + index_0 | 0));
      }
      tmp$.call(list, list_0);
    }
    var valuesList = list;
    return new Matrix(valuesList);
  }
  function buildMutableMatrix(w, h, f) {
    var list = ArrayList_init(h);
    for (var index = 0; index < h; index++) {
      var tmp$ = list.add_11rb$;
      var list_0 = ArrayList_init(w);
      for (var index_0 = 0; index_0 < w; index_0++) {
        list_0.add_11rb$(f(Kotlin.imul(index, w) + index_0 | 0));
      }
      tmp$.call(list, list_0);
    }
    var valuesList = list;
    return new MutableMatrix(valuesList);
  }
  function mergesort$merge(l1, l2) {
    return l1.size === 0 ? l2 : l2.size === 0 ? l1 : l1.get_za3lpa$(0) < l2.get_za3lpa$(0) ? plus(listOf(l1.get_za3lpa$(0)), mergesort$merge(drop(l1, 1), l2)) : plus(listOf(l2.get_za3lpa$(0)), mergesort$merge(l1, drop(l2, 1)));
  }
  function mergesort(l) {
    var merge = mergesort$merge;
    if (l.size <= 1) {
      return l;
    } else if (l.size === 2) {
      return l.get_za3lpa$(0) > l.get_za3lpa$(1) ? listOf_0([l.get_za3lpa$(1), l.get_za3lpa$(0)]) : listOf_0([l.get_za3lpa$(0), l.get_za3lpa$(1)]);
    } else {
      var half = l.size / 2 | 0;
      return merge(mergesort(dropLast(l, l.size - half | 0)), mergesort(drop(l, half)));
    }
  }
  function randomList$subRandomList(l, s) {
    if (s > 0) {
      var rand = l.get_za3lpa$(random(new IntRange(0, l.size - 1 | 0), Random.Default));
      var tmp$ = listOf(rand);
      var tmp$_0 = randomList$subRandomList;
      var destination = ArrayList_init_0();
      var tmp$_1;
      tmp$_1 = l.iterator();
      while (tmp$_1.hasNext()) {
        var element = tmp$_1.next();
        if (element !== rand)
          destination.add_11rb$(element);
      }
      return plus(tmp$, tmp$_0(destination, s - 1 | 0));
    } else {
      return emptyList();
    }
  }
  function randomList(from, to, size, repeat) {
    if (size <= 0 || from > to) {
      return emptyList();
    } else if (repeat) {
      var list = ArrayList_init(size);
      for (var index = 0; index < size; index++) {
        list.add_11rb$(random(new IntRange(from, to), Random.Default));
      }
      return mergesort(list);
    } else {
      var size_0 = to - from + 1 | 0;
      var list_0 = ArrayList_init(size_0);
      for (var index_0 = 0; index_0 < size_0; index_0++) {
        list_0.add_11rb$(index_0 + from | 0);
      }
      var generator = list_0;
      if (size > (to - from | 0)) {
        return generator;
      } else {
        var subRandomList = randomList$subRandomList;
        return mergesort(subRandomList(generator, size));
      }
    }
  }
  function randomSublist(l, size) {
    if (size <= 0) {
      return emptyList();
    } else if (size >= l.size)
      return l;
    else {
      var idx = random(new IntRange(0, l.size - 1 | 0), Random.Default);
      return plus(listOf(l.get_za3lpa$(idx)), randomSublist(plus(dropLast(l, l.size - idx | 0), drop(l, idx + 1 | 0)), size - 1 | 0));
    }
  }
  function Par(a, b) {
    this.a = a;
    this.b = b;
  }
  Par.prototype.toString = function () {
    return '[' + this.a + ',' + this.b + ']';
  };
  Par.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Par',
    interfaces: []
  };
  function combinatoricList$pairValueToList(value, l) {
    var destination = ArrayList_init(collectionSizeOrDefault(l, 10));
    var tmp$;
    tmp$ = l.iterator();
    while (tmp$.hasNext()) {
      var item = tmp$.next();
      destination.add_11rb$(new Par(value, item));
    }
    return destination;
  }
  function combinatoricList(list) {
    var pairValueToList = combinatoricList$pairValueToList;
    var destination = ArrayList_init(collectionSizeOrDefault(list, 10));
    var tmp$;
    tmp$ = list.iterator();
    while (tmp$.hasNext()) {
      var item = tmp$.next();
      destination.add_11rb$(pairValueToList(item, list));
    }
    var tmp$_0;
    var accumulator = emptyList();
    tmp$_0 = destination.iterator();
    while (tmp$_0.hasNext()) {
      var element = tmp$_0.next();
      accumulator = plus(accumulator, element);
    }
    return accumulator;
  }
  function seconds2CounterString(t) {
    var min = t / 60 | 0;
    var sec = t % 60;
    var minString = min < 10 ? '0' + min : min.toString();
    var secString = sec < 10 ? '0' + sec : sec.toString();
    return minString + ':' + secString;
  }
  function Board(width, height, bombCells) {
    this.width = width;
    this.height = height;
    this.bombCells = bombCells;
    this.bombCount = this.bombCells.size;
    this.flagCount_0 = 0;
    this.originMatrix_0 = buildMatrix(this.width, this.height, Board$originMatrix$lambda(this));
    this.board_0 = buildMatrix(this.width, this.height, Board$board$lambda(this));
    this.visibleBoard_0 = buildMutableMatrix(this.width, this.height, Board$visibleBoard$lambda);
  }
  Board.prototype.getFlagCount = function () {
    return this.flagCount_0;
  };
  Board.prototype.clearNumbersAround_0 = function (x, y) {
    var visibleValue = this.visibleBoard_0.getValue_vux9f0$(x, y);
    var actualValue = this.board_0.getValue_vux9f0$(x, y);
    if (actualValue == null || actualValue === -1 || !equals(visibleValue, '#')) {
      return;
    } else {
      this.visibleBoard_0.setValue_vq7693$(x, y, toString(actualValue));
      if (actualValue === 0) {
        var $receiver = combinatoricList(listOf_0([-1, 0, 1]));
        var destination = ArrayList_init_0();
        var tmp$;
        tmp$ = $receiver.iterator();
        while (tmp$.hasNext()) {
          var element = tmp$.next();
          if (element.a !== 0 || element.b !== 0)
            destination.add_11rb$(element);
        }
        var adjacentIndexes = destination;
        var tmp$_0;
        tmp$_0 = adjacentIndexes.iterator();
        while (tmp$_0.hasNext()) {
          var element_0 = tmp$_0.next();
          this.clearNumbersAround_0(x + element_0.a | 0, y + element_0.b | 0);
        }
      }}
  };
  Board.prototype.clearCellsAround_0 = function (x, y) {
    var $receiver = combinatoricList(listOf_0([-1, 0, 1]));
    var destination = ArrayList_init_0();
    var tmp$;
    tmp$ = $receiver.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      if (element.a !== 0 || element.b !== 0)
        destination.add_11rb$(element);
    }
    var adjacentIndexes = destination;
    var tmp$_0;
    tmp$_0 = adjacentIndexes.iterator();
    while (tmp$_0.hasNext()) {
      var element_0 = tmp$_0.next();
      var new_x = x + element_0.a | 0;
      var new_y = y + element_0.b | 0;
      var visibleValue = this.visibleBoard_0.getValue_vux9f0$(new_x, new_y);
      var actualValue = this.board_0.getValue_vux9f0$(new_x, new_y);
      if (actualValue != null && equals(visibleValue, '#')) {
        if (actualValue === -1) {
          this.visibleBoard_0.setValue_vq7693$(new_x, new_y, 'x');
        } else {
          this.clearNumbersAround_0(new_x, new_y);
        }
      }}
  };
  function Board$revealAllBombs$lambda(closure$actualValueList, closure$visibleValueList, this$Board) {
    return function (x) {
      if (closure$actualValueList.get_za3lpa$(x) === -1 && equals(closure$visibleValueList.get_za3lpa$(x), '#'))
        return 'x';
      else {
        var visibleValue = this$Board.visibleBoard_0.getValue_vux9f0$(x % this$Board.width, x / this$Board.width | 0);
        return visibleValue != null ? visibleValue : '#';
      }
    };
  }
  Board.prototype.revealAllBombs = function () {
    var actualValueList = this.board_0.toList();
    var visibleValueList = this.visibleBoard_0.toList();
    this.visibleBoard_0 = buildMutableMatrix(this.width, this.height, Board$revealAllBombs$lambda(actualValueList, visibleValueList, this));
  };
  Board.prototype.interact_vux9f0$ = function (idx, inputType) {
    var y = idx / this.width | 0;
    var x = idx % this.width;
    var visibleValue = this.visibleBoard_0.getValue_vux9f0$(x, y);
    var actualValue = this.board_0.getValue_vux9f0$(x, y);
    if (visibleValue == null || actualValue == null) {
      println('Invalid input!');
    } else {
      if (inputType === 2) {
        if (equals(visibleValue, '#')) {
          if (this.flagCount_0 < this.bombCount) {
            this.visibleBoard_0.setValue_vq7693$(x, y, 'F');
            this.flagCount_0 = this.flagCount_0 + 1 | 0;
          }} else if (equals(visibleValue, 'F')) {
          this.visibleBoard_0.setValue_vq7693$(x, y, '#');
          this.flagCount_0 = this.flagCount_0 - 1 | 0;
        }} else if (inputType === 0) {
        if (equals(visibleValue, '#')) {
          if (actualValue === -1) {
            this.visibleBoard_0.setValue_vq7693$(x, y, 'x');
          } else {
            this.clearNumbersAround_0(x, y);
          }
        }} else if (inputType === 1) {
        var cellValue = this.board_0.getValue_vux9f0$(x, y);
        var $receiver = listOf_0(['#', 'x', 'F']);
        var any$result;
        any$break: do {
          var tmp$;
          if (Kotlin.isType($receiver, Collection) && $receiver.isEmpty()) {
            any$result = false;
            break any$break;
          }tmp$ = $receiver.iterator();
          while (tmp$.hasNext()) {
            var element = tmp$.next();
            if (equals(element, visibleValue)) {
              any$result = true;
              break any$break;
            }}
          any$result = false;
        }
         while (false);
        var visibleNumber = !any$result;
        if (visibleNumber && cellValue != null && this.adjacentFlagCount_0(x, y) >= cellValue) {
          this.clearCellsAround_0(x, y);
        }}}
  };
  function Board$getVisibleBoard$lambda(x) {
    switch (x) {
      case '#':
        return 'generic_cell';
      case 'F':
        return 'flag_cell';
      case 'x':
        return 'bomb_cell';
      default:return 'number_cell';
    }
  }
  function Board$getVisibleBoard$lambda_0(s) {
    var $receiver = listOf_0(['#', 'F', 'x', '0']);
    var any$result;
    any$break: do {
      var tmp$;
      if (Kotlin.isType($receiver, Collection) && $receiver.isEmpty()) {
        any$result = false;
        break any$break;
      }tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var element = tmp$.next();
        if (equals(element, s)) {
          any$result = true;
          break any$break;
        }}
      any$result = false;
    }
     while (false);
    return any$result ? '&nbsp;' : s;
  }
  Board.prototype.getVisibleBoard = function () {
    var classSelector = Board$getVisibleBoard$lambda;
    var valueSelector = Board$getVisibleBoard$lambda_0;
    return this.visibleBoard_0.toButtons_e8cgui$('cell', 'FrontEnd.clicaCelula', classSelector, valueSelector);
  };
  Board.prototype.adjacentFlagCount_0 = function (x, y) {
    var visibleValue = this.visibleBoard_0.getValue_vux9f0$(x, y);
    if (visibleValue == null) {
      return -1;
    } else {
      var $receiver = combinatoricList(listOf_0([-1, 0, 1]));
      var destination = ArrayList_init_0();
      var tmp$;
      tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var element = tmp$.next();
        if (element.a !== 0 || element.b !== 0)
          destination.add_11rb$(element);
      }
      var adjacentIndexes = destination;
      var destination_0 = ArrayList_init(collectionSizeOrDefault(adjacentIndexes, 10));
      var tmp$_0;
      tmp$_0 = adjacentIndexes.iterator();
      while (tmp$_0.hasNext()) {
        var item = tmp$_0.next();
        destination_0.add_11rb$(this.visibleBoard_0.getValue_vux9f0$(x + item.a | 0, y + item.b | 0));
      }
      var adjacentValues = destination_0;
      var destination_1 = ArrayList_init_0();
      var tmp$_1;
      tmp$_1 = adjacentValues.iterator();
      while (tmp$_1.hasNext()) {
        var element_0 = tmp$_1.next();
        if (equals(element_0, 'F'))
          destination_1.add_11rb$(element_0);
      }
      return destination_1.size;
    }
  };
  Board.prototype.adjacentBombCount_0 = function (x, y) {
    var cellValue = this.originMatrix_0.getValue_vux9f0$(x, y);
    if (cellValue == null || cellValue === -1) {
      return -1;
    } else {
      var $receiver = combinatoricList(listOf_0([-1, 0, 1]));
      var destination = ArrayList_init_0();
      var tmp$;
      tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var element = tmp$.next();
        if (element.a !== 0 || element.b !== 0)
          destination.add_11rb$(element);
      }
      var adjacentIndexes = destination;
      var destination_0 = ArrayList_init(collectionSizeOrDefault(adjacentIndexes, 10));
      var tmp$_0;
      tmp$_0 = adjacentIndexes.iterator();
      while (tmp$_0.hasNext()) {
        var item = tmp$_0.next();
        destination_0.add_11rb$(this.originMatrix_0.getValue_vux9f0$(x + item.a | 0, y + item.b | 0));
      }
      var adjacentValues = destination_0;
      var destination_1 = ArrayList_init_0();
      var tmp$_1;
      tmp$_1 = adjacentValues.iterator();
      while (tmp$_1.hasNext()) {
        var element_0 = tmp$_1.next();
        if (element_0 === -1)
          destination_1.add_11rb$(element_0);
      }
      return destination_1.size;
    }
  };
  Board.prototype.perdeu = function () {
    var $receiver = this.visibleBoard_0.toList();
    var any$result;
    any$break: do {
      var tmp$;
      if (Kotlin.isType($receiver, Collection) && $receiver.isEmpty()) {
        any$result = false;
        break any$break;
      }tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var element = tmp$.next();
        if (equals(element, 'x')) {
          any$result = true;
          break any$break;
        }}
      any$result = false;
    }
     while (false);
    return any$result;
  };
  Board.prototype.ganhou = function () {
    var $receiver = this.visibleBoard_0.toList();
    var destination = ArrayList_init_0();
    var tmp$;
    tmp$ = $receiver.iterator();
    loop_label: while (tmp$.hasNext()) {
      var element = tmp$.next();
      var $receiver_0 = listOf_0(['#', 'x', 'F']);
      var any$result;
      any$break: do {
        var tmp$_0;
        if (Kotlin.isType($receiver_0, Collection) && $receiver_0.isEmpty()) {
          any$result = false;
          break any$break;
        }tmp$_0 = $receiver_0.iterator();
        while (tmp$_0.hasNext()) {
          var element_0 = tmp$_0.next();
          if (equals(element_0, element)) {
            any$result = true;
            break any$break;
          }}
        any$result = false;
      }
       while (false);
      if (!any$result)
        destination.add_11rb$(element);
    }
    var totalDescobertos = destination.size;
    return totalDescobertos === (Kotlin.imul(this.width, this.height) - this.bombCount | 0);
  };
  function Board$originMatrix$lambda(this$Board) {
    return function (x) {
      var $receiver = this$Board.bombCells;
      var any$result;
      any$break: do {
        var tmp$;
        if (Kotlin.isType($receiver, Collection) && $receiver.isEmpty()) {
          any$result = false;
          break any$break;
        }tmp$ = $receiver.iterator();
        while (tmp$.hasNext()) {
          var element = tmp$.next();
          if (element === x) {
            any$result = true;
            break any$break;
          }}
        any$result = false;
      }
       while (false);
      return any$result ? -1 : x;
    };
  }
  function Board$board$lambda(this$Board) {
    return function (x) {
      return this$Board.adjacentBombCount_0(x % this$Board.width, x / this$Board.width | 0);
    };
  }
  function Board$visibleBoard$lambda(it) {
    return '#';
  }
  Board.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Board',
    interfaces: []
  };
  var largura;
  var altura;
  function probeBoard$lambda(it) {
    return '&nbsp;';
  }
  var probeBoard;
  var board;
  var cronometro;
  var bandeira;
  var tabuleiro;
  var timer;
  var seconds;
  var bombas;
  var dificuldade;
  function setCronometro(str) {
    cronometro.innerHTML = str;
  }
  function setBandeira(remainingBombs) {
    var txt = (remainingBombs < 10 ? '00' : remainingBombs < 100 ? '0' : '') + remainingBombs.toString();
    bandeira.innerHTML = txt;
  }
  function setDificuldade$lambda(it) {
    return '&nbsp;';
  }
  function setDificuldade(dif) {
    var tmp$, tmp$_0, tmp$_1;
    dificuldade = dif;
    switch (dificuldade) {
      case 0:
        tmp$ = 8;
        break;
      case 1:
        tmp$ = 13;
        break;
      default:tmp$ = 21;
        break;
    }
    largura = tmp$;
    switch (dificuldade) {
      case 0:
        tmp$_0 = 5;
        break;
      case 1:
        tmp$_0 = 8;
        break;
      default:tmp$_0 = 13;
        break;
    }
    altura = tmp$_0;
    switch (dificuldade) {
      case 0:
        tmp$_1 = 5;
        break;
      case 1:
        tmp$_1 = 20;
        break;
      default:tmp$_1 = 50;
        break;
    }
    bombas = tmp$_1;
    probeBoard = buildMatrix(largura, altura, setDificuldade$lambda);
    setBandeira(bombas);
  }
  function getDificuldade() {
    switch (dificuldade) {
      case 0:
        return 'F\xE1cil';
      case 1:
        return 'M\xE9dio';
      default:return 'Dif\xEDcil';
    }
  }
  function criaProbe$lambda(it) {
    return 'generic_cell';
  }
  function criaProbe$lambda_0(it) {
    return '&nbsp;';
  }
  function criaProbe() {
    setBandeira(bombas);
    setCronometro('00:00');
    tabuleiro.innerHTML = probeBoard.toButtons_e8cgui$('probe', 'FrontEnd.clicaProbe', criaProbe$lambda, criaProbe$lambda_0);
    window.clearInterval(timer);
  }
  function clicaProbe$lambda() {
    setCronometro(seconds2CounterString((seconds = seconds + 1 | 0, seconds)));
    return Unit;
  }
  function clicaProbe(event, cellId) {
    var tmp$, tmp$_0;
    if (event.button === 0) {
      var idx = toInt(cellId.substring(6));
      var w = probeBoard.w;
      var h = probeBoard.h;
      tmp$_0 = listOf(idx);
      if (idx === 0)
        tmp$ = listOf_0([idx + 1 | 0, idx + w | 0, idx + w + 1 | 0]);
      else if (idx === (w - 1 | 0))
        tmp$ = listOf_0([idx - 1 | 0, idx + w - 1 | 0, idx + w | 0]);
      else if (idx < (w - 1 | 0))
        tmp$ = listOf_0([idx - 1 | 0, idx + 1 | 0, idx + w - 1 | 0, idx + w | 0, idx + w + 1 | 0]);
      else if (idx === Kotlin.imul(w, h - 1 | 0))
        tmp$ = listOf_0([idx + 1 | 0, idx - w | 0, idx - w + 1 | 0]);
      else if (idx === (Kotlin.imul(w, h) - 1 | 0))
        tmp$ = listOf_0([idx - 1 | 0, idx - w | 0, idx - w - 1 | 0]);
      else if (idx > Kotlin.imul(w, h - 1 | 0))
        tmp$ = listOf_0([idx - 1 | 0, idx + 1 | 0, idx - w | 0, idx - w - 1 | 0, idx - w + 1 | 0]);
      else if (idx % w === 0)
        tmp$ = listOf_0([idx + 1 | 0, idx - w | 0, idx - w + 1 | 0, idx + w | 0, idx + w + 1 | 0]);
      else if (idx % w === (w - 1 | 0))
        tmp$ = listOf_0([idx - 1 | 0, idx - w | 0, idx - w - 1 | 0, idx + w | 0, idx + w - 1 | 0]);
      else
        tmp$ = listOf_0([idx - w - 1 | 0, idx - w | 0, idx - w + 1 | 0, idx - 1 | 0, idx + 1 | 0, idx + w - 1 | 0, idx + w | 0, idx + w + 1 | 0]);
      var spareCells = {v: plus(tmp$_0, tmp$)};
      var size = Kotlin.imul(w, h);
      var list = ArrayList_init(size);
      for (var index = 0; index < size; index++) {
        list.add_11rb$(index);
      }
      var destination = ArrayList_init_0();
      var tmp$_1;
      tmp$_1 = list.iterator();
      loop_label: while (tmp$_1.hasNext()) {
        var element = tmp$_1.next();
        var $receiver = spareCells.v;
        var any$result;
        any$break: do {
          var tmp$_2;
          if (Kotlin.isType($receiver, Collection) && $receiver.isEmpty()) {
            any$result = false;
            break any$break;
          }tmp$_2 = $receiver.iterator();
          while (tmp$_2.hasNext()) {
            var element_0 = tmp$_2.next();
            if (element_0 === element) {
              any$result = true;
              break any$break;
            }}
          any$result = false;
        }
         while (false);
        if (!any$result)
          destination.add_11rb$(element);
      }
      var possibleBombCells = destination;
      board = new Board(w, h, randomSublist(possibleBombCells, bombas));
      tabuleiro.innerHTML = board.getVisibleBoard();
      clicaCelula(event, 'cell_' + idx);
      seconds = 0;
      timer = window.setInterval(clicaProbe$lambda, 1000);
    }}
  function clicaCelula(event, cellId) {
    var tmp$;
    if (!board.ganhou() && !board.perdeu()) {
      var matrix_idx = toInt(cellId.substring(5));
      board.interact_vux9f0$(matrix_idx, event.button);
      if (board.perdeu()) {
        window.clearInterval(timer);
        board.revealAllBombs();
      } else if (board.ganhou()) {
        println('GANHAMO');
        var problematic = Kotlin.isType(tmp$ = document.getElementById('problematic'), HTMLDivElement) ? tmp$ : throwCCE();
        problematic.innerHTML = '<img width="0" height="0" src onerror="msg()">';
        window.clearInterval(timer);
      }tabuleiro.innerHTML = board.getVisibleBoard();
    }setBandeira(board.bombCount - board.getFlagCount() | 0);
  }
  function main() {
    setDificuldade(0);
    setBandeira(0);
    setCronometro('00:00');
  }
  _.buttonString_w74nik$ = buttonString;
  _.Matrix = Matrix;
  _.MutableMatrix = MutableMatrix;
  _.buildMatrix_9p1r4w$ = buildMatrix;
  _.buildMutableMatrix_9p1r4w$ = buildMutableMatrix;
  _.mergesort_pqoyrt$ = mergesort;
  _.randomList_x84gdh$ = randomList;
  _.randomSublist_rs6med$ = randomSublist;
  _.Par = Par;
  _.combinatoricList_bemo1h$ = combinatoricList;
  _.seconds2CounterString_za3lpa$ = seconds2CounterString;
  _.Board = Board;
  Object.defineProperty(_, 'largura', {
    get: function () {
      return largura;
    },
    set: function (value) {
      largura = value;
    }
  });
  Object.defineProperty(_, 'altura', {
    get: function () {
      return altura;
    },
    set: function (value) {
      altura = value;
    }
  });
  Object.defineProperty(_, 'probeBoard', {
    get: function () {
      return probeBoard;
    },
    set: function (value) {
      probeBoard = value;
    }
  });
  Object.defineProperty(_, 'board', {
    get: function () {
      return board;
    },
    set: function (value) {
      board = value;
    }
  });
  Object.defineProperty(_, 'cronometro', {
    get: function () {
      return cronometro;
    },
    set: function (value) {
      cronometro = value;
    }
  });
  Object.defineProperty(_, 'bandeira', {
    get: function () {
      return bandeira;
    },
    set: function (value) {
      bandeira = value;
    }
  });
  Object.defineProperty(_, 'tabuleiro', {
    get: function () {
      return tabuleiro;
    },
    set: function (value) {
      tabuleiro = value;
    }
  });
  Object.defineProperty(_, 'timer', {
    get: function () {
      return timer;
    },
    set: function (value) {
      timer = value;
    }
  });
  Object.defineProperty(_, 'seconds', {
    get: function () {
      return seconds;
    },
    set: function (value) {
      seconds = value;
    }
  });
  Object.defineProperty(_, 'bombas', {
    get: function () {
      return bombas;
    },
    set: function (value) {
      bombas = value;
    }
  });
  Object.defineProperty(_, 'dificuldade', {
    get: function () {
      return dificuldade;
    },
    set: function (value) {
      dificuldade = value;
    }
  });
  _.setCronometro_61zpoe$ = setCronometro;
  _.setBandeira_za3lpa$ = setBandeira;
  _.setDificuldade = setDificuldade;
  _.getDificuldade = getDificuldade;
  _.criaProbe = criaProbe;
  _.clicaProbe = clicaProbe;
  _.clicaCelula = clicaCelula;
  _.main = main;
  largura = 15;
  altura = 10;
  probeBoard = buildMatrix(largura, altura, probeBoard$lambda);
  board = new Board(largura, altura, emptyList());
  var tmp$, tmp$_0, tmp$_1;
  cronometro = Kotlin.isType(tmp$ = document.getElementById('cronometro'), HTMLDivElement) ? tmp$ : throwCCE();
  bandeira = Kotlin.isType(tmp$_0 = document.getElementById('bandeira'), HTMLDivElement) ? tmp$_0 : throwCCE();
  tabuleiro = Kotlin.isType(tmp$_1 = document.getElementById('tabuleiro'), HTMLDivElement) ? tmp$_1 : throwCCE();
  timer = 0;
  seconds = 0;
  bombas = 0;
  dificuldade = 0;
  main();
  Kotlin.defineModule('FrontEnd', _);
  return _;
}(typeof FrontEnd === 'undefined' ? {} : FrontEnd, kotlin);
