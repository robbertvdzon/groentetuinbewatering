<template id="play">
  <app-frame>

    <div>


      <div class="headerBorder">
        <table width=100%>
          <tr height="50px">
            <td>
              &nbsp;
              Mode:{{ userdata.role }} , Aan zet: {{ board.player }}
              <span v-if="board.mate" class="schaakmat">
                <br>
                Spel afgelopen: SCHAAKMAT!
              </span>
              <span v-if="board.draw">
                <br>
                Spel afgelopen: DRAW!
              </span>
              <span v-if="board.kingAttached && !board.mate">
                : SCHAAK!
              </span>
            </td>
          </tr>
        </table>
      </div>
      <table width="100%">
        <tr>
          <td></td>
          <td width="90%">
            <table>
              <tbody>
              <tr v-for="row in rows">
                <td><span class="label" >{{row.id}}</span></td>
                <td v-for="col in cols" v-on:click="move(col.id+row.id)" v-bind:class="checkSquareClass(row, col)">
                  <div v-bind:class="getPieceImage(row, col)" v-bind:id="col.id+row.id+'img'">
                    <img src="/icons/png/stukken/empty.png">
                  </div>
                </td>
              </tr>
              <tr>
                <td></td>
                <td align="center"><span class="label" >A</span></td>
                <td align="center"><span class="label" >B</span></td>
                <td align="center"><span class="label" >C</span></td>
                <td align="center"><span class="label" >D</span></td>
                <td align="center"><span class="label" >E</span></td>
                <td align="center"><span class="label" >F</span></td>
                <td align="center"><span class="label" >G</span></td>
                <td align="center"><span class="label" >H</span></td>
              </tr>

              </tbody>
            </table>

          </td>
          <td></td>
        </tr>
      </table>

      <div class="headerBorder">
        <table width=100%>
          <tr height="50px">
            <td>
              &nbsp;
              <span v-if="userdata.role=='SPECTATOR'">
                <button type="submit" v-on:click="login" class="button">Login</button>
              </span>
        <span v-if="userdata.role!='SPECTATOR'">
                <button v-on:click="toBase" class="button">Opnieuw spelen</button>
                <button v-on:click="logout" class="button">Logout</button>
              </span>
            </td>
          </tr>
        </table>

      </div>


      <br>

      <!--
            <iframe width="640" height="480" src="https://rtsp.me/embed/8HkH3QT8/" frameborder="0" allowfullscreen></iframe>
      -->
      <span v-if="userdata.role=='ADMIN'">
            <hr>
              <button type="submit" v-on:click="reset">Reset board</button>
              <button type="submit" v-on:click="sleep">Sleep</button>
              <button type="submit" v-on:click="home">Home</button>
              <button type="submit" v-on:click="computerSingleMove">Computer zet</button>
              <button type="submit" v-on:click="demo">demo</button>
              <button type="submit" v-on:click="startautoplay">Start Autoplay</button>
              <button type="submit" v-on:click="stopautoplay">Stop Autoplay</button>
              <button type="submit" v-on:click="manual">manual</button>
       </span>

      <br>
    </div>
  </app-frame>
</template>

<style>
.blacksquare {
  background-image: url("/icons/png/vlakken/zwart.png");
  object-fit: contain;
  background-size: contain;
}

.whitesquare {
  background-image: url("/icons/png/vlakken/wit.png");
  object-fit: contain;
  background-size: contain;
}

.blacksquare_dot {
  background-image: url("/icons/png/vlakken/zwart-punt.png");
  object-fit: contain;
  background-size: contain;
}

.whitesquare_dot {
  background-image: url("/icons/png/vlakken/wit-punt.png");
  object-fit: contain;
  background-size: contain;
}

.blacksquare_circle {
  background-image: url("/icons/png/vlakken/zwart-circel.png");
  object-fit: contain;
  background-size: contain;
}

.whitesquare_circle {
  background-image: url("/icons/png/vlakken/wit-circel.png");
  object-fit: contain;
  background-size: contain;
}


.not_selected_pion_wit {
  background-image: url("/icons/png/stukken/not_selected/pion_wit.png");
  object-fit: contain;
  background-size: contain;
}

.not_selected_toren_wit {
  background-image: url("/icons/png/stukken/not_selected/toren_wit.png");
  object-fit: contain;
  background-size: contain;
}

.not_selected_paard_wit {
  background-image: url("/icons/png/stukken/not_selected/paard_wit.png");
  object-fit: contain;
  background-size: contain;
}

.not_selected_loper_wit {
  background-image: url("/icons/png/stukken/not_selected/loper_wit.png");

  object-fit: contain;
  background-size: contain;
}

.not_selected_dame_wit {
  background-image: url("/icons/png/stukken/not_selected/dame_wit.png");

  object-fit: contain;
  background-size: contain;
}

.not_selected_koning_wit {
  background-image: url("/icons/png/stukken/not_selected/koning_wit.png");

  object-fit: contain;
  background-size: contain;
}

