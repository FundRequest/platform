import Vue from "vue";
import HelloDecoratorComponent from "./components/HelloDecoratorComponent.vue";
import StatisticTile from "./components/StatisticTile.vue";

let v = new Vue({
    el: "#vue-app",
    data: { name: "World" },
    components: {
        'hello-decorator-component': HelloDecoratorComponent,
        'statistic-tile': StatisticTile
    }
});
