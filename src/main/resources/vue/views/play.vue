<template id="play">
    <app-frame>

        <div>


            <div class="headerBorder">
                <table width=100%>
                    <tr height="50px">
                        <td>&nbsp;
                            Usermode:{{ userdata.role }}<br>
                            Sproeier status: {{ userdata.role }} <br>
                            Laatste keer gesproeied: {{ tuin.klep }} <br>
                            Regenval gisteren: {{ tuin.gisteren }} <br>
                            Regenval eergisteren: ?? <br>
                            Verwachting morgen: ?? <br>
                            Planning sproeier: ??
                        </td>
                    </tr>
                </table>
            </div>

            <div class="headerBorder">
                <table width=100%>
                    <tr height="50px">
                        <td>
                            &nbsp;
                            <span v-if="userdata.role!='ADMIN'">
                <button type="submit" v-on:click="login" class="button">Login</button>
              </span>
                            <span v-if="userdata.role=='ADMIN'">
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
                <button v-on:click="plus5" class="button">+5 minuten</button>
                <button v-on:click="min5" class="button">-5 minuten</button>
       </span>

            <br>
        </div>
    </app-frame>
</template>

<style>


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
        tuin: {
            klep: "",
            gisteren: ""
        },
        van: "",
        naar: "",
        possible_tos: [],
        testfield: {"s": "dd"},
    }),
    computed: {},

    created() {
        this.load()
    },
    methods: {
        load: function (event) {
            fetch(`/api/game/load`)
                .then(res => res.text())
                .then(text => this.updateTuin(JSON.parse(text)))
                .catch(err => alert(err));
            fetch(`/api/userdata`)
                .then(res => res.text())
                .then(text => this.userdata = JSON.parse(text))
                .catch(() => alert("Error"));
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
        updateTuin: function (newTuin) {
            this.tuin = newTuin;
        },
        plus5: function () {
            fetch(`/api/game/plus5`)
                .then(res => res.text())
                .then(text => this.updateTuin(JSON.parse(text)))
                .catch(() => alert("Error"));
        },
        min5: function () {
            fetch(`/api/game/min5`)
                .then(res => res.text())
                .then(text => this.updateTuin(JSON.parse(text)))
                .catch(() => alert("Error"));
        },
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
