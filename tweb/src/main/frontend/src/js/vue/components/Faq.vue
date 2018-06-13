<template>
    <div class="row">
        <div class="col-12">
            <h2 class="section-title" v-if="showTitle">Frequently Asked Questions</h2>
            <h3 class="section-subtitle" v-if="subtitle">
                {{subtitle}}
            </h3>
        </div>
        <div class="col-12">
            <div class="card" v-if="faqs.length > 0">
                <faq-item v-bind:title="faq.title" v-for="faq in faqs" v-bind:key="faq.title">
                    <span v-html="faq.body"></span>
                </faq-item>
            </div>
        </div>
        <div class="col-12" v-if="faqs.length <= 0">
            <div class="card">
                <div class="card-body faq">
                    <h4 class="faq__title d-flex align-items-start align-content-center justify-content-between"><span>No FAQ's available at this time.</span>
                    </h4>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import FaqItem from "./FaqItem";
    import Utils from "../../classes/Utils";

    @Component({
        components: {FaqItem}
    })
    export default class Faq extends Vue {
        @Prop({default: true}) showTitle?: boolean;
        @Prop() location: string;

        public subtitle: string = "";
        public faqs: Array<{ title: string, body: string }> = [];

        mounted() {
            this._loadFaqItems();
        }

        private async _loadFaqItems() {
            let faq = await Utils.getJSON(this.location) as { subtitle: string, faqItems: Array<{ title: string, body: string }> };
            this.subtitle = faq.subtitle;
            this.faqs = faq.faqItems;
        }
    }
</script>

<style lang="scss">
</style>
