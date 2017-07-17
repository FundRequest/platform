requirejs.config({
    "baseUrl": "js/lib",
    "paths": {
        "app": "../app",
        'jquery': '//code.jquery.com/jquery-3.2.1.min',
        'jquery-storage-api': '//cdnjs.cloudflare.com/ajax/libs/jquery-storage-api/1.9.4/jquery.storageapi.min',
        'jquery-easing': '//cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min',
        'jQuery-slimScroll': '//cdnjs.cloudflare.com/ajax/libs/jQuery-slimScroll/1.3.8/jquery.slimscroll.min',
        'screenfull': '//cdnjs.cloudflare.com/ajax/libs/screenfull.js/3.3.1/screenfull.min',
        'animo': '//cdnjs.cloudflare.com/ajax/libs/animo.js/1.0.3/animo.min',
        'modernizr': 'modernizr.custom',
        'angle': 'angle',
        'bootstrap': '//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min',

        //DataTables core
        'datatables.net': 'https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min',
        'datatables.net-bs': 'https://cdn.datatables.net/1.10.15/js/dataTables.bootstrap.min'

    },
    "shim": {

        'jquery': {'exports': 'jquery'},
        'bootstrap': {'deps': ['jquery'], 'exports': 'bootstrap'},
        'datatables.net': {'deps': ['jquery', 'bootstrap']},
        'datatables.net-bs': {'deps': ['jquery', 'bootstrap', 'datatables.net']},
        'jquery-storage-api': {'deps': ['jquery']},
        'jquery-easing': {'deps': ['jquery']},
        'jQuery-slimScroll': {'deps': ['jquery']},
        'screenfull': {'deps': ['jquery']},
        'animo': {'deps': ['jquery']},
        'angle': {'deps': ['jquery', 'modernizr', 'bootstrap', 'jquery-storage-api', 'jquery-easing', 'animo', 'jQuery-slimScroll', 'screenfull']}
    }
});