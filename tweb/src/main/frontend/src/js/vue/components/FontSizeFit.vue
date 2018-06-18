<template>
    <span class="font-size-fit" v-bind:style="fontSizeStyle"><slot></slot></span>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";

    @Component
    export default class FontSizeFit extends Vue {
        @Prop({ default: 15 }) maxSize: number;

        private fontSize: number = this.maxSize;

		get fontSizeStyle() {
			return {
				fontSize: this.fontSize + 'px'
			}
		}

        mounted() {
            this.fontSize = this.maxSize;
            this._resizeText();
        }

		updated() {
			this._resizeText();
		}

        private _resizeText() {
			let el = this.$el as HTMLElement;
            if (el.offsetWidth > el.parentElement.offsetWidth) {
				this.fontSize = this.fontSize - 1;
			}
        }
    }
</script>

<style>
    .font-size-fit {
        white-space: nowrap;
    }
</style>
