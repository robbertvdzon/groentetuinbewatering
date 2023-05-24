<template id="manual">
  <app-frame>

    <div>

      <nav>
            <button type="submit" v-on:click="close">Terug</button>
      </nav>

      <br>

      <span>
              <button type="submit" v-on:click="home_vert">home vertical</button>
              <button type="submit" v-on:click="home_hor">home horizontal</button>
              <button type="submit" v-on:click="sleeping">Sleep</button>
              <button type="submit" v-on:click="bootsound">Bootsound</button>
       </span>
      <br>
      <span>
              <button type="submit" v-on:click="clamp1">Pak1</button>
              <button type="submit" v-on:click="release1">Laat los1</button>
              <button type="submit" v-on:click="clamp2">Pak2</button>
              <button type="submit" v-on:click="release2">Laat los2</button>
       </span>
      <br>
      <br>
      <table>
        <tr>
          <td class="label">
            Naar vlak:
          </td>
          <td>
            <input v-model="vlak">
          </td>
          <td>
            <button type="submit" v-on:click="movevlak1">naar vlak arm1</button>
            <button type="submit" v-on:click="movevlak2">naar vlak arm2</button>
          </td>
        </tr>

        <tr>
          <td class="label">
            Manual pos:
          </td>
          <td>
            <input v-model="pos">
          </td>
          <td>
            <button type="submit" v-on:click="move">move</button>
          </td>
        </tr>

        <tr>
          <td class="label">
            A8:
          </td>
          <td>
            <input v-model="a8pos">
          </td>
          <td>
            <button type="submit" v-on:click="saveA8">Save</button>
          </td>
        </tr>

        <tr>
          <td class="label">
            H1:
          </td>
          <td>
            <input v-model="h1pos">
          </td>
          <td>
            <button type="submit" v-on:click="saveH1">Save</button>
          </td>
        </tr>

        <tr>
          <td class="label">
            A11:
          </td>
          <td>
            <input v-model="a11pos">
          </td>
          <td>
            <button type="submit" v-on:click="saveA11">Save</button>
          </td>
        </tr>

        <tr>
          <td class="label">
            H10:
          </td>
          <td>
            <input v-model="h10pos">
          </td>
          <td>
            <button type="submit" v-on:click="saveH10">Save</button>
          </td>
        </tr>

        <tr>
          <td class="label">
            A21:
          </td>
          <td>
            <input v-model="a21pos">
          </td>
          <td>
            <button type="submit" v-on:click="saveA21">Save</button>
          </td>
        </tr>

        <tr>
          <td class="label">
            H20:
          </td>
          <td>
            <input v-model="h20pos">
          </td>
          <td>
            <button type="submit" v-on:click="saveH20">Save</button>
          </td>
        </tr>

        <tr>
          <td class="label">
            Pakker hoogte:
          </td>
          <td>
            <input v-model="pakkerhoogte">
          </td>
          <td>
            <button type="submit" v-on:click="savePakkerhoogte">Save</button>
          </td>
        </tr>

        <tr>
          <td class="label">
            Snelheid:
          </td>
          <td>
            <input v-model="snelheid">
          </td>
          <td>
            <button type="submit" v-on:click="saveSnelheid">Save</button>
          </td>
        </tr>

      </table>
      <br>
      <br>
      <button type="submit" v-on:click="rebuildapp">rebuild application</button>
      <br>
      <br>
      <button type="submit" v-on:click="loadFen('3qr2k/pbpp2pp/1p5N/3Q2b1/2P1P3/P7/1PP2PPP/R4RK1 w - - 0 1')">Load Puzzle-1: map in 2</button>
      <br>
      <button type="submit" v-on:click="loadFen('r1bq2k1/ppp2r1p/2np1pNQ/2bNpp2/2B1P3/3P4/PPP2PPP/R3K2R w KQ - 0 1')">Load Puzzle-2: map in 2</button>
      <br>
      <button type="submit" v-on:click="loadFen('r1bk3r/1pp2ppp/pb1p1n2/n2P4/B3P1q1/2Q2N2/PB3PPP/RN3RK1 w - - 0 1')">Load Puzzle-3: map in 2</button>
      <br>
      <button type="submit" v-on:click="loadFen('1kr5/ppN2ppr/8/3p2n1/3bb3/8/n3PPPQ/4R1K1 w - - 0 1')">Load Puzzle-4: map in 3</button>





    </div>
  </app-frame>
