<template>
  <div>
    <div></div>
    <button class="btn btn-outline-faucet icon-left mb-2 mb-md-0"
       v-on:click="callFaucet"
       v-show="ethAddress">
      <i class="fas fa-shower"></i>
      <span>Get some test FND on Kovan</span>
    </button>
    <div class="note text-muted">Note: please be patient. This action can take up to 15 seconds.</div>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import Alert from "../../classes/Alert";
    import Utils from "../../classes/Utils";

    @Component
    export default class Faucet extends Vue {

        @Prop() address: string;
        @Prop() name: string;

        public ethAddress: string = '';


        mounted() {
            this.ethAddress = this.address;
            document.addEventListener('fnd:input-changed', (e: CustomEvent) => {
                if (e.detail.name === this.name) {
                    this.ethAddress = e.detail.value;
                }
            });
        }

        public callFaucet(e) {
            Utils.showLoading();
            $.get('https://faucet.fundrequest.io/faucet?address=' + this.ethAddress)
                .done(function (e: any) {
                    Alert.success(`FND tokens are on their way, you can view the transaction on <a target="_blank" href="https://kovan.etherscan.io/tx/${e.transactionhash}">etherscan</a>.`);
                })
                .fail(function (e: any) {
                    Alert.error(e.responseText);
                })
                .always(function () {
                    Utils.hideLoading();
                });

        }
    }
</script>

<style scoped>
  .note {
    font-style: italic;
    margin-top: 0.5rem;
  }
</style>
