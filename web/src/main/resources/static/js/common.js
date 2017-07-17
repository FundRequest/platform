requirejs.config({
    "baseUrl": "js/lib",
    "paths": {
        "app": "../app",
        'jquery': '//code.jquery.com/jquery-3.2.1.min',
        'bootstrap': '//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min',

        //DataTables core
        'datatables.net': 'https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min',
        'datatables.net-bs': 'https://cdn.datatables.net/1.10.15/js/dataTables.bootstrap.min'

    },
    "shim": {

        'jquery': {
            'exports': 'jquery'
        },
        'bootstrap': {
            'deps': ['jquery'],
            'exports': 'bootstrap'
        },
        'datatables.net': {
            'deps': ['jquery', 'bootstrap']
        },
        'datatables.net-bs': {
            'deps': ['jquery', 'bootstrap', 'datatables.net']
        }
    }
});