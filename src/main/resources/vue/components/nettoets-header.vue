<template id="nettoets-header">
  <div class="container">
    <div class="header clearfix">

    <table>
      <tr>
        <td>
          <img _ngcontent-byh-c1="" alt="Liander Logo" width="100px" src="/logo.png">
        </td>
        <td width="20px">
        </td>
        <td>
          <h3 class="header_title" id="header_title" name="header_title">{{ title }}</h3>
        </td>
      </tr>
    </table>

    </div>
  </div>
</template>

<script>
Vue.component("nettoets-header", {
  props: ['activebutton', 'title'],
  template: "#nettoets-header"});

$(document).ready(function () {
  $("#berekeningpage").click(function () {
    window.location.href = "/demo";
  });
  $("#operationallogpage").click(function () {
    window.location.href = "/status";
  });
});

</script>
