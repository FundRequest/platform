<template>
    <div class="card-body faq faq--vue-collapse" v-if="id">
        <h4 class="faq__title d-flex align-content-center justify-content-between" v-if="title"
            @click="showCollapse = !showCollapse"
            :class="showCollapse ? 'collapsed' : null"
            :aria-controls="id"
            :aria-expanded="showCollapse ? 'true' : 'false'">
            <span>{{title}}</span>
            <i class="fa fa-chevron-left" :class="showCollapse ? 'collapsed' : null"></i>
        </h4>
        <b-collapse :id="id" class="faq__answer" v-model="showCollapse">
            {{description}}
            <slot></slot>
        </b-collapse>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import Utils from "../../classes/Utils";
    import bCollapse from "bootstrap-vue/es/components/collapse/collapse";

    @Component({
        components: {
            bCollapse
        }
    })
    export default class FaqItem extends Vue {
        @Prop() intercom: string;
        @Prop() title: string;
        @Prop() description?: string;

        public showCollapse = false;
        public id: string = "";

        mounted() {
            this.id = Utils.generateUUID();
        }

        //public get intercomArticleHtml() {
        //    return Utils.getHTML(Locations.getHelpPageUrl(this.intercom));
        //}

        public open() {

        }
    }
</script>

<style lang="scss">
    .faq--vue-collapse {
        .faq__title {
            cursor: pointer;
        }
        .fa {
            transition: .3s transform ease-out;
        }
        .fa.collapsed {
            transform: rotate(-90deg);
        }
    }
</style>
