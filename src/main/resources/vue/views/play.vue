<template id="play">
    <app-frame>

        <div>


            <div class="headerBorder">
                <table width=300px>
                    <tr>
                        <td>Usermode:</td>
                        <td>{{ userdata.role }}</td>
                    </tr>
                    <tr>
                        <td>Laatste keer gesproeied:</td>
                        <td>{{ tuin.lastTimeOpen }}</td>
                    </tr>
                    <tr>
                        <td>Regenval gisteren:</td>
                        <td>{{ tuin.rainYesterday }}</td>
                    </tr>
                    <tr>
                        <td>Regenval vandaag:</td>
                        <td>{{ tuin.rainToday }}</td>
                    </tr>
                    <tr>
                        <td>Verwachting morgen:</td>
                        <td>{{ tuin.rainTomorrow }}</td>
                    </tr>
                    <tr>
                        <td>Planning sproeier:</td>
                        <td>{{ tuin.displayData.plannedTime }}</td>
                    </tr>
                    <tr>
                        <td>Timer sproeier:</td>
                        <td>{{ tuin.displayData.time }}</td>
                    </tr>
                    <tr>
                        <td>KlepState:</td>
                        <td>{{ tuin.displayData.klepState }}</td>
                    </tr>
                    <tr>
                        <td>Ip:</td>
                        <td>{{ tuin.displayData.ip }}</td>
                    </tr>
                    <tr>
                        <td>Manual:</td>
                        <td>{{ tuin.displayData.manual }}</td>
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
                <button v-on:click="reload" class="button">reload</button>
                <button v-on:click="plus1" class="button">+1 minuut</button>
                <button v-on:click="min1" class="button">-1 minuut</button>
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
        reload: function () {
            fetch(`/api/game/reload`)
                .then(res => res.text())
                .then(text => this.updateTuin(JSON.parse(text)))
                .catch(() => alert("Error"));
        },
        plus1: function () {
            fetch(`/api/game/plus1`)
                .then(res => res.text())
                .then(text => this.updateTuin(JSON.parse(text)))
                .catch(() => alert("Error"));
        },
        min1: function () {
            fetch(`/api/game/min1`)
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