</template>
<script>
Vue.component("manual", {
  template: "#manual",
  data: () => ({
    manual: null,
    pos: "1000,1000",
    a8pos: "",
    a21pos: "",
    a11pos: "",
    h1pos: "",
    h10pos: "",
    h20pos: "",
    snelheid: "2.0",
    pakkerhoogte: "",
    vlak: "A8"
  }),
  created() {
    this.load()
  },
  methods: {
    loadFen: function(fen){
      axios.post(`/api/game/loadfen`, fen)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })

    },
    load: function (event) {
      fetch(`/api/a8`)
          .then(res => res.text())
          .then(text => this.a8pos = text)
          .catch(() => alert("Error"));

      fetch(`/api/a21`)
          .then(res => res.text())
          .then(text => this.a21pos = text)
          .catch(() => alert("Error"));

      fetch(`/api/a11`)
          .then(res => res.text())
          .then(text => this.a11pos = text)
          .catch(() => alert("Error"));

      fetch(`/api/h1`)
          .then(res => res.text())
          .then(text => this.h1pos = text)
          .catch(() => alert("Error"));

      fetch(`/api/h10`)
          .then(res => res.text())
          .then(text => this.h10pos = text)
          .catch(() => alert("Error"));

      fetch(`/api/h20`)
      fetch(`/api/h20`)
          .then(res => res.text())
          .then(text => this.h20pos = text)
          .catch(() => alert("Error"));

      fetch(`/api/snelheid`)
          .then(res => res.text())
          .then(text => this.snelheid = text)
          .catch(() => alert("Error"));

      fetch(`/api/pakkerhoogte`)
          .then(res => res.text())
          .then(text => this.pakkerhoogte = text)
          .catch(() => alert("Error"));

    },
    home_vert: function (event) {
      fetch(`/api/home_vert`)
          .catch(() => alert("Error"));
    },
    home_hor: function (event) {
      fetch(`/api/home_hor`)
          .catch(() => alert("Error"));
    },
    sleeping: function (event) {
      fetch(`/api/sleep`)
          .catch(() => alert("Error"));
    },
    clamp1: function (event) {
      fetch(`/api/clamp1`)
          .catch(() => alert("Error"));
    },
    release1: function (event) {
      fetch(`/api/release1`)
          .catch(() => alert("Error"));
    },
    clamp2: function (event) {
      fetch(`/api/clamp2`)
          .catch(() => alert("Error"));
    },
    release2: function (event) {
      fetch(`/api/release2`)
          .catch(() => alert("Error"));
    },
    bootsound: function (event) {
      fetch(`/api/bootsound`)
          .catch(() => alert("Error"));
    },
    move: function (event) {
      axios.post(`/api/move`, this.pos)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    movevlak1: function (event) {
      axios.post(`/api/movevlak`, this.vlak + " 0")
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    movevlak2: function (event) {
      axios.post(`/api/movevlak`, this.vlak + " 1")
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    saveA8: function (event) {
      axios.post(`/api/a8`, this.a8pos)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    saveA11: function (event) {
      axios.post(`/api/a11`, this.a11pos)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    saveA21: function (event) {
      axios.post(`/api/a21`, this.a21pos)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    saveH1: function (event) {
      axios.post(`/api/h1`, this.h1pos)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    saveH10: function (event) {
      axios.post(`/api/h10`, this.h10pos)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    saveH20: function (event) {
      axios.post(`/api/h20`, this.h20pos)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    savePakkerhoogte: function (event) {
      axios.post(`/api/pakkerhoogte`, this.pakkerhoogte)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    saveSnelheid: function (event) {
      axios.post(`/api/snelheid`, this.snelheid)
          .then(res => {
          })
          .catch(error => {
            alert("Error")
          })
    },
    rebuildapp: function (event) {
      fetch(`/api/rebuild`)
          .catch(() => alert("Error while rebuilding"));
    },
    close: function (event) {
      window.location.href = "/play";
    }
  }
});

</script>
<style scoped>
.myBorder {
  color: white;
  background-color: #4d6b85;
}
.label {
  color: white;
}

</style>