.not_selected_pion_zwart {
  background-image: url("/icons/png/stukken/not_selected/pion_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.not_selected_toren_zwart {
  background-image: url("/icons/png/stukken/not_selected/toren_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.not_selected_paard_zwart {
  background-image: url("/icons/png/stukken/not_selected/paard_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.not_selected_loper_zwart {
  background-image: url("/icons/png/stukken/not_selected/loper_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.not_selected_dame_zwart {
  background-image: url("/icons/png/stukken/not_selected/dame_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.not_selected_koning_zwart {
  background-image: url("/icons/png/stukken/not_selected/koning_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.selected_pion_wit {
  background-image: url("/icons/png/stukken/selected/pion_wit.png");
  object-fit: contain;
  background-size: contain;
}

.selected_toren_wit {
  background-image: url("/icons/png/stukken/selected/toren_wit.png");
  object-fit: contain;
  background-size: contain;
}

.selected_paard_wit {
  background-image: url("/icons/png/stukken/selected/paard_wit.png");
  object-fit: contain;
  background-size: contain;
}

.selected_loper_wit {
  background-image: url("/icons/png/stukken/selected/loper_wit.png");

  object-fit: contain;
  background-size: contain;
}

.selected_dame_wit {
  background-image: url("/icons/png/stukken/selected/dame_wit.png");

  object-fit: contain;
  background-size: contain;
}

.selected_koning_wit {
  background-image: url("/icons/png/stukken/selected/koning_wit.png");

  object-fit: contain;
  background-size: contain;
}

.selected_pion_zwart {
  background-image: url("/icons/png/stukken/selected/pion_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.selected_toren_zwart {
  background-image: url("/icons/png/stukken/selected/toren_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.selected_paard_zwart {
  background-image: url("/icons/png/stukken/selected/paard_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.selected_loper_zwart {
  background-image: url("/icons/png/stukken/selected/loper_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.selected_dame_zwart {
  background-image: url("/icons/png/stukken/selected/dame_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.selected_koning_zwart {
  background-image: url("/icons/png/stukken/selected/koning_zwart.png");

  object-fit: contain;
  background-size: contain;
}

.empty {
  background-image: url("/icons/png/stukken/empty.png");
  object-fit: contain;
  background-size: contain;
}


.button {
  border: none;
  background-color: white; /* Green */
  color: black;
  padding: 5px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
}

</style>

<script>

Vue.component("play", {
  template: "#play",
  data: () => ({
    status: null,
    userdata: {
      username: "",
      role: ""
    },
    van: "",
    naar: "",
    possible_tos: [],
    testfield: {"s": "dd"},
    boardrow: [
      {boardcol: ["?", "?", "?", "?", "?", "?", "?", "?"]},
      {boardcol: ["?", "?", "?", "?", "?", "?", "?", "?"]},
      {boardcol: ["?", "?", "?", "?", "?", "?", "?", "?"]},
      {boardcol: ["?", "?", "?", "?", "?", "?", "?", "?"]},
      {boardcol: ["?", "?", "?", "?", "?", "?", "?", "?"]},
      {boardcol: ["?", "?", "?", "?", "?", "?", "?", "?"]},
      {boardcol: ["?", "?", "?", "?", "?", "?", "?", "p"]},
      {boardcol: ["?", "?", "?", "?", "?", "?", "?", "t"]},
    ],
    board: {
      player: "",
      availableMoves: []
    },
    rows: [
      {id: 8},
      {id: 7},
      {id: 6},
      {id: 5},
      {id: 4},
      {id: 3},
      {id: 2},
      {id: 1},
    ],

    cols: [
      {id: 'A', index: 0},
      {id: 'B', index: 1},
      {id: 'C', index: 2},
      {id: 'D', index: 3},
      {id: 'E', index: 4},
      {id: 'F', index: 5},
      {id: 'G', index: 6},
      {id: 'H', index: 7},
    ]
  }),
  computed: {
    "columns": function columns() {
      if (this.rows.length == 0) {
        return [];
      }
      return Object.keys(this.rows[0])
    }
  },

  created() {
    this.load()
  },
  methods: {
    load: function (event) {
      fetch(`/api/game/load`)
          .then(res => res.text())
          .then(text => this.updateBoard(JSON.parse(text)))
          .catch(err => alert(err));
      fetch(`/api/userdata`)
          .then(res => res.text())
          .then(text => this.userdata = JSON.parse(text))
          .catch(() => alert("Error"));
    },
    reset: function (event) {
      fetch(`/api/game/reset`)
          .then(res => res.text())
          .then(text => this.updateBoard(JSON.parse(text)))
          .catch(() => alert("Error"));
    },
    sleep: function (event) {
      fetch(`/api/game/sleep`)
          .then(res => res.text())
          .catch(() => alert("Error"));
    },
    home: function (event) {
      fetch(`/api/game/home`)
          .then(res => res.text())
          .catch(() => alert("Error"));
    },
    moveAway: function (event) {
      axios.post(`/api/move`, "100,100")
    },
    toBase: function (event) {
      fetch(`/api/game/restoreboard`)
          .then(res => res.text())
          .catch(() => alert("Error"));
    },
    computerMove: function (event) {
      fetch(`/api/game/computermove`)
          .then(res => res.text())
          .then(text => this.updateBoard(JSON.parse(text)))
          .then(text => this.home())
          .catch(() => alert("Error"));
    },
    computerSingleMove: function (event) {
      fetch(`/api/game/computermove`)
          .then(res => res.text())
          .then(text => this.updateBoard(JSON.parse(text)))
          .catch(() => alert("Error"));
    },
    startautoplay: function (event) {
      fetch(`/api/game/startautoplay`)
          .then(res => res.text())
          .catch(() => alert("Error"));
    },
    stopautoplay: function (event) {
      fetch(`/api/game/stopautoplay`)
          .then(res => res.text())
          .catch(() => alert("Error"));
    },
    move: function (vlak) {
      if (this.userdata.role == 'SPECTATOR') return


      // if this is the second selection
      if (this.van != "") {
        if (this.possible_tos.includes(vlak)) {
          // ok!
        } else {
          // not ok! pretent like this is the first move
          this.van = ""
        }
      }


      // if first selected
      if (this.van == "") {
        this.possible_tos = []
        this.board.availableMoves.forEach(
            element => {
              if (vlak == element.from) {
                this.possible_tos.push(element.to)
              }
            }
        );

        if (this.possible_tos.length === 0) {
          // not valid
          this.van = ""
          return
        }
      }


      if (this.van == "") {
        this.van = vlak
      } else {
        if (this.naar == "") {
          this.naar = vlak
          fetch(`/api/game/ownmove/` + this.van + '/' + this.naar)
              .then(res => res.text())
              .then(text => this.updateBoard(JSON.parse(text)))
              .then(text => this.computerMove())
              .catch(() => alert("Error"));
          this.naar = ""
          this.van = ""
          this.possible_tos = []
        } else {
          this.van = vlak
          this.naar = ""
        }
      }
    },
    logout: function (event) {
      fetch(`/api/logout`)
          .catch(() => alert("Error"))
          .then(text => window.location.href = "/play");
    },
    login: function (event) {
      window.location.href = "/login";
    },
    demo: function (event) {
      window.location.href = "/demo";
    },
    manual: function (event) {
      window.location.href = "/manual";
    },
    again: function () {
      this.naar = ""
      this.van = ""
    },
    updateBoard: function (newBoard) {
      console.info(newBoard)
      this.board = newBoard


      console.log(this.board);
      rows2 = this.rows
      // console.log("t2");
      squares = this.board.squares
      // console.log("t3");
      boardrow = this.boardrow
      // console.log("t4");

      this.cols.forEach(function (col, index) {
        // console.log("t5");
        rows2.forEach(function (row, index2) {
          sq = col.id + "" + row.id
          // console.log("t6");
          squares.forEach(function (item, index3) {
            if (item.field === sq) {
              boardrow[7 - index2].boardcol[index] = item.piece
            }
          });
        });
      });


      this.testfield = "jj"

    },
    checkSquareClass: function (row, col) {
      var vlak = col.id + row.id
      var postfix = ""
      if (this.possible_tos.includes(vlak)) {
        if (this.boardrow[row.id - 1].boardcol[col.index] == ' ')
          postfix = "_dot"
        else
          postfix = "_circle"
      }

      if ((row.id + col.index) % 2 === 1) return "blacksquare" + postfix;
      if ((row.id + col.index) % 2 === 0) return "whitesquare" + postfix;

    },
    getPieceImage: function (row, col) {
      var vlak = col.id + row.id
      var prefix = "not_"
      if (this.selectedVlak(vlak)) {
        prefix = ""
      }
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'p') return prefix + "selected_pion_wit";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'r') return prefix + "selected_toren_wit";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'n') return prefix + "selected_paard_wit";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'b') return prefix + "selected_loper_wit";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'q') return prefix + "selected_dame_wit";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'k') return prefix + "selected_koning_wit";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'P') return prefix + "selected_pion_zwart";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'R') return prefix + "selected_toren_zwart";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'N') return prefix + "selected_paard_zwart";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'B') return prefix + "selected_loper_zwart";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'Q') return prefix + "selected_dame_zwart";
      if (this.boardrow[row.id - 1].boardcol[col.index] == 'K') return prefix + "selected_koning_zwart";
      if (this.boardrow[row.id - 1].boardcol[col.index] == ' ') return "empty";
    },
    selectedVlak: function (vlak) {
      return vlak == this.van;
    }


  }
});

$(document).ready(function () {
  $("#playPrev").click(function () {
    window.location.href = "/manual";
  });
  $("#playNext").click(function () {
    window.location.href = "/demo";
  });
});

</script>
<style scoped>
.headerBorder {
  color: white;
  background-color: #663d00;
}
.schaakmat {
  font-size: 38px;
}
.label {
  color: white;
}

</style>
