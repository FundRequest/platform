<template>
    <div class="card bg-primary text-white">
        <div class="card-body media align-items-center">
            <div class="w-25 mr-3 text-center">
                <i class="fa-3x fa" :class="iconClass"></i>
            </div>
            <div class="media-body">
                <h3>{{value}}</h3>
                <div>{{description}}</div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import { Vue, Component, Prop } from "vue-property-decorator";

    @Component
    export default class HelloDecorator extends Vue {
        @Prop({required: true}) iconClass!: string;
        @Prop({required: true}) value!: string;
        @Prop({required: true}) description!: string;
    }
</script>

<style></style>

