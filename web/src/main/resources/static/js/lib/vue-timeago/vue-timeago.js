(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
  typeof define === 'function' && define.amd ? define(factory) :
  (global.VueTimeago = factory());
}(this, (function () { 'use strict';

var MINUTE = 60;
var HOUR = MINUTE * 60;
var DAY = HOUR * 24;
var WEEK = DAY * 7;
var MONTH = DAY * 30;
var YEAR = DAY * 365;

function pluralOrSingular(data, locale) {
  if (data === 'just now') {
    return locale
  }
  var count = Math.round(data);
  if (Array.isArray(locale)) {
    return count > 1
      ? locale[1].replace(/%s/, count)
      : locale[0].replace(/%s/, count)
  }
  return locale.replace(/%s/, count)
}

function formatTime(time) {
  var d = new Date(time);
  return d.toLocaleString()
}

function install(
  Vue,
  ref
) {
  if ( ref === void 0 ) ref = {};
  var name = ref.name; if ( name === void 0 ) name = 'timeago';
  var locale = ref.locale; if ( locale === void 0 ) locale = 'en-US';
  var locales = ref.locales; if ( locales === void 0 ) locales = null;

  if (!locales || Object.keys(locales).length === 0) {
    throw new TypeError('Expected locales to have at lease one locale.')
  }

  var VueTimeago = {
    props: {
      since: {
        required: true
      },
      locale: String,
      maxTime: Number,
      autoUpdate: Number,
      format: Function
    },
    data: function data() {
      return {
        now: new Date().getTime()
      }
    },
    computed: {
      currentLocale: function currentLocale() {
        var current = locales[this.locale || locale];
        if (!current) {
          return locales[locale]
        }
        return current
      },
      sinceTime: function sinceTime() {
        return new Date(this.since).getTime()
      },
      timeago: function timeago() {
        var seconds = this.now / 1000 - this.sinceTime / 1000;

        if (this.maxTime && seconds > this.maxTime) {
          clearInterval(this.interval);
          return this.format
            ? this.format(this.sinceTime)
            : formatTime(this.sinceTime)
        }

        var ret = seconds <= 5
          ? pluralOrSingular('just now', this.currentLocale[0])
          : seconds < MINUTE
            ? pluralOrSingular(seconds, this.currentLocale[1])
            : seconds < HOUR
              ? pluralOrSingular(seconds / MINUTE, this.currentLocale[2])
              : seconds < DAY
                ? pluralOrSingular(seconds / HOUR, this.currentLocale[3])
                : seconds < WEEK
                  ? pluralOrSingular(seconds / DAY, this.currentLocale[4])
                  : seconds < MONTH
                    ? pluralOrSingular(seconds / WEEK, this.currentLocale[5])
                    : seconds < YEAR
                      ? pluralOrSingular(seconds / MONTH, this.currentLocale[6])
                      : pluralOrSingular(seconds / YEAR, this.currentLocale[7]);

        return ret
      }
    },
    mounted: function mounted() {
      if (this.autoUpdate) {
        this.update();
      }
    },
    render: function render(h) {
      return h(
        'time',
        {
          attrs: {
            datetime: new Date(this.since)
          }
        },
        this.timeago
      )
    },
    watch: {
      autoUpdate: function autoUpdate(newAutoUpdate) {
        this.stopUpdate();
        // only update when it's not falsy value
        // which means you cans set it to 0 to disable auto-update
        if (newAutoUpdate) {
          this.update();
        }
      }
    },
    methods: {
      update: function update() {
        var this$1 = this;

        var period = this.autoUpdate * 1000;
        this.interval = setInterval(function () {
          this$1.now = new Date().getTime();
        }, period);
      },
      stopUpdate: function stopUpdate() {
        clearInterval(this.interval);
        this.interval = null;
      }
    },
    beforeDestroy: function beforeDestroy() {
      this.stopUpdate();
    }
  };

  Vue.component(name, VueTimeago);
}

return install;

})));
