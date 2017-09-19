requirejs.config({
    "baseUrl": "/js/lib",
    "paths": {
        "app": "/js/app",
        "components": "/js/app/components",
        'jquery': '//code.jquery.com/jquery-3.2.1.min',
        'jquery-storage-api': '//cdnjs.cloudflare.com/ajax/libs/jquery-storage-api/1.9.4/jquery.storageapi.min',
        'jquery-easing': '//cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min',
        'jQuery-slimScroll': '//cdnjs.cloudflare.com/ajax/libs/jQuery-slimScroll/1.3.8/jquery.slimscroll.min',
        'screenfull': '//cdnjs.cloudflare.com/ajax/libs/screenfull.js/3.3.1/screenfull.min',
        'sockjs': '//cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min',
        'stompjs': '//cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min',
        'animo': '//cdnjs.cloudflare.com/ajax/libs/animo.js/1.0.3/animo.min',
        'modernizr': 'modernizr.custom',
        'angle': 'angle',
        'bootstrap-tagsinput': 'bootstrap-tagsinput.min',
        'bootstrap': '//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min',
        'vuejs': '//cdnjs.cloudflare.com/ajax/libs/vue/2.4.2/vue.min',
        'vue-timeago': 'vue-timeago',

        //require-plugins
        'text': 'require-plugins/text',
        'async': 'require-plugins/async',
        'font': 'require-plugins/font',
        'goog': 'require-plugins/goog',
        'image': 'require-plugins/image',
        'json': 'require-plugins/json',
        'noext': 'require-plugins/noext',
        'propertyParser' : 'require-plugins/propertyParser',

        //DataTables core
        'datatables.net': 'https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min',
        'datatables.net-bs': 'https://cdn.datatables.net/1.10.15/js/dataTables.bootstrap.min'
    },
    "shim": {
        'jquery': {'exports': 'jquery'},
        'jquery-storage-api': {'deps': ['jquery']},
        'jquery-easing': {'deps': ['jquery']},
        'jQuery-slimScroll': {'deps': ['jquery']},
        'bootstrap-tagsinput': {'deps': ['jquery']},
        'bootstrap': {'deps': ['jquery'], 'exports': 'bootstrap'},
        'datatables.net': {'deps': ['jquery', 'bootstrap']},
        'datatables.net-bs': {'deps': ['jquery', 'bootstrap', 'datatables.net']},
        'screenfull': {'deps': ['jquery']},
        'sockjs': {'deps': ['stompjs']},
        'animo': {'deps': ['jquery']},
        'angle': {'deps': ['jquery', 'modernizr', 'bootstrap', 'bootstrap-tagsinput', 'jquery-storage-api', 'jquery-easing', 'animo', 'jQuery-slimScroll', 'screenfull', 'datatables.net-bs', 'datatables.net']},
    }
});