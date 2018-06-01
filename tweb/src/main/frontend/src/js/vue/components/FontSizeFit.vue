<template>
    <span class="font-size-fit" v-bind:style="{ fontSize: fontSize + 'px'}" ref="fontSizeFit"><slot></slot></span>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";

    @Component
    export default class FontSizeFit extends Vue {
        @Prop() maxSize: number;

        public fontSize: number = 15;

        mounted() {
            if (this.maxSize != null) {
                this.fontSize = this.maxSize;
            }
            this._resizeText(this.$refs["fontSizeFit"] as HTMLElement);
        }

        private _resizeText(el: HTMLElement) {
            if (el) {
                el.style.fontSize = (parseInt(el.style.fontSize.slice(0, -2)) - 1) + "px";
                if (el.offsetWidth > el.parentElement.offsetWidth) {
                    this._resizeText(el);
                }
            }
        }
    }
</script>

<style>
    .font-size-fit {
        white-space: nowrap;
    }
</style>