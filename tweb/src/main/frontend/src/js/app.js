define(['jquery', 'bootstrap.bundle', 'waves', 'clipboard'], function() {
    require(['mdb' , 'app/main']);

    setTimeout(function(){
        document.body.classList.remove('preload');
    },500);
});