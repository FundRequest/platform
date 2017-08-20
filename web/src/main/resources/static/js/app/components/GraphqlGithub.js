/**
 *  Object to use the GraphQl of github to query issues
 * */
define(function() {
    'use strict';

    var uri = 'https://api.github.com/graphql';

    // TODO: Authorization with app key instead of personal key, and go via backend
    var header = {
        'Authorization': 'bearer 9e7b7596f002b7f25738bdcb9e24fc6e6f584256',
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    };

    var xhr;

    function GraphqlGithub() {
        xhr = new XMLHttpRequest();
        xhr.responseType = 'json';

        xhr.onload = function() {
            console.log('data returned:', xhr.response);
        }
    }

    function makeReadyToSend() {
        xhr.open('POST', uri);
        for (var key in header) {
            if (header.hasOwnProperty(key)) {
                xhr.setRequestHeader(key, header[key]);
            }
        }
    }

    GraphqlGithub.prototype.send = function(query, variables) {
        makeReadyToSend();
        xhr.send(JSON.stringify({
            query: query,
            variables: variables
        }));
    };

    return GraphqlGithub;
});