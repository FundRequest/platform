<template>
    <div>
        <select class="form-control form-control--fnd"
                v-bind:id="id"
                v-bind:value="JSON.stringify(value)"
                v-on:input="$emit('input', JSON.parse($event.target.value))">
            <slot></slot>
            <option v-if="options" v-for="option in options" v-bind:value="JSON.stringify(option.value)">{{option.title}}</option>
        </select>
        <i class="fa fa-chevron-down"></i>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";

    @Component
    export default class FndSelect extends Vue {
        @Prop() id: string;
        @Prop() value: any;
        @Prop() options: Array<{title: string, value: any}>;
    }
</script>

<style>
    .form-control--fnd {
        position: relative;
        -moz-appearance: none;
        -webkit-appearance: none;
        border-radius: 5px !important;
        box-shadow: none;
        padding: 0 1.75rem 0 .75rem !important;
    }

    .form-control--fnd + .fa {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        right: .75rem;
    }
</style>
